
import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Automatically attach the JWT token when available
api.interceptors.request.use(
  (config) => {
    const activeSessionToken = localStorage.getItem('sr_token');

    if (activeSessionToken) {
      config.headers.Authorization = `Bearer ${activeSessionToken}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// Log API errors for debugging
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API request failed:', {
      url: error.config?.url,
      status: error.response?.status,
      data: error.response?.data,
      message: error.message,
    });

    return Promise.reject(error);
  }
);

export default api;
