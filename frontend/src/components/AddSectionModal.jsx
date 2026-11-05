import { useEffect, useState } from 'react'
import { createSection, updateSection } from '../api/sections'
import { getVideos } from '../api/videos'
import { getConspects } from '../api/conspects'

const COLORS = ['green', 'red', 'orange', 'blue', 'purple']

export default function AddSectionModal({ section, onClose, onCreated }) {
    const [title, setTitle] = useState(section?.title || '')
    const [description, setDescription] = useState(section?.description || '')
    const [videoId, setVideoId] = useState(section?.videoId || '')
    const [conspectDocumentId, setConspectDocumentId] = useState(section?.conspectDocumentId || '')
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
        const payload = {
            title: title.trim(),
            description: description.trim() || null,
            videoId: videoId ? Number(videoId) : null,
            conspectDocumentId: conspectDocumentId ? Number(conspectDocumentId) : null,
        }

        const promise = section
            ? updateSection(section.id, payload)
            : createSection(payload)

        promise
            .then(() => {
                setSaving(false)
                onCreated()
            })
            .catch(err => {
                console.error(err)
                setError('Не удалось сохранить раздел')
                setSaving(false)
            })
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    {/* Динамический заголовок */}
                    <h2>{section ? 'Редактировать раздел' : 'Добавить раздел'}</h2>
                    <button className="modal-close" onClick={onClose}>×</button>
                </div>

                <div className="modal-body">
                    <label className="form-label">Название раздела</label>
                    <input className="form-input" value={title} onChange={e => setTitle(e.target.value)} placeholder="Например: Фонетика" />

                    <label className="form-label">Описание (необязательно)</label>
                    <textarea className="form-input form-textarea" value={description} onChange={e => setDescription(e.target.value)} placeholder="Краткое описание раздела" />

                    <label className="form-label">Видео</label>
                    <select className="form-input" value={videoId} onChange={e => setVideoId(e.target.value)}>
                        <option value="">— не выбрано —</option>
                        {videos.map(v => <option key={v.id} value={v.id}>{v.title}</option>)}
                    </select>

                    <label className="form-label">Конспект</label>
                    <select className="form-input" value={conspectDocumentId} onChange={e => setConspectDocumentId(e.target.value)}>
                        <option value="">— не выбрано —</option>
                        {conspects.map(c => <option key={c.id} value={c.id}>{c.title}</option>)}
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
                        {/* Кнопка + удалена */}
                    </div>

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