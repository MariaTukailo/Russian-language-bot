import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getSectionById, deleteSection } from '../../api/sections'

function pluralize(count, one, few, many) {
    const mod10 = count % 10
    const mod100 = count % 100
    if (mod10 === 1 && mod100 !== 11) return one
    if (mod10 >= 2 && mod10 <= 4 && (mod100 < 10 || mod100 >= 20)) return few
    return many
}

export default function SectionDetail() {
    const { id } = useParams()
    const navigate = useNavigate()
    const [section, setSection] = useState(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        getSectionById(id)
            .then(data => { setSection(data); setLoading(false) })
            .catch(() => setLoading(false))
    }, [id])

    const handleDelete = () => {
        if (window.confirm('Удалить этот раздел?')) {
            deleteSection(id).then(() => navigate('/admin/sections'))
        }
    }

    if (loading) return <p>Загрузка...</p>
    if (!section) return null

    const topicsCount = section.topics?.length || 0

    return (
        <div className="section-detail-page">
            <button className="btn btn-ghost" onClick={() => navigate('/admin/sections')}>
                ← Назад к разделам
            </button>

            <header className="section-detail-header">
                <div className="header-title-row">
                    <h1>{section.title}</h1>
                    <button className="btn btn-danger btn-sm" onClick={handleDelete}>Удалить раздел</button>
                </div>
                <p className="section-detail-meta">
                    {topicsCount} {pluralize(topicsCount, 'тема', 'темы', 'тем')}
                    {section.passed ? ' • ✅ пройден' : ''}
                </p>
            </header>

            {section.description && (
                <section className="section-detail-description">
                    <h2>Описание</h2>
                    <p>{section.description}</p>
                </section>
            )}

            <section className="section-detail-resources">
                <h2>Материалы</h2>
                <div className="resource-row">
                    <span className="resource-label">🎬 Видео</span>
                    <span className="resource-value">{section.videoTitle || '— не выбрано —'}</span>
                </div>
                <div className="resource-row">
                    <span className="resource-label">📖 Конспект</span>
                    <span className="resource-value">{section.conspectTitle || '— не выбрано —'}</span>
                </div>
            </section>

            <section className="section-detail-topics">
                <h2>Темы</h2>
                {topicsCount === 0 && <p className="empty">В этом разделе пока нет тем.</p>}
                {section.topics?.map(topic => (
                    <div
                        key={topic.id}
                        className="topic-row"
                        onClick={() => navigate(`/admin/topics`)}
                        style={{ cursor: 'pointer' }}
                    >
                        <div className="topic-row-title">{topic.title}</div>
                        <div className="topic-row-meta">
                            {topic.testsCount || 0} {pluralize(topic.testsCount, 'тест', 'теста', 'тестов')}
                        </div>
                    </div>
                ))}
            </section>

            <div className="section-detail-actions">
                <button className="btn btn-primary" onClick={() => navigate('/admin/topics')}>
                    Перейти к темам
                </button>
            </div>
        </div>
    )
}