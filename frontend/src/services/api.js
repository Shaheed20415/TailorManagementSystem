import axios from 'axios';
export const api = axios.create({ baseURL: 'http://localhost:8080/api' });
api.interceptors.response.use(r => r, e => { throw new Error(e.response?.data?.message || 'Server error'); });
export const money = n => `₹${Number(n || 0).toLocaleString('en-IN')}`;
export const fmt = d => d ? new Date(d).toLocaleDateString('en-IN') : '-';
