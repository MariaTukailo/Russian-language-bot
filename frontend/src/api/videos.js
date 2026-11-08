import axios from 'axios'

const BASE = '/api/videos'

export const getVideos = () => axios.get(BASE).then(res => res.data)
export const getVideoById = (id) => axios.get(`${BASE}/${id}`).then(res => res.data)
export const createVideo = (data) => axios.post(BASE, data).then(res => res.data)
export const updateVideo = (id, data) => axios.put(`${BASE}/${id}`, data).then(res => res.data)
export const deleteVideo = (id) => axios.delete(`${BASE}/${id}`)