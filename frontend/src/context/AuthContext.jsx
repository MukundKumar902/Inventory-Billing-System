import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem('user');
    return savedUser ? JSON.parse(savedUser) : null;
  });

  const login = async (username, password) => {
    const response = await api.post('/auth/login', { username, password });
    const userData = response.data;
    setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
    return userData;
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('user');
  };

  const isAdmin = user?.roles?.includes('ROLE_ADMIN');
  const isCashier = user?.roles?.includes('ROLE_CASHIER');

  return (
    <AuthContext.Provider value={{ user, login, logout, isAdmin, isCashier }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
