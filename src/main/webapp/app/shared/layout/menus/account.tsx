import React, { useEffect } from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { NavDropdown } from './menu-components';
import { getEntities as getExtraUsers } from 'app/entities/extra-user/extra-user.reducer';

export const AccountMenu = ({ isAuthenticated = false }) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    if (isAuthenticated) {
      dispatch(getExtraUsers({ page: 0, size: 20, sort: 'id,asc' }));
    }
  }, [isAuthenticated]);

  const account = useAppSelector(state => state.authentication.account);
  const extraUsers = useAppSelector(state => state.extraUser.entities);
  const matchingUserExtra = extraUsers.find(userextra => userextra.user.id === account?.id);

  const accountMenuItemsAuthenticated = () => (
    <>
      <MenuItem icon="wrench" to="/account/settings" data-cy="settings">
        <Translate contentKey="global.menu.account.settings">Settings</Translate>
      </MenuItem>
      {matchingUserExtra && (
        <MenuItem icon="user" to={`/extra-user/${matchingUserExtra.id}`} data-cy="personalData">
          Personal Data
        </MenuItem>
      )}
      <MenuItem icon="lock" to="/account/password" data-cy="passwordItem">
        <Translate contentKey="global.menu.account.password">Password</Translate>
      </MenuItem>
      <MenuItem icon="sign-out-alt" to="/logout" data-cy="logout">
        <Translate contentKey="global.menu.account.logout">Sign out</Translate>
      </MenuItem>
    </>
  );

  const accountMenuItems = () => (
    <>
      <MenuItem id="login-item" icon="sign-in-alt" to="/login" data-cy="login">
        <Translate contentKey="global.menu.account.login">Sign in</Translate>
      </MenuItem>
      <MenuItem icon="user-plus" to="/account/register" data-cy="register">
        <Translate contentKey="global.menu.account.register">Register</Translate>
      </MenuItem>
    </>
  );

  return (
    <NavDropdown icon="user" name={translate('global.menu.account.main')} id="account-menu" data-cy="accountMenu">
      {isAuthenticated ? accountMenuItemsAuthenticated() : accountMenuItems()}
    </NavDropdown>
  );
};

export default AccountMenu;
