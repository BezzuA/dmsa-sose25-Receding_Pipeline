import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import Sidebar from './layout/Sidebar';
import DashboardHome from './pages/dashboard/DashboardHome';
import BookCharging from './pages/dashboard/BookCharging';
import ChargingHistory from './pages/dashboard/ChargingHistory';
import Payment from './pages/dashboard/Payment';
import Statistics from './pages/dashboard/Statistics';
import Settings from './pages/dashboard/Settings';
import './App.css';

function DashboardLayout({ user, onLogout }) {
  const navigate = useNavigate();
  const menuToRoute = {
    'Dashboard': '/',
    'Book Charging': '/book-charging',
    'Charging History': '/charging-history',
    'Payment': '/payment',
    'Statistics': '/statistics',
    'Settings': '/settings',
  };
  const routeToMenu = Object.fromEntries(Object.entries(menuToRoute).map(([k, v]) => [v, k]));
  const activePage = routeToMenu[window.location.pathname] || 'Dashboard';

  return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      <Sidebar
        onNavigate={menu => navigate(menuToRoute[menu])}
        active={activePage}
      />
      <div style={{ flex: 1 }}>
        <div style={{ display: 'flex', justifyContent: 'flex-end', padding: '1rem' }}>
          <span style={{ marginRight: '1rem' }}>Welcome, {user.name || user.email}!</span>
          <button onClick={onLogout}>Logout</button>
        </div>
        <div className="dashboard-content">
          <Routes>
            <Route path="/" element={<DashboardHome />} />
            <Route path="/book-charging" element={<BookCharging />} />
            <Route path="/charging-history" element={<ChargingHistory />} />
            <Route path="/payment" element={<Payment />} />
            <Route path="/statistics" element={<Statistics />} />
            <Route path="/settings" element={<Settings />} />
            <Route path="*" element={<Navigate to="/" />} />
          </Routes>
        </div>
      </div>
    </div>
  );
}

function App() {
  const [user, setUser] = useState(() => {
    // Try to load user from localStorage
    const saved = localStorage.getItem('user');
    return saved ? JSON.parse(saved) : null;
  });
  const [showSignup, setShowSignup] = useState(false);

  const handleLogin = (userData) => {
    setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
  };

  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem('user');
  };

  const handleSignup = () => {
    setShowSignup(false);
  };

  return (
    <Router>
      {!user ? (
        <div>
          {showSignup ? (
            <>
              <SignupPage onSignup={handleSignup} />
              <p>Already have an account? <button onClick={() => setShowSignup(false)}>Login</button></p>
            </>
          ) : (
            <>
              <LoginPage onLogin={handleLogin} />
              <p>Don't have an account? <button onClick={() => setShowSignup(true)}>Sign Up</button></p>
            </>
          )}
        </div>
      ) : (
        <DashboardLayout user={user} onLogout={handleLogout} />
      )}
    </Router>
  );
}

export default App;
