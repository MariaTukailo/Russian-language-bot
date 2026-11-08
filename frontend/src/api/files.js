import axios from 'axios'

export const uploadImage = (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return axios.post('/api/files/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    }).then(res => res.data.url)
}