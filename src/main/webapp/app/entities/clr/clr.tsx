import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IClr } from 'app/shared/model/clr.model';
import { getEntities } from './clr.reducer';

export const Clr = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const clrList = useAppSelector(state => state.clr.entities);
  const loading = useAppSelector(state => state.clr.loading);
  const totalItems = useAppSelector(state => state.clr.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

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

  return (
    <div>
      <h2 id="clr-heading" data-cy="ClrHeading">
        <Translate contentKey="sifApp.clr.home.title">Clrs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> <Translate contentKey="sifApp.clr.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/clr/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sifApp.clr.home.createLabel">Create new Clr</Translate>
          </Link>
          
        </div>
      </h2>
      <div className="table-responsive">
        {clrList && clrList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sifApp.clr.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('constructeurAutomobile')}>
                  <Translate contentKey="sifApp.clr.constructeurAutomobile">Constructeur Automobile</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('modelVoiture')}>
                  <Translate contentKey="sifApp.clr.modelVoiture">Model Voiture</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('anneeVoiture')}>
                  <Translate contentKey="sifApp.clr.anneeVoiture">Annee Voiture</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('etatPneu')}>
                  <Translate contentKey="sifApp.clr.etatPneu">Etat Pneu</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('image')}>
                  <Translate contentKey="sifApp.clr.image">Image</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dateProduction')}>
                  <Translate contentKey="sifApp.clr.dateProduction">Date Production</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="sifApp.clr.appelCommande">Appel Commande</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {clrList.map((clr, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/clr/${clr.id}`} color="link" size="sm">
                      {clr.id}
                    </Button>
                  </td>
                  <td>{clr.constructeurAutomobile}</td>
                  <td>{clr.modelVoiture}</td>
                  <td>{clr.anneeVoiture}</td>
                  <td>{clr.etatPneu}</td>
                  <td>
                    {clr.image ? (
                      <div>
                        {clr.imageContentType ? (
                          <a onClick={openFile(clr.imageContentType, clr.image)}>
                            <img src={`data:${clr.imageContentType};base64,${clr.image}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {clr.imageContentType}, {byteSize(clr.image)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>
                    {clr.dateProduction ? <TextFormat type="date" value={clr.dateProduction} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{clr.appelCommande ? <Link to={`/appel-commande/${clr.appelCommande.id}`}>{clr.appelCommande.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/clr/${clr.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/clr/${clr.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/clr/${clr.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
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
              <Translate contentKey="sifApp.clr.home.notFound">No Clrs found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={clrList && clrList.length > 0 ? '' : 'd-none'}>
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

export default Clr;
