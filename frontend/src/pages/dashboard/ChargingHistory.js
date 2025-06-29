import React, { useEffect, useState } from 'react';
import { getBookingsByUserId } from '../../api/bookingApi';

function ChargingHistory() {
  const [history, setHistory] = useState([]);
  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
      getBookingsByUserId(user.id).then(res => setHistory(res.data));
    }
  }, []);

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Charging History</h2>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th>Location</th>
            <th>Date</th>
            <th>Amount</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {history.map(item => (
            <tr key={item.id}>
              <td>{item.stationName || item.stationId}</td>
              <td>{item.startTime ? new Date(item.startTime).toLocaleString() : '-'}</td>
              <td>{item.amount ? `$${item.amount}` : '-'}</td>
              <td>
                <span style={{
                  color:
                    item.status === 'COMPLETED' ? 'green' :
                    item.status === 'IN_USE' ? 'orange' :
                    item.status === 'APPROVED' ? 'blue' :
                    item.status === 'PENDING' ? 'gray' :
                    item.status === 'CANCELLED' ? 'red' :
                    item.status === 'FAILED' ? 'red' : 'black'
                }}>{item.status}</span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default ChargingHistory; 