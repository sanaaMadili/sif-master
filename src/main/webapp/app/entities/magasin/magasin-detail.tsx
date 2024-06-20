import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './magasin.reducer';

export const MagasinDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const magasinEntity = useAppSelector(state => state.magasin.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="magasinDetailsHeading">
          <Translate contentKey="sifApp.magasin.detail.title">Magasin</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{magasinEntity.id}</dd>
          <dt>
            <span id="codeMagasin">
              <Translate contentKey="sifApp.magasin.codeMagasin">Code Magasin</Translate>
            </span>
          </dt>
          <dd>{magasinEntity.codeMagasin}</dd>
          <dt>
            <span id="pays">
              <Translate contentKey="sifApp.magasin.pays">Pays</Translate>
            </span>
          </dt>
          <dd>{magasinEntity.pays}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="sifApp.magasin.address">Address</Translate>
            </span>
          </dt>
          <dd>{magasinEntity.address}</dd>
        </dl>
        <Button tag={Link} to="/magasin" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/magasin/${magasinEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MagasinDetail;
