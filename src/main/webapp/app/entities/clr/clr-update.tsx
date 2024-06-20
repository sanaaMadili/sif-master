import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getEntities as getReceptions } from 'app/entities/reception/reception.reducer';
import{ createEntity as createEntityExtraUser,updateEntity as updateEntityExtraUser } from 'app/entities/extra-user/extra-user.reducer';
import { getEntities as getExtraUsers } from 'app/entities/extra-user/extra-user.reducer';
import { getEntities as getReclamations } from 'app/entities/reclamation/reclamation.reducer';
import { getEntities as getArticles } from 'app/entities/article/article.reducer';
import { Table, Button, Row, Col, FormText } from 'reactstrap';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAppelCommande } from 'app/shared/model/appel-commande.model';
import { getEntities as getLigneCommandes } from 'app/entities/ligne-commande/ligne-commande.reducer';
import { IClr } from 'app/shared/model/clr.model';
import { getEntity, updateEntity, createEntity, reset } from './clr.reducer';
import { getEntities as getCommandes,getEntity as getCommande, updateEntity as updateCommande, createEntity as createCommande } from 'app/entities/appel-commande/appel-commande.reducer';
import { getEntities as getClients, getEntity as getClient, updateEntity as updateClient, createEntity as createClient} from 'app/entities/client/client.reducer';
import { getEntity as getLigne, updateEntity as updateLigne, createEntity as createLigne} from 'app/entities/ligne-commande/ligne-commande.reducer';
const ClrUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const account = useAppSelector(state => state.authentication.account);
  const articles = useAppSelector(state => state.article.entities);
  const { id } = useParams<'id'>();
  const isNew = id === undefined;
  const receptions = useAppSelector(state => state.reception.entities);
  const clients = useAppSelector(state => state.client.entities);
  const extraUsers = useAppSelector(state => state.extraUser.entities);
  const appelCommandes = useAppSelector(state => state.appelCommande.entities);
  const ligneCommandes = useAppSelector(state => state.ligneCommande.entities);
  const [selectedReception, setSelectedReception] = useState(null);

  const clrEntity = useAppSelector(state => state.clr.entity);
  const loading = useAppSelector(state => state.clr.loading);
  const updating = useAppSelector(state => state.clr.updating);
  const updateSuccess = useAppSelector(state => state.clr.updateSuccess);
  const [fileExtension, setFileExtension] = useState(null);
  const [selectedTypePneu, setSelectedTypePneu] = useState('');
  const [selectedRefPneu, setSelectedRefPneu] = useState('');
  const [formData, setFormData] = useState({ qte: '', article: '', typePneu: '', refPneu: '' });
  const [editingRowIndex, setEditingRowIndex] = useState(null);
