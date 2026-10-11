import StatCard from '../../components/StatCard'
import ActionItem from '../../components/ActionItem'
import { adminStats, recentActions } from '../../mocks/stats'

export default function Dashboard() {
    return (
        <div className="dashboard">
            <header className="dashboard-header">
                <h1>Панель администратора</h1>
                <p className="dashboard-subtitle">
                    Управляйте контентом и следите за обучением
                </p>
            </header>

            <section className="stats-grid">
                <StatCard
                    value={adminStats.sectionsCount}
                    label="Раздела"
                    color="green"
                />
                <StatCard
                    value={adminStats.topicsCount}
                    label="Тем"
                    color="blue"
                />
                <StatCard
                    value={adminStats.questionsCount}
                    label="Вопросов"
                    color="red"
                />
                <StatCard
                    value={adminStats.usersCount}
                    label="Пользователей"
                    color="orange"
                />
            </section>

            <section className="recent-actions">
                <h2>Последние действия</h2>
                {recentActions.map(action => (
                    <ActionItem
                        key={action.id}
                        title={action.title}
                        subtitle={action.subtitle}
                        date={action.date}
                    />
                ))}
            </section>
        </div>
    )
}