import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export const api = axios.create({
  baseURL: `${API_BASE_URL}/api`
});

api.interceptors.response.use(
  r => r,
  e => {
    throw new Error(e.response?.data?.message || 'Server error');
  }
);

export const money = n => `₹${Number(n || 0).toLocaleString('en-IN')}`;
export const fmt = d => d ? new Date(d).toLocaleDateString('en-IN') : '-';