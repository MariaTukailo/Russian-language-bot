import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getTopicsList, deleteTopic } from '../../api/topics'
import { getSectionsList } from '../../api/sections'
import AddTopicModal from '../../components/AddTopicModal'
import ConfirmModal from '../../components/ConfirmModal'

function pluralize(count, one, few, many) {
    const mod10 = count % 10, mod100 = count % 100;
    if (mod10 === 1 && mod100 !== 11) return one;
    if (mod10 >= 2 && mod10 <= 4 && (mod100 < 10 || mod100 >= 20)) return few;
    return many;
}

export default function Topics() {
    const [topics, setTopics] = useState([])
    const [sections, setSections] = useState([])
    const [filterSectionId, setFilterSectionId] = useState('')
    const [loading, setLoading] = useState(true)
    const [modalOpen, setModalOpen] = useState(false)
    const [editingTopic, setEditingTopic] = useState(null)
    const [deleteId, setDeleteId] = useState(null)
    const navigate = useNavigate()

    const loadData = () => {
        setLoading(true)
        Promise.all([getTopicsList(), getSectionsList()])
            .then(([topicsData, sectionsData]) => {
                setTopics(topicsData)
                setSections(sectionsData)
                setLoading(false)
            })
            .catch(console.error)
    }

    useEffect(() => { loadData() }, [])

    const confirmDelete = () => {
        if (deleteId) {
            deleteTopic(deleteId).then(() => { setDeleteId(null); loadData(); })
        }
    }

    const filtered = filterSectionId
        ? topics.filter(t => Number(t.sectionId) === Number(filterSectionId))
        : topics

    return (
        <div className="sections-page">
            <header className="page-header">
                <div>
                    <h1>Темы</h1>
                    <p className="page-subtitle">Управление темами курса</p>
                </div>
                <button className="btn btn-primary" onClick={() => { setEditingTopic(null); setModalOpen(true) }}>
                    + Добавить тему
                </button>
            </header>

            <div className="filter-bar">
                <select className="form-input" value={filterSectionId} onChange={e => setFilterSectionId(e.target.value)}>
                    <option value="">Все разделы</option>
                    {sections.map(s => <option key={s.id} value={s.id}>{s.title}</option>)}
                </select>
            </div>

            {loading && <p>Загрузка...</p>}
            {!loading && filtered.length === 0 && <p className="empty">В этом разделе пока нет тем.</p>}

            <div className="sections-list">
                {filtered.map(topic => (
                    <div
                        key={topic.id}
                        className="section-row"
                        onClick={() => navigate(`/admin/topics/${topic.id}`)}
                        style={{ cursor: 'pointer' }}
                    >
                        <div className="section-info">
                            <div className="section-title">{topic.title}</div>
                            <div className="section-meta">
                                {topic.testsCount} {pluralize(topic.testsCount, 'тест', 'теста', 'тестов')}
                                {topic.sectionTitle && <span className="badge">{topic.sectionTitle}</span>}
                            </div>
                        </div>

                        <div className="action-buttons" onClick={e => e.stopPropagation()}>
                            <button className="btn-icon" onClick={() => { setEditingTopic(topic); setModalOpen(true) }} title="Редактировать">✎</button>
                            <button className="btn-icon btn-danger" onClick={() => setDeleteId(topic.id)} title="Удалить">✕</button>
                        </div>
                    </div>
                ))}
            </div>

            {modalOpen && (
                <AddTopicModal topic={editingTopic} onClose={() => setModalOpen(false)} onCreated={() => { setModalOpen(false); loadData(); }} />
            )}
            {deleteId && (
                <ConfirmModal
                    title="Удалить тему?"
                    message="Все тесты и вопросы внутри этой темы будут безвозвратно удалены."
                    onConfirm={confirmDelete}
                    onCancel={() => setDeleteId(null)}
                />
            )}
        </div>
    )
}