import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { DollarSign, ShoppingCart, Package, AlertTriangle, TrendingUp, Printer, Image as ImageIcon } from 'lucide-react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { Bar } from 'react-chartjs-2';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

export const DashboardPage = () => {
  const [todayReport, setTodayReport] = useState(null);
  const [lowStockList, setLowStockList] = useState([]);
  const [recentInvoices, setRecentInvoices] = useState([]);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const reportRes = await api.get('/reports/today');
      setTodayReport(reportRes.data);

      const lowStockRes = await api.get('/products/low-stock');
      setLowStockList(lowStockRes.data);

      const invoicesRes = await api.get('/invoices');
      setRecentInvoices(invoicesRes.data.slice(-5).reverse());
    } catch (err) {
      console.error('Error fetching dashboard stats:', err);
    }
  };

  const chartData = {
    labels: ['9 AM', '12 PM', '3 PM', '6 PM', '9 PM'],
    datasets: [
      {
        label: 'Sales Revenue (₹)',
        data: [1200, 3400, 2100, 4800, (todayReport?.totalRevenue || 1500)],
        backgroundColor: 'rgba(37, 99, 235, 0.85)',
        borderRadius: 8,
      },
    ],
  };

  const downloadPdf = (id) => {
    window.open(`/api/invoices/${id}/pdf`, '_blank');
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">
      <div>
        <h1 className="text-2xl font-bold text-gray-900 flex items-center space-x-2">
          <TrendingUp className="w-7 h-7 text-blue-600" />
          <span>Business Analytics Dashboard</span>
        </h1>
        <p className="text-gray-500 text-sm">Real-time overview of daily revenue, transactions, and stock health</p>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200 flex items-center space-x-4">
          <div className="w-12 h-12 bg-blue-50 text-blue-600 rounded-xl flex items-center justify-center">
            <DollarSign className="w-6 h-6" />
          </div>
          <div>
            <p className="text-xs text-gray-500 font-medium uppercase">Today's Revenue</p>
            <h3 className="text-2xl font-bold text-gray-900">₹{todayReport?.totalRevenue?.toFixed(2) || '0.00'}</h3>
          </div>
        </div>

        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200 flex items-center space-x-4">
          <div className="w-12 h-12 bg-emerald-50 text-emerald-600 rounded-xl flex items-center justify-center">
            <ShoppingCart className="w-6 h-6" />
          </div>
          <div>
            <p className="text-xs text-gray-500 font-medium uppercase">Total Bills Generated</p>
            <h3 className="text-2xl font-bold text-gray-900">{todayReport?.totalInvoices || 0}</h3>
          </div>
        </div>

        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200 flex items-center space-x-4">
          <div className="w-12 h-12 bg-purple-50 text-purple-600 rounded-xl flex items-center justify-center">
            <Package className="w-6 h-6" />
          </div>
          <div>
            <p className="text-xs text-gray-500 font-medium uppercase">Total Products</p>
            <h3 className="text-2xl font-bold text-gray-900">{todayReport?.totalProductsCount || 0}</h3>
          </div>
        </div>

        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200 flex items-center space-x-4">
          <div className="w-12 h-12 bg-amber-50 text-amber-600 rounded-xl flex items-center justify-center">
            <AlertTriangle className="w-6 h-6" />
          </div>
          <div>
            <p className="text-xs text-gray-500 font-medium uppercase">Low Stock Alert</p>
            <h3 className="text-2xl font-bold text-amber-600">{todayReport?.lowStockProductsCount || 0} Items</h3>
          </div>
        </div>
      </div>

      {/* Chart & Low Stock Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-7 gap-8">
        <div className="lg:col-span-4 bg-white p-6 rounded-2xl shadow-sm border border-gray-200">
          <h2 className="text-lg font-bold text-gray-900 mb-4">Daily Sales Revenue Chart</h2>
          <div className="h-64">
            <Bar data={chartData} options={{ responsive: true, maintainAspectRatio: false }} />
          </div>
        </div>

        <div className="lg:col-span-3 bg-white p-6 rounded-2xl shadow-sm border border-gray-200 flex flex-col">
          <h2 className="text-lg font-bold text-gray-900 mb-4 flex items-center justify-between">
            <span className="flex items-center space-x-2">
              <AlertTriangle className="w-5 h-5 text-amber-500" />
              <span>Low Stock Warnings</span>
            </span>
            <span className="text-xs bg-amber-100 text-amber-800 font-bold px-2.5 py-0.5 rounded-full">
              {lowStockList.length}
            </span>
          </h2>

          <div className="flex-1 overflow-y-auto max-h-[260px] space-y-3">
            {lowStockList.length === 0 ? (
              <p className="text-sm text-gray-400 text-center py-8">All product stocks are healthy!</p>
            ) : (
              lowStockList.map(item => (
                <div key={item.id} className="flex justify-between items-center p-3 bg-amber-50/50 rounded-xl border border-amber-200/60">
                  <div className="flex items-center space-x-3">
                    <div className="w-10 h-10 rounded-lg bg-white border border-gray-200 overflow-hidden flex items-center justify-center flex-shrink-0">
                      {item.imageUrl ? (
                        <img
                          src={item.imageUrl}
                          alt={item.name}
                          className="w-full h-full object-cover"
                          onError={(e) => {
                            e.target.onerror = null;
                            e.target.src = 'https://images.unsplash.com/photo-1542838132-92c53300491e?w=100&auto=format&fit=crop&q=60';
                          }}
                        />
                      ) : (
                        <ImageIcon className="w-4 h-4 text-gray-400" />
                      )}
                    </div>
                    <div>
                      <h4 className="font-semibold text-gray-900 text-sm">{item.name}</h4>
                      <p className="text-xs text-gray-500">Unit Price: ₹{item.price}</p>
                    </div>
                  </div>
                  <span className="text-xs font-bold text-amber-700 bg-amber-100 px-2.5 py-1 rounded-lg">
                    {item.currentStock} {item.unit} left
                  </span>
                </div>
              ))
            )}
          </div>
        </div>
      </div>

      {/* Recent Invoices Table */}
      <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200">
        <h2 className="text-lg font-bold text-gray-900 mb-4">Recent Billed Transactions</h2>
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-gray-50 text-gray-600 text-xs uppercase font-semibold border-b border-gray-200">
                <th className="p-3">Invoice #</th>
                <th className="p-3">Customer</th>
                <th className="p-3">Payment</th>
                <th className="p-3">Total Amount</th>
                <th className="p-3 text-right">PDF Invoice</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100 text-sm">
              {recentInvoices.map(inv => (
                <tr key={inv.id} className="hover:bg-gray-50/50">
                  <td className="p-3 font-semibold text-blue-600">{inv.invoiceNumber}</td>
                  <td className="p-3 text-gray-700">{inv.customerName} ({inv.customerPhone})</td>
                  <td className="p-3">
                    <span className="text-xs px-2 py-0.5 rounded bg-gray-100 font-bold text-gray-700">
                      {inv.paymentMode}
                    </span>
                  </td>
                  <td className="p-3 font-bold text-gray-900">₹{inv.grandTotal.toFixed(2)}</td>
                  <td className="p-3 text-right">
                    <button
                      onClick={() => downloadPdf(inv.id)}
                      className="p-1.5 text-blue-600 hover:bg-blue-50 rounded-lg transition inline-flex items-center space-x-1 text-xs font-semibold"
                    >
                      <Printer className="w-4 h-4" />
                      <span>Print PDF</span>
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
