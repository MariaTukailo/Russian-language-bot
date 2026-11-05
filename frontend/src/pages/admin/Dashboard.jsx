import { useEffect, useState } from 'react'
import StatCard from '../../components/StatCard'
import { getDashboardStats } from '../../api/stats'

export default function Dashboard() {
    const [stats, setStats] = useState({
        sectionsCount: 0,
        topicsCount: 0,
        questionsCount: 0,
        usersCount: 0
    })
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        getDashboardStats()
            .then(data => {
                setStats(data)
                setLoading(false)
            })
            .catch(err => {
                console.error(err)
                setLoading(false)
            })
    }, [])

    return (
        <div className="dashboard">
            <header className="dashboard-header">
                <h1>Панель администратора</h1>
                <p className="dashboard-subtitle">
                    Актуальная статистика вашего курса
                </p>
            </header>

            {loading ? (
                <p>Загрузка данных...</p>
            ) : (
                <section className="stats-grid">
                    <StatCard
                        value={stats.sectionsCount}
                        label="Разделов"
                        color="green"
                    />
                    <StatCard
                        value={stats.topicsCount}
                        label="Тем"
                        color="blue"
                    />
                    <StatCard
                        value={stats.questionsCount}
                        label="Вопросов"
                        color="red"
                    />
                    <StatCard
                        value={stats.usersCount}
                        label="Пользователей"
                        color="orange"
                    />
                </section>
            )}
        </div>
    )
}