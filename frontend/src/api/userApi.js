// User API service for UserService
import axios from 'axios';

const BASE_URL = process.env.REACT_APP_USER_SERVICE_URL || '/api/users';

export const signup = (data) => axios.post(`${BASE_URL}/signup`, data);
export const signin = (data) => axios.post(`${BASE_URL}/signin`, data);
export const updateUser = (id, updatedUser) => axios.put(`${BASE_URL}/${id}`, updatedUser);
export const getAllUsers = () => axios.get(`${BASE_URL}`);
export const getUser = (id) => axios.get(`${BASE_URL}/${id}`);
export const createUser = (data) => axios.post(`${BASE_URL}`, data);
export const deleteUser = (id) => axios.delete(`${BASE_URL}/${id}`);
export const getBalance = (id) => axios.get(`${BASE_URL}/${id}/balance`);
export const topUp = (id, amount) => axios.post(`${BASE_URL}/${id}/topup`, null, { params: { amount } });
export const deductBalance = (id, amount) => axios.post(`${BASE_URL}/${id}/deduct`, null, { params: { amount } }); 