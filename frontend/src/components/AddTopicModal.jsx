import { useEffect, useState } from 'react'
import { createTopic, updateTopic } from '../api/topics'
import { getSectionsList } from '../api/sections'

export default function AddTopicModal({ topic, onClose, onCreated }) {
    const [sections, setSections] = useState([])
    const [sectionId, setSectionId] = useState(topic?.sectionId || '')
    const [title, setTitle] = useState(topic?.title || '')
    const [description, setDescription] = useState(topic?.description || '')
    const [saving, setSaving] = useState(false)
    const [error, setError] = useState(null)

    useEffect(() => {
        getSectionsList().then(setSections).catch(console.error)
    }, [])

    const handleSubmit = () => {
        if (!sectionId) { setError('Выберите раздел'); return }
        if (!title.trim()) { setError('Введите название'); return }

        setSaving(true)
        const payload = { title: title.trim(), description: description.trim() || null, tests: [] }
        const promise = topic ? updateTopic(topic.id, payload) : createTopic(payload, Number(sectionId))

        promise
            .then(() => { setSaving(false); onCreated() })
            .catch(err => { console.error(err); setError('Ошибка сохранения'); setSaving(false) })
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>{topic ? 'Редактировать тему' : 'Добавить тему'}</h2>
                    <button className="modal-close" onClick={onClose}>×</button>
                </div>
                <div className="modal-body">
                    <label className="form-label">Раздел</label>
                    <select className="form-input" value={sectionId} onChange={e => setSectionId(e.target.value)} disabled={!!topic}>
                        <option value="">— Выберите раздел —</option>
                        {sections.map(s => <option key={s.id} value={s.id}>{s.title}</option>)}
                    </select>
                    <label className="form-label">Название темы</label>
                    <input className="form-input" value={title} onChange={e => setTitle(e.target.value)} />
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