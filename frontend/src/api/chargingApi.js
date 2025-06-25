// Charging API service for ChargingService
import axios from 'axios';

const BASE_URL = process.env.REACT_APP_CHARGING_SERVICE_URL || '/stations';

export const getAllStations = () => axios.get(`${BASE_URL}`);
export const getStation = (id) => axios.get(`${BASE_URL}/${id}`);
export const createStation = (station) => axios.post(`${BASE_URL}`, station);
export const updateStation = (id, updated) => axios.put(`${BASE_URL}/${id}`, updated);
export const deleteStation = (id) => axios.delete(`${BASE_URL}/${id}`);
export const getStationsByOwner = (ownerId) => axios.get(`${BASE_URL}/owner/${ownerId}`); 