import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Store, KeyRound, User, AlertCircle } from 'lucide-react';

export const LoginPage = () => {
  const [username, setUsername] = useState('admin');
  const [password, setPassword] = useState('admin123');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const user = await login(username, password);
      if (user.roles.includes('ROLE_ADMIN')) {
        navigate('/dashboard');
      } else {
        navigate('/billing');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid username or password');
    } finally {
      setLoading(false);
    }
  };

  const handleDemoFill = (role) => {
    if (role === 'admin') {
      setUsername('admin');
      setPassword('admin123');
    } else {
      setUsername('cashier');
      setPassword('cashier123');
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4">
      <div className="max-w-md w-full bg-white rounded-2xl shadow-xl overflow-hidden border border-gray-200">
        <div className="bg-gradient-to-r from-blue-600 to-indigo-700 p-8 text-white text-center">
          <div className="w-16 h-16 bg-white/10 rounded-2xl flex items-center justify-center mx-auto mb-4 backdrop-blur-md">
            <Store className="w-10 h-10 text-white" />
          </div>
          <h2 className="text-2xl font-bold tracking-tight">SmartStore Billing System</h2>
          <p className="text-blue-100 text-sm mt-1">Retail Inventory & POS Software</p>
        </div>

        <form onSubmit={handleSubmit} className="p-8 space-y-6">
          {error && (
            <div className="p-4 bg-red-50 border border-red-200 text-red-700 rounded-xl text-sm flex items-center space-x-2">
              <AlertCircle className="w-5 h-5 flex-shrink-0" />
              <span>{error}</span>
            </div>
          )}

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">Username</label>
            <div className="relative">
              <User className="w-5 h-5 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
              <input
                type="text"
                required
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-600 focus:border-transparent outline-none transition"
                placeholder="Enter username"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">Password</label>
            <div className="relative">
              <KeyRound className="w-5 h-5 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
              <input
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-600 focus:border-transparent outline-none transition"
                placeholder="Enter password"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-600 hover:bg-blue-700 text-white py-3.5 rounded-xl font-semibold shadow-md transition duration-200 flex items-center justify-center disabled:opacity-50"
          >
            {loading ? 'Authenticating...' : 'Login to System'}
          </button>

          <div className="border-t border-gray-100 pt-6 text-center">
            <p className="text-xs text-gray-500 mb-3">Quick Demo Auto-fill Credentials:</p>
            <div className="flex space-x-3 justify-center">
              <button
                type="button"
                onClick={() => handleDemoFill('admin')}
                className="px-3 py-1.5 bg-blue-50 text-blue-700 hover:bg-blue-100 rounded-lg text-xs font-semibold border border-blue-200 transition"
              >
                Owner (admin / admin123)
              </button>
              <button
                type="button"
                onClick={() => handleDemoFill('cashier')}
                className="px-3 py-1.5 bg-emerald-50 text-emerald-700 hover:bg-emerald-100 rounded-lg text-xs font-semibold border border-emerald-200 transition"
              >
                Cashier (cashier / cashier123)
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};
