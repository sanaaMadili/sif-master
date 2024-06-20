import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getEntities as getExtraUsers } from 'app/entities/extra-user/extra-user.reducer';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArticle } from 'app/shared/model/article.model';
import { getEntities as getArticles } from 'app/entities/article/article.reducer';
import { IMagasin } from 'app/shared/model/magasin.model';
import { getEntities as getMagasins } from 'app/entities/magasin/magasin.reducer';
import { IEtatStock } from 'app/shared/model/etat-stock.model';
import { getEntity, updateEntity, createEntity, reset } from './etat-stock.reducer';
import { createEntity as createEntityExtraUser, updateEntity as updateEntityExtraUser } from 'app/entities/extra-user/extra-user.reducer';
import { getEntities as getMagasiniers} from 'app/entities/magasinier/magasinier.reducer';

export const EtatStockUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;
  const account = useAppSelector(state => state.authentication.account);
  const extraUsers = useAppSelector(state => state.extraUser.entities);

  const articles = useAppSelector(state => state.article.entities);
  const magasins = useAppSelector(state => state.magasin.entities);
  const etatStockEntity = useAppSelector(state => state.etatStock.entity);
  const loading = useAppSelector(state => state.etatStock.loading);
  const updating = useAppSelector(state => state.etatStock.updating);
  const updateSuccess = useAppSelector(state => state.etatStock.updateSuccess);
  /*const matchingUserExtra = extraUsers.find(userextra => userextra.user.id === account?.id);*/
  const magasiniers = useAppSelector(state => state.magasinier.entities);

  const handleClose = () => {
    navigate('/etat-stock' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getArticles({}));
    dispatch(getMagasins({}));
    dispatch(getExtraUsers({}));
    dispatch(getMagasiniers({})); // Ensure that magasiniers are fetched

  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const matchingUserExtra = extraUsers.find(userextra => userextra.user.id === account?.id);
    const matchingMagasinier=magasiniers.find(m=>m.extraUser.id=== matchingUserExtra?.id);
    console.log("magasinier",matchingMagasinier);
    const entity = {
      ...etatStockEntity,
      ...values,
      article: articles.find(it => it.id.toString() === values.article.toString()),
      magasin: matchingMagasinier.magasin,
    };

    if (isNew) {
      dispatch(createEntity(entity));
      console.log("magasinier",matchingMagasinier.magasin.id);
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...etatStockEntity,
          article: etatStockEntity?.article?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sifApp.etatStock.home.createOrEditLabel" data-cy="EtatStockCreateUpdateHeading">
            <Translate contentKey="sifApp.etatStock.home.createOrEditLabel">Create or edit a EtatStock</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="etat-stock-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('sifApp.etatStock.qte')} id="etat-stock-qte" name="qte" data-cy="qte" type="text" />
              <ValidatedField
                label={translate('sifApp.etatStock.location')}
                id="etat-stock-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                id="etat-stock-article"
                name="article"
                data-cy="article"
                label={translate('sifApp.etatStock.article')}
                type="select"
              >
                <option value="" key="0" />
                {articles
                  ? articles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/etat-stock" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default EtatStockUpdate;
