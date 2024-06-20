import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getEntities as getExtraUsers } from 'app/entities/extra-user/extra-user.reducer';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './clr.reducer';

export const ClrDetail = () => {
  const dispatch = useAppDispatch();
  const extraUsers = useAppSelector(state => state.extraUser.entities);

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
    dispatch(getExtraUsers({}));

  }, []);

  const clrEntity = useAppSelector(state => state.clr.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clrDetailsHeading">
          <Translate contentKey="sifApp.clr.detail.title">Clr</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{clrEntity.id}</dd>
          <dt>
            <span id="constructeurAutomobile">
              <Translate contentKey="sifApp.clr.constructeurAutomobile">Constructeur Automobile</Translate>
            </span>
          </dt>
          <dd>{clrEntity.constructeurAutomobile}</dd>
          <dt>
            <span id="modelVoiture">
              <Translate contentKey="sifApp.clr.modelVoiture">Model Voiture</Translate>
            </span>
          </dt>
          <dd>{clrEntity.modelVoiture}</dd>
          <dt>
            <span id="anneeVoiture">
              <Translate contentKey="sifApp.clr.anneeVoiture">Annee Voiture</Translate>
            </span>
          </dt>
          <dd>{clrEntity.anneeVoiture}</dd>
          <dt>
            <span id="etatPneu">
              <Translate contentKey="sifApp.clr.etatPneu">Etat Pneu</Translate>
            </span>
          </dt>
          <dd>{clrEntity.etatPneu}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="sifApp.clr.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {clrEntity.image ? (
              <div>
                {clrEntity.imageContentType ? (
                  <a onClick={openFile(clrEntity.imageContentType, clrEntity.image)}>
                    <img src={`data:${clrEntity.imageContentType};base64,${clrEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {clrEntity.imageContentType}, {byteSize(clrEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="dateProduction">
              <Translate contentKey="sifApp.clr.dateProduction">Date Production</Translate>
            </span>
          </dt>
          <dd>
            {clrEntity.dateProduction ? <TextFormat value={clrEntity.dateProduction} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="sifApp.clr.appelCommande">Appel Commande</Translate>
          </dt>
          <dd>{clrEntity.appelCommande ? clrEntity.appelCommande.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/clr" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/clr/${clrEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClrDetail;
