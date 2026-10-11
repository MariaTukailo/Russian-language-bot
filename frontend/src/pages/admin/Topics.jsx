import { useEffect, useState } from 'react'
import { getTopicsList } from '../../api/topics'
import { getSectionsList } from '../../api/sections'

function pluralize(count, one, few, many) {
    const mod10 = count % 10
    const mod100 = count % 100
    if (mod10 === 1 && mod100 !== 11) return one
    if (mod10 >= 2 && mod10 <= 4 && (mod100 < 10 || mod100 >= 20)) return few
    return many
}

export default function Topics() {
    const [topics, setTopics] = useState([])
    const [sections, setSections] = useState([])
    const [filterSectionId, setFilterSectionId] = useState('')
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        Promise.all([
            getTopicsList(),
            getSectionsList(),
        ]).then(([topicsData, sectionsData]) => {
            setTopics(topicsData)
            setSections(sectionsData)
            setLoading(false)
        })
    }, [])

    const filtered = filterSectionId
        ? topics.filter(t => t.sectionId === Number(filterSectionId))
        : topics

    return (
        <div className="sections-page">
            <header className="page-header">
                <div>
                    <h1>Темы</h1>
                    <p className="page-subtitle">Все темы курса</p>
                </div>
                <button className="btn btn-primary">+ Добавить тему</button>
            </header>

            <div className="form-label">Фильтр по разделу</div>
            <select
                className="form-input"
                value={filterSectionId}
                onChange={e => setFilterSectionId(e.target.value)}
            >
                <option value="">Все разделы</option>
                {sections.map(s => (
                    <option key={s.id} value={s.id}>{s.title}</option>
                ))}
            </select>

            {loading && <p>Загрузка...</p>}

            <div className="sections-list" style={{ marginTop: 16 }}>
                {filtered.map(topic => {
                    const testsLabel = pluralize(
                        topic.testsCount, 'тест', 'теста', 'тестов'
                    )
                    return (
                        <div key={topic.id} className="section-row">
                            <div className="section-info">
                                <div className="section-title">{topic.title}</div>
                                <div className="section-meta">
                                    {topic.testsCount} {testsLabel}
                                    {topic.sectionTitle ? ` • ${topic.sectionTitle}` : ''}
                                </div>
                            </div>
                            <div className="section-arrow">›</div>
                        </div>
                    )
                })}
            </div>
        </div>
    )
}