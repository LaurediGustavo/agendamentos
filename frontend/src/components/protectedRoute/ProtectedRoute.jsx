// ProtectedRoute.js
import { Navigate } from 'react-router-dom';
import { isAuthenticated } from '../../services/auth_service';

export function ProtectedRoute({ element, ...rest }) {
  return isAuthenticated() ? element : <Navigate to="/login" />;
}
