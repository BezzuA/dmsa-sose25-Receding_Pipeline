import React, { useEffect, useState } from 'react';
import { getStatsByOwner } from '../../api/statisticsApi';

function Statistics() {
  const [stats, setStats] = useState(null);
  const user = JSON.parse(localStorage.getItem('user'));
  const ownerId = user?.id;
  useEffect(() => {
    if (ownerId) {
      getStatsByOwner(ownerId).then(res => setStats(res.data));
    }
  }, [ownerId]);

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Statistics</h2>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th>Owner</th>
            <th>Total Sessions</th>
            <th>Total Amount</th>
          </tr>
        </thead>
        <tbody>
          {stats ? (
            <tr key={stats.ownerId}>
              <td>{user?.email || stats.ownerId}</td>
              <td>{stats.totalBookings}</td>
              <td>${stats.totalEarnings}</td>
            </tr>
          ) : (
            <tr><td colSpan="3">No statistics available.</td></tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default Statistics; 