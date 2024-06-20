import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reclamation.reducer';

export const ReclamationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reclamationEntity = useAppSelector(state => state.reclamation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reclamationDetailsHeading">
          <Translate contentKey="sifApp.reclamation.detail.title">Reclamation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reclamationEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="sifApp.reclamation.date">Date</Translate>
            </span>
          </dt>
          <dd>{reclamationEntity.date}</dd>
          <dt>
            <span id="pieceJointe">
              <Translate contentKey="sifApp.reclamation.pieceJointe">Piece Jointe</Translate>
            </span>
          </dt>
          <dd>
            {reclamationEntity.pieceJointe ? (
              <div>
                {reclamationEntity.pieceJointeContentType ? (
                  <a onClick={openFile(reclamationEntity.pieceJointeContentType, reclamationEntity.pieceJointe)}>
                    <img
                      src={`data:${reclamationEntity.pieceJointeContentType};base64,${reclamationEntity.pieceJointe}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {reclamationEntity.pieceJointeContentType}, {byteSize(reclamationEntity.pieceJointe)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="raison">
              <Translate contentKey="sifApp.reclamation.raison">Raison</Translate>
            </span>
          </dt>
          <dd>{reclamationEntity.raison}</dd>
          <dt>
            <Translate contentKey="sifApp.reclamation.client">Client</Translate>
          </dt>
          <dd>{reclamationEntity.client ? reclamationEntity.client.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/reclamation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reclamation/${reclamationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReclamationDetail;
