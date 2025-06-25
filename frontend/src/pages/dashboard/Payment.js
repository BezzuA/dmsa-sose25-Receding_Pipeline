import React, { useEffect, useState } from 'react';
import { getPaymentsByUser } from '../../api/paymentApi';
import { getBalance } from '../../api/userApi';

function Payment() {
  const [payments, setPayments] = useState([]);
  const [balance, setBalance] = useState(0);
  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
      getPaymentsByUser(user.id).then(res => setPayments(res.data));
      getBalance(user.id).then(res => setBalance(res.data));
    }
  }, []);

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Payment</h2>
      <div style={{ marginBottom: '1rem' }}>Balance: <b>${balance.toFixed(2)}</b></div>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th>Date</th>
            <th>Amount</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {payments.map(item => (
            <tr key={item.id}>
              <td>{item.date || item.createdAt}</td>
              <td>${item.amount}</td>
              <td>{item.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Payment; 