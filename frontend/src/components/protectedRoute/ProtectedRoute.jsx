import { Navigate } from 'react-router-dom';
import { isAuthenticated, getRoles } from '../../services/auth_service';

export function ProtectedRoute({ element, roles, ...rest }) {
  const userRoles = getRoles(); // Obtenha as funções do usuário
  const isAuthorized = userRoles ? roles.some(role => userRoles.includes(role)) : false; // Verifique se o usuário tem alguma das funções necessárias

  if (isAuthenticated()) {
    return isAuthorized ? element : <Navigate to="/" />;
  } else {
    return <Navigate to="/login" />;
  }
}