import axios from 'axios'

const BASE = '/api/conspects'

export const getConspects = () =>
    axios.get(BASE).then(res => res.data)

export const getConspectById = (id) =>
    axios.get(`${BASE}/${id}`).then(res => res.data)

export const createConspect = (data) =>
    axios.post(BASE, data).then(res => res.data)

export const deleteConspect = (id) =>
    axios.delete(`${BASE}/${id}`)