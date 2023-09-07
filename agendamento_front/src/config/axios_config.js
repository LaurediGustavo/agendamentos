import axios from "axios";
import { getToken } from "../services/auth_service";

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

export default api;
