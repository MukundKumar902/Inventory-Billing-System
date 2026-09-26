import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { Search, ShoppingCart, Trash2, Plus, Minus, FileText, CheckCircle2, AlertTriangle, User, Phone, CreditCard, Printer, Image as ImageIcon } from 'lucide-react';

export const BillingPage = () => {
  const [products, setProducts] = useState([]);
  const [search, setSearch] = useState('');
  const [cart, setCart] = useState([]);
  const [customerName, setCustomerName] = useState('');
  const [customerPhone, setCustomerPhone] = useState('');
  const [paymentMode, setPaymentMode] = useState('CASH');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [createdInvoice, setCreatedInvoice] = useState(null);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const res = await api.get('/products');
      setProducts(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const filteredProducts = products.filter(p =>
    p.name.toLowerCase().includes(search.toLowerCase()) ||
    (p.barcode && p.barcode.includes(search))
  );

  const addToCart = (product) => {
    setError('');
    const existing = cart.find(item => item.productId === product.id);
    if (existing) {
      if (existing.quantity >= product.currentStock) {
        setError(`Cannot add more '${product.name}'. Only ${product.currentStock} in stock.`);
        return;
      }
      setCart(cart.map(item =>
        item.productId === product.id ? { ...item, quantity: item.quantity + 1 } : item
      ));
    } else {
      if (product.currentStock < 1) {
        setError(`Product '${product.name}' is Out of Stock!`);
        return;
      }
      setCart([...cart, {
        productId: product.id,
        name: product.name,
        price: product.price,
        gstPercent: product.gstPercent || 0,
        currentStock: product.currentStock,
        quantity: 1,
        unit: product.unit || 'pcs',
        imageUrl: product.imageUrl || ''
      }]);
    }
  };

  const updateQuantity = (productId, newQty) => {
    setError('');
    const item = cart.find(i => i.productId === productId);
    if (!item) return;
    if (newQty > item.currentStock) {
      setError(`Stock limit reached! Max available: ${item.currentStock}`);
      return;
    }
    if (newQty <= 0) {
      removeFromCart(productId);
    } else {
      setCart(cart.map(i => i.productId === productId ? { ...i, quantity: newQty } : i));
    }
  };

  const removeFromCart = (productId) => {
    setCart(cart.filter(item => item.productId !== productId));
  };

  // Calculations
  const calculateTotals = () => {
    let subtotal = 0;
    let tax = 0;
    cart.forEach(item => {
      const base = item.price * item.quantity;
      const gst = (base * item.gstPercent) / 100;
      subtotal += base;
      tax += gst;
    });
    return {
      subtotal,
      tax,
      grandTotal: subtotal + tax
    };
  };

  const { subtotal, tax, grandTotal } = calculateTotals();

  const handleGenerateInvoice = async () => {
    if (cart.length === 0) {
      setError('Cart is empty. Add products to generate bill.');
      return;
    }
    setError('');
    setLoading(true);

    const payload = {
      customerName,
      customerPhone,
      paymentMode,
      items: cart.map(i => ({
        productId: i.productId,
        quantity: i.quantity
      }))
    };

    try {
      const res = await api.post('/invoices', payload);
      setCreatedInvoice(res.data);
      setCart([]);
      setCustomerName('');
      setCustomerPhone('');
      fetchProducts(); // Refresh stock numbers
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to generate invoice.');
    } finally {
      setLoading(false);
    }
  };

  const downloadPdf = (invoiceId) => {
    window.open(`/api/invoices/${invoiceId}/pdf`, '_blank');
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex flex-col lg:flex-row gap-8">
        
        {/* Left Side: Product Selector & Catalog */}
        <div className="lg:w-7/12 space-y-6">
          <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200">
            <h2 className="text-xl font-bold text-gray-900 mb-4 flex items-center space-x-2">
              <Search className="w-5 h-5 text-blue-600" />
              <span>Search Products & Barcode</span>
            </h2>

            <div className="relative mb-6">
              <input
                type="text"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                placeholder="Scan barcode or type product name..."
                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-600 outline-none text-base"
              />
              <Search className="w-5 h-5 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
            </div>

            {/* Product Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 max-h-[520px] overflow-y-auto pr-2">
              {filteredProducts.map(product => {
                const isLowStock = product.currentStock <= product.minStockAlert;
                const isOut = product.currentStock === 0;

                return (
                  <div
                    key={product.id}
                    onClick={() => !isOut && addToCart(product)}
                    className={`p-3.5 rounded-xl border transition cursor-pointer flex gap-3 ${
                      isOut
                        ? 'bg-gray-50 border-gray-200 opacity-60 cursor-not-allowed'
                        : 'bg-white border-gray-200 hover:border-blue-500 hover:shadow-md'
                    }`}
                  >
                    {/* Product Image Thumbnail */}
                    <div className="w-16 h-16 rounded-lg bg-gray-100 border border-gray-200 overflow-hidden flex items-center justify-center flex-shrink-0 self-center">
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

                    <div className="flex-1 flex flex-col justify-between">
                      <div>
                        <div className="flex justify-between items-start">
                          <h3 className="font-semibold text-gray-900 text-sm leading-tight">{product.name}</h3>
                          <span className="text-[11px] px-2 py-0.5 rounded-full font-medium bg-gray-100 text-gray-600 flex-shrink-0 ml-1">
                            {product.unit}
                          </span>
                        </div>
                        {product.barcode && (
                          <p className="text-[11px] text-gray-400 mt-0.5 font-mono">BC: {product.barcode}</p>
                        )}
                      </div>

                      <div className="flex justify-between items-center mt-2">
                        <div>
                          <span className="text-base font-bold text-blue-600">₹{product.price.toFixed(2)}</span>
                          {product.gstPercent > 0 && (
                            <span className="text-[10px] text-gray-500 ml-1">(+{product.gstPercent}%)</span>
                          )}
                        </div>

                        <span className={`text-[11px] px-2 py-0.5 rounded-full font-semibold ${
                          isOut
                            ? 'bg-red-100 text-red-700'
                            : isLowStock
                            ? 'bg-amber-100 text-amber-700'
                            : 'bg-emerald-100 text-emerald-700'
                        }`}>
                          {product.currentStock} in stock
                        </span>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        </div>

        {/* Right Side: Cart & Bill Generator */}
        <div className="lg:w-5/12 space-y-6">
          <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200 flex flex-col h-full">
            <div className="flex justify-between items-center mb-6 pb-4 border-b border-gray-100">
              <h2 className="text-xl font-bold text-gray-900 flex items-center space-x-2">
                <ShoppingCart className="w-5 h-5 text-blue-600" />
                <span>Current Billing Cart</span>
              </h2>
              <span className="bg-blue-50 text-blue-700 text-xs font-bold px-3 py-1 rounded-full">
                {cart.length} Items
              </span>
            </div>

            {error && (
              <div className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 rounded-xl text-xs flex items-center space-x-2">
                <AlertTriangle className="w-4 h-4 flex-shrink-0" />
                <span>{error}</span>
              </div>
            )}

            {/* Cart Items List */}
            <div className="flex-1 max-h-[300px] overflow-y-auto space-y-3 mb-6">
              {cart.length === 0 ? (
                <div className="text-center py-12 text-gray-400">
                  <ShoppingCart className="w-12 h-12 mx-auto mb-2 opacity-30" />
                  <p className="text-sm">Cart is empty. Click items to add.</p>
                </div>
              ) : (
                cart.map(item => (
                  <div key={item.productId} className="flex justify-between items-center p-3 bg-gray-50 rounded-xl border border-gray-100">
                    <div className="flex items-center space-x-3 flex-1 pr-2">
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
                        <h4 className="font-semibold text-gray-800 text-sm leading-snug">{item.name}</h4>
                        <p className="text-xs text-gray-500">
                          ₹{item.price} x {item.quantity} = <strong className="text-gray-900">₹{(item.price * item.quantity).toFixed(2)}</strong>
                        </p>
                      </div>
                    </div>

                    <div className="flex items-center space-x-2">
                      <div className="flex items-center border border-gray-300 rounded-lg bg-white">
                        <button
                          onClick={() => updateQuantity(item.productId, item.quantity - 1)}
                          className="p-1 hover:bg-gray-100 text-gray-600"
                        >
                          <Minus className="w-3.5 h-3.5" />
                        </button>
                        <span className="px-2 text-xs font-bold">{item.quantity}</span>
                        <button
                          onClick={() => updateQuantity(item.productId, item.quantity + 1)}
                          className="p-1 hover:bg-gray-100 text-gray-600"
                        >
                          <Plus className="w-3.5 h-3.5" />
                        </button>
                      </div>

                      <button
                        onClick={() => removeFromCart(item.productId)}
                        className="text-gray-400 hover:text-red-600 p-1"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </div>
                ))
              )}
            </div>

            {/* Customer Details Form */}
            <div className="space-y-3 pt-4 border-t border-gray-100 mb-6">
              <div className="grid grid-cols-2 gap-3">
                <div className="relative">
                  <User className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
                  <input
                    type="text"
                    placeholder="Customer Name"
                    value={customerName}
                    onChange={(e) => setCustomerName(e.target.value)}
                    className="w-full pl-9 pr-3 py-2 text-xs border border-gray-300 rounded-lg focus:ring-1 focus:ring-blue-600 outline-none"
                  />
                </div>
                <div className="relative">
                  <Phone className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
                  <input
                    type="text"
                    placeholder="Phone Number"
                    value={customerPhone}
                    onChange={(e) => setCustomerPhone(e.target.value)}
                    className="w-full pl-9 pr-3 py-2 text-xs border border-gray-300 rounded-lg focus:ring-1 focus:ring-blue-600 outline-none"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-semibold text-gray-600 mb-1">Payment Mode</label>
                <div className="grid grid-cols-3 gap-2">
                  {['CASH', 'CARD', 'UPI'].map(mode => (
                    <button
                      key={mode}
                      type="button"
                      onClick={() => setPaymentMode(mode)}
                      className={`py-1.5 text-xs font-bold rounded-lg border transition ${
                        paymentMode === mode
                          ? 'bg-blue-600 text-white border-blue-600'
                          : 'bg-white text-gray-600 border-gray-300 hover:bg-gray-50'
                      }`}
                    >
                      {mode}
                    </button>
                  ))}
                </div>
              </div>
            </div>

            {/* Totals Summary & Generate Button */}
            <div className="bg-gray-50 p-4 rounded-xl space-y-2 mb-4 border border-gray-200">
              <div className="flex justify-between text-xs text-gray-600">
                <span>Subtotal:</span>
                <span>₹{subtotal.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-xs text-gray-600">
                <span>GST Tax:</span>
                <span>₹{tax.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-base font-bold text-gray-900 pt-2 border-t border-gray-200">
                <span>Grand Total:</span>
                <span className="text-blue-600">₹{grandTotal.toFixed(2)}</span>
              </div>
            </div>

            <button
              onClick={handleGenerateInvoice}
              disabled={loading || cart.length === 0}
              className="w-full bg-emerald-600 hover:bg-emerald-700 text-white py-3 rounded-xl font-bold text-base shadow-md transition disabled:opacity-50 flex items-center justify-center space-x-2"
            >
              <FileText className="w-5 h-5" />
              <span>{loading ? 'Processing Transaction...' : 'Generate Bill & Save Invoice'}</span>
            </button>
          </div>
        </div>
      </div>

      {/* Invoice Generated Success Modal */}
      {createdInvoice && (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-xs flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl max-w-md w-full p-6 text-center shadow-2xl space-y-4">
            <div className="w-16 h-16 bg-emerald-100 text-emerald-600 rounded-full flex items-center justify-center mx-auto">
              <CheckCircle2 className="w-10 h-10" />
            </div>
            <h3 className="text-2xl font-bold text-gray-900">Invoice Generated!</h3>
            <p className="text-sm text-gray-500">
              Invoice Number: <strong className="text-gray-800">{createdInvoice.invoiceNumber}</strong>
            </p>
            <p className="text-xl font-bold text-emerald-600">Total Billed: ₹{createdInvoice.grandTotal.toFixed(2)}</p>

            <div className="pt-4 flex flex-col space-y-3">
              <button
                onClick={() => downloadPdf(createdInvoice.id)}
                className="w-full bg-blue-600 hover:bg-blue-700 text-white py-2.5 rounded-xl font-semibold shadow-md flex items-center justify-center space-x-2"
              >
                <Printer className="w-5 h-5" />
                <span>View & Download Invoice PDF</span>
              </button>
              <button
                onClick={() => setCreatedInvoice(null)}
                className="w-full bg-gray-100 hover:bg-gray-200 text-gray-700 py-2.5 rounded-xl font-semibold"
              >
                Close & Next Customer
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
