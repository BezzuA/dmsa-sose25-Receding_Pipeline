import React, { useEffect, useState } from 'react';
import { getUser, updateUser } from '../../api/userApi';

function Settings() {
  const [profile, setProfile] = useState({ name: '', email: '' });
  const [message, setMessage] = useState('');
  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
      getUser(user.id).then(res => setProfile(res.data));
    }
  }, []);

  const handleChange = e => {
    setProfile({ ...profile, [e.target.name]: e.target.value });
  };

  const handleSave = async e => {
    e.preventDefault();
    const user = JSON.parse(localStorage.getItem('user'));
    try {
      await updateUser(user.id, profile);
      setMessage('Profile updated!');
    } catch {
      setMessage('Update failed.');
    }
  };

  return (
    <div style={{ padding: '2rem', maxWidth: 400 }}>
      <h2>Settings</h2>
      <form onSubmit={handleSave} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
        <input name="name" value={profile.name} onChange={handleChange} placeholder="Name" />
        <input name="email" value={profile.email} onChange={handleChange} placeholder="Email" />
        <button type="submit">Save</button>
      </form>
      {message && <div style={{ marginTop: '1rem' }}>{message}</div>}
    </div>
  );
}

export default Settings; 