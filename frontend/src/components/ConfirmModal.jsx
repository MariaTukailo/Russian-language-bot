export default function ConfirmModal({ title, message, onConfirm, onCancel }) {
    return (
        <div className="modal-overlay" onClick={onCancel}>
            <div className="modal modal-sm" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>{title || 'Подтверждение'}</h2>
                    <button className="modal-close" onClick={onCancel}>×</button>
                </div>
                <div className="modal-body">
                    <p style={{ margin: 0, fontSize: '15px', lineHeight: '1.5' }}>{message}</p>
                </div>
                <div className="modal-footer">
                    <button className="btn btn-ghost" onClick={onCancel}>Отмена</button>
                    <button className="btn btn-danger" onClick={onConfirm}>Удалить</button>
                </div>
            </div>
        </div>
    )
}