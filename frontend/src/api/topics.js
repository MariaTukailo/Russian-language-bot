import axios from 'axios'

const BASE = '/api/topics'

export const getTopicsList = () => axios.get(`${BASE}/list`).then(res => res.data)
export const getTopics = () => axios.get(BASE).then(res => res.data)
export const getTopicById = (id) => axios.get(`${BASE}/${id}`).then(res => res.data)

export const createTopic = (data, sectionId) => {
    const url = sectionId ? `${BASE}?sectionId=${sectionId}` : BASE
    return axios.post(url, data).then(res => res.data)
}

export const updateTopic = (id, data) => axios.put(`${BASE}/${id}`, data).then(res => res.data)
export const deleteTopic = (id) => axios.delete(`${BASE}/${id}`)
export const getTopicsBySection = (sectionId) => axios.get(`${BASE}/by-section/${sectionId}`).then(res => res.data)