import React from 'react';
import './Sidebar.css';
import { NavLink } from 'react-router-dom';

const Sidebar = ({ onNavigate, active }) => {
  const menu = [
    'Dashboard',
    'Book Charging',
    'Charging History',
    'Payment',
    'Statistics',
    'Settings',
  ];
  // const user = JSON.parse(localStorage.getItem('user'));
  // const isOwner = user && user.role === 'OWNER';

  return (
    <div className="sidebar">
      <h2>Dashboard</h2>
      <ul>
        {menu.map(item => (
          <li
            key={item}
            className={active === item ? 'active' : ''}
            onClick={() => onNavigate(item)}
          >
            {item}
          </li>
        ))}
        {/* Removed conditional NavLink for Statistics */}
      </ul>
    </div>
  );
};

export default Sidebar; 