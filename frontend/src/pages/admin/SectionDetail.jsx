import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getSectionById, deleteSection } from '../../api/sections'
import ConfirmModal from '../../components/ConfirmModal'

function pluralize(count, one, few, many) {
    const mod10 = count % 10, mod100 = count % 100;
    if (mod10 === 1 && mod100 !== 11) return one;
    if (mod10 >= 2 && mod10 <= 4 && (mod100 < 10 || mod100 >= 20)) return few;
    return many;
}

export default function SectionDetail() {
    const { id } = useParams()
    const navigate = useNavigate()
    const [section, setSection] = useState(null)
    const [loading, setLoading] = useState(true)
    const [deleteOpen, setDeleteOpen] = useState(false)

    useEffect(() => {
        getSectionById(id)
            .then(data => { setSection(data); setLoading(false) })
            .catch(() => setLoading(false))
    }, [id])

    const confirmDelete = () => {
        deleteSection(id).then(() => navigate('/admin/sections'))
    }

    if (loading) return <p>Загрузка...</p>
    if (!section) return <p>Раздел не найден</p>

    const topicsCount = section.topics?.length || 0

    return (
        <div className="section-detail-page">
            <button className="btn btn-ghost" onClick={() => navigate('/admin/sections')}>
                ← Назад к разделам
            </button>

            <header className="section-detail-header">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <h1>{section.title}</h1>
                    <button className="btn btn-ghost btn-danger" onClick={() => setDeleteOpen(true)}>Удалить</button>
                </div>
                <p className="section-detail-meta">
                    {topicsCount} {pluralize(topicsCount, 'тема', 'темы', 'тем')}
                </p>
            </header>

            {section.description && (
                <section className="section-detail-description">
                    <h2>Описание</h2>
                    <p>{section.description}</p>
                </section>
            )}

            <section className="section-detail-topics" style={{ marginTop: 24 }}>
                <h2>Темы</h2>
                {topicsCount === 0 && <p className="empty">В этом разделе пока нет тем.</p>}
                {section.topics?.map(topic => (
                    <div key={topic.id} className="topic-row" onClick={() => navigate(`/admin/topics/${topic.id}`)} style={{ cursor: 'pointer' }}>
                        <div className="topic-row-title">{topic.title}</div>
                        <div className="topic-row-meta">
                            {topic.testsCount || 0} {pluralize(topic.testsCount, 'тест', 'теста', 'тестов')}
                        </div>
                    </div>
                ))}
            </section>

            <div className="section-detail-actions" style={{ marginTop: 24 }}>
                <button className="btn btn-primary" onClick={() => navigate('/admin/topics')}>Перейти к темам</button>
            </div>

            {deleteOpen && (
                <ConfirmModal
                    title="Удалить раздел?"
                    message="Все темы и тесты внутри будут удалены."
                    onConfirm={confirmDelete}
                    onCancel={() => setDeleteOpen(false)}
                />
            )}
        </div>
    )
}