import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getEntities as getReceptions } from 'app/entities/reception/reception.reducer';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons'; // Assurez-vous d'avoir importé cette icône
import { faFilePdf } from '@fortawesome/free-solid-svg-icons';
import { getUsers,createUser,getRoles,updateUser } from 'app/modules/administration/user-management/user-management.reducer';
import { Button, Table, Modal, ModalHeader, ModalBody, ModalFooter, Col,Row } from 'reactstrap';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getMagasins } from 'app/entities/magasin/magasin.reducer';
import jsPDF from 'jspdf';
import { faFileCsv } from '@fortawesome/free-solid-svg-icons';
import { getEntities as getLigneCommandes } from 'app/entities/ligne-commande/ligne-commande.reducer';
import { getEntities as getArticles } from 'app/entities/article/article.reducer';
import autoTable from 'jspdf-autotable';
import { IAppelCommande } from 'app/shared/model/appel-commande.model';
import { getEntities } from './appel-commande.reducer';
import { getEntities as getExtraUsers } from 'app/entities/extra-user/extra-user.reducer';
import { getEntities as getEtatStocks, updateEntity as updateEtatStock } from 'app/entities/etat-stock/etat-stock.reducer';

import { getEntities as getClients, getEntity as getClient, updateEntity as updateClient, createEntity as createClient} from 'app/entities/client/client.reducer';
import { getEntities as getMagasiniers, getEntity, updateEntity, createEntity, reset} from 'app/entities/magasinier/magasinier.reducer';
import axios from 'axios';
export const AppelCommande = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const users = useAppSelector(state => state.userManagement.users);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );
  const magasiniers = useAppSelector(state => state.magasinier.entities);
  const magasins = useAppSelector(state => state.magasin.entities);
  const receptions = useAppSelector(state => state.reception.entities);
  const articles = useAppSelector(state => state.article.entities);
  const etatStocks = useAppSelector(state => state.etatStock.entities);

  const account = useAppSelector(state => state.authentication.account);
  const clients = useAppSelector(state => state.client.entities);
  const extraUsers = useAppSelector(state => state.extraUser.entities);
  const appelCommandeList = useAppSelector(state => state.appelCommande.entities);
  const loading = useAppSelector(state => state.appelCommande.loading);
  const totalItems = useAppSelector(state => state.appelCommande.totalItems);
  const [status, setStatus] = useState(0);
  const ligneCommandes = useAppSelector(state => state.ligneCommande.entities);

  const handleValidateCommande = async (id) => {
    try {
      const response = await axios.patch(`/api/appel-commandes/${id}`, { status: 13 });
      if (response.status === 200) {
        setStatus(13); // Met à jour le status dans l'état local
      }
    } catch (error) {
      console.error('Erreur lors de la validation de la commande', error);
    }
  };
  const getAllEntities = () => {
    dispatch(getClients({}));
    dispatch(getExtraUsers({}));
    dispatch(getReceptions({}));
    dispatch(getMagasiniers({}));
    dispatch(getMagasins({}));
    dispatch(getLigneCommandes({}))
    dispatch(getEtatStocks({}))

    dispatch(getArticles({}));
    dispatch(getUsers({}));



    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };
  // Dans votre composant AppelCommande, ajoutez une fonction pour valider la commande
  
  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };
  

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };
  const [commandeArticles, setCommandeArticles] = useState([]);
  
  const [etatStocksLoaded, setEtatStocksLoaded] = useState(false);
  const [ligneCommandesLoaded, setLigneCommandesLoaded] = useState(false);

  useEffect(() => {
    dispatch(getEtatStocks({})).then(() => setEtatStocksLoaded(true));
    dispatch(getLigneCommandes({})).then(() => setLigneCommandesLoaded(true));
  }, []);


