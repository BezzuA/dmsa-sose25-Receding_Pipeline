// Statistics API service for StatisticsService
import axios from 'axios';

const BASE_URL = process.env.REACT_APP_STATISTICS_SERVICE_URL || '/api/statistics';

export const getAllStats = () => axios.get(`${BASE_URL}`);
export const getStatsByStation = (stationId) => axios.get(`${BASE_URL}/station/${stationId}`); 