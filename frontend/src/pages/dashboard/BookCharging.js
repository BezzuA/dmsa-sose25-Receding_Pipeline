import React, { useState, useEffect } from 'react';
import { getAvailableStations, bookStation } from '../../api/bookingApi';
import { getAllStations } from '../../api/chargingApi';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: markerIcon2x,
  iconUrl: markerIcon,
  shadowUrl: markerShadow,
});

function BookCharging() {
  const [stations, setStations] = useState([]);
  const [selectedStationId, setSelectedStationId] = useState("");
  const [selectedStationName, setSelectedStationName] = useState("");
  const [start, setStart] = useState('');
  const [end, setEnd] = useState('');
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetch('http://localhost:8084/api/charging/available')
      .then(res => res.json())
      .then(data => setStations(data));
  }, []);

  const handleBook = async (e) => {
    e.preventDefault();
    setMessage('');
    const user = JSON.parse(localStorage.getItem('user'));
    if (!user) return setMessage('User not found');
    try {
      await bookStation({ userId: user.id, stationId: Number(selectedStationId), startTime: start, endTime: end });
      setMessage('Booking successful!');
    } catch (err) {
      setMessage('Booking failed.');
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Book Charging</h2>
      <form onSubmit={handleBook} style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
        <select value={selectedStationId} onChange={e => {
          setSelectedStationId(e.target.value);
          const station = stations.find(s => s.id.toString() === e.target.value);
          setSelectedStationName(station ? station.name : "");
        }}>
          <option value="">Location</option>
          {stations.map(station => (
            <option key={station.id} value={station.id}>{station.name}</option>
          ))}
        </select>
        <input type="datetime-local" value={start} onChange={e => setStart(e.target.value)} required />
        <input type="datetime-local" value={end} onChange={e => setEnd(e.target.value)} required />
        <button type="submit">Schedule</button>
      </form>
      {message && <div style={{ marginTop: '1rem' }}>{message}</div>}
      <div style={{ height: '400px', width: '100%', marginTop: '2rem' }}>
        <MapContainer center={[51.5145, 7.4653]} zoom={12} style={{ height: '100%', width: '100%' }}>
          <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
          {stations.map(station => (
            <Marker
              key={station.id}
              position={[station.latitude, station.longitude]}
              eventHandlers={{
                click: () => {
                  setSelectedStationId(station.id.toString());
                  setSelectedStationName(station.name);
                }
              }}
            >
              <Popup>
                <b>{station.name}</b><br />
                Price: ${station.pricePerMinute}/min<br />
              </Popup>
            </Marker>
          ))}
        </MapContainer>
      </div>
    </div>
  );
}

export default BookCharging; 