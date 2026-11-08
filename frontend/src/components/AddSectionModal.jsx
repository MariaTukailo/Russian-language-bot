import { useState } from 'react'
import { createSection, updateSection } from '../api/sections'

export default function AddSectionModal({ section, onClose, onCreated }) {
    const [title, setTitle] = useState(section?.title || '')
    const [description, setDescription] = useState(section?.description || '')
    const [saving, setSaving] = useState(false)
    const [error, setError] = useState(null)

    const handleSubmit = () => {
        if (!title.trim()) {
            setError('Введите название раздела')
            return
        }
        setSaving(true)
        const payload = { title: title.trim(), description: description.trim() || null }
        const promise = section ? updateSection(section.id, payload) : createSection(payload)

        promise
            .then(() => { setSaving(false); onCreated() })
            .catch(err => { console.error(err); setError('Не удалось сохранить'); setSaving(false) })
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>{section ? 'Редактировать раздел' : 'Добавить раздел'}</h2>
                    <button className="modal-close" onClick={onClose}>×</button>
                </div>
                <div className="modal-body">
                    <label className="form-label">Название раздела</label>
                    <input className="form-input" value={title} onChange={e => setTitle(e.target.value)} placeholder="Например: Фонетика" />

                    <label className="form-label">Описание</label>
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