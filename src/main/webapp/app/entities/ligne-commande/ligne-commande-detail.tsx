import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ligne-commande.reducer';

export const LigneCommandeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ligneCommandeEntity = useAppSelector(state => state.ligneCommande.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ligneCommandeDetailsHeading">
          <Translate contentKey="sifApp.ligneCommande.detail.title">LigneCommande</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ligneCommandeEntity.id}</dd>
          <dt>
            <span id="qte">
              <Translate contentKey="sifApp.ligneCommande.qte">Qte</Translate>
            </span>
          </dt>
          <dd>{ligneCommandeEntity.qte}</dd>
          <dt>
            <Translate contentKey="sifApp.ligneCommande.article">Article</Translate>
          </dt>
          <dd>{ligneCommandeEntity.article ? ligneCommandeEntity.article.id : ''}</dd>
          <dt>
            <Translate contentKey="sifApp.ligneCommande.appelCommande">Appel Commande</Translate>
          </dt>
          <dd>{ligneCommandeEntity.appelCommande ? ligneCommandeEntity.appelCommande.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ligne-commande" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ligne-commande/${ligneCommandeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LigneCommandeDetail;
