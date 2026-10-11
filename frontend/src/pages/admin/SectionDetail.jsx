import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getSectionById } from '../../api/sections'

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
    const [error, setError] = useState(null)

    useEffect(() => {
        setLoading(true)
        getSectionById(id)
            .then(data => {
                setSection(data)
                setLoading(false)
            })
            .catch(err => {
                console.error(err)
                setError('Раздел не найден')
                setLoading(false)
            })
    }, [id])

    if (loading) return <p>Загрузка...</p>
    if (error) return <p style={{ color: 'red' }}>{error}</p>
    if (!section) return null

    const topicsCount = section.topics?.length || 0

    return (
        <div className="section-detail-page">
            <button
                className="btn btn-ghost"
                onClick={() => navigate('/admin/sections')}
            >
                ← Назад к разделам
            </button>

            <header className="section-detail-header">
                <h1>{section.title}</h1>
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
                    <span className="resource-value">
            {section.videoTitle || '— не выбрано —'}
          </span>
                </div>
                <div className="resource-row">
                    <span className="resource-label">📖 Конспект</span>
                    <span className="resource-value">
            {section.conspectTitle || '— не выбрано —'}
          </span>
                </div>
            </section>

            <section className="section-detail-topics">
                <h2>Темы</h2>
                {topicsCount === 0 && (
                    <p className="empty">В этом разделе пока нет тем.</p>
                )}
                {section.topics.map(topic => {
                    const testsCount = topic.testsCount || 0
                    return (
                        <div key={topic.id} className="topic-row">
                            <div className="topic-row-title">{topic.title}</div>
                            <div className="topic-row-meta">
                                {testsCount} {pluralize(testsCount, 'тест', 'теста', 'тестов')}
                            </div>
                        </div>
                    )
                })}
            </section>

            <div className="section-detail-actions">
                <button className="btn btn-primary">+ Добавить тему</button>
                <button className="btn btn-ghost">Редактировать раздел</button>
            </div>
        </div>
    )
}