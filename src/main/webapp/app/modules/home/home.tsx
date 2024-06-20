import './home.scss';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import React, { useEffect, useState } from 'react';
import { getEntities as getArticles } from 'app/entities/article/article.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { getEntities as getCommandes } from 'app/entities/appel-commande/appel-commande.reducer';
import { getEntities as getMagasiniers } from 'app/entities/magasinier/magasinier.reducer';
import { getEntities as getLigneCommandes } from 'app/entities/ligne-commande/ligne-commande.reducer';
import { PieChart, Pie, Cell, Tooltip, Legend } from 'recharts';
import { getEntities as getReclamations } from 'app/entities/reclamation/reclamation.reducer';
import { getEntities as getClrs } from 'app/entities/clr/clr.reducer';
import { getEntities as getMagasins } from 'app/entities/magasin/magasin.reducer';
import { getEntities as getReceptions } from 'app/entities/reception/reception.reducer';

import { getEntities as getCoes } from 'app/entities/coe/coe.reducer';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, LineChart, Line } from 'recharts';
import moment from 'moment';
import Magasinier from 'app/entities/magasinier/magasinier';

interface DashboardItemProps {
  count: string | number;
  title: string;
  link: string;
  bgColor: string;
}

interface OrdersByTypeData {
  type: string;
  count: number;
}

const DashboardItem: React.FC<DashboardItemProps> = ({ count, title, link, bgColor }) => (
  <div className={`dashboard-item ${bgColor}`}>
    <div className="count">{count}</div>
    <div className="title">{title}</div>
    <Link to={link} className="link">Plus d&apos;info</Link>
  </div>
);

