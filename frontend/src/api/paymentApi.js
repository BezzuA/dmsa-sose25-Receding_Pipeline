// Payment API service for PaymentService
import axios from 'axios';

const BASE_URL = process.env.REACT_APP_PAYMENT_SERVICE_URL || '/api/payments';

export const getAllPayments = () => axios.get(`${BASE_URL}`);
export const getPayment = (id) => axios.get(`${BASE_URL}/${id}`);
export const makePayment = (payment) => axios.post(`${BASE_URL}`, payment);
export const getPaymentsByUser = (userId) => axios.get(`${BASE_URL}/user/${userId}`);
export const getPaymentsByBooking = (bookingId) => axios.get(`${BASE_URL}/booking/${bookingId}`); 