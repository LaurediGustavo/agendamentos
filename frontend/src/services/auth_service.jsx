export const TOKEN_KEY = "@agendamentos-Token";
export const ROLES_KEY = "@agendamentos-Roles";
export const USER_ID_KEY = "@agendamentos-User-Id";
export const USER_NAME_KEY = "@agendamentos-UserName";

export const isAuthenticated = () => localStorage.getItem(TOKEN_KEY) !== null;
export const getToken = () => localStorage.getItem(TOKEN_KEY);
export const getRoles = () => localStorage.getItem(ROLES_KEY);
export const getUserId = () => localStorage.getItem(USER_ID_KEY);
export const getUserName = () => localStorage.getItem(USER_NAME_KEY);

export const logout = () => {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(ROLES_KEY);
  localStorage.removeItem(USER_ID_KEY);
  localStorage.removeItem(USER_NAME_KEY);
};

export const login = (userName, usuario_id, token, roles) => {
  localStorage.setItem(USER_NAME_KEY, userName);
  localStorage.setItem(TOKEN_KEY, token);
  localStorage.setItem(ROLES_KEY, roles);
  localStorage.setItem(USER_ID_KEY, usuario_id);
};