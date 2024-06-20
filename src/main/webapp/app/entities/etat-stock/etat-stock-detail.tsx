import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './etat-stock.reducer';

export const EtatStockDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const etatStockEntity = useAppSelector(state => state.etatStock.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="etatStockDetailsHeading">
          <Translate contentKey="sifApp.etatStock.detail.title">EtatStock</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{etatStockEntity.id}</dd>
          <dt>
            <span id="qte">
              <Translate contentKey="sifApp.etatStock.qte">Qte</Translate>
            </span>
          </dt>
          <dd>{etatStockEntity.qte}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="sifApp.etatStock.location">Location</Translate>
            </span>
          </dt>
          <dd>{etatStockEntity.location}</dd>
          <dt>
            <Translate contentKey="sifApp.etatStock.article">Article</Translate>
          </dt>
          <dd>{etatStockEntity.article ? etatStockEntity.article.id : ''}</dd>
          <dt>
            <Translate contentKey="sifApp.etatStock.magasin">Magasin</Translate>
          </dt>
          <dd>{etatStockEntity.magasin ? etatStockEntity.magasin.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/etat-stock" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/etat-stock/${etatStockEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EtatStockDetail;
