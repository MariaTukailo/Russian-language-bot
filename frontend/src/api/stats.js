import axios from 'axios'

export const getDashboardStats = () =>
    axios.get('/api/stats/dashboard').then(res => res.data)