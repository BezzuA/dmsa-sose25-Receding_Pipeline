import React, { useEffect, useState } from 'react';
import { getBookingsByUserId } from '../../api/bookingApi';
import { getBalance } from '../../api/userApi';

function DashboardHome() {
  const [recentHistory, setRecentHistory] = useState([]);
  const [balance, setBalance] = useState(null);

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
      getBookingsByUserId(user.id).then(res => {
        // Show the 5 most recent bookings
        setRecentHistory(res.data.slice(-5).reverse());
      });
      getBalance(user.id).then(res => setBalance(res.data));
    }
  }, []);

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Book Charging</h2>
      {/* Book Charging widget will go here */}
      <div style={{ marginTop: '2rem' }}>
        <h3>Recent Charging History</h3>
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr>
              <th>Station</th>
              <th>Start</th>
              <th>End</th>
              <th>Amount</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {recentHistory.length === 0 ? (
              <tr><td colSpan={5} style={{ textAlign: 'center' }}>No recent bookings</td></tr>
            ) : (
              recentHistory.map(item => (
                <tr key={item.id}>
                  <td>{item.stationName || item.stationId}</td>
                  <td>{item.startTime || item.start}</td>
                  <td>{item.endTime || item.end}</td>
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
              ))
            )}
          </tbody>
        </table>
      </div>
      <div style={{ marginTop: '2rem', display: 'flex', gap: '2rem' }}>
        <div>
          <h3>Payment</h3>
          <div style={{ fontSize: '1.2rem', marginTop: '0.5rem' }}>
            Balance: {balance !== null ? <b>${balance.toFixed(2)}</b> : 'Loading...'}
          </div>
        </div>
        <div>
          <h3>Statistics</h3>
          {/* Statistics widget will go here */}
        </div>
      </div>
    </div>
  );
}

export default DashboardHome; 