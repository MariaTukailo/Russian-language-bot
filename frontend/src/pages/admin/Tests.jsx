import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { getTests, deleteTest } from '../../api/tests'
import { getTopicsList } from '../../api/topics'
import TestModal from '../../components/TestModal'
import ConfirmModal from '../../components/ConfirmModal'

export default function Tests() {
    const [tests, setTests] = useState([])
    const [topics, setTopics] = useState([])
    const [loading, setLoading] = useState(true)
    const [modalOpen, setModalOpen] = useState(false)
    const [editingTest, setEditingTest] = useState(null)
    const [deleteId, setDeleteId] = useState(null)
    const [searchParams] = useSearchParams()
    const filterTopicId = searchParams.get('topicId')

    const loadData = () => {
        setLoading(true)
        Promise.all([getTests(), getTopicsList()])
            .then(([testsData, topicsData]) => {
                setTests(testsData)
                setTopics(topicsData)
                setLoading(false)
            })
            .catch(console.error)
    }

    useEffect(() => { loadData() }, [])

    const confirmDelete = () => {
        if (deleteId) {
            deleteTest(deleteId).then(() => { setDeleteId(null); loadData(); })
        }
    }

    const filteredTests = filterTopicId
        ? tests.filter(t => Number(t.topicId) === Number(filterTopicId))
        : tests

    const groupedByTopic = filteredTests.reduce((acc, test) => {
        const topic = topics.find(t => Number(t.id) === Number(test.topicId))
        const topicTitle = topic ? topic.title : (test.topicTitle || 'Без темы')
        if (!acc[topicTitle]) acc[topicTitle] = { items: [], sectionName: topic?.sectionTitle }
        acc[topicTitle].items.push(test)
        return acc
    }, {})

    return (
        <div className="sections-page">
            <header className="page-header">
                <div>
                    <h1>Тесты</h1>
                    <p className="page-subtitle">Управление тестами</p>
                </div>
                <button className="btn btn-primary" onClick={() => { setEditingTest(null); setModalOpen(true) }}>
                    + Создать тест
                </button>
            </header>

            {loading && <p>Загрузка...</p>}
            {!loading && Object.keys(groupedByTopic).length === 0 && <p className="empty">Тестов пока нет.</p>}

            {Object.entries(groupedByTopic).map(([topicTitle, group]) => (
                <div key={topicTitle} className="test-group">
                    <div className="test-group-header">
                        <h2 className="test-group-title">{topicTitle}</h2>
                        {group.sectionName && <span className="badge badge-outline">{group.sectionName}</span>}
                    </div>
                    <div className="test-blocks">
                        {group.items.map(test => (
                            <div key={test.id} className="test-card">
                                <div className="test-card-header">
                                    <h3>{test.title}</h3>
                                    <div className="action-buttons">
                                        <button className="btn-icon" onClick={() => { setEditingTest(test); setModalOpen(true) }}>✎</button>
                                        <button className="btn-icon btn-danger" onClick={() => setDeleteId(test.id)}>✕</button>
                                    </div>
                                </div>
                                <p className="test-card-meta">{test.questions?.length || 0} вопросов</p>
                            </div>
                        ))}
                    </div>
                </div>
            ))}

            {modalOpen && (
                <TestModal test={editingTest} topics={topics} onClose={() => setModalOpen(false)} onSaved={() => { setModalOpen(false); loadData(); }} />
            )}
            {deleteId && (
                <ConfirmModal
                    title="Удалить тест?"
                    message="Все вопросы теста будут утеряны."
                    onConfirm={confirmDelete}
                    onCancel={() => setDeleteId(null)}
                />
            )}
        </div>
    )
}