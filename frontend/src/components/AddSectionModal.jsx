import { useEffect, useState } from 'react'
import { createSection } from '../api/sections'
import { getVideos } from '../api/videos'
import { getConspects } from '../api/conspects'

const COLORS = ['green', 'red', 'orange', 'blue', 'purple']

export default function AddSectionModal({ onClose, onCreated }) {
    const [title, setTitle] = useState('')
    const [description, setDescription] = useState('')
    const [videoId, setVideoId] = useState('')
    const [conspectDocumentId, setConspectDocumentId] = useState('')
    const [color, setColor] = useState('green')
    const [videos, setVideos] = useState([])
    const [conspects, setConspects] = useState([])
    const [saving, setSaving] = useState(false)
    const [error, setError] = useState(null)

    useEffect(() => {
        getVideos().then(setVideos).catch(() => {})
        getConspects().then(setConspects).catch(() => {})
    }, [])

    const handleSubmit = () => {
        if (!title.trim()) {
            setError('Введите название раздела')
            return
        }

        setSaving(true)
        createSection({
            title: title.trim(),
            description: description.trim() || null,
            videoId: videoId ? Number(videoId) : null,
            conspectDocumentId: conspectDocumentId ? Number(conspectDocumentId) : null,
        })
            .then(() => {
                setSaving(false)
                onCreated()
            })
            .catch(err => {
                console.error(err)
                setError('Не удалось создать раздел')
                setSaving(false)
            })
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Добавить раздел</h2>
                    <button className="modal-close" onClick={onClose}>×</button>
                </div>

                <div className="modal-body">
                    <label className="form-label">Название раздела</label>
                    <input
                        className="form-input"
                        placeholder="Например: Фонетика"
                        value={title}
                        onChange={e => setTitle(e.target.value)}
                    />

                    <label className="form-label">Описание (необязательно)</label>
                    <textarea
                        className="form-input form-textarea"
                        placeholder="Краткое описание раздела"
                        value={description}
                        onChange={e => setDescription(e.target.value)}
                    />

                    <label className="form-label">Видео</label>
                    <select
                        className="form-input"
                        value={videoId}
                        onChange={e => setVideoId(e.target.value)}
                    >
                        <option value="">— не выбрано —</option>
                        {videos.map(v => (
                            <option key={v.id} value={v.id}>{v.title}</option>
                        ))}
                    </select>

                    <label className="form-label">Конспект</label>
                    <select
                        className="form-input"
                        value={conspectDocumentId}
                        onChange={e => setConspectDocumentId(e.target.value)}
                    >
                        <option value="">— не выбрано —</option>
                        {conspects.map(c => (
                            <option key={c.id} value={c.id}>{c.title}</option>
                        ))}
                    </select>

                    <label className="form-label">Иконка / цвет</label>
                    <div className="color-picker">
                        {COLORS.map(c => (
                            <button
                                key={c}
                                type="button"
                                className={`color-dot color-dot-${c} ${color === c ? 'active' : ''}`}
                                onClick={() => setColor(c)}
                            />
                        ))}
                        <button type="button" className="color-dot color-dot-empty">+</button>
                    </div>

                    {error && <p style={{ color: 'red', marginTop: 8 }}>{error}</p>}
                </div>

                <div className="modal-footer">
                    <button className="btn btn-ghost" onClick={onClose}>Отмена</button>
                    <button
                        className="btn btn-primary"
                        onClick={handleSubmit}
                        disabled={saving}
                    >
                        {saving ? 'Сохранение...' : 'Сохранить'}
                    </button>
                </div>
            </div>
        </div>
    )
}