import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm, isEmail, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getEntities as getExtraUsers } from 'app/entities/extra-user/extra-user.reducer';

import { getEntities as getClients, getEntity as getClient, updateEntity as updateClient, createEntity as createClient} from 'app/entities/client/client.reducer';

import { getEntities as getReclamations } from 'app/entities/reclamation/reclamation.reducer';
import{ createEntity as createEntityExtraUser,updateEntity as updateEntityExtraUser } from 'app/entities/extra-user/extra-user.reducer';

import { locales, languages } from 'app/config/translation';
import { getUser, getRoles, updateUser, createUser, reset } from './user-management.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserManagementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const clientEntity = useAppSelector(state => state.client.entities);
  const clients = useAppSelector(state => state.client.entities);
  const userEntityy = useAppSelector(state => state.client.entity);
  const extraUsers = useAppSelector(state => state.extraUser.entities);
  const reclamations = useAppSelector(state => state.reclamation.entities);
  const { login } = useParams<'login'>();
  const isNew = login === undefined;
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
  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getUser(login));
      dispatch(getClients({}));
    }
    dispatch(getRoles());
    dispatch(getExtraUsers({}));
    dispatch(getReclamations({}));
    dispatch(getClients({}));

    return () => {
      dispatch(reset());
    };
  }, [login]);

  const handleClose = () => {
    navigate('/admin/user-management');
  };

  const saveUser = async values => {
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
      const savedEntityUser = await dispatch(createUser(userEntity));
      const newUser = savedEntityUser.payload["data"];
        const extraUserEntity = {
        cin: values.cin,
        image: values.image,
        imageContentType:"image/"+fileExtension,
        numeroTelephone: values.numeroTelephone,
        dateNaissance: values.dateNaissance,
        adresse: values.adresse,
        pays: values.pays,
        user: newUser,
      };
      const savedEntityExtraUser = await dispatch(createEntityExtraUser(extraUserEntity));
      const newExtraUser = savedEntityExtraUser.payload["data"];
      const entityClient = {
        profession: values.profession,
        extraUser: newExtraUser,
      };
      
      await dispatch(createClient(entityClient));
      /*dispatch(createUser(values));*/
    } else {
             const userEntity = {
              id: user.id,
              login: values.login,
              firstName: values.firstName,
              lastName: values.lastName,
              email: values.email,
              activated: values.activated,
              langKey: values.langKey,
              authorities: ['ROLE_USER'],
            };
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
            const matchingClient = clients.find(client => client.extraUser.id === newExtraUser.id);
            const entityClient = {
              id: matchingClient?.id,
              profession: values.profession,
              extraUser: newExtraUser,
            };
      
      await dispatch(updateClient(entityClient));
      /*dispatch(updateUser(values));*/
    }
    handleClose();
  };
  const isInvalid = false;
  const user = useAppSelector(state => state.userManagement.user);
  const loading = useAppSelector(state => state.userManagement.loading);
  const updating = useAppSelector(state => state.userManagement.updating);
  const authorities = useAppSelector(state => state.userManagement.authorities);

  
  const matchingUserExtra = extraUsers.find((userextra) => userextra.user.id === user?.id);
  console.log("les clients",clients)
  const matchingClient = clients.find((client) => client.extraUser.id === matchingUserExtra?.id);
  const defaultValues = () => (isNew ? {} : {
    
    ...user,
    cin: matchingUserExtra?.cin,
    image: matchingUserExtra?.image,
    imageContentType: matchingUserExtra?.imageContentType,
    numeroTelephone: matchingUserExtra?.numeroTelephone,
    dateNaissance: matchingUserExtra?.dateNaissance,
    adresse: matchingUserExtra?.adresse,
    pays: matchingUserExtra?.pays,
    profession: matchingClient?.profession,    
    });
    
  
  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>
          Créer ou éditer un client
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveUser} >
              {user.id ? (
                <ValidatedField
                  type="text"
                  name="id"
                  required
                  readOnly
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
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
                
                  <option >
                    Role_User
                  </option>
                
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
                label={translate('sifApp.client.profession')}
                id="client-profession"
                name="profession"
                data-cy="profession"
                type="text"
              />
              <Button tag={Link} to="/admin/user-management" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" type="submit" disabled={isInvalid || updating}>
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

export default UserManagementUpdate;
