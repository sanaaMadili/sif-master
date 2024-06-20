import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMagasinier } from 'app/shared/model/magasinier.model';
import { getEntities as getMagasiniers } from 'app/entities/magasinier/magasinier.reducer';
import { IMagasin } from 'app/shared/model/magasin.model';
import { getEntity, updateEntity, createEntity, reset } from './magasin.reducer';

export const MagasinUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const magasiniers = useAppSelector(state => state.magasinier.entities);
  const magasinEntity = useAppSelector(state => state.magasin.entity);
  const loading = useAppSelector(state => state.magasin.loading);
  const updating = useAppSelector(state => state.magasin.updating);
  const updateSuccess = useAppSelector(state => state.magasin.updateSuccess);

  const handleClose = () => {
    navigate('/magasin' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMagasiniers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...magasinEntity,
      ...values,
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
          ...magasinEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sifApp.magasin.home.createOrEditLabel" data-cy="MagasinCreateUpdateHeading">
            <Translate contentKey="sifApp.magasin.home.createOrEditLabel">Create or edit a Magasin</Translate>
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
                  id="magasin-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sifApp.magasin.codeMagasin')}
                id="magasin-codeMagasin"
                name="codeMagasin"
                data-cy="codeMagasin"
                type="text"
              />
              <ValidatedField label={translate('sifApp.magasin.pays')} id="magasin-pays" name="pays" data-cy="pays" type="text" />
              <ValidatedField
                label={translate('sifApp.magasin.address')}
                id="magasin-address"
                name="address"
                data-cy="address"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/magasin" replace color="info">
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

export default MagasinUpdate;
