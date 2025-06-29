import React, { useEffect, useState } from 'react';
import { getStationsByOwner, createStation, updateStation, deleteStation, isStationInUse } from '../../api/chargingApi';
import { getAllBookings, completeBooking, cancelBooking, getBookingsForOwner, approveBooking, startBooking } from '../../api/bookingApi';

function OwnerDashboard() {
  const [bookings, setBookings] = useState([]);
  const [stations, setStations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showStationModal, setShowStationModal] = useState(false);
  const [editingStation, setEditingStation] = useState(null);
  const [stationForm, setStationForm] = useState({
    name: '',
    pricePerMinute: '',
    latitude: '',
    longitude: '',
    availableFrom: '',
    availableUntil: ''
  });
  const [stationError, setStationError] = useState('');
  const user = JSON.parse(localStorage.getItem('user'));
  const ownerId = user.id;
  const [stationInUse, setStationInUse] = useState({});

  const fetchStations = () => {
    getStationsByOwner(ownerId).then(res => {
      setStations(res.data);
      // Fetch in-use status for each station
      Promise.all(
        res.data.map(station =>
          isStationInUse(station.id).then(resp => [station.id, resp.data])
        )
      ).then(results => {
        const statusMap = {};
        results.forEach(([id, inUse]) => { statusMap[id] = inUse; });
        setStationInUse(statusMap);
      });
    });
  };

  useEffect(() => {
    if (!user) return;
    setLoading(true);
    Promise.all([
      getStationsByOwner(user.id),
      getBookingsForOwner(ownerId)
    ]).then(([stationsRes, bookingsRes]) => {
      setStations(stationsRes.data);
      setBookings(Array.isArray(bookingsRes) ? bookingsRes : []);
      // Fetch in-use status for each station
      Promise.all(
        stationsRes.data.map(station =>
          isStationInUse(station.id).then(resp => [station.id, resp.data])
        )
      ).then(results => {
        const statusMap = {};
        results.forEach(([id, inUse]) => { statusMap[id] = inUse; });
        setStationInUse(statusMap);
      });
      setLoading(false);
    }).catch(() => {
      setError('Failed to load data');
      setBookings([]);
      setLoading(false);
    });
  }, [ownerId]);

  const handleApprove = async (id) => {
    await approveBooking(id);
    getBookingsForOwner(ownerId).then(data => setBookings(Array.isArray(data) ? data : []));
  };
  const handleStart = async (id) => {
    await startBooking(id);
    getBookingsForOwner(ownerId).then(data => setBookings(Array.isArray(data) ? data : []));
  };
  const handleComplete = async (id) => {
    await completeBooking(id);
    getBookingsForOwner(ownerId).then(data => setBookings(Array.isArray(data) ? data : []));
  };
  const handleCancel = async (id) => {
    await cancelBooking(id);
    getBookingsForOwner(ownerId).then(data => setBookings(Array.isArray(data) ? data : []));
  };

  const safeBookings = Array.isArray(bookings) ? bookings : [];

  // Station CRUD handlers
  const openCreateStation = () => {
    setEditingStation(null);
    setStationForm({
      name: '',
      pricePerMinute: '',
      latitude: '',
      longitude: '',
      availableFrom: '',
      availableUntil: ''
    });
    setShowStationModal(true);
    setStationError('');
  };
  const openEditStation = (station) => {
    setEditingStation(station);
    setStationForm({
      name: station.name,
      pricePerMinute: station.pricePerMinute,
      latitude: station.latitude,
      longitude: station.longitude,
      availableFrom: station.availableFrom ? station.availableFrom.slice(0, 16) : '',
      availableUntil: station.availableUntil ? station.availableUntil.slice(0, 16) : ''
    });
    setShowStationModal(true);
    setStationError('');
  };
  const handleDeleteStation = async (id) => {
    if (window.confirm('Are you sure you want to delete this station?')) {
      await deleteStation(id);
      fetchStations();
    }
  };
  const handleStationFormChange = (e) => {
    setStationForm({ ...stationForm, [e.target.name]: e.target.value });
  };
  const handleStationFormSubmit = async (e) => {
    e.preventDefault();
    setStationError('');
    try {
      const payload = {
        ...stationForm,
        ownerId: ownerId,
        pricePerMinute: parseFloat(stationForm.pricePerMinute),
        latitude: parseFloat(stationForm.latitude),
        longitude: parseFloat(stationForm.longitude),
        availableFrom: stationForm.availableFrom,
        availableUntil: stationForm.availableUntil
      };
      if (editingStation) {
        await updateStation(editingStation.id, payload);
      } else {
        await createStation(payload);
      }
      setShowStationModal(false);
      fetchStations();
    } catch (err) {
      setStationError('Failed to save station.');
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Owner Dashboard</h2>
      <h3>Your Charging Stations</h3>
      <button onClick={openCreateStation} style={{ marginBottom: '1rem' }}>Add New Station</button>
      <table style={{ width: '100%', borderCollapse: 'collapse', marginBottom: '2rem' }}>
        <thead>
          <tr>
            <th>Name</th>
            <th>Price/Min</th>
            <th>Lat</th>
            <th>Lng</th>
            <th>Available From</th>
            <th>Available Until</th>
            <th>In Use</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {stations.length === 0 ? (
            <tr><td colSpan={8} style={{ textAlign: 'center' }}>No stations found</td></tr>
          ) : (
            stations.map(station => (
              <tr key={station.id}>
                <td>{station.name}</td>
                <td>${station.pricePerMinute}</td>
                <td>{station.latitude}</td>
                <td>{station.longitude}</td>
                <td>{station.availableFrom ? new Date(station.availableFrom).toLocaleString() : '-'}</td>
                <td>{station.availableUntil ? new Date(station.availableUntil).toLocaleString() : '-'}</td>
                <td>{stationInUse[station.id] ? 'Yes' : 'No'}</td>
                <td>
                  <button onClick={() => openEditStation(station)} style={{ marginRight: 8 }}>Edit</button>
                  <button onClick={() => handleDeleteStation(station.id)}>Delete</button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
      {showStationModal && (
        <div style={{
          position: 'fixed', top: 0, left: 0, width: '100vw', height: '100vh',
          background: 'rgba(0,0,0,0.3)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000
        }}>
          <div style={{ background: '#fff', padding: '2rem', borderRadius: '10px', minWidth: 350 }}>
            <h3>{editingStation ? 'Edit Station' : 'Add New Station'}</h3>
            <form onSubmit={handleStationFormSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              <input name="name" placeholder="Name" value={stationForm.name} onChange={handleStationFormChange} required />
              <input name="pricePerMinute" type="number" step="0.01" placeholder="Price per minute" value={stationForm.pricePerMinute} onChange={handleStationFormChange} required />
              <input name="latitude" type="number" step="0.0001" placeholder="Latitude" value={stationForm.latitude} onChange={handleStationFormChange} required />
              <input name="longitude" type="number" step="0.0001" placeholder="Longitude" value={stationForm.longitude} onChange={handleStationFormChange} required />
              <label>Available From: <input name="availableFrom" type="datetime-local" value={stationForm.availableFrom} onChange={handleStationFormChange} required /></label>
              <label>Available Until: <input name="availableUntil" type="datetime-local" value={stationForm.availableUntil} onChange={handleStationFormChange} required /></label>
              {stationError && <div style={{ color: 'red' }}>{stationError}</div>}
              <div style={{ display: 'flex', gap: '1rem', justifyContent: 'flex-end' }}>
                <button type="button" onClick={() => setShowStationModal(false)}>Cancel</button>
                <button type="submit">Save</button>
              </div>
            </form>
          </div>
        </div>
      )}
      <h3>Bookings for Your Stations</h3>
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
                    {b.status === 'APPROVED' && (
                      <>
                        <button onClick={() => handleStart(b.id)} style={{ marginRight: 8 }}>Start</button>
                        <button onClick={() => handleCancel(b.id)}>Cancel</button>
                      </>
                    )}
                    {b.status === 'IN_USE' && (
                      <>
                        <button onClick={() => handleComplete(b.id)} style={{ marginRight: 8 }}>Complete</button>
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