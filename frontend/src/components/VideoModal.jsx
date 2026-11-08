import { useState } from 'react'
import { createVideo, updateVideo } from '../api/videos'

export default function VideoModal({ video, onClose, onSaved }) {
    const [title, setTitle] = useState(video?.title || '')
    const [description, setDescription] = useState(video?.description || '')
    const [url, setUrl] = useState(video?.url || '')
    const [saving, setSaving] = useState(false)
    const [error, setError] = useState(null)

    const handleSubmit = () => {
        if (!title.trim() || !url.trim()) {
            setError('Заполните название и ссылку')
            return
        }
        setSaving(true)
        const payload = { title: title.trim(), description: description.trim() || null, url: url.trim() }
        const promise = video ? updateVideo(video.id, payload) : createVideo(payload)

        promise
            .then(() => { setSaving(false); onSaved() })
            .catch(err => { console.error(err); setError('Ошибка сохранения'); setSaving(false) })
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>{video ? 'Редактировать видео' : 'Добавить видео'}</h2>
                    <button className="modal-close" onClick={onClose}>×</button>
                </div>
                <div className="modal-body">
                    <label className="form-label">Ссылка на YouTube (или VK)</label>
                    <input className="form-input" value={url} onChange={e => setUrl(e.target.value)} placeholder="https://youtu.be/..." />
                    <label className="form-label">Название видео</label>
                    <input className="form-input" value={title} onChange={e => setTitle(e.target.value)} placeholder="Например: Введение в фонетику" />
                    <label className="form-label">Описание (необязательно)</label>
                    <textarea className="form-input form-textarea" value={description} onChange={e => setDescription(e.target.value)} />
                    {error && <p style={{ color: 'red', marginTop: 8 }}>{error}</p>}
                </div>
                <div className="modal-footer">
                    <button className="btn btn-ghost" onClick={onClose}>Отмена</button>
                    <button className="btn btn-primary" onClick={handleSubmit} disabled={saving}>
                        {saving ? 'Сохранение...' : 'Сохранить'}
                    </button>
                </div>
            </div>
        </div>
    )
}