import React, { useEffect, useState } from 'react';
import { getPaymentsByUser } from '../../api/paymentApi';
import { getBalance, topUp } from '../../api/userApi';

function Payment() {
  const [payments, setPayments] = useState([]);
  const [balance, setBalance] = useState(0);
  const [showModal, setShowModal] = useState(false);
  const [topUpAmount, setTopUpAmount] = useState('');
  const [topUpError, setTopUpError] = useState('');
  const [topUpLoading, setTopUpLoading] = useState(false);

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
      getPaymentsByUser(user.id).then(res => setPayments(res.data));
      getBalance(user.id).then(res => setBalance(res.data));
    }
  }, []);

  const handleTopUp = async (e) => {
    e.preventDefault();
    setTopUpError('');
    setTopUpLoading(true);
    const user = JSON.parse(localStorage.getItem('user'));
    try {
      await topUp(user.id, topUpAmount);
      const newBalance = await getBalance(user.id);
      setBalance(newBalance.data);
      setShowModal(false);
      setTopUpAmount('');
    } catch (err) {
      setTopUpError('Top up failed.');
    } finally {
      setTopUpLoading(false);
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Payment</h2>
      <div style={{ marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '1rem' }}>
        Balance: <b>${balance.toFixed(2)}</b>
        <button onClick={() => setShowModal(true)} style={{ padding: '0.4rem 1rem', fontSize: '1rem' }}>Top Up Balance</button>
      </div>
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
              <td>{item.paymentTime ? new Date(item.paymentTime).toLocaleString() : '-'}</td>
              <td>${item.amount !== undefined ? Number(item.amount).toFixed(2) : '-'}</td>
              <td>{item.status || '-'}</td>
            </tr>
          ))}
        </tbody>
      </table>
      {showModal && (
        <div style={{
          position: 'fixed', top: 0, left: 0, width: '100vw', height: '100vh',
          background: 'rgba(0,0,0,0.3)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000
        }}>
          <div style={{ background: '#fff', padding: '2rem', borderRadius: '10px', minWidth: 300 }}>
            <h3>Top Up Balance</h3>
            <form onSubmit={handleTopUp} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              <input
                type="number"
                min="1"
                step="0.01"
                placeholder="Enter amount"
                value={topUpAmount}
                onChange={e => setTopUpAmount(e.target.value)}
                required
                style={{ padding: '0.5rem', fontSize: '1rem' }}
              />
              {topUpError && <div style={{ color: 'red' }}>{topUpError}</div>}
              <div style={{ display: 'flex', gap: '1rem', justifyContent: 'flex-end' }}>
                <button type="button" onClick={() => setShowModal(false)} disabled={topUpLoading}>Cancel</button>
                <button type="submit" disabled={topUpLoading || !topUpAmount}>Top Up</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default Payment; 