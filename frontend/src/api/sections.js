import axios from 'axios'

const BASE = '/api/sections'

export const getSectionsList = () =>
    axios.get(`${BASE}/list`).then(res => res.data)

export const getSections = () =>
    axios.get(BASE).then(res => res.data)

export const getSectionById = (id) =>
    axios.get(`${BASE}/${id}`).then(res => res.data)

export const createSection = (data) =>
    axios.post(BASE, data).then(res => res.data)

export const updateSection = (id, data) =>
    axios.put(`${BASE}/${id}`, data).then(res => res.data)

export const deleteSection = (id) =>
    axios.delete(`${BASE}/${id}`)

export const markSectionPassed = (id) =>
    axios.put(`${BASE}/${id}/pass`).then(res => res.data)