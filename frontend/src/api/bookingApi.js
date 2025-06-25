// Booking API service for BookingService
import axios from 'axios';

const BASE_URL = process.env.REACT_APP_BOOKING_SERVICE_URL || '/api/bookings';

export const getAllBookings = () => axios.get(`${BASE_URL}`);
export const getBooking = (id) => axios.get(`${BASE_URL}/${id}`);
export const createBooking = (booking) => axios.post(`${BASE_URL}`, booking);
export const cancelBooking = (id) => axios.post(`${BASE_URL}/${id}/cancel`);
export const getBookingsByUserId = (userId) => axios.get(`${BASE_URL}/user/${userId}`);
export const completeBooking = (id) => axios.post(`${BASE_URL}/${id}/complete`);
export const getAvailableStations = (start, end) => axios.get(`${BASE_URL}/available`, { params: { start, end } });
export const bookStation = (request) => axios.post(`${BASE_URL}/book`, request); 