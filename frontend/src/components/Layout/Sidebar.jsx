import { NavLink } from 'react-router-dom'
import { currentUser } from '../../mocks/currentUser'

const menuItems = [
    { to: '/admin', label: 'Главная', end: true },
    { to: '/admin/sections', label: 'Разделы' },
    { to: '/admin/topics', label: 'Темы' },
    { to: '/admin/questions', label: 'Вопросы' },
    { to: '/admin/users', label: 'Пользователи' },
    { to: '/admin/statistics', label: 'Статистика' },
    { to: '/admin/settings', label: 'Настройки' },
]

export default function Sidebar() {
    const initials = currentUser.name
        .split(' ')
        .map(w => w[0])
        .join('')
        .toUpperCase()

    return (
        <aside className="sidebar">
            <div className="sidebar-user">
                <div className="sidebar-avatar">{initials}</div>
                <div>
                    <div className="sidebar-name">{currentUser.name}</div>
                    <div className="sidebar-role">Администратор</div>
                </div>
            </div>

            <nav className="sidebar-nav">
                {menuItems.map(item => (
                    <NavLink
                        key={item.to}
                        to={item.to}
                        end={item.end}
                        className={({ isActive }) =>
                            'sidebar-link' + (isActive ? ' active' : '')
                        }
                    >
                        {item.label}
                    </NavLink>
                ))}
            </nav>

            <button className="sidebar-logout">Выйти</button>
        </aside>
    )
}