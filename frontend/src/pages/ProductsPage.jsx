import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import { Package, Plus, Edit2, Trash2, Search, AlertTriangle, Filter, X, Image as ImageIcon } from 'lucide-react';

export const ProductsPage = () => {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [search, setSearch] = useState('');
  const [showLowStockOnly, setShowLowStockOnly] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  const { isAdmin } = useAuth();

  const [formData, setFormData] = useState({
    name: '',
    barcode: '',
    price: '',
    gstPercent: 0,
    currentStock: 0,
    minStockAlert: 5,
    unit: 'pcs',
    imageUrl: '',
    categoryId: ''
  });

  useEffect(() => {
    fetchProducts();
    fetchCategories();
  }, []);

  const fetchProducts = async () => {
    try {
      const res = await api.get('/products');
      setProducts(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchCategories = async () => {
    try {
      const res = await api.get('/categories');
      setCategories(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const openAddModal = () => {
    setEditingProduct(null);
    setFormData({
      name: '',
      barcode: '',
      price: '',
      gstPercent: 0,
      currentStock: 0,
      minStockAlert: 5,
      unit: 'pcs',
      imageUrl: '',
      categoryId: categories[0]?.id || ''
    });
    setShowModal(true);
  };

  const openEditModal = (product) => {
    setEditingProduct(product);
    setFormData({
      name: product.name,
      barcode: product.barcode || '',
      price: product.price,
      gstPercent: product.gstPercent || 0,
      currentStock: product.currentStock,
      minStockAlert: product.minStockAlert || 5,
      unit: product.unit || 'pcs',
      imageUrl: product.imageUrl || '',
      categoryId: product.categoryId || ''
    });
    setShowModal(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingProduct) {
        await api.put(`/products/${editingProduct.id}`, formData);
      } else {
        await api.post('/products', formData);
      }
      setShowModal(false);
      fetchProducts();
    } catch (err) {
      alert(err.response?.data?.message || 'Error saving product');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        await api.delete(`/products/${id}`);
        fetchProducts();
      } catch (err) {
        alert('Failed to delete product.');
      }
    }
  };

  const filteredProducts = products.filter(p => {
    const matchesSearch = p.name.toLowerCase().includes(search.toLowerCase()) ||
                          (p.barcode && p.barcode.includes(search));
    const matchesLowStock = showLowStockOnly ? p.currentStock <= p.minStockAlert : true;
    return matchesSearch && matchesLowStock;
  });

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 flex items-center space-x-2">
            <Package className="w-7 h-7 text-blue-600" />
            <span>Product & Stock Management</span>
          </h1>
          <p className="text-gray-500 text-sm">Track product photos, prices, categories, and inventory stock levels</p>
        </div>

        {isAdmin && (
          <button
            onClick={openAddModal}
            className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2.5 rounded-xl font-semibold shadow-md transition flex items-center space-x-2 text-sm"
          >
            <Plus className="w-4 h-4" />
            <span>Add New Product</span>
          </button>
        )}
      </div>

      {/* Filters & Search */}
      <div className="bg-white p-4 rounded-2xl shadow-sm border border-gray-200 flex flex-col md:flex-row gap-4 justify-between items-center">
        <div className="relative w-full md:w-96">
          <input
            type="text"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search by name or barcode..."
            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-600 outline-none text-sm"
          />
          <Search className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
        </div>

        <button
          onClick={() => setShowLowStockOnly(!showLowStockOnly)}
          className={`px-4 py-2 rounded-xl text-xs font-bold border transition flex items-center space-x-2 ${
            showLowStockOnly
              ? 'bg-amber-50 text-amber-700 border-amber-300 shadow-xs'
              : 'bg-white text-gray-600 border-gray-300 hover:bg-gray-50'
          }`}
        >
          <AlertTriangle className="w-4 h-4 text-amber-500" />
          <span>Low Stock Warning Only ({products.filter(p => p.currentStock <= p.minStockAlert).length})</span>
        </button>
      </div>

      {/* Products Table */}
      <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-gray-50 border-b border-gray-200 text-gray-600 text-xs uppercase font-semibold">
                <th className="p-4 w-16">Image</th>
                <th className="p-4">Product Name</th>
                <th className="p-4">Category</th>
                <th className="p-4">Barcode</th>
                <th className="p-4">Price</th>
                <th className="p-4">GST %</th>
                <th className="p-4">Stock Status</th>
                {isAdmin && <th className="p-4 text-right">Actions</th>}
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100 text-sm">
              {filteredProducts.length === 0 ? (
                <tr>
                  <td colSpan={isAdmin ? 8 : 7} className="p-8 text-center text-gray-400">
                    No products found matching your search.
                  </td>
                </tr>
              ) : (
                filteredProducts.map(product => {
                  const isLow = product.currentStock <= product.minStockAlert;
                  return (
                    <tr key={product.id} className="hover:bg-gray-50/50 transition">
                      <td className="p-4">
                        <div className="w-12 h-12 rounded-lg bg-gray-100 border border-gray-200 overflow-hidden flex items-center justify-center flex-shrink-0">
                          {product.imageUrl ? (
                            <img
                              src={product.imageUrl}
                              alt={product.name}
                              className="w-full h-full object-cover"
                              onError={(e) => {
                                e.target.onerror = null;
                                e.target.src = 'https://images.unsplash.com/photo-1542838132-92c53300491e?w=100&auto=format&fit=crop&q=60';
                              }}
                            />
                          ) : (
                            <ImageIcon className="w-6 h-6 text-gray-400" />
                          )}
                        </div>
                      </td>
                      <td className="p-4 font-semibold text-gray-900">{product.name}</td>
                      <td className="p-4 text-gray-600">
                        <span className="px-2.5 py-1 bg-gray-100 text-gray-700 text-xs font-medium rounded-md">
                          {product.categoryName || 'General'}
                        </span>
                      </td>
                      <td className="p-4 text-gray-500 font-mono text-xs">{product.barcode || '-'}</td>
                      <td className="p-4 font-bold text-gray-900">₹{product.price.toFixed(2)}</td>
                      <td className="p-4 text-gray-600">{product.gstPercent}%</td>
                      <td className="p-4">
                        <span className={`px-2.5 py-1 rounded-full text-xs font-bold inline-flex items-center space-x-1 ${
                          isLow ? 'bg-amber-100 text-amber-800' : 'bg-emerald-100 text-emerald-800'
                        }`}>
                          {isLow && <AlertTriangle className="w-3.5 h-3.5 mr-1 text-amber-600" />}
                          <span>{product.currentStock} {product.unit}</span>
                        </span>
                      </td>
                      {isAdmin && (
                        <td className="p-4 text-right space-x-2">
                          <button
                            onClick={() => openEditModal(product)}
                            className="p-1.5 text-gray-500 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition"
                          >
                            <Edit2 className="w-4 h-4" />
                          </button>
                          <button
                            onClick={() => handleDelete(product.id)}
                            className="p-1.5 text-gray-500 hover:text-red-600 hover:bg-red-50 rounded-lg transition"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </td>
                      )}
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add / Edit Product Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-xs flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl max-w-lg w-full p-6 shadow-2xl space-y-6 max-h-[90vh] overflow-y-auto">
            <div className="flex justify-between items-center pb-4 border-b border-gray-100">
              <h3 className="text-lg font-bold text-gray-900">
                {editingProduct ? 'Edit Product' : 'Add New Product'}
              </h3>
              <button onClick={() => setShowModal(false)} className="text-gray-400 hover:text-gray-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-xs font-semibold text-gray-700 mb-1">Product Name *</label>
                <input
                  type="text"
                  required
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:ring-1 focus:ring-blue-600 outline-none"
                  placeholder="e.g. Amul Butter 500g"
                />
              </div>

              {/* Image URL & Live Preview */}
              <div>
                <label className="block text-xs font-semibold text-gray-700 mb-1">Product Image URL</label>
                <div className="flex gap-3 items-center">
                  <input
                    type="url"
                    value={formData.imageUrl}
                    onChange={(e) => setFormData({ ...formData, imageUrl: e.target.value })}
                    placeholder="https://example.com/product.jpg"
                    className="flex-1 px-3 py-2 text-sm border border-gray-300 rounded-lg focus:ring-1 focus:ring-blue-600 outline-none"
                  />
                  <div className="w-12 h-12 rounded-lg bg-gray-100 border border-gray-200 overflow-hidden flex items-center justify-center flex-shrink-0">
                    {formData.imageUrl ? (
                      <img
                        src={formData.imageUrl}
                        alt="Preview"
                        className="w-full h-full object-cover"
                        onError={(e) => {
                          e.target.onerror = null;
                          e.target.src = 'https://images.unsplash.com/photo-1542838132-92c53300491e?w=100&auto=format&fit=crop&q=60';
                        }}
                      />
                    ) : (
                      <ImageIcon className="w-5 h-5 text-gray-400" />
                    )}
                  </div>
                </div>
                <p className="text-[11px] text-gray-400 mt-1">Paste any image web link (Unsplash, imgur, CDN, etc.)</p>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-gray-700 mb-1">Barcode / SKU</label>
                  <input
                    type="text"
                    value={formData.barcode}
                    onChange={(e) => setFormData({ ...formData, barcode: e.target.value })}
                    className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:ring-1 focus:ring-blue-600 outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-gray-700 mb-1">Category</label>
                  <select
                    value={formData.categoryId}
                    onChange={(e) => setFormData({ ...formData, categoryId: e.target.value })}
                    className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:ring-1 focus:ring-blue-600 outline-none"
                  >
                    <option value="">Select Category</option>
                    {categories.map(c => (
                      <option key={c.id} value={c.id}>{c.name}</option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-gray-700 mb-1">Price (₹) *</label>
                  <input
                    type="number"
                    step="0.01"
                    required
                    value={formData.price}
                    onChange={(e) => setFormData({ ...formData, price: e.target.value })}
                    className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:ring-1 focus:ring-blue-600 outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-gray-700 mb-1">GST Tax %</label>
                  <input
                    type="number"
                    step="0.1"
                    value={formData.gstPercent}
                    onChange={(e) => setFormData({ ...formData, gstPercent: e.target.value })}
                    className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:ring-1 focus:ring-blue-600 outline-none"
                  />
                </div>
              </div>

              <div className="grid grid-cols-3 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-gray-700 mb-1">Stock *</label>
                  <input
                    type="number"
                    required
                    value={formData.currentStock}
                    onChange={(e) => setFormData({ ...formData, currentStock: e.target.value })}
                    className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:ring-1 focus:ring-blue-600 outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-gray-700 mb-1">Low Alert Min</label>
                  <input
                    type="number"
                    value={formData.minStockAlert}
                    onChange={(e) => setFormData({ ...formData, minStockAlert: e.target.value })}
                    className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:ring-1 focus:ring-blue-600 outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-gray-700 mb-1">Unit</label>
                  <input
                    type="text"
                    value={formData.unit}
                    onChange={(e) => setFormData({ ...formData, unit: e.target.value })}
                    placeholder="pcs, kg, box"
                    className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:ring-1 focus:ring-blue-600 outline-none"
                  />
                </div>
              </div>

              <div className="pt-4 flex space-x-3">
                <button
                  type="submit"
                  className="flex-1 bg-blue-600 hover:bg-blue-700 text-white py-2.5 rounded-xl font-semibold shadow-md text-sm"
                >
                  Save Product
                </button>
                <button
                  type="button"
                  onClick={() => setShowModal(false)}
                  className="px-4 bg-gray-100 hover:bg-gray-200 text-gray-700 py-2.5 rounded-xl text-sm font-medium"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
