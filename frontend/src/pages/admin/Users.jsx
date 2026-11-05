import { useEffect, useState } from 'react'
import { getUsers } from '../../api/users'
import UserDetailsModal from '../../components/UserDetailsModal'

export default function Users() {
    const [users, setUsers] = useState([])
    const [loading, setLoading] = useState(true)
    const [search, setSearch] = useState('')
    const [selectedUser, setSelectedUser] = useState(null)

    useEffect(() => {
        getUsers()
            .then(data => {
                setUsers(data)
                setLoading(false)
            })
            .catch(console.error)
    }, [])

    const filteredUsers = users.filter(u =>
        u.username.toLowerCase().includes(search.toLowerCase()) ||
        String(u.telegramId).includes(search)
    )

    return (
        <div className="sections-page">
            <header className="page-header">
                <div>
                    <h1>Ученики</h1>
                    <p className="page-subtitle">Все пользователи, запустившие бота</p>
                </div>
            </header>

            <div className="filter-bar">
                <input
                    type="text"
                    className="form-input"
                    placeholder="Поиск по имени или Telegram ID..."
                    value={search}
                    onChange={e => setSearch(e.target.value)}
                />
            </div>

            {loading && <p>Загрузка...</p>}

            {!loading && (
                <div className="users-table-container">
                    <table className="users-table">
                        <thead>
                        <tr>
                            <th>Ученик</th>
                            <th>Telegram ID</th>
                            <th>Монеты</th>
                            <th>Дата входа</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        {filteredUsers.map(user => (
                            <tr key={user.id}>
                                <td>
                                    <div className="user-cell">
                                        <div className="user-avatar-small">
                                            {user.username.charAt(0).toUpperCase()}
                                        </div>
                                        <strong>{user.username}</strong>
                                    </div>
                                </td>
                                <td className="text-muted">{user.telegramId}</td>
                                <td><strong>🪙 {user.coins}</strong></td>
                                <td className="text-muted">
                                    {new Date(user.createdAt).toLocaleDateString('ru-RU')}
                                </td>
                                <td>
                                    <button
                                        className="btn btn-ghost btn-sm"
                                        onClick={() => setSelectedUser(user)}
                                    >
                                        Подробнее →
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                    {filteredUsers.length === 0 && <p className="empty">Пользователи не найдены.</p>}
                </div>
            )}

            {selectedUser && (
                <UserDetailsModal
                    user={selectedUser}
                    onClose={() => setSelectedUser(null)}
                />
            )}
        </div>
    )
}