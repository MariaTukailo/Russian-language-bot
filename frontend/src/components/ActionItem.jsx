export default function ActionItem({ title, subtitle, date }) {
    return (
        <div className="action-item">
            <div className="action-item-icon" />
            <div className="action-item-body">
                <div className="action-item-title">{title}</div>
                <div className="action-item-subtitle">{subtitle}</div>
            </div>
            <div className="action-item-date">{date}</div>
        </div>
    )
}