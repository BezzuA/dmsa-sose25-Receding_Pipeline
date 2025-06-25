import React from 'react';
import './Sidebar.css';

const Sidebar = ({ onNavigate, active }) => {
  const menu = [
    'Dashboard',
    'Book Charging',
    'Charging History',
    'Payment',
    'Statistics',
    'Settings',
  ];
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
      </ul>
    </div>
  );
};

export default Sidebar; 