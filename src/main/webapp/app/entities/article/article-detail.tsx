import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './article.reducer';

export const ArticleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const articleEntity = useAppSelector(state => state.article.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="articleDetailsHeading">
          <Translate contentKey="sifApp.article.detail.title">Article</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{articleEntity.id}</dd>
          <dt>
            <span id="cai">
              <Translate contentKey="sifApp.article.cai">Cai</Translate>
            </span>
          </dt>
          <dd>{articleEntity.cai}</dd>
          <dt>
            <span id="refPneu">
              <Translate contentKey="sifApp.article.refPneu">Ref Pneu</Translate>
            </span>
          </dt>
          <dd>{articleEntity.refPneu}</dd>
          <dt>
            <span id="typePneu">
              <Translate contentKey="sifApp.article.typePneu">Type Pneu</Translate>
            </span>
          </dt>
          <dd>{articleEntity.typePneu}</dd>
          <dt>
            <span id="valeur">
              <Translate contentKey="sifApp.article.valeur">Valeur</Translate>
            </span>
          </dt>
          <dd>{articleEntity.valeur}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="sifApp.article.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {articleEntity.image ? (
              <div>
                {articleEntity.imageContentType ? (
                  <a onClick={openFile(articleEntity.imageContentType, articleEntity.image)}>
                    <img src={`data:${articleEntity.imageContentType};base64,${articleEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {articleEntity.imageContentType}, {byteSize(articleEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="devise">
              <Translate contentKey="sifApp.article.devise">Devise</Translate>
            </span>
          </dt>
          <dd>{articleEntity.devise}</dd>
        </dl>
        <Button tag={Link} to="/article" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/article/${articleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ArticleDetail;
