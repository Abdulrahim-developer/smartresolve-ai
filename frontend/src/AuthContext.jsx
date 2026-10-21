import React, { createContext, useContext, useState, useEffect } from 'react';
import api from './services/api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  // Global state context carriers to govern system view structures
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  // Validation handshake effect loop: checks browser memory cache on startup
  useEffect(() => {
    const cachedToken = localStorage.getItem('sr_token');
    const cachedUser = localStorage.getItem('sr_user');

    if (cachedToken && cachedUser) {
      try {
        setUser(JSON.parse(cachedUser));
        setIsAuthenticated(true);
      } catch (e) {
        localStorage.clear();
      }
    }
    setLoading(false);
  }, []);

  /**
   * Dispatches login input variables securely down into your backend API containers via Axios.
   * Maps roles cleanly to establish instant conditional routing paths.
   */
  const login = async (email, password) => {
    try {
      const response = await api.post('/auth/login', { email, password });
      const { token, role, displayName } = response.data;

      // Extract credentials and drop them into local storage parameter caches
      localStorage.setItem('sr_token', token);
      
      const userData = { email: displayName, role };
      localStorage.setItem('sr_user', JSON.stringify(userData));

      setUser(userData);
      setIsAuthenticated(true);
      return userData; // Returns user metadata layout to trigger UI transitions
    } catch (error) {
      throw error || 'Authentication sequence encountered a routing failure.';
    }
  };

  /**
   * Flushes browser token storage matrices to execute a secure stateless session exit.
   */
  const logout = () => {
    localStorage.clear();
    setUser(null);
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider value={{ user, isAuthenticated, loading, login, logout }}>
      {!loading && children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth matrix hooks must be wrapped natively inside an AuthProvider room.');
  }
  return context;
}
