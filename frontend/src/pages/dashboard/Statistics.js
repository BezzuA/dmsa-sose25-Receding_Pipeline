import React, { useEffect, useState } from 'react';
import { getAllStats } from '../../api/statisticsApi';

function Statistics() {
  const [stats, setStats] = useState([]);
  useEffect(() => {
    getAllStats().then(res => setStats(res.data));
  }, []);

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Statistics</h2>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th>Station</th>
            <th>Total Sessions</th>
            <th>Total Amount</th>
          </tr>
        </thead>
        <tbody>
          {stats.map(item => (
            <tr key={item.stationId || item.id}>
              <td>{item.stationName || item.stationId}</td>
              <td>{item.totalSessions}</td>
              <td>${item.totalAmount}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Statistics; 