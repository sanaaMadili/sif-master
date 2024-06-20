import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reception.reducer';

export const ReceptionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const receptionEntity = useAppSelector(state => state.reception.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="receptionDetailsHeading">
          <Translate contentKey="sifApp.reception.detail.title">Reception</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{receptionEntity.id}</dd>
          <dt>
            <span id="pays">
              <Translate contentKey="sifApp.reception.pays">Pays</Translate>
            </span>
          </dt>
          <dd>{receptionEntity.pays}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="sifApp.reception.address">Address</Translate>
            </span>
          </dt>
          <dd>{receptionEntity.address}</dd>
        </dl>
        <Button tag={Link} to="/reception" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reception/${receptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReceptionDetail;
