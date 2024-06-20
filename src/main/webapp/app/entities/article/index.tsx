import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Article from './article';
import ArticleDetail from './article-detail';
import ArticleUpdate from './article-update';
import ArticleDeleteDialog from './article-delete-dialog';

const ArticleRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Article />} />
    <Route path="new" element={<ArticleUpdate />} />
    <Route path=":id">
      <Route index element={<ArticleDetail />} />
      <Route path="edit" element={<ArticleUpdate />} />
      <Route path="delete" element={<ArticleDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ArticleRoutes;
