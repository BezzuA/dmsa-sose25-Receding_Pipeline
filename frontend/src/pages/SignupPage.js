import React, { useState } from 'react';
import { signup } from '../api/userApi';

function SignupPage({ onSignup }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [role, setRole] = useState('CAR_USER');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess(false);
    try {
      await signup({ email, password, username: name, role });
      setSuccess(true);
      onSignup && onSignup();
    } catch (err) {
      setError('Signup failed. Try again.');
    }
  };

  return (
    <div className="auth-container">
      <h2>Sign Up</h2>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1.2rem' }}>
        <input
          type="text"
          placeholder="Name"
          value={name}
          onChange={e => setName(e.target.value)}
          required
        />
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={e => setPassword(e.target.value)}
          required
        />
        <div style={{ display: 'flex', flexDirection: 'row', gap: '2rem', marginTop: '0.5rem', marginBottom: '0.5rem' }}>
          <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <input
              type="radio"
              name="role"
              value="OWNER"
              checked={role === 'OWNER'}
              onChange={() => setRole('OWNER')}
            />
            Owner
          </label>
          <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <input
              type="radio"
              name="role"
              value="CAR_USER"
              checked={role === 'CAR_USER'}
              onChange={() => setRole('CAR_USER')}
            />
            User
          </label>
        </div>
        <button type="submit">Sign Up</button>
        {error && <div className="error">{error}</div>}
        {success && <div className="success">Signup successful! You can now log in.</div>}
      </form>
    </div>
  );
}

export default SignupPage; 