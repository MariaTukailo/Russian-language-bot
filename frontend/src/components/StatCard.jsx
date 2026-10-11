export default function StatCard({ value, label, color = 'primary' }) {
    return (
        <div className={`stat-card stat-card-${color}`}>
            <div className="stat-card-value">{value}</div>
            <div className="stat-card-label">{label}</div>
        </div>
    )
}