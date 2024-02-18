import axios from "axios";
import { getToken } from "./auth_service";

const api = axios.create({
  baseURL: "http://localhost:8080" // Substitua pela URL do seu backend
});

api.interceptors.request.use(async config => {
  const token = getToken();
  
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

api.interceptors.response.use(
  response => response,
  async error => {
    if (error.response && error.response.status === 401) {
      localStorage.clear();
      
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);


export default api;
