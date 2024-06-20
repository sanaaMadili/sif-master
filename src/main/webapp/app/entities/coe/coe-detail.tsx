import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './coe.reducer';

export const CoeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const coeEntity = useAppSelector(state => state.coe.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="coeDetailsHeading">
          <Translate contentKey="sifApp.coe.detail.title">Coe</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{coeEntity.id}</dd>
          <dt>
            <span id="typeVoiture">
              <Translate contentKey="sifApp.coe.typeVoiture">Type Voiture</Translate>
            </span>
          </dt>
          <dd>{coeEntity.typeVoiture}</dd>
          <dt>
            <span id="poidsVoiture">
              <Translate contentKey="sifApp.coe.poidsVoiture">Poids Voiture</Translate>
            </span>
          </dt>
          <dd>{coeEntity.poidsVoiture}</dd>
          <dt>
            <span id="vitesseVoiture">
              <Translate contentKey="sifApp.coe.vitesseVoiture">Vitesse Voiture</Translate>
            </span>
          </dt>
          <dd>{coeEntity.vitesseVoiture}</dd>
          <dt>
            <Translate contentKey="sifApp.coe.appelCommande">Appel Commande</Translate>
          </dt>
          <dd>{coeEntity.appelCommande ? coeEntity.appelCommande.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/coe" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/coe/${coeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CoeDetail;
