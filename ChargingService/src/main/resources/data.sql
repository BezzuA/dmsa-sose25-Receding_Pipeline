-- Sample charging stations
INSERT INTO charging (name, owner_id, price_per_minute, latitude, longitude, available_from, available_until) VALUES 
('Downtown Station', 3, 0.50, 40.7128, -74.0060, '2025-01-01 00:00:00', '2025-12-31 23:59:59'),
('Mall Parking', 3, 0.75, 40.7589, -73.9851, '2025-01-01 00:00:00', '2025-12-31 23:59:59'),
('Airport Terminal', 3, 1.00, 40.6413, -73.7781, '2025-01-01 00:00:00', '2025-12-31 23:59:59'),
('City Center', 5, 0.80, 40.7500, -73.9900, '2025-01-01 00:00:00', '2025-12-31 23:59:59'); 