import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IReception } from 'app/shared/model/reception.model';
import { getEntities as getReceptions } from 'app/entities/reception/reception.reducer';
import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { IAppelCommande } from 'app/shared/model/appel-commande.model';
import { getEntity, updateEntity, createEntity, reset } from './appel-commande.reducer';

export const AppelCommandeUpdate = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const receptions = useAppSelector(state => state.reception.entities);
  const clients = useAppSelector(state => state.client.entities);
  const appelCommandeEntity = useAppSelector(state => state.appelCommande.entity);
  const loading = useAppSelector(state => state.appelCommande.loading);
  const updating = useAppSelector(state => state.appelCommande.updating);
  const updateSuccess = useAppSelector(state => state.appelCommande.updateSuccess);

  const handleClose = () => {
    navigate('/appel-commande' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getReceptions({}));
    dispatch(getClients({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...appelCommandeEntity,
      ...values,
      reception: receptions.find(it => it.id.toString() === values.reception.toString()),
      client: clients.find(it => it.id.toString() === values.client.toString()),
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
          ...appelCommandeEntity,
          reception: appelCommandeEntity?.reception?.id,
          client: appelCommandeEntity?.client?.id,
        };
  const canEditFields = !account.authorities.includes('ROLE_MAGASINIER');
  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sifApp.appelCommande.home.createOrEditLabel" data-cy="AppelCommandeCreateUpdateHeading">
            <Translate contentKey="sifApp.appelCommande.home.createOrEditLabel">Create or edit a AppelCommande</Translate>
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
                  id="appel-commande-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sifApp.appelCommande.numCommande')}
                id="appel-commande-numCommande"
                name="numCommande"
                data-cy="numCommande"
                type="text"
                disabled={!isNew && !canEditFields}
              />
              <ValidatedField
                label={translate('sifApp.appelCommande.dateCommande')}
                id="appel-commande-dateCommande"
                name="dateCommande"
                data-cy="dateCommande"
                type="date"
                disabled={!isNew && !canEditFields}
              />
              <ValidatedField
                label={translate('sifApp.appelCommande.dateLivraison')}
                id="appel-commande-dateLivraison"
                name="dateLivraison"
                data-cy="dateLivraison"
                type="date"
                
              />
              <ValidatedField
                label={translate('sifApp.appelCommande.dateExpedition')}
                id="appel-commande-dateExpedition"
                name="dateExpedition"
                data-cy="dateExpedition"
                type="date"
                
              />
              <ValidatedField
                label={translate('sifApp.appelCommande.status')}
                id="appel-commande-status"
                name="status"
                data-cy="status"
                type="text"
                

              />
              <ValidatedField
                label={translate('sifApp.appelCommande.annomalie')}
                id="appel-commande-annomalie"
                name="annomalie"
                data-cy="annomalie"
                type="text"
                disabled={!isNew && !canEditFields}
              />
              <ValidatedField
                id="appel-commande-reception"
                name="reception"
                data-cy="reception"
                label={translate('sifApp.appelCommande.reception')}
                type="select"
                disabled={!isNew && !canEditFields}
              >
                <option value="" key="0" />
                {receptions
                  ? receptions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="appel-commande-client"
                name="client"
                data-cy="client"
                label={translate('sifApp.appelCommande.client')}
                type="select"
                disabled={!isNew && !canEditFields}
              >
                <option value="" key="0" />
                {clients
                  ? clients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/appel-commande" replace color="info">
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

export default AppelCommandeUpdate;
