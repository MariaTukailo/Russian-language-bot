import { HashRouter, Routes, Route, Navigate } from 'react-router-dom'
import AdminLayout from './components/Layout/AdminLayout'
import Dashboard from './pages/admin/Dashboard'
import Sections from './pages/admin/Sections'
import SectionDetail from './pages/admin/SectionDetail'
import Topics from './pages/admin/Topics'
import Questions from './pages/admin/Questions'
import Users from './pages/admin/Users'
import Statistics from './pages/admin/Statistics'
import Settings from './pages/admin/Settings'

function App() {
    return (
        <HashRouter>
            <Routes>
                <Route path="/" element={<Navigate to="/admin" replace />} />
                <Route path="/admin" element={<AdminLayout />}>
                    <Route index element={<Dashboard />} />
                    <Route path="sections" element={<Sections />} />
                    <Route path="sections/:id" element={<SectionDetail />} />
                    <Route path="topics" element={<Topics />} />
                    <Route path="questions" element={<Questions />} />
                    <Route path="users" element={<Users />} />
                    <Route path="statistics" element={<Statistics />} />
                    <Route path="settings" element={<Settings />} />
                </Route>
            </Routes>
        </HashRouter>
    )
}

export default App
