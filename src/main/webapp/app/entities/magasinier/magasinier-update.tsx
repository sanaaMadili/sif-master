import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate, ValidatedField, ValidatedForm, isEmail, ValidatedBlobField } from 'react-jhipster';
import { locales, languages } from 'app/config/translation';
import { getEntities as getExtraUsers } from 'app/entities/extra-user/extra-user.reducer';
import { createEntity as createEntityExtraUser, updateEntity as updateEntityExtraUser } from 'app/entities/extra-user/extra-user.reducer';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getMagasins } from 'app/entities/magasin/magasin.reducer';
import { getEntities as getMagasiniers, getEntity, updateEntity, createEntity, reset} from './magasinier.reducer';
import { getUsers,createUser,getRoles,updateUser } from 'app/modules/administration/user-management/user-management.reducer';

export const MagasinierUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;
  const [selectedMagasin, setSelectedMagasin] = useState(null);

  const magasins = useAppSelector(state => state.magasin.entities);
  const magasinierEntity = useAppSelector(state => state.magasinier.entity);
  const loading = useAppSelector(state => state.magasinier.loading);
  const user = useAppSelector(state => state.userManagement.user);
  const updating = useAppSelector(state => state.magasinier.updating);
  const updateSuccess = useAppSelector(state => state.magasinier.updateSuccess);
  const [fileExtension, setFileExtension] = useState(null);
  const authorities = useAppSelector(state => state.userManagement.authorities);
  const magasiniers = useAppSelector(state => state.magasinier.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const extraUsers = useAppSelector(state => state.extraUser.entities);

  const handleClose = () => {
    navigate('/magasinier' + location.search);
  };

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

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
      dispatch(getMagasiniers({})); // Ensure that magasiniers are fetched
      dispatch(getUsers({}));

    }

    dispatch(getExtraUsers({}));
    dispatch(getMagasins({}));
    dispatch(getRoles());
    dispatch(getUsers({}));
    dispatch(getMagasiniers({})); // Ensure that magasiniers are fetched
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = async values => {
    if (isNew) {
    const userEntity = {
      login: values.login,
      firstName: values.firstName,
      lastName: values.lastName,
      email: values.email,
      activated: values.activated,
      langKey: values.langKey,
      authorities: ['ROLE_USER'],
    };
    console.log("userEntity",userEntity)

    const savedEntityUser = await dispatch(createUser(userEntity));
    const newUser = savedEntityUser.payload["data"];

    const extraUserEntity = {
      cin: values.cin,
      image: values.image,
      imageContentType: "image/" + fileExtension,
      numeroTelephone: values.numeroTelephone,
      dateNaissance: values.dateNaissance,
      adresse: values.adresse,
      pays: values.pays,
      user: newUser,
    };

    const savedEntityExtraUser = await dispatch(createEntityExtraUser(extraUserEntity));
    const newExtraUser = savedEntityExtraUser.payload["data"];

    const entity = {
      profession: values.profession,
      extraUser: newExtraUser,
      magasin: magasins.find(it => it.id.toString() === values.magasin.toString()),
    };

    
    dispatch(createEntity(entity));
    } else {

      const matchingMagasinier = magasiniers.find((m) => m.id === magasinierEntity?.id);
      const matchingextra = extraUsers.find((u) => u.id === matchingMagasinier?.extraUser?.id);
      const matchingUser = users.find((u) => u.id === matchingextra?.user.id);      
      
      const userEntity = {
        id: matchingUser.id,
        login: values.login,
        firstName: values.firstName,
        lastName: values.lastName,
        email: values.email,
        activated: values.activated,
        langKey: values.langKey,
        authorities: ['ROLE_USER'],
      };
      console.log("user",user)
      const savedEntityUser = await dispatch(updateUser(userEntity));
            const newUser = savedEntityUser.payload["data"];
            const matchingUserExtra = extraUsers.find((userextra) => userextra.user.id === user?.id);
            console.log('matchingextra', matchingUserExtra)
            const extraUserEntity = {
              id: matchingUserExtra?.id,
              cin: values.cin,
              image: values.image,
              imageContentType:"image/"+fileExtension,
              numeroTelephone: values.numeroTelephone,
              dateNaissance: values.dateNaissance,
              pays: values.pays,
              adresse: values.adresse,
              user: newUser,
            };
            const savedEntityExtraUser = await dispatch(updateEntityExtraUser(extraUserEntity));
            const newExtraUser = savedEntityExtraUser.payload["data"];
            const entity = {
              id:matchingMagasinier?.id,
              profession: values.profession,
              extraUser: newExtraUser,
              magasin: magasins.find(it => it.id.toString() === values.magasin.toString()),
              
            };
      
      dispatch(updateEntity(entity));
    }
  };
  const defaultValues = () => {
    if (isNew) {
      return {};
    } else {
      const matchingUserExtra = extraUsers.find((extra) => extra.id === magasinierEntity?.extraUser?.id);
      console.log("matchingUserExtra",matchingUserExtra)
      const loginValue = matchingUserExtra ? matchingUserExtra.user.login : '';
      const firstnamee = matchingUserExtra ? matchingUserExtra.user.firstName : '';
      const lastnamee = matchingUserExtra ? matchingUserExtra.user.lastName : '';
      const images = matchingUserExtra ? magasinierEntity.extraUser.image : '';
      const imageContentType= matchingUserExtra?.imageContentType;

      const email = matchingUserExtra ? matchingUserExtra.user.email : '';
      const langKeyy = matchingUserExtra ? matchingUserExtra.user.langKey : '';
      const authoritiess = matchingUserExtra ? matchingUserExtra.user.authorities : ['Role_USER']; // Définit le rôle par défaut à "Role_USER"
      return {
        ...magasinierEntity,
        extraUserId: matchingUserExtra || null,
        cin: magasinierEntity?.extraUser?.cin || '',
        image: magasinierEntity?.extraUser?.image,
        imageContentType: magasinierEntity?.extraUser?.imageContentType,
        numeroTelephone: magasinierEntity?.extraUser?.numeroTelephone || '',
        dateNaissance: magasinierEntity?.extraUser?.dateNaissance || '',
        adresse: magasinierEntity?.extraUser?.adresse || '',
        pays: magasinierEntity?.extraUser?.pays || '',
        login: loginValue,
        firstName: firstnamee,
        lastName: lastnamee,
        email: email,
        langKey: langKeyy,
        authorities: authoritiess,
        profession: magasinierEntity?.profession,
        magasin: magasinierEntity?.magasin?.id,

      };
    }
  };


  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sifApp.magasinier.home.createOrEditLabel" data-cy="MagasinierCreateUpdateHeading">
            <Translate contentKey="sifApp.magasinier.home.createOrEditLabel">Create or edit a Magasinier</Translate>
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
                  id="magasinier-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sifApp.magasinier.profession')}
                id="magasinier-profession"
                name="profession"
                data-cy="profession"
                type="text"
              />
              <ValidatedField
                type="text"
                name="login"
                label={translate('userManagement.login')}
                validate={{
                  required: {
                    value: true,
                    message: translate('register.messages.validate.login.required'),
                  },
                  pattern: {
                    value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
                    message: translate('register.messages.validate.login.pattern'),
                  },
                  minLength: {
                    value: 1,
                    message: translate('register.messages.validate.login.minlength'),
                  },
                  maxLength: {
                    value: 50,
                    message: translate('register.messages.validate.login.maxlength'),
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="firstName"
                label={translate('userManagement.firstName')}
                validate={{
                  maxLength: {
                    value: 50,
                    message: translate('entity.validation.maxlength', { max: 50 }),
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="lastName"
                label={translate('userManagement.lastName')}
                validate={{
                  maxLength: {
                    value: 50,
                    message: translate('entity.validation.maxlength', { max: 50 }),
                  },
                }}
              />
              <FormText>This field cannot be longer than 50 characters.</FormText>
              <ValidatedField
                name="email"
                label={translate('global.form.email.label')}
                placeholder={translate('global.form.email.placeholder')}
                type="email"
                validate={{
                  required: {
                    value: true,
                    message: translate('global.messages.validate.email.required'),
                  },
                  minLength: {
                    value: 5,
                    message: translate('global.messages.validate.email.minlength'),
                  },
                  maxLength: {
                    value: 254,
                    message: translate('global.messages.validate.email.maxlength'),
                  },
                  validate: v => isEmail(v) || translate('global.messages.validate.email.invalid'),
                }}
              />
              <ValidatedField
                type="checkbox"
                name="activated"
                check
                value={true}
                disabled={!user.id}
                label={translate('userManagement.activated')}
              />
              <ValidatedField type="select" name="langKey" label={translate('userManagement.langKey')}>
                {locales.map(locale => (
                  <option value={locale} key={locale}>
                    {languages[locale].name}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField type="select" name="authorities" multiple label={translate('userManagement.profiles')}>
                {authorities.map(role => (
                  <option value={role} key={role}>
                    {role}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label={translate('sifApp.extraUser.cin')} id="extra-user-cin" name="cin" data-cy="cin" type="text" />
              <ValidatedBlobField
                label={translate('sifApp.extraUser.image')}
                id="extra-user-image"
                name="image"
                data-cy="image"
                isImage
                accept="image/*"
                onChange={handleFileChange}
              />
              <ValidatedField
                label={translate('sifApp.extraUser.numeroTelephone')}
                id="extra-user-numeroTelephone"
                name="numeroTelephone"
                data-cy="numeroTelephone"
                type="text"
              />
              <ValidatedField
                label={translate('sifApp.extraUser.dateNaissance')}
                id="extra-user-dateNaissance"
                name="dateNaissance"
                data-cy="dateNaissance"
                type="date"
              />
              <ValidatedField
                label={translate('sifApp.extraUser.adresse')}
                id="extra-user-adresse"
                name="adresse"
                data-cy="adresse"
                type="text"
              />
              <ValidatedField label={translate('sifApp.extraUser.pays')} id="extra-user-pays" name="pays" data-cy="pays" type="text" />
              <ValidatedField
                id="magasinier-magasin"
                name="magasin"
                data-cy="magasin"
                label={translate('sifApp.magasinier.magasin')}
                type="select"
              >
                <option value="" key="0" />
                {magasins
                  ? magasins.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/magasinier" replace color="info">
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

export default MagasinierUpdate;
