import React from 'react';

function DashboardHome() {
  return (
    <div style={{ padding: '2rem' }}>
      <h2>Book Charging</h2>
      {/* Book Charging widget will go here */}
      <div style={{ marginTop: '2rem' }}>
        <h3>Recent Charging History</h3>
        {/* Recent Charging History table will go here */}
      </div>
      <div style={{ marginTop: '2rem', display: 'flex', gap: '2rem' }}>
        <div>
          <h3>Payment</h3>
          {/* Payment widget will go here */}
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