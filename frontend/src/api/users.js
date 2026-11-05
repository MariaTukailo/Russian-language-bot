import axios from 'axios'

const BASE = '/api/users'

export const getUsers = () =>
    axios.get(BASE).then(res => res.data)

export const getUserById = (id) =>
    axios.get(`${BASE}/${id}`).then(res => res.data)