const [editedQte, setEditedQte] = useState('');
const [editedArticle, setEditedArticle] = useState('');
const [editedValeur, setEditedValeur] = useState(''); // Ajoutez cet état pour gérer la valeur éditée de l'article

  const handleClose = () => {
    navigate('/clr' + location.search);
  };
  
  const startEditing = index => {
    setEditingRowIndex(index);
    setEditedQte(tableData[index].qte);
    setEditedArticle(tableData[index].article.id);
    setEditedValeur(tableData[index].article.valeur); // Initialisez la valeur éditée

  };
  
  const saveEditing = index => {
    const updatedTableData = [...tableData];
    const updatedArticle = articles.find(it => it.id.toString() === editedArticle.toString());
    updatedTableData[index] = { ...updatedTableData[index], qte: editedQte, article: { ...updatedArticle, valeur: editedValeur }};
    setTableData(updatedTableData);
    setEditingRowIndex(null);
     // Mettre à jour la valeur de l'article édité
  setEditedValeur(updatedArticle.valeur);
  };
  
  const cancelEditing = () => {
    setEditingRowIndex(null);
  };
  
  
  useEffect(() => {
    if (isNew) {
      dispatch(reset());
      dispatch(getArticles({}));
    } else {
      dispatch(getEntity(id));
      dispatch(getArticles({}));
    }

    dispatch(getCommandes({}));
    dispatch(getReceptions({}));
    dispatch(getExtraUsers({}));
    dispatch(getReclamations({}));
    dispatch(getClients({}));
    dispatch(getArticles({}));
    dispatch(getLigneCommandes({}))
  }, []);

  const handleTypePneuChange = event => {
    const typePneu = event.target.value;
    setSelectedTypePneu(typePneu);
    setFormData(prevState => ({ 
      ...prevState, 
      typePneu: typePneu, 
      refPneu: '', 
      article: '' 
    }));
  };

  const handleRefPneuChange = event => {
    const refPneu = event.target.value;
    setSelectedRefPneu(refPneu);
    setFormData(prevState => {
      const updatedFormData = { ...prevState, refPneu: refPneu };
      const article = articles.find(it => it.refPneu === updatedFormData.refPneu);
      if (article) {
        updatedFormData.article = article.id;
        // Mettre à jour automatiquement le prix de l'article
        setFormData(prevState => ({ ...prevState, valeur: article.valeur }));
      }
      return updatedFormData;
    });
  };
  

  const handleFileChange = event => {
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
    if (!isNew) {
  
      if (clrEntity && clrEntity.appelCommande) {
        const comm = appelCommandes.find((c) => c.id === clrEntity.appelCommande.id);
  
        if (comm) {
          const ligneComm = ligneCommandes.filter((lc) => lc.appelCommande && lc.appelCommande.id === comm.id);
          setTableData(ligneComm);
        }
        if (comm) {
          const recep = receptions.find((r) => r.id === comm.reception.id);
          setSelectedReception(recep);
          
        }
      }
  
      
    }
  }, [clrEntity, isNew, appelCommandes, ligneCommandes, receptions]);
  
  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const generateRandomSixDigitNumber = (): number => {
    return Math.floor(100000 + Math.random() * 900000);
  };

  const [tableData, setTableData] = useState([]);

  const handleFormChange = event => {
    const { name, value } = event.target;
    setFormData(prevState => ({ 
      ...prevState, 
      [name]: value 
    }));
  };

  const addTableRow = () => {
    const article = articles.find(it => it.id.toString() === formData.article.toString());
    const newRow = { qte: formData.qte, article: article };
    setTableData([...tableData, newRow]);
    setFormData({ qte: '', article: '', typePneu: '', refPneu: '' });
  };

  const deleteTableRow = index => {
    const newData = [...tableData];
    newData.splice(index, 1);
    setTableData(newData);
  };

  const saveEntity = async values => {
    if (isNew) {
    const numCommande = generateRandomSixDigitNumber();
    const matchingUserExtra = extraUsers.find(userextra => userextra.user.id === account?.id);
    console.log("extrauser",matchingUserExtra)
    const matchingClient = clients.find(client => client.extraUser.id === matchingUserExtra.id);
    console.log("client",clients)
    console.log("client",matchingClient)
    // Get the current date in the desired format (e.g., 'YYYY-MM-DD')
    const currentDate = new Date().toISOString().split('T')[0];
    const commandeEntity = {
      numCommande: numCommande,
      dateCommande: currentDate,
      dateLivraison: '0001-01-01',
      dateExpedition: '0001-01-01',
      status: 0,
      annomalie: 0,
      reception: receptions.find(it => it.id.toString() === values.reception.toString()),
      client: matchingClient,
    };
    const savedEntityComm = await dispatch(createCommande(commandeEntity));
    const newCommande = savedEntityComm.payload["data"];

    for (const row of tableData) {
      const ligneComm = {
        qte: row.qte,
        article: row.article,
        appelCommande: newCommande,
      };
      await dispatch(createLigne(ligneComm));
    }

    const entity = {
      constructeurAutomobile: values.constructeurAutomobile,
      modelVoiture: values.modelVoiture,
      anneeVoiture: values.anneeVoiture,
      etatPneu: values.etatPneu,
      image: values.image,
      imageContentType: "image/" + fileExtension,
      dateProduction: values.dateProduction,
      appelCommande: newCommande,
    };

    
      dispatch(createEntity(entity));
    } else {
    const comm = appelCommandes.find(c => c.id === clrEntity.appelCommande.id);
    console.log("commande",comm)
    
    const entity = {
      ...clrEntity,
      constructeurAutomobile: values.constructeurAutomobile,
      modelVoiture: values.modelVoiture,
      anneeVoiture: values.anneeVoiture,
      etatPneu: values.etatPneu,
      image: values.image,
      imageContentType: "image/" + fileExtension,
      dateProduction: values.dateProduction,
      appelCommande: comm,
    };
    dispatch(updateEntity(entity));

    for (const row of tableData) {
      const ligneComm = {
        id: row.id,  // Assuming that 'row' contains an 'id' field for existing line items
        qte: row.qte,
        article: row.article,
        appelCommande: comm,
      };
      dispatch(updateLigne(ligneComm));
    }
    // Update the reception for the commande
    const updatedReception = receptions.find(it => it.id.toString() === values.reception.toString());
    if (updatedReception) {
      const updatedCommande = {
        ...comm,
        reception: updatedReception
      };
      await dispatch(updateCommande(updatedCommande));
    }
  }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...clrEntity,
          appelCommande: clrEntity?.appelCommande?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sifApp.clr.home.createOrEditLabel" data-cy="ClrCreateUpdateHeading">
            <Translate contentKey="sifApp.clr.home.createOrEditLabel">Create or edit a Clr</Translate>
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
                  id="clr-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sifApp.clr.constructeurAutomobile')}
                id="clr-constructeurAutomobile"
                name="constructeurAutomobile"
                data-cy="constructeurAutomobile"
                type="text"
              />
              <ValidatedField
                label={translate('sifApp.clr.modelVoiture')}
                id="clr-modelVoiture"
                name="modelVoiture"
                data-cy="modelVoiture"
                type="text"
              />
              <ValidatedField
                label={translate('sifApp.clr.anneeVoiture')}
                id="clr-anneeVoiture"
                name="anneeVoiture"
                data-cy="anneeVoiture"
                type="text"
              />
              <ValidatedField label={translate('sifApp.clr.etatPneu')} id="clr-etatPneu" name="etatPneu" data-cy="etatPneu" type="text" />
              <ValidatedBlobField
                label={translate('sifApp.clr.image')}
                id="clr-image"
                name="image"
                data-cy="image"
                isImage
                accept="image/*"
                onChange={handleFileChange}
              />
              <ValidatedField
                label={translate('sifApp.clr.dateProduction')}
                id="clr-dateProduction"
                name="dateProduction"
                data-cy="dateProduction"
                type="date"
              />
              <ValidatedField
              id="appel-commande-reception"
              name="reception"
              data-cy="reception"
              label={translate('sifApp.appelCommande.reception')}
              type="select"
            >
              <option value="" key="0" />
              {receptions
                ? receptions.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.pays}
                    </option>
                  ))
                : null}
            </ValidatedField>

              <ValidatedField
                id="article.typePneu"
                name="typePneu"
                data-cy="typePneu"
                label={translate('sifApp.article.typePneu')}
                type="select"
                value={formData.typePneu}
                onChange={handleTypePneuChange}
              >
                <option value="" key="0" />
                {articles.map(otherEntity => (
                  <option value={otherEntity.typePneu} key={otherEntity.id}>
                    {otherEntity.typePneu}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="article.refPneu"
                name="refPneu"
                data-cy="refPneu"
                label={translate('sifApp.article.refPneu')}
                type="select"
                value={formData.refPneu}
                onChange={handleRefPneuChange}
              >
                <option value="" key="0" />
                {articles
                  ? articles.map(otherEntity => (
                      <option value={otherEntity.refPneu} key={otherEntity.id}>
                        {otherEntity.refPneu}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('sifApp.ligneCommande.qte')}
                id="ligne-commande-qte"
                name="qte"
                data-cy="qte"
                type="text"
                value={formData.qte}
                onChange={handleFormChange}
              />
              <ValidatedField
                id="ligne-commande-article"
                name="article"
                data-cy="article"
                label={translate('sifApp.ligneCommande.article')}
                type="select"
                value={formData.article}
                onChange={handleFormChange}
              >
                <option value="" key="0" />
                {articles
                  .filter(article => article.refPneu.toString() === selectedRefPneu.toString())
                  .map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.id}
                    </option>
                  ))}
              </ValidatedField>
              <Button color="secondary" onClick={addTableRow} disabled={!formData.qte || !formData.article}>
                <FontAwesomeIcon icon="plus" />
                &nbsp;
                ADD
              </Button>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/clr" replace color="info">
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
        <Col md="4">
          <Table striped>
            <thead>
              <tr>
                <th>Quantity</th>
                <th>Article</th>
                <th>Prix</th>
                <th>Supprimer</th>
              </tr>
            </thead>
            <tbody>
  {tableData.map((data, index) => (
    <tr key={index}>
      {editingRowIndex === index ? (
        <>
          <td>
            <input
              type="text"
              value={editedQte}
              onChange={e => setEditedQte(e.target.value)}
            />
          </td>
          <td>
          <select
            value={editedArticle}
            onChange={e => {
              setEditedArticle(e.target.value);
              const selectedArticle = articles.find(article => article.id.toString() === e.target.value);
              if (selectedArticle) {
                setEditedValeur(selectedArticle.valeur);
              }
            }}
          >
            {articles.map(article => (
              <option key={article.id} value={article.id}>
                {article.id}
              </option>
            ))}
          </select>
        </td>
          <td>{editedValeur}</td> {/* Afficher la nouvelle valeur de l'article */}

          <td>
            <Button color="primary" onClick={() => saveEditing(index)}>
              Save
            </Button>
            <Button color="secondary" onClick={cancelEditing}>
              Cancel
            </Button>
          </td>
        </>
      ) : (
        <>
          <td>{data.qte}</td>
          <td>{data.article.id}</td>
          <td>{data.article.valeur}</td>
          <td>
            <Button color="warning" onClick={() => startEditing(index)}>
              Edit
            </Button>
            <Button color="danger" onClick={() => deleteTableRow(index)}>
              <FontAwesomeIcon icon="trash" />
            </Button>
          </td>
        </>
      )}
    </tr>
  ))}
</tbody>

          </Table>
        </Col>
      </Row>
    </div>
  );
};

export default ClrUpdate;
