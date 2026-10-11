import axios from 'axios'

const BASE = '/api/topics'

export const getTopicsList = () =>
    axios.get(`${BASE}/list`).then(res => res.data)

export const getTopics = () =>
    axios.get(BASE).then(res => res.data)

export const getTopicById = (id) =>
    axios.get(`${BASE}/${id}`).then(res => res.data)

export const createTopic = (data) =>
    axios.post(BASE, data).then(res => res.data)

export const updateTopic = (id, data) =>
    axios.put(`${BASE}/${id}`, data).then(res => res.data)

export const deleteTopic = (id) =>
    axios.delete(`${BASE}/${id}`)
export const getTopicsBySection = (sectionId) =>
    axios.get(`/api/topics/by-section/${sectionId}`).then(res => res.data)g