const handleLivrayerCommande = (commandeId) => {
  if (!etatStocksLoaded || !ligneCommandesLoaded) {
    console.log("Data not loaded yet");
    return;
  }

  // Logique pour changer le statut de la commande ici...
  // Assurez-vous de mettre à jour la commande pour refléter le nouveau statut (12 pour "Expédiée")

  // Récupérer les articles de la commande
  const articles = ligneCommandes.filter((ligne) => ligne.appelCommande.id === commandeId).map((ligne) => ligne.article);

  // Afficher les articles dans la console
  console.log("Articles de la commande :", articles);

  if (articles) {
    // Parcourir les articles de la commande
    articles.forEach((a) => {
      // Récupérer l'article et sa quantité dans chaque ligne de commande
      const articleId = a.id;
      const articleCai = a.cai;
      
      // Trouver la ligne de commande correspondant à l'article
      const ligneCommande = ligneCommandes.find((ligne) => ligne.article.id === articleId && ligne.appelCommande.id === commandeId);
      
      // Trouver la quantité de l'article dans la table d'état de stock
      const etatStockArticle = etatStocks.find((etat) => etat.article.id === articleId);
      const quantiteStock = etatStockArticle ? etatStockArticle.qte : 0;

      if (ligneCommande) {
        // Récupérer la quantité commandée pour cet article
        const quantiteCommande = ligneCommande.qte;
      
        // Afficher les détails de l'article, sa quantité demandée et sa quantité en stock
        console.log("Article:", articleId);
        console.log("Quantité demandée:", articleCai);
        console.log("Quantité commandée:", quantiteCommande);
        console.log("Quantité en stock:", quantiteStock);

        // Soustraire la quantité commandée de la quantité en stock
        const nouvelleQuantiteStock = quantiteStock - quantiteCommande;

        // Mettre à jour la base de données avec la nouvelle quantité en stock
        dispatch(updateEtatStock({ ...etatStockArticle, qte: nouvelleQuantiteStock }));
        
        console.log(`Quantité en stock mise à jour pour l'article ${articleId}: ${nouvelleQuantiteStock}`);
      }
    });
  }
  // Mettre à jour l'état avec les articles de la commande
  setCommandeArticles(articles);
};



  

  if (!account.authorities.includes('ROLE_ADMIN') && !account.authorities.includes('ROLE_USER') && !account.authorities.includes('ROLE_MAGASINIER')) {
    return (
      <div>
        <h2 id="appel-commande-heading" data-cy="AppelCommandeHeading">
          <Translate contentKey="sifApp.appelCommande.home.title">Appel Commandes</Translate>
        </h2>
        <div className="alert alert-danger">
          You are not authorized to view this page
        </div>
      </div>
    );
  }
  const PdfViewer = ({ isOpen, toggle, pdfData }) => (
    <Modal isOpen={isOpen} toggle={toggle} size="lg">
      <ModalHeader toggle={toggle}>Bon de Commande</ModalHeader>
      <ModalBody>
        <embed src={pdfData} width="100%" height="500px" type="application/pdf" />
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={toggle}>
          Fermer
        </Button>
      </ModalFooter>
    </Modal>
  );
  const [isOpen, setIsOpen] = useState(false);
  const [pdfData, setPdfData] = useState(null);

  const toggle = () => setIsOpen(!isOpen);

  const generatePdf = (appelCommande, ligneCommandes, articles) => {
    const doc = new jsPDF();
    doc.setFontSize(18);
    doc.text('BON DE COMMANDE', 105, 20, { align: 'center' });

    const client = clients.find(c => c.id === appelCommande.client.id);
    const extraUser = extraUsers.find(ex => ex.id === client.extraUser.id);

    doc.setFontSize(12);
    doc.text(`Num - commande: ${appelCommande.numCommande}`, 10, 40);
    doc.text(`Nom - Prénom: ${extraUser?.user?.firstName}`, 10, 50);
    doc.text(`Adresse: ${extraUser.adresse}`, 10, 60);
    doc.text(`Tél.: ${appelCommande.numCommande}`, 10, 70);
    doc.text(`Email: ${extraUser?.user?.email}`, 10, 80);

    const tableHeader = [['N° page', 'Dénomination de l\'article', 'Prix unitaire', 'Quantité', 'Montant']];
    const tableRows = ligneCommandes.filter(lc => lc.appelCommande.id === appelCommande.id).map(lc => {
      const article = articles.find(a => a.id === lc.article.id);
      return [1, article.cai, article.valeur, lc.qte, article.valeur * lc.qte];
    });

    autoTable(doc, { startY: 90, head: tableHeader, body: tableRows });
    const totalAmount = tableRows.reduce((total, row) => total + row[4], 0);
    doc.setFontSize(12);
    doc.text(`TOTAL: ${totalAmount}`, 150, 200);
    doc.text('Date et Signature', 10, 210);

    // Obtenir l'URL du PDF en blob
    const pdfBlob = doc.output('bloburl');
    setPdfData(pdfBlob);
    toggle();
  };
  const generateCsv = (appelCommande, ligneCommandes, articles, etatStocks) => {
    // Définir les en-têtes de colonnes clairs
    const csvContent = "N° page,Dénomination de l'article,Prix unitaire,Quantité commandée,Quantité en stock,Montant,Localisation\n";
  
    // Mapper les lignes de commande filtrées et formater les données en CSV
    const tableRows = ligneCommandes.filter(lc => lc.appelCommande.id === appelCommande.id).map(lc => {
      const article = articles.find(a => a.id === lc.article.id);
      const etatStockArticle = etatStocks.find(etat => etat.article.id === article.id);
      const numPage = 1; // Remplacez par la valeur réelle si nécessaire
      const designation = article.cai;
      const prixUnitaire = article.valeur;
      const quantiteCommande = lc.qte;
      const quantiteStock = etatStockArticle ? etatStockArticle.qte : 0;
      const locationStock = etatStockArticle ? etatStockArticle.location : '';

      const montant = article.valeur * lc.qte;
      
      // Retourner chaque ligne formatée en CSV
      return `${numPage},${designation},${prixUnitaire},${quantiteCommande},${quantiteStock},${montant},${locationStock}\n`;
    });
  
    // Concaténer les en-têtes et les lignes de données en une seule chaîne CSV
    const csvData = csvContent + tableRows.join('');
  
    // Créer un blob à partir des données CSV
    const blob = new Blob([csvData], { type: 'text/csv;charset=utf-8;' });
  
    // Générer l'URL du blob pour le téléchargement
    const url = URL.createObjectURL(blob);
  
    // Créer un élément <a> pour le téléchargement du fichier CSV
    const link = document.createElement("a");
    link.setAttribute("href", url);
    link.setAttribute("download", `commande_${appelCommande.numCommande}.csv`);
  
    // Ajouter le lien à la page et déclencher le téléchargement
    document.body.appendChild(link);
    link.click();
  
    // Nettoyer après le téléchargement
    document.body.removeChild(link);
  };
  
  
