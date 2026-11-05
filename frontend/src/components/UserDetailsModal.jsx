import { useEffect, useState } from 'react'
import axios from 'axios'

export default function UserDetailsModal({ user, onClose }) {
    const [achievements, setAchievements] = useState([])
    const [history, setHistory] = useState([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        Promise.all([
            axios.get(`/api/achievements/user/${user.id}`).then(res => res.data).catch(() => []),
            axios.get(`/api/history/user/${user.id}`).then(res => res.data).catch(() => [])
        ]).then(([achievData, histData]) => {
            setAchievements(achievData)
            setHistory(histData)
            setLoading(false)
        })
    }, [user.id])

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal modal-large" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Профиль: {user.username}</h2>
                    <button className="modal-close" onClick={onClose}>×</button>
                </div>
                <div className="modal-body">
                    <div className="user-stats-row">
                        <div className="user-stat-box">
                            <span>ID в Telegram</span>
                            <strong>{user.telegramId}</strong>
                        </div>
                        <div className="user-stat-box">
                            <span>Монеты</span>
                            <strong>🪙 {user.coins}</strong>
                        </div>
                        <div className="user-stat-box">
                            <span>В системе с</span>
                            <strong>{new Date(user.createdAt).toLocaleDateString('ru-RU')}</strong>
                        </div>
                    </div>

                    {loading ? <p>Загрузка данных...</p> : (
                        <>
                            <hr className="divider" />
                            <h3>Достижения ({achievements.length})</h3>
                            <div className="user-achievements">
                                {achievements.length === 0 && <p className="empty">Пока нет достижений</p>}
                                {achievements.map(a => (
                                    <span key={a.id} className="badge badge-outline" style={{ padding: '6px 12px' }}>
                                        🏆 {a.title}
                                    </span>
                                ))}
                            </div>

                            <hr className="divider" />
                            <h3>Пройденные тесты ({history.length})</h3>
                            <div className="user-history-list">
                                {history.length === 0 && <p className="empty">Тесты еще не проходил</p>}
                                {history.map(h => (
                                    <div key={h.id} className="history-item">
                                        <div>
                                            <div className="history-title">{h.testTitle}</div>
                                            <div className="history-date">
                                                {new Date(h.completedAt).toLocaleString('ru-RU')}
                                            </div>
                                        </div>
                                        <div className="history-score">
                                            {h.correctAnswers} / {h.totalQuestions}
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </>
                    )}
                </div>
                <div className="modal-footer">
                    <button className="btn btn-ghost" onClick={onClose}>Закрыть</button>
                </div>
            </div>
        </div>
    )
}