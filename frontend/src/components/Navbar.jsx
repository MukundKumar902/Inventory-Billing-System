import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { ShoppingBag, ShoppingCart, Package, BarChart3, LogOut, UserCheck } from 'lucide-react';

export const Navbar = () => {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  if (!user) return null;

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const navItemClass = (path) =>
    `flex items-center space-x-2 px-3 py-2 rounded-lg text-sm font-medium transition ${
      location.pathname === path
        ? 'bg-blue-600 text-white shadow-sm'
        : 'text-gray-600 hover:bg-gray-100 hover:text-gray-900'
    }`;

  return (
    <nav className="bg-white border-b border-gray-200 sticky top-0 z-50 shadow-xs">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center space-x-8">
            <Link to="/" className="flex items-center space-x-3">
              <div className="w-10 h-10 bg-blue-600 rounded-xl flex items-center justify-center text-white shadow-md">
                <ShoppingBag className="w-6 h-6" />
              </div>
              <div>
                <span className="text-xl font-bold text-gray-900 tracking-tight block">SmartStore</span>
                <span className="text-xs text-blue-600 font-medium block -mt-1">POS & Inventory</span>
              </div>
            </Link>

            <div className="hidden md:flex space-x-2">
              {isAdmin && (
                <Link to="/dashboard" className={navItemClass('/dashboard')}>
                  <BarChart3 className="w-4 h-4" />
                  <span>Dashboard</span>
                </Link>
              )}
              <Link to="/billing" className={navItemClass('/billing')}>
                <ShoppingCart className="w-4 h-4" />
                <span>POS Billing</span>
              </Link>
              <Link to="/products" className={navItemClass('/products')}>
                <Package className="w-4 h-4" />
                <span>Products & Stock</span>
              </Link>
            </div>
          </div>

          <div className="flex items-center space-x-4">
            <div className="flex items-center space-x-2 bg-gray-50 border border-gray-200 px-3 py-1.5 rounded-lg">
              <UserCheck className="w-4 h-4 text-blue-600" />
              <div className="text-left">
                <span className="text-xs font-semibold text-gray-800 block">{user.fullName || user.username}</span>
                <span className="text-[10px] font-bold text-blue-600 uppercase tracking-wider block">
                  {isAdmin ? 'Owner (Admin)' : 'Cashier'}
                </span>
              </div>
            </div>

            <button
              onClick={handleLogout}
              className="p-2 text-gray-500 hover:text-red-600 hover:bg-red-50 rounded-lg transition"
              title="Logout"
            >
              <LogOut className="w-5 h-5" />
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
};
