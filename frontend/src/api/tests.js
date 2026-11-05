import axios from 'axios'

const BASE = '/api/tests'

export const getTests = () => axios.get(BASE).then(res => res.data)
export const getTestById = (id) => axios.get(`${BASE}/${id}`).then(res => res.data)

export const createTest = (data, topicId) => {
    const url = topicId ? `${BASE}?topicId=${topicId}` : BASE
    return axios.post(url, data).then(res => res.data)
}

export const updateTest = (id, data) => axios.put(`${BASE}/${id}`, data).then(res => res.data)
export const deleteTest = (id) => axios.delete(`${BASE}/${id}`)