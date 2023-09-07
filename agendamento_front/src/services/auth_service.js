export const TOKEN_KEY = "@agendamentos-Token";
export const ROLES_KEY = "@agendamentos-Roles";

export const isAuthenticated = () => localStorage.getItem(TOKEN_KEY) !== null;
export const getToken = () => localStorage.getItem(TOKEN_KEY);
export const getRoles = () => localStorage.getItem(ROLES_KEY);

export const logout = () => {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(ROLES_KEY);
};

export const login = (token, roles) => {
  localStorage.setItem(TOKEN_KEY, token);
  localStorage.setItem(ROLES_KEY, roles);
};