const Home: React.FC = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const isAdmin = account?.authorities && account.authorities.includes('ROLE_ADMIN');
  const articles = useAppSelector(state => state.article.entities);
  const receptions = useAppSelector(state => state.reception.entities);

  const users = useAppSelector(state => state.userManagement.users);
  const clients = useAppSelector(state => state.client.entities);
  const magasiniers = useAppSelector(state => state.magasinier.entities);
  const magasins = useAppSelector(state => state.magasin.entities);

  const ligneCommandeList = useAppSelector(state => state.ligneCommande.entities);
  const appelCommandeList = useAppSelector(state => state.appelCommande.entities);
  const reclamations = useAppSelector(state => state.reclamation.entities);
  const [fidelClients, setFidelClients] = useState<string[]>([]);
  const [normalClients, setNormalClients] = useState<string[]>([]);
  const [mostOrderedArticles, setMostOrderedArticles] = useState<{ id: string, code: string }[]>([]);
  const clrCommandeList = useAppSelector(state => state.clr.entities);
  const coeCommandeList = useAppSelector(state => state.coe.entities);

  // Fonction pour calculer le nombre de commandes par mois
  const calculateOrdersByMonth = (commandes: any[]) => {
    const ordersByMonth: { [key: string]: number } = {};

    commandes.forEach(commande => {
      const date = new Date(commande.dateCommande);
      const monthYear = `${date.getMonth() + 1}/${date.getFullYear()}`;

      if (!ordersByMonth[monthYear]) {
        ordersByMonth[monthYear] = 1;
      } else {
        ordersByMonth[monthYear]++;
      }
    });

    const data = Object.keys(ordersByMonth).map(monthYear => ({
      monthYear,
      orders: ordersByMonth[monthYear],
    }));

    return data;
  };

  const ordersData = calculateOrdersByMonth(appelCommandeList);

  useEffect(() => {
    if (isAdmin) {
      dispatch(getArticles({}));
      dispatch(getUsers({}));
      dispatch(getClients({}));
      dispatch(getCommandes({}));
      dispatch(getMagasiniers({}));
      dispatch(getMagasins({}));
      dispatch(getReceptions({}));
      dispatch(getLigneCommandes({}));
      dispatch(getReclamations({}));
      dispatch(getClrs({}));
      dispatch(getCoes({}));
    }
  }, [dispatch, isAdmin]);

  useEffect(() => {
    if (appelCommandeList.length > 0) {
      const clientCommandCounts = appelCommandeList.reduce((acc, commande) => {
        const clientId = commande.client?.id;
        if (clientId) {
          if (!acc[clientId]) {
            acc[clientId] = 0;
          }
          acc[clientId]++;
        }
        return acc;
      }, {} as Record<string, number>);

      const fidel = [];
      const normal = [];

      for (const [clientId, count] of Object.entries(clientCommandCounts)) {
        if (count > 2) {
          fidel.push(clientId);
        } else {
          normal.push(clientId);
        }
      }

      setFidelClients(fidel);
      setNormalClients(normal);
    }
  }, [appelCommandeList]);

  useEffect(() => {
    if (ligneCommandeList.length > 0) {
      const articleCommandCounts = ligneCommandeList.reduce((acc, ligneCommande) => {
        const articleId = ligneCommande.article?.id;
        const articleCode = ligneCommande.article?.cai;
        if (articleId && articleCode) {
          if (!acc[articleId]) {
            acc[articleId] = { count: 0, code: articleCode };
          }
          acc[articleId].count++;
        }
        return acc;
      }, {} as Record<string, { count: number, code: string }>);

      const mostOrdered = [];

      for (const [articleId, data] of Object.entries(articleCommandCounts)) {
        const typedData = data as { count: number, code: string };
        if (typedData.count > 3) {
          mostOrdered.push({ id: articleId, code: typedData.code });
        }
      }

      setMostOrderedArticles(mostOrdered);
    }
  }, [ligneCommandeList]);

  const clrOrders = clrCommandeList.length;
  const coeOrders = coeCommandeList.length;

  const ordersByTypeData: OrdersByTypeData[] = [
    { type: 'CLR', count: clrOrders },
    { type: 'COE', count: coeOrders },
  ];

  const calculateOrdersByClient = (commandes: any[]) => {
    const ordersByClient: { clientId: string; count: number }[] = [];

    commandes.forEach(commande => {
      const clientId = commande.client?.id;

      if (clientId) {
        const existingEntryIndex = ordersByClient.findIndex(entry => entry.clientId === clientId);

        if (existingEntryIndex !== -1) {
          ordersByClient[existingEntryIndex].count++;
        } else {
          ordersByClient.push({
            clientId,
            count: 1,
          });
        }
      }
    });

    return ordersByClient;
  };

  const ordersByClientData = calculateOrdersByClient(appelCommandeList);
  const findMagasinByMagasinier = (magasinier, magasins) => {
    return magasins.find(m => m.id === magasinier.magasin?.id);
  };
  const findReceptionByMagasin = (magasin, receptions) => {
    return receptions.find(r => r.pays === magasin.pays && r.address === magasin.address);
  };
  const filterCommandesByReception = (commandes, reception) => {
    return commandes.filter(c => c.reception?.id === reception.id);
  };
  const calculateOrdersByMagasinier = (appelCommandeList, magasiniers, magasins, receptions) => {
    const ordersByMagasinier = [];
  
    magasiniers.forEach(magasinier => {
      const matchingMagasin = findMagasinByMagasinier(magasinier, magasins);
  
      if (matchingMagasin) {
        const matchingReception = findReceptionByMagasin(matchingMagasin, receptions);
  
        if (matchingReception) {
          const filteredAppelCommandeList = filterCommandesByReception(appelCommandeList, matchingReception);
          const magasinierId = magasinier.id;
          const count = filteredAppelCommandeList.length;
  
          if (count > 0) {
            ordersByMagasinier.push({
              magasinierId,
              count,
            });
          }
        }
      }
    });
  
    return ordersByMagasinier;
  };
  const ordersByMagasinier = calculateOrdersByMagasinier(appelCommandeList, magasiniers, magasins, receptions);
  console.log(ordersByMagasinier);
        


  if (!isAdmin) {
    return (
      <div className="user-homepage">
        <div className="background-image">
          <h1 className="welcome-message">
            <strong>Bienvenue dans l'application de gestion des commandes</strong>
          </h1>
          <p>
            This is your home page. Sign in if you don't have an account?&nbsp;
            <Link to="/account/register" className="alert-link">
              Register a new account
            </Link>
          </p>
        </div>
      </div>
    );
  }

  const chartData = [
    { name: 'Clients fidèles', value: fidelClients.length },
    { name: 'Clients normaux', value: normalClients.length },
  ];

  const COLORS = ['#0088FE', '#00C49F'];
  
  return (
    <div className="dashboard-container">
      <DashboardItem count={articles.length} title="Articles" link="/articles" bgColor="bg-blue" />
      <DashboardItem count={coeCommandeList.length} title="Commandes premiere monte" link="/appel-commande" bgColor="bg-green" />
      <DashboardItem count={magasiniers.length} title="Magasiniers" link="/magasinier" bgColor="bg-red" />
      <DashboardItem count={clients.length} title="Clients" link="/client" bgColor="bg-purple" />
      <DashboardItem count={users.length} title="Utilisateurs" link="/admin/user-management" bgColor="bg-gray" />
      <DashboardItem count={clrCommandeList.length} title="les commandés de remplacement" link="/sales" bgColor="bg-yellow" />
      <DashboardItem count={reclamations.length} title="Nombre de réclamations" link="/sales" bgColor="bg-yellow" />

      <div className="chart-container">
        <h3>Nombre de commandes par mois</h3>
        <BarChart width={600} height={300} data={ordersData}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="monthYear" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="orders" fill="#8884d8" />
        </BarChart>
      </div>

      <div className="chart-container">
        <h3>Nombre de commandes par type</h3>
        <PieChart width={400} height={400}>
          <Pie data={ordersByTypeData} dataKey="count" nameKey="type" cx="50%" cy="50%" outerRadius={150} fill="#8884d8" label>
            {ordersByTypeData.map((entry, index) => (
              <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
            ))}
          </Pie>
          <Tooltip />
          <Legend />
        </PieChart>
      </div>
      <div className="chart-container">
        <h3>Commandes par client</h3>
        <BarChart width={600} height={300} data={ordersByClientData}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="clientId" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="count" fill="#FF33DA" />
        </BarChart>
      </div>

      <div className="chart-container">
        <h3>Commandes par magasinier</h3>
        <BarChart width={600} height={300} data={ordersByMagasinier}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="magasinierId" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="count" fill="#33FFEC" />
        </BarChart>
      </div>
      <div className="table-container">
        <h3>Articles le plus commandés</h3>
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Code Article</th>
            </tr>
          </thead>
          <tbody>
            {mostOrderedArticles.map(article => (
              <tr key={article.id}>
                <td>{article.id}</td>
                <td>{article.code}</td>
              </tr>
            ))}
          </tbody>
        </table>
        
      </div>

      
    </div>
  );
};

export default Home;
