import React from 'react';
import { Translate } from 'react-jhipster';
import PrivateRoute from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER,AUTHORITIES.MAGASINIER]}>
      <MenuItem icon="asterisk" to="/appel-commande">
        <Translate contentKey="global.menu.entities.appelCommande" />
      </MenuItem>
      </PrivateRoute>
      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]}>
      <MenuItem icon="asterisk" to="/clr">
        <Translate contentKey="global.menu.entities.clr" />
      </MenuItem>
      </PrivateRoute>
      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]}>
      <MenuItem icon="asterisk" to="/coe">
        <Translate contentKey="global.menu.entities.coe" />
      </MenuItem>
      </PrivateRoute>
      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]}>
      <MenuItem icon="asterisk" to="/ligne-commande">
        <Translate contentKey="global.menu.entities.ligneCommande" />
      </MenuItem>
      </PrivateRoute>
      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.MAGASINIER]}>
      <MenuItem icon="asterisk" to="/article">
        <Translate contentKey="global.menu.entities.article" />
      </MenuItem>
      </PrivateRoute>
      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]}>
      <MenuItem icon="asterisk" to="/magasin">
        <Translate contentKey="global.menu.entities.magasin" />
      </MenuItem>
      </PrivateRoute>
      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]}>
      <MenuItem icon="asterisk" to="/reception">
        <Translate contentKey="global.menu.entities.reception" />
      </MenuItem>
      </PrivateRoute>
      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]}>
      <MenuItem icon="asterisk" to="/extra-user">
        <Translate contentKey="global.menu.entities.extraUser" />
      </MenuItem>
      </PrivateRoute>
      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]}>
      <MenuItem icon="asterisk" to="/client">
        <Translate contentKey="global.menu.entities.client" />
      </MenuItem>
      </PrivateRoute>
      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]}>
      <MenuItem icon="asterisk" to="/magasinier">
        <Translate contentKey="global.menu.entities.magasinier" />
      </MenuItem>
      </PrivateRoute>
      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.MAGASINIER]}>
      <MenuItem icon="asterisk" to="/etat-stock">
        <Translate contentKey="global.menu.entities.etatStock" />
      </MenuItem>
      </PrivateRoute>
      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]}>
      <MenuItem icon="asterisk" to="/reclamation">
        <Translate contentKey="global.menu.entities.reclamation" />
      </MenuItem>
      </PrivateRoute>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
