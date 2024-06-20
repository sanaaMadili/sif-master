import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './appel-commande.reducer';

export const AppelCommandeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const appelCommandeEntity = useAppSelector(state => state.appelCommande.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="appelCommandeDetailsHeading">
          <Translate contentKey="sifApp.appelCommande.detail.title">AppelCommande</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{appelCommandeEntity.id}</dd>
          <dt>
            <span id="numCommande">
              <Translate contentKey="sifApp.appelCommande.numCommande">Num Commande</Translate>
            </span>
          </dt>
          <dd>{appelCommandeEntity.numCommande}</dd>
          <dt>
            <span id="dateCommande">
              <Translate contentKey="sifApp.appelCommande.dateCommande">Date Commande</Translate>
            </span>
          </dt>
          <dd>
            {appelCommandeEntity.dateCommande ? (
              <TextFormat value={appelCommandeEntity.dateCommande} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="dateLivraison">
              <Translate contentKey="sifApp.appelCommande.dateLivraison">Date Livraison</Translate>
            </span>
          </dt>
          <dd>
            {appelCommandeEntity.dateLivraison ? (
              <TextFormat value={appelCommandeEntity.dateLivraison} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="dateExpedition">
              <Translate contentKey="sifApp.appelCommande.dateExpedition">Date Expedition</Translate>
            </span>
          </dt>
          <dd>
            {appelCommandeEntity.dateExpedition ? (
              <TextFormat value={appelCommandeEntity.dateExpedition} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="sifApp.appelCommande.status">Status</Translate>
            </span>
          </dt>
          <dd>{appelCommandeEntity.status}</dd>
          <dt>
            <span id="annomalie">
              <Translate contentKey="sifApp.appelCommande.annomalie">Annomalie</Translate>
            </span>
          </dt>
          <dd>{appelCommandeEntity.annomalie}</dd>
          <dt>
            <Translate contentKey="sifApp.appelCommande.reception">Reception</Translate>
          </dt>
          <dd>{appelCommandeEntity.reception ? appelCommandeEntity.reception.id : ''}</dd>
          <dt>
            <Translate contentKey="sifApp.appelCommande.client">Client</Translate>
          </dt>
          <dd>{appelCommandeEntity.client ? appelCommandeEntity.client.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/appel-commande" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/appel-commande/${appelCommandeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AppelCommandeDetail;