const generateMagasinierReport = (magasinier, commandes, magasins, ligneCommandes, articles, etatStocks) => {
  const csvContent = "ID Magasinier,Nom Magasinier,Numéro de Commande,Date de Commande,Statut,Client,Quantité Commandée,Quantité en Stock,Montant,Localisation\n";

  // Filtrer les articles gérés par le magasinier
  const articlesByMagasinier = articles.filter(a => {
    const magasin = magasins.find(m => m.id === a.magasinId);
    return magasin && magasin.magasinierId === magasinier.id;
  });

  // Filtrer les lignes de commande pour les articles gérés par ce magasinier
  const tableRows = ligneCommandes.filter(lc => {
    return articlesByMagasinier.some(a => a.id === lc.article.id);
  }).map(lc => {
    const article = articles.find(a => a.id === lc.article.id);
    const commande = commandes.find(c => c.id === lc.appelCommande.id);
    const etatStockArticle = etatStocks.find(etat => etat.article.id === lc.article.id);
    const client = commande.client || {};
    const quantiteCommande = lc.qte;
    const quantiteStock = etatStockArticle ? etatStockArticle.qte : 0;
    const montant = article.valeur * lc.qte;
    const locationStock = etatStockArticle ? etatStockArticle.location : '';

    return `${magasinier.id},${magasinier.nom},${commande.numCommande},${commande.dateCommande},${commande.status},${client.nom || ''},${quantiteCommande},${quantiteStock},${montant},${locationStock}\n`;
  });

  // Concaténer les en-têtes et les lignes de données en une seule chaîne CSV
  const csvData = csvContent + tableRows.join('');
  
  // Créer un blob à partir des données CSV
  const blob = new Blob([csvData], { type: 'text/csv;charset=utf-8;' });

  // Générer l'URL du blob pour le téléchargement
  const url = URL.createObjectURL(blob);

  // Créer un élément <a> pour le téléchargement du fichier CSV
  const link = document.createElement("a");
  link.setAttribute("href", url);
  link.setAttribute("download", `rapport_magasinier_${magasinier.id}.csv`);

  // Ajouter le lien à la page et déclencher le téléchargement
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

// Exemple d'utilisation dans un composant React avec un bouton






  if (account.authorities.includes('ROLE_ADMIN')) {
    return (
      <div>
        <h2 id="appel-commande-heading" data-cy="AppelCommandeHeading">
          <Translate contentKey="sifApp.appelCommande.home.title">Appel Commandes</Translate>
          <div className="d-flex justify-content-end">
            <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
              <FontAwesomeIcon icon="sync" spin={loading} />{' '}
              <Translate contentKey="sifApp.appelCommande.home.refreshListLabel">Refresh List</Translate>
            </Button>
            <Link to="/appel-commande/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp;
              <Translate contentKey="sifApp.appelCommande.home.createLabel">Create new Appel Commande</Translate>
            </Link>
          </div>
        </h2>
        <div className="table-responsive">
          {appelCommandeList && appelCommandeList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="sifApp.appelCommande.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('numCommande')}>
                    <Translate contentKey="sifApp.appelCommande.numCommande">Num Commande</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('dateCommande')}>
                    <Translate contentKey="sifApp.appelCommande.dateCommande">Date Commande</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('dateLivraison')}>
                    <Translate contentKey="sifApp.appelCommande.dateLivraison">Date Livraison</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('dateExpedition')}>
                    <Translate contentKey="sifApp.appelCommande.dateExpedition">Date Expedition</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('status')}>
                    <Translate contentKey="sifApp.appelCommande.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('annomalie')}>
                    <Translate contentKey="sifApp.appelCommande.annomalie">Annomalie</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="sifApp.appelCommande.reception">Reception</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="sifApp.appelCommande.client">Client</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {appelCommandeList.map((appelCommande, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/appel-commande/${appelCommande.id}`} color="link" size="sm">
                        {appelCommande.id}
                      </Button>
                    </td>
                    <td>{appelCommande.numCommande}</td>
                    <td>
                      {appelCommande.dateCommande ? (
                        <TextFormat type="date" value={appelCommande.dateCommande} format={APP_LOCAL_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>
                      {appelCommande.dateLivraison ? (
                        <TextFormat type="date" value={appelCommande.dateLivraison} format={APP_LOCAL_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>
                      {appelCommande.dateExpedition ? (
                        <TextFormat type="date" value={appelCommande.dateExpedition} format={APP_LOCAL_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>{appelCommande.status}</td>
                    <td>{appelCommande.annomalie}</td>
                    <td>
                      {appelCommande.reception ? (
                        <Link to={`/reception/${appelCommande.reception.id}`}>{appelCommande.reception.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>{appelCommande.client ? <Link to={`/client/${appelCommande.client.id}`}>{appelCommande.client.id}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/appel-commande/${appelCommande.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/appel-commande/${appelCommande.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/appel-commande/${appelCommande.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="sifApp.appelCommande.home.notFound">No Appel Commandes found</Translate>
              </div>
            )
          )}
        </div>
        {totalItems ? (
          <div className={appelCommandeList && appelCommandeList.length > 0 ? '' : 'd-none'}>
            <div className="justify-content-center d-flex">
              <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
            </div>
            <div className="justify-content-center d-flex">
              <JhiPagination
                activePage={paginationState.activePage}
                onSelect={handlePagination}
                maxButtons={5}
                itemsPerPage={paginationState.itemsPerPage}
                totalItems={totalItems}
              />
            </div>
          </div>
        ) : (
          ''
        )}
      </div>
    );
  
  }
  

  let filteredAppelCommandeList = appelCommandeList;
  



  const StatusButton: React.FC<{ status: number }> = ({ status }) => {
    let color = '';
    let text = '';
  
    switch (status) {
      case 1:
        color = 'success';
        text = 'Commande validée';
        break;
      case 12:
        color = 'warning';
        text = 'Expédiée';
        break;
      case 0:
        color = 'danger';
        text = 'Non validée';
        break;
      case 13:
          color = 'secondary';
          text = 'livré';
          break; 
      default:
        color = 'secondary';
        text = 'Statut inconnu';
    }
  
    return <Button color={color}>{text}</Button>;
  };



  if (account.authorities.includes('ROLE_USER')) {
    const matchingUserExtra = extraUsers.find(userextra => userextra.user.id === account?.id);
    const matchingClient = clients.find(client => client.extraUser.id === matchingUserExtra?.id);
    const filteredAppelCommandeList = appelCommandeList.filter(a => a.client.id === matchingClient?.id);
  
    return (
      <div>
        <h2 id="appel-commande-heading" data-cy="AppelCommandeHeading">
          <Translate contentKey="sifApp.appelCommande.home.title">Appel Commandes</Translate>
          <div className="d-flex justify-content-end">
            <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
              <FontAwesomeIcon icon="sync" spin={loading} />{' '}
              <Translate contentKey="sifApp.appelCommande.home.refreshListLabel">Refresh List</Translate>
            </Button>
            <Link to="/appel-commande/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp;
              <Translate contentKey="sifApp.appelCommande.home.createLabel">Create new Appel Commande</Translate>
            </Link>
          </div>
        </h2>
        <div className="table-responsive">
          {filteredAppelCommandeList && filteredAppelCommandeList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="sifApp.appelCommande.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('numCommande')}>
                    <Translate contentKey="sifApp.appelCommande.numCommande">Num Commande</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('dateCommande')}>
                    <Translate contentKey="sifApp.appelCommande.dateCommande">Date Commande</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('dateLivraison')}>
                    <Translate contentKey="sifApp.appelCommande.dateLivraison">Date Livraison</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('dateExpedition')}>
                    <Translate contentKey="sifApp.appelCommande.dateExpedition">Date Expedition</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('status')}>
                    <Translate contentKey="sifApp.appelCommande.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('annomalie')}>
                    <Translate contentKey="sifApp.appelCommande.annomalie">Annomalie</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="sifApp.appelCommande.reception">Reception</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="sifApp.appelCommande.client">Client</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {filteredAppelCommandeList.map((appelCommande, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/appel-commande/${appelCommande.id}`} color="link" size="sm">
                        {appelCommande.id}
                      </Button>
                    </td>
                    <td>{appelCommande.numCommande}</td>
                    <td>
                      {appelCommande.dateCommande ? (
                        <TextFormat type="date" value={appelCommande.dateCommande} format={APP_LOCAL_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>
                      {appelCommande.dateLivraison ? (
                        <TextFormat type="date" value={appelCommande.dateLivraison} format={APP_LOCAL_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>
                      {appelCommande.dateExpedition ? (
                        <TextFormat type="date" value={appelCommande.dateExpedition} format={APP_LOCAL_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>
                      <StatusButton status={appelCommande.status} />
                    </td>
                    <td>{appelCommande.annomalie}</td>
                    <td>
                      {appelCommande.reception ? (
                        <Link to={`/reception/${appelCommande.reception.id}`}>{appelCommande.reception.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>{appelCommande.client ? <Link to={`/client/${appelCommande.client.id}`}>{appelCommande.client.id}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/appel-commande/${appelCommande.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button color="danger" size="sm" onClick={() => generatePdf(appelCommande, ligneCommandes, articles)}>
                          <FontAwesomeIcon icon={faFilePdf} />
                          <span className="d-none d-md-inline">Bon de commande</span>
                        </Button>
                        <PdfViewer isOpen={isOpen} toggle={toggle} pdfData={pdfData} />
                        <Button color="primary" size="sm" onClick={() => generateCsv(appelCommande, ligneCommandes, articles, etatStocks)
}>
                        <FontAwesomeIcon icon={faFileCsv} /> CSV
                      </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="sifApp.appelCommande.home.notFound">No Appel Commandes found</Translate>
              </div>
            )
          )}
        </div>
        {totalItems ? (
          <div className={filteredAppelCommandeList && filteredAppelCommandeList.length > 0 ? '' : 'd-none'}>
            <div className="justify-content-center d-flex">
              <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
            </div>
            <div className="justify-content-center d-flex">
              <JhiPagination
                activePage={paginationState.activePage}
                onSelect={handlePagination}
                maxButtons={5}
                itemsPerPage={paginationState.itemsPerPage}
                totalItems={totalItems}
              />
            </div>
          </div>
        ) : (
          ''
        )}
      </div>
    );
  }
  if (account.authorities.includes('ROLE_MAGASINIER')) {
    const matchingUserExtra = extraUsers.find(userextra => userextra.user.id === account?.id);
    const matchingMagasinier = magasiniers.find((m) => m.extraUser.id === matchingUserExtra?.id);
    const matchingClient = clients.find(client => client.extraUser.id === matchingUserExtra?.id);
    const matchingMagasin = magasins.find((m) => m.id === matchingMagasinier?.magasin?.id);
    const matchingReception = receptions.find((m) => m.pays === matchingMagasin?.pays && m.address === matchingMagasin?.address );
    filteredAppelCommandeList = appelCommandeList.filter(a => a.reception.id === matchingReception?.id);
  }


  return (
    <div>
      <h2 id="appel-commande-heading" data-cy="AppelCommandeHeading">
        <Translate contentKey="sifApp.appelCommande.home.title">Appel Commandes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sifApp.appelCommande.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/appel-commande/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sifApp.appelCommande.home.createLabel">Create new Appel Commande</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {filteredAppelCommandeList && filteredAppelCommandeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sifApp.appelCommande.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('numCommande')}>
                  <Translate contentKey="sifApp.appelCommande.numCommande">Num Commande</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dateCommande')}>
                  <Translate contentKey="sifApp.appelCommande.dateCommande">Date Commande</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dateLivraison')}>
                  <Translate contentKey="sifApp.appelCommande.dateLivraison">Date Livraison</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dateExpedition')}>
                  <Translate contentKey="sifApp.appelCommande.dateExpedition">Date Expedition</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="sifApp.appelCommande.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('annomalie')}>
                  <Translate contentKey="sifApp.appelCommande.annomalie">Annomalie</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="sifApp.appelCommande.reception">Reception</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="sifApp.appelCommande.client">Client</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {filteredAppelCommandeList.map((appelCommande, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/appel-commande/${appelCommande.id}`} color="link" size="sm">
                      {appelCommande.id}
                    </Button>
                  </td>
                  <td>{appelCommande.numCommande}</td>
                  <td>
                    {appelCommande.dateCommande ? (
                      <TextFormat type="date" value={appelCommande.dateCommande} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {appelCommande.dateLivraison ? (
                      <TextFormat type="date" value={appelCommande.dateLivraison} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {appelCommande.dateExpedition ? (
                      <TextFormat type="date" value={appelCommande.dateExpedition} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{appelCommande.status}</td>
                  <td>{appelCommande.annomalie}</td>
                  <td>
                    {appelCommande.reception ? (
                      <Link to={`/reception/${appelCommande.reception.id}`}>{appelCommande.reception.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{appelCommande.client ? <Link to={`/client/${appelCommande.client.id}`}>{appelCommande.client.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/appel-commande/${appelCommande.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                            tag={Link}
                            to={`/appel-commande/${appelCommande.id}/edit`}
                            color={appelCommande.status === 12 ? "success" : "danger"}
                            size="sm"
                            onClick={() => handleLivrayerCommande(appelCommande.id)} // Appel de la fonction handleLivrayerCommande ici
                          >
                            <FontAwesomeIcon icon={faCheckCircle} /> Livrer la commande
                          </Button>

                      
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="sifApp.appelCommande.home.notFound">No Appel Commandes found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={filteredAppelCommandeList && filteredAppelCommandeList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default AppelCommande;
