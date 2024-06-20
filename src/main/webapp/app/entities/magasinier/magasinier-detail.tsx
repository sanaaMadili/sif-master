import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col, Card, CardBody } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getEntities as getMagasins } from 'app/entities/magasin/magasin.reducer';
import { getUsers, getRoles } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getExtraUsers } from 'app/entities/extra-user/extra-user.reducer';
import { getEntities as getMagasiniers, getEntity } from './magasinier.reducer';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';

export const MagasinierDetail = () => {
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
    dispatch(getExtraUsers({}));
    dispatch(getMagasins({}));
    dispatch(getRoles());
    dispatch(getUsers({}));
    dispatch(getMagasiniers({}));
  }, [dispatch, id]);

  const magasiniers = useAppSelector(state => state.magasinier.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const extraUsers = useAppSelector(state => state.extraUser.entities);
  const magasinierEntity = useAppSelector(state => state.magasinier.entity);

  const matchingUserExtra = extraUsers.find(extra => extra.id === magasinierEntity?.extraUser?.id);
  const loginValue = matchingUserExtra?.user?.login || '';
  const firstName = matchingUserExtra?.user?.firstName || '';
  const lastName = matchingUserExtra?.user?.lastName || '';
  const email = matchingUserExtra?.user?.email || '';
  const photo = matchingUserExtra?.photo || '';
  const photoContentType = matchingUserExtra?.photoContentType || '';

  return (
    <Row className="justify-content-center">
      <Col md="8">
        <Card>
          <CardBody>
            <Row>
              <Col md="4" className="text-center">
              <dt>
            <span id="image">
              <Translate contentKey="sifApp.extraUser.image">Image</Translate>
            </span>
          </dt>
          <dd>
          {matchingUserExtra?.image ? (
              <div>
                {matchingUserExtra?.imageContentType ? (
                  <a onClick={openFile(matchingUserExtra?.imageContentType, matchingUserExtra?.image)}>
                    <img src={`data:${matchingUserExtra?.imageContentType};base64,${matchingUserExtra?.image}`} style={{ maxHeight: '70px'}} />
                  </a>
                ) : null}
                <span>
                  {matchingUserExtra?.imageContentType}, {byteSize(matchingUserExtra?.image)}
                </span>
              </div>
            ) : null}
          </dd>
              </Col>
              <Col md="8">
                <h2>{firstName} {lastName}</h2>
                <h5>Profession: {magasinierEntity.profession}</h5>
                <p className="text-muted">
                  <Translate contentKey="global.field.id">ID</Translate>: {magasinierEntity.id}
                </p>
              </Col>
            </Row>
            <div className="mt-4">
              <dl className="row">
                <dt className="col-sm-3">
                  <Translate contentKey="userManagement.login">Login</Translate>
                </dt>
                <dd className="col-sm-9">{loginValue}</dd>
                <dt className="col-sm-3">
                  <Translate contentKey="userManagement.email">Email</Translate>
                </dt>
                <dd className="col-sm-9">{email}</dd>
                <dt className="col-sm-3">
                  <Translate contentKey="sifApp.extraUser.cin">Cin</Translate>
                </dt>
                <dd className="col-sm-9">{matchingUserExtra?.cin}</dd>
                <dt className="col-sm-3">
                  <Translate contentKey="sifApp.extraUser.numeroTelephone">Numero Telephone</Translate>
                </dt>
                <dd className="col-sm-9">{matchingUserExtra?.numeroTelephone}</dd>
                <dt className="col-sm-3">
                  <Translate contentKey="sifApp.extraUser.dateNaissance">Date Naissance</Translate>
                </dt>
                <dd className="col-sm-9">
                  {matchingUserExtra?.dateNaissance ? (
                    <TextFormat value={matchingUserExtra.dateNaissance} type="date" format={APP_LOCAL_DATE_FORMAT} />
                  ) : null}
                </dd>
                <dt className="col-sm-3">
                  <Translate contentKey="sifApp.extraUser.adresse">Adresse</Translate>
                </dt>
                <dd className="col-sm-9">{matchingUserExtra?.adresse}</dd>
                <dt className="col-sm-3">
                  <Translate contentKey="sifApp.extraUser.pays">Pays</Translate>
                </dt>
                <dd className="col-sm-9">{matchingUserExtra?.pays}</dd>

                <dt className="col-sm-3">
                  <Translate contentKey="sifApp.magasinier.magasin">Magasin</Translate>
                </dt>
                <dd className="col-sm-9">{magasinierEntity.magasin ? magasinierEntity.magasin.codeMagasin : ''}</dd>
              </dl>
            </div>
            <Button tag={Link} to="/magasinier" replace color="info" className="mt-3">
              <FontAwesomeIcon icon="arrow-left" />{' '}
              <span className="d-none d-md-inline">
                <Translate contentKey="entity.action.back">Back</Translate>
              </span>
            </Button>
            &nbsp;
            <Button tag={Link} to={`/magasinier/${magasinierEntity.id}/edit`} replace color="primary" className="mt-3">
              <FontAwesomeIcon icon="pencil-alt" />{' '}
              <span className="d-none d-md-inline">
                <Translate contentKey="entity.action.edit">Edit</Translate>
              </span>
            </Button>
          </CardBody>
        </Card>
      </Col>
    </Row>
  );
};

export default MagasinierDetail;
