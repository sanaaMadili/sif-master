import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getEntities as getExtraUsers } from 'app/entities/extra-user/extra-user.reducer';
import { getEntities as getMagasins } from 'app/entities/magasin/magasin.reducer';
import { getUsers,createUser,getRoles,updateUser } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity as getStock, updateEntity as updateStock, createEntity as createStock} from 'app/entities/etat-stock/etat-stock.reducer';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getMagasiniers} from 'app/entities/magasinier/magasinier.reducer';

import { IArticle } from 'app/shared/model/article.model';
import { getEntities, getEntity, updateEntity, createEntity, reset } from './article.reducer';

export const ArticleUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;
  const account = useAppSelector(state => state.authentication.account);
  const extraUsers = useAppSelector(state => state.extraUser.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const magasiniers = useAppSelector(state => state.magasinier.entities);
  const magasins = useAppSelector(state => state.magasin.entities);
  const articles = useAppSelector(state => state.article.entities);

  const articleEntity = useAppSelector(state => state.article.entity);
  const loading = useAppSelector(state => state.article.loading);
  const updating = useAppSelector(state => state.article.updating);
  const updateSuccess = useAppSelector(state => state.article.updateSuccess);
  const [fileExtension, setFileExtension] = useState(null);

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const fileName = file.name;
      const fileExtension = fileName.split('.').pop();
      setFileExtension(fileExtension);
    } else {
      setFileExtension(null);
    }
  };
  const handleClose = () => {
    navigate('/article' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
      dispatch(getExtraUsers({}));
      dispatch(getUsers({}));
      dispatch(getMagasiniers({}));
      dispatch(getMagasins({}));
      dispatch(getEntities({}));

      


    } else {
      dispatch(getEntity(id));
      dispatch(getExtraUsers({}));
      dispatch(getUsers({}));
      dispatch(getMagasiniers({}));
      dispatch(getMagasins({}));
      dispatch(getEntities({}));


    }
    dispatch(getExtraUsers({}));
    dispatch(getUsers({}));
    dispatch(getMagasiniers({}));
    dispatch(getMagasins({}));
    dispatch(getEntities({}));


  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = async values => {
    // Création ou mise à jour de l'entité "article"
    const entity = {
      ...articleEntity,
      ...values,
      imageContentType: "image/" + fileExtension,
      // Ajoutez d'autres champs requis si nécessaire
    };
    console.log("articleentity",entity)
    if (isNew) {

      const savedEntityComm = await dispatch(createEntity(entity));
      const newarticle = savedEntityComm.payload["data"];
       // Si vous devez également créer une nouvelle entité "article"
    
    const matchingUserExtra = extraUsers.find(userextra => userextra.user.id === account?.id);
    console.log(" matchingUserExtra", matchingUserExtra)

    const matchingMagasinier = magasiniers.find((m) => m.extraUser.id === matchingUserExtra?.id);
    console.log("matchingMagasinier",matchingMagasinier)

    const matchingMagasin = magasins.find((m) => m.id === matchingMagasinier?.magasin?.id);
    console.log("magasin",matchingMagasin)
    console.log("articles",articles)

    const matchingArticle = articles.find((a) => a.cai === entity?.cai);
    console.log("matchingArticle",matchingArticle)

    // Création de l'objet pour l'entité "etat stock"
    const etatStockEntity = {
      qte: values.qte, // La quantité
      location: values.location, // La localisation
      article: newarticle, // L'article associé à l'entrée d'état de stock
      magasin: matchingMagasin, // Le magasin associé à l'entrée d'état de stock
      // Ajoutez d'autres champs requis si nécessaire
    };
  
    // Utilisez la fonction createEntity pour créer une nouvelle entrée dans la table "etat stock"
    dispatch(createStock(etatStockEntity));
  }
  else {
      dispatch(updateEntity(entity)); // Si vous devez mettre à jour une entité "article" existante
    }
};


  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...articleEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sifApp.article.home.createOrEditLabel" data-cy="ArticleCreateUpdateHeading">
            <Translate contentKey="sifApp.article.home.createOrEditLabel">Create or edit a Article</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="article-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('sifApp.article.cai')} id="article-cai" name="cai" data-cy="cai" type="text" />
              <ValidatedField
                label={translate('sifApp.article.refPneu')}
                id="article-refPneu"
                name="refPneu"
                data-cy="refPneu"
                type="text"
              />
              <ValidatedField
                label={translate('sifApp.article.typePneu')}
                id="article-typePneu"
                name="typePneu"
                data-cy="typePneu"
                type="text"
              />
              <ValidatedField label={translate('sifApp.article.valeur')} id="article-valeur" name="valeur" data-cy="valeur" type="text" />
              <ValidatedBlobField
                label={translate('sifApp.article.image')}
                id="article-image"
                name="image"
                data-cy="image"
                isImage
                accept="image/*"
                onChange={handleFileChange}
              />
              <ValidatedField label={translate('sifApp.article.devise')} id="article-devise" name="devise" data-cy="devise" type="text" />
              <ValidatedField label={translate('sifApp.etatStock.qte')} id="etat-stock-qte" name="qte" data-cy="qte" type="text" />
              <ValidatedField
                label={translate('sifApp.etatStock.location')}
                id="etat-stock-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/article" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ArticleUpdate;
