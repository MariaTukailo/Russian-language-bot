import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getTopicById } from '../../api/topics'
import TestModal from '../../components/TestModal'

const getYouTubeId = (url) => {
    if (!url) return null;
    const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
    const match = url.match(regExp);
    return (match && match[2].length === 11) ? match[2] : null;
}

export default function TopicDetail() {
    const { id } = useParams()
    const navigate = useNavigate()
    const [topic, setTopic] = useState(null)
    const [loading, setLoading] = useState(true)
    const [isTestModalOpen, setIsTestModalOpen] = useState(false)

    const loadTopic = () => {
        getTopicById(id)
            .then(data => { setTopic(data); setLoading(false) })
            .catch(() => setLoading(false))
    }

    useEffect(() => {
        loadTopic()
    }, [id])

    if (loading) return <p>Загрузка...</p>
    if (!topic) return <p>Тема не найдена</p>

    const ytId = getYouTubeId(topic.videoUrl)

    return (
        <div className="section-detail-page">
            <button className="btn btn-ghost" onClick={() => navigate('/admin/topics')}>
                ← Назад к темам
            </button>

            <header className="section-detail-header">
                <h1>{topic.title}</h1>
                {topic.description && <p className="section-detail-meta">{topic.description}</p>}
            </header>

            <section className="section-detail-resources">
                <h2>Материалы</h2>

                <div className="resource-row" style={{ alignItems: 'flex-start', paddingTop: '16px', paddingBottom: '16px' }}>
                    <span className="resource-label">🎬 Видеоурок:</span>
                    <div style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'flex-end' }}>
                        {topic.videoUrl ? (
                            <a href={topic.videoUrl} target="_blank" rel="noreferrer" className="video-link-wrapper">
                                {ytId && <img src={`https://img.youtube.com/vi/${ytId}/mqdefault.jpg`} alt="Превью" className="video-thumb" />}
                                <span style={{ marginTop: 8 }}>{topic.videoTitle} ↗</span>
                            </a>
                        ) : (
                            <span className="resource-value">— не выбрано —</span>
                        )}
                    </div>
                </div>

                {topic.conspectUrls && topic.conspectUrls.length > 0 && (
                    <div style={{ marginTop: 16 }}>
                        <span className="resource-label">📖 Конспект:</span>
                        <div className="mock-pdf-container" style={{ marginTop: 8 }}>
                            {topic.conspectUrls.map((url, idx) => (
                                <div key={idx} className="mock-pdf-page">
                                    <div className="page-number">Стр. {idx + 1}</div>
                                    <img src={url} alt={`Страница ${idx + 1}`} style={{ width: '100%', height: '100%', objectFit: 'contain' }} />
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </section>

            <section className="section-detail-topics" style={{ marginTop: 24 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <h2>Тесты по этой теме</h2>
                    <button className="btn btn-primary btn-sm" onClick={() => setIsTestModalOpen(true)}>
                        + Создать тест
                    </button>
                </div>

                {(!topic.tests || topic.tests.length === 0) && <p className="empty">Тестов пока нет.</p>}

                {topic.tests?.map(test => (
                    <div key={test.id} className="topic-row" style={{ cursor: 'pointer' }}>
                        <div className="topic-row-title">{test.title}</div>
                        <div className="topic-row-meta">
                            {test.questions?.length || 0} вопросов
                        </div>
                    </div>
                ))}
            </section>

            {isTestModalOpen && (
                <TestModal
                    test={null}
                    topics={[topic]}
                    onClose={() => setIsTestModalOpen(false)}
                    onSaved={() => {
                        setIsTestModalOpen(false);
                        loadTopic();
                    }}
                />
            )}
        </div>
    )
}