import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './extra-user.reducer';

export const ExtraUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const extraUserEntity = useAppSelector(state => state.extraUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="extraUserDetailsHeading">
          <Translate contentKey="sifApp.extraUser.detail.title">ExtraUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{extraUserEntity.id}</dd>
          <dt>
            <span id="cin">
              <Translate contentKey="sifApp.extraUser.cin">Cin</Translate>
            </span>
          </dt>
          <dd>{extraUserEntity.cin}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="sifApp.extraUser.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {extraUserEntity.image ? (
              <div>
                {extraUserEntity.imageContentType ? (
                  <a onClick={openFile(extraUserEntity.imageContentType, extraUserEntity.image)}>
                    <img src={`data:${extraUserEntity.imageContentType};base64,${extraUserEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {extraUserEntity.imageContentType}, {byteSize(extraUserEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="numeroTelephone">
              <Translate contentKey="sifApp.extraUser.numeroTelephone">Numero Telephone</Translate>
            </span>
          </dt>
          <dd>{extraUserEntity.numeroTelephone}</dd>
          <dt>
            <span id="dateNaissance">
              <Translate contentKey="sifApp.extraUser.dateNaissance">Date Naissance</Translate>
            </span>
          </dt>
          <dd>
            {extraUserEntity.dateNaissance ? (
              <TextFormat value={extraUserEntity.dateNaissance} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="adresse">
              <Translate contentKey="sifApp.extraUser.adresse">Adresse</Translate>
            </span>
          </dt>
          <dd>{extraUserEntity.adresse}</dd>
          <dt>
            <span id="pays">
              <Translate contentKey="sifApp.extraUser.pays">Pays</Translate>
            </span>
          </dt>
          <dd>{extraUserEntity.pays}</dd>
          <dt>
            <Translate contentKey="sifApp.extraUser.user">User</Translate>
          </dt>
          <dd>{extraUserEntity.user ? extraUserEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/extra-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/extra-user/${extraUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExtraUserDetail;
