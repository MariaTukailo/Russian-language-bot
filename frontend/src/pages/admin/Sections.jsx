import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getSectionsList } from '../../api/sections'
import AddSectionModal from '../../components/AddSectionModal'

const COLORS = ['green', 'red', 'orange', 'blue', 'purple']

function pluralize(count, one, few, many) {
    const mod10 = count % 10
    const mod100 = count % 100
    if (mod10 === 1 && mod100 !== 11) return one
    if (mod10 >= 2 && mod10 <= 4 && (mod100 < 10 || mod100 >= 20)) return few
    return many
}

export default function Sections() {
    const [sections, setSections] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(null)
    const [modalOpen, setModalOpen] = useState(false)
    const navigate = useNavigate()

    const load = () => {
        setLoading(true)
        getSectionsList()
            .then(data => {
                setSections(data)
                setLoading(false)
            })
            .catch(err => {
                console.error(err)
                setError('Не удалось загрузить разделы')
                setLoading(false)
            })
    }

    useEffect(() => {
        load()
    }, [])

    const handleCreated = () => {
        setModalOpen(false)
        load()
    }

    return (
        <div className="sections-page">
            <header className="page-header">
                <div>
                    <h1>Разделы</h1>
                    <p className="page-subtitle">Управление разделами курса</p>
                </div>
                <button className="btn btn-primary" onClick={() => setModalOpen(true)}>
                    + Добавить раздел
                </button>
            </header>

            {loading && <p>Загрузка...</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {!loading && sections.length === 0 && (
                <p className="empty">Разделов пока нет. Добавьте первый.</p>
            )}

            <div className="sections-list">
                {sections.map((section, index) => {
                    const color = COLORS[index % COLORS.length]
                    const topicsLabel = pluralize(
                        section.topicsCount,
                        'тема', 'темы', 'тем'
                    )
                    return (
                        <div
                            key={section.id}
                            className="section-row"
                            onClick={() => navigate(`/admin/sections/${section.id}`)}
                        >
                            <div className={`section-badge section-badge-${color}`}>
                                {index + 1}
                            </div>
                            <div className="section-info">
                                <div className="section-title">{section.title}</div>
                                <div className="section-meta">
                                    {section.topicsCount} {topicsLabel}
                                </div>
                            </div>
                            <div className="section-arrow">›</div>
                        </div>
                    )
                })}
            </div>

            {modalOpen && (
                <AddSectionModal
                    onClose={() => setModalOpen(false)}
                    onCreated={handleCreated}
                />
            )}
        </div>
    )
}