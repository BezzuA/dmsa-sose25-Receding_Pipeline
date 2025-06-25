import React, { useState, useEffect } from 'react';
import { getAvailableStations, bookStation } from '../../api/bookingApi';

function BookCharging() {
  const [stations, setStations] = useState([]);
  const [selectedStation, setSelectedStation] = useState('');
  const [start, setStart] = useState('');
  const [end, setEnd] = useState('');
  const [message, setMessage] = useState('');

  useEffect(() => {
    if (start && end) {
      getAvailableStations(start, end)
        .then(res => setStations(res.data))
        .catch(() => setStations([]));
    }
  }, [start, end]);

  const handleBook = async (e) => {
    e.preventDefault();
    setMessage('');
    const user = JSON.parse(localStorage.getItem('user'));
    if (!user) return setMessage('User not found');
    try {
      await bookStation({ userId: user.id, stationId: selectedStation, start, end });
      setMessage('Booking successful!');
    } catch {
      setMessage('Booking failed.');
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Book Charging</h2>
      <form onSubmit={handleBook} style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
        <select value={selectedStation} onChange={e => setSelectedStation(e.target.value)} required>
          <option value="">Location</option>
          {stations.map(station => (
            <option key={station.id} value={station.id}>{station.location || station.name}</option>
          ))}
        </select>
        <input type="datetime-local" value={start} onChange={e => setStart(e.target.value)} required />
        <input type="datetime-local" value={end} onChange={e => setEnd(e.target.value)} required />
        <button type="submit">Schedule</button>
      </form>
      {message && <div style={{ marginTop: '1rem' }}>{message}</div>}
    </div>
  );
}

export default BookCharging; 