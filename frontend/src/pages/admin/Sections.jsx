import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getSectionsList, deleteSection } from '../../api/sections'
import AddSectionModal from '../../components/AddSectionModal'
import ConfirmModal from '../../components/ConfirmModal'

const COLORS = ['green', 'red', 'orange', 'blue', 'purple']

function pluralize(count, one, few, many) {
    const mod10 = count % 10, mod100 = count % 100;
    if (mod10 === 1 && mod100 !== 11) return one;
    if (mod10 >= 2 && mod10 <= 4 && (mod100 < 10 || mod100 >= 20)) return few;
    return many;
}

export default function Sections() {
    const [sections, setSections] = useState([])
    const [loading, setLoading] = useState(true)
    const [modalOpen, setModalOpen] = useState(false)
    const [editingSection, setEditingSection] = useState(null)
    const [deleteId, setDeleteId] = useState(null)
    const navigate = useNavigate()

    const load = () => {
        setLoading(true)
        getSectionsList().then(data => {
            setSections(data)
            setLoading(false)
        }).catch(console.error)
    }

    useEffect(() => { load() }, [])

    const confirmDelete = () => {
        if (deleteId) {
            deleteSection(deleteId).then(() => {
                setDeleteId(null)
                load()
            })
        }
    }

    return (
        <div className="sections-page">
            <header className="page-header">
                <div>
                    <h1>Разделы</h1>
                    <p className="page-subtitle">Управление разделами курса</p>
                </div>
                <button className="btn btn-primary" onClick={() => { setEditingSection(null); setModalOpen(true) }}>
                    + Добавить раздел
                </button>
            </header>

            {loading && <p>Загрузка...</p>}

            <div className="sections-list">
                {sections.map((section, index) => {
                    const color = COLORS[index % COLORS.length]
                    const topicsLabel = pluralize(section.topicsCount, 'тема', 'темы', 'тем')
                    return (
                        <div key={section.id} className="section-row" onClick={() => navigate(`/admin/sections/${section.id}`)}>
                            <div className={`section-badge section-badge-${color}`}>{index + 1}</div>
                            <div className="section-info">
                                <div className="section-title">{section.title}</div>
                                <div className="section-meta">{section.topicsCount} {topicsLabel}</div>
                            </div>
                            <div className="action-buttons" onClick={e => e.stopPropagation()}>
                                <button className="btn-icon" onClick={() => { setEditingSection(section); setModalOpen(true) }}>✎</button>
                                <button className="btn-icon btn-danger" onClick={() => setDeleteId(section.id)}>✕</button>
                            </div>
                        </div>
                    )
                })}
            </div>

            {modalOpen && (
                /* Передаем section={editingSection} */
                <AddSectionModal
                    section={editingSection}
                    onClose={() => setModalOpen(false)}
                    onCreated={() => { setModalOpen(false); load(); }}
                />
            )}

            {deleteId && (
                <ConfirmModal
                    title="Удалить раздел?"
                    message="Вы уверены? Все темы и тесты, привязанные к этому разделу, будут удалены безвозвратно."
                    onConfirm={confirmDelete}
                    onCancel={() => setDeleteId(null)}
                />
            )}
        </div>
    )
}