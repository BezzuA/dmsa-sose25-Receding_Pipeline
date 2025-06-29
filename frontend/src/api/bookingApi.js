// Booking API service for BookingService
import axios from 'axios';

const BASE_URL = process.env.REACT_APP_BOOKING_SERVICE_URL || '/api/bookings';

export const getAllBookings = () => axios.get(`${BASE_URL}`);
export const getBooking = (id) => axios.get(`${BASE_URL}/${id}`);
export const createBooking = (booking) => axios.post(`${BASE_URL}`, booking);
export const cancelBooking = async (bookingId) => {
  const response = await fetch(`/api/bookings/${bookingId}/cancel`, { method: 'POST' });
  if (!response.ok) throw new Error('Failed to cancel booking');
  return response;
};
export const getBookingsByUserId = (userId) => axios.get(`${BASE_URL}/user/${userId}`);
export const completeBooking = async (bookingId) => {
  const response = await fetch(`/api/bookings/${bookingId}/complete`, { method: 'POST' });
  if (!response.ok) throw new Error('Failed to approve booking');
  return response.json();
};
export const getAvailableStations = (start, end) => axios.get(`${BASE_URL}/available`, {
  params: { start, end }
});
export const bookStation = (request) => axios.post(`${BASE_URL}/book`, request);
export const getBookingsForOwner = async (ownerId) => {
  const response = await fetch(`/api/bookings/owner-bookings?ownerId=${ownerId}`);
  if (!response.ok) throw new Error('Failed to fetch owner bookings');
  return response.json();
}; 