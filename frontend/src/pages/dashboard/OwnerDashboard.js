import React, { useEffect, useState } from 'react';
import { getStationsByOwner } from '../../api/chargingApi';
import { getAllBookings, completeBooking, cancelBooking, getBookingsForOwner } from '../../api/bookingApi';

function OwnerDashboard() {
  const [bookings, setBookings] = useState([]);
  const [stations, setStations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const user = JSON.parse(localStorage.getItem('user'));
  const ownerId = user.id;

  useEffect(() => {
    if (!user) return;
    setLoading(true);
    Promise.all([
      getStationsByOwner(user.id),
      getBookingsForOwner(ownerId)
    ]).then(([stationsRes, bookingsRes]) => {
      setStations(stationsRes.data);
      setBookings(Array.isArray(bookingsRes) ? bookingsRes : []);
      setLoading(false);
    }).catch(() => {
      setError('Failed to load data');
      setBookings([]);
      setLoading(false);
    });
  }, [ownerId]);

  const handleApprove = async (id) => {
    await completeBooking(id);
    getBookingsForOwner(ownerId).then(data => setBookings(Array.isArray(data) ? data : []));
  };
  const handleCancel = async (id) => {
    await cancelBooking(id);
    getBookingsForOwner(ownerId).then(data => setBookings(Array.isArray(data) ? data : []));
  };

  const safeBookings = Array.isArray(bookings) ? bookings : [];

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Owner Dashboard</h2>
      {error && <div className="error">{error}</div>}
      {loading ? (
        <div>Loading...</div>
      ) : (
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr>
              <th>Booking ID</th>
              <th>Station ID</th>
              <th>User ID</th>
              <th>Status</th>
              <th>Start Time</th>
              <th>End Time</th>
              <th>Amount</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {safeBookings.length === 0 ? (
              <tr><td colSpan={7} style={{ textAlign: 'center' }}>No bookings for your stations</td></tr>
            ) : (
              safeBookings.map(b => (
                <tr key={b.id}>
                  <td>{b.id}</td>
                  <td>{b.stationId}</td>
                  <td>{b.userId}</td>
                  <td>{b.status}</td>
                  <td>{b.startTime}</td>
                  <td>{b.endTime}</td>
                  <td>{b.amount ? `$${b.amount.toFixed(2)}` : '-'}</td>
                  <td>
                    {b.status === 'PENDING' && (
                      <>
                        <button onClick={() => handleApprove(b.id)} style={{ marginRight: 8 }}>Approve</button>
                        <button onClick={() => handleCancel(b.id)}>Cancel</button>
                      </>
                    )}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default OwnerDashboard; 