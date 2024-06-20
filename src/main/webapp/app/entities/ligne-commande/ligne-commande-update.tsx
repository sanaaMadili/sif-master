import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArticle } from 'app/shared/model/article.model';
import { getEntities as getArticles } from 'app/entities/article/article.reducer';
import { IAppelCommande } from 'app/shared/model/appel-commande.model';
import { getEntities as getAppelCommandes } from 'app/entities/appel-commande/appel-commande.reducer';
import { ILigneCommande } from 'app/shared/model/ligne-commande.model';
import { getEntity, updateEntity, createEntity, reset } from './ligne-commande.reducer';

export const LigneCommandeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const articles = useAppSelector(state => state.article.entities);
  const appelCommandes = useAppSelector(state => state.appelCommande.entities);
  const ligneCommandeEntity = useAppSelector(state => state.ligneCommande.entity);
  const loading = useAppSelector(state => state.ligneCommande.loading);
  const updating = useAppSelector(state => state.ligneCommande.updating);
  const updateSuccess = useAppSelector(state => state.ligneCommande.updateSuccess);

  const handleClose = () => {
    navigate('/ligne-commande' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getArticles({}));
    dispatch(getAppelCommandes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...ligneCommandeEntity,
      ...values,
      article: articles.find(it => it.id.toString() === values.article.toString()),
      appelCommande: appelCommandes.find(it => it.id.toString() === values.appelCommande.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...ligneCommandeEntity,
          article: ligneCommandeEntity?.article?.id,
          appelCommande: ligneCommandeEntity?.appelCommande?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sifApp.ligneCommande.home.createOrEditLabel" data-cy="LigneCommandeCreateUpdateHeading">
            <Translate contentKey="sifApp.ligneCommande.home.createOrEditLabel">Create or edit a LigneCommande</Translate>
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
                  id="ligne-commande-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('sifApp.ligneCommande.qte')} id="ligne-commande-qte" name="qte" data-cy="qte" type="text" />
              <ValidatedField
                id="ligne-commande-article"
                name="article"
                data-cy="article"
                label={translate('sifApp.ligneCommande.article')}
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
              <ValidatedField
                id="ligne-commande-appelCommande"
                name="appelCommande"
                data-cy="appelCommande"
                label={translate('sifApp.ligneCommande.appelCommande')}
                type="select"
              >
                <option value="" key="0" />
                {appelCommandes
                  ? appelCommandes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ligne-commande" replace color="info">
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

export default LigneCommandeUpdate;
