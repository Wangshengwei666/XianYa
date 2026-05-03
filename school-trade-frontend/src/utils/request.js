import axios from 'axios';

const service = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080',
    timeout: 10000,
    withCredentials: true
});

service.interceptors.request.use(
    config => config,
    error => {
        console.error('Request Error:', error);
        return Promise.reject(error);
    }
);

service.interceptors.response.use(
    response => {
        const res = response.data;
        return res;
    },
    error => {
        console.error('Response Error:', error);
        return Promise.reject(error);
    }
);

export default service;
