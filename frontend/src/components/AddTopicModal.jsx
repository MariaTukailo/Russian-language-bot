import { useEffect, useRef, useState } from 'react'
import { createTopic, updateTopic } from '../api/topics'
import { getSectionsList } from '../api/sections'
import { getVideos, createVideo } from '../api/videos' // <--- ИМПОРТ createVideo
import { uploadImage } from '../api/files'

const getYouTubeId = (url) => {
    if (!url) return null;
    const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
    const match = url.match(regExp);
    return (match && match[2].length === 11) ? match[2] : null;
}

export default function AddTopicModal({ topic, onClose, onCreated }) {
    const [sections, setSections] = useState([])
    const [videos, setVideos] = useState([])

    const [sectionId, setSectionId] = useState(topic?.sectionId ? String(topic.sectionId) : '')
    const [title, setTitle] = useState(topic?.title || '')
    const [description, setDescription] = useState(topic?.description || '')
    const [videoId, setVideoId] = useState(topic?.videoId ? String(topic.videoId) : '')

    const [conspectUrls, setConspectUrls] = useState(
        topic?.conspectUrls && topic.conspectUrls.length > 0 ? topic.conspectUrls : ['']
    )

    const [saving, setSaving] = useState(false)
    const [uploading, setUploading] = useState(false)
    const [error, setError] = useState(null)

    const fileInputRef = useRef(null)

    // Состояния для быстрого создания видео
    const [isAddingVideo, setIsAddingVideo] = useState(false)
    const [newVideoTitle, setNewVideoTitle] = useState('')
    const [newVideoUrl, setNewVideoUrl] = useState('')

    useEffect(() => {
        getSectionsList().then(setSections).catch(console.error)
        getVideos().then(setVideos).catch(console.error)
    }, [])

    const selectedVideo = videos.find(v => String(v.id) === videoId)
    const ytId = getYouTubeId(selectedVideo?.url)

    // Обработчик быстрого добавления видео
    const handleQuickAddVideo = () => {
        if (!newVideoTitle.trim() || !newVideoUrl.trim()) {
            alert("Заполните название и ссылку на видео");
            return;
        }
        createVideo({ title: newVideoTitle, url: newVideoUrl, description: '' })
            .then(createdVideo => {
                setVideos([...videos, createdVideo]); // Добавляем в список
                setVideoId(String(createdVideo.id));  // Сразу выбираем
                setIsAddingVideo(false);              // Закрываем форму
                setNewVideoTitle('');
                setNewVideoUrl('');
            })
            .catch(err => console.error(err));
    }

    const handleUrlChange = (index, value) => {
        const newUrls = [...conspectUrls];
        newUrls[index] = value;
        setConspectUrls(newUrls);
    }

    const addUrlField = () => setConspectUrls([...conspectUrls, '']);

    const removeUrlField = (index) => {
        const newUrls = conspectUrls.filter((_, i) => i !== index);
        setConspectUrls(newUrls.length > 0 ? newUrls : ['']);
    }

    const handleFileUpload = async (e) => {
        const files = Array.from(e.target.files);
        if (files.length === 0) return;

        setUploading(true);
        try {
            const uploadedUrls = [];
            for (const file of files) {
                const url = await uploadImage(file);
                uploadedUrls.push(url);
            }
            const currentValidUrls = conspectUrls.filter(u => u.trim() !== '');
            setConspectUrls([...currentValidUrls, ...uploadedUrls]);
        } catch (err) {
            console.error(err);
            setError('Ошибка при загрузке картинок');
        } finally {
            setUploading(false);
            if (fileInputRef.current) fileInputRef.current.value = '';
        }
    }

    const handleSubmit = () => {
        if (!sectionId) { setError('Выберите раздел'); return }
        if (!title.trim()) { setError('Введите название'); return }

        setSaving(true)
        const cleanUrls = conspectUrls.map(u => u.trim()).filter(u => u !== '')

        const payload = {
            title: title.trim(),
            description: description.trim() || null,
            videoId: videoId ? Number(videoId) : null,
            conspectUrls: cleanUrls,
            tests: topic ? topic.tests : []
        }

        const promise = topic ? updateTopic(topic.id, payload) : createTopic(payload, Number(sectionId))

        promise
            .then(() => { setSaving(false); onCreated() })
            .catch(err => { console.error(err); setError('Ошибка сохранения'); setSaving(false) })
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>{topic ? 'Редактировать тему' : 'Добавить тему'}</h2>
                    <button className="modal-close" onClick={onClose}>×</button>
                </div>
                <div className="modal-body">
                    <label className="form-label">Раздел</label>
                    <select className="form-input" value={sectionId} onChange={e => setSectionId(e.target.value)} disabled={!!topic}>
                        <option value="">— Выберите раздел —</option>
                        {sections.map(s => <option key={s.id} value={s.id}>{s.title}</option>)}
                    </select>

                    <label className="form-label">Название темы</label>
                    <input className="form-input" value={title} onChange={e => setTitle(e.target.value)} />

                    <label className="form-label">Описание</label>
                    <textarea className="form-input form-textarea" value={description} onChange={e => setDescription(e.target.value)} />

                    {/* БЛОК ВИДЕО С КНОПКОЙ ДОБАВЛЕНИЯ */}
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: 16, marginBottom: 8 }}>
                        <label className="form-label" style={{ margin: 0 }}>Видеоурок</label>
                        <button
                            type="button"
                            className="btn btn-ghost btn-sm"
                            onClick={() => setIsAddingVideo(!isAddingVideo)}
                        >
                            {isAddingVideo ? 'Отмена' : '+ Добавить видео'}
                        </button>
                    </div>

                    {isAddingVideo ? (
                        <div className="quick-create-box">
                            <input className="form-input" placeholder="Название видео" value={newVideoTitle} onChange={e => setNewVideoTitle(e.target.value)} />
                            <input className="form-input" placeholder="Ссылка на YouTube" value={newVideoUrl} onChange={e => setNewVideoUrl(e.target.value)} style={{ marginTop: 8 }} />
                            <button type="button" className="btn btn-primary btn-sm" onClick={handleQuickAddVideo} style={{ marginTop: 8 }}>
                                Сохранить видео
                            </button>
                        </div>
                    ) : (
                        <select className="form-input" value={videoId} onChange={e => setVideoId(e.target.value)}>
                            <option value="">— не выбрано —</option>
                            {videos.map(v => <option key={v.id} value={v.id}>{v.title}</option>)}
                        </select>
                    )}

                    {ytId && !isAddingVideo && (
                        <div className="video-preview-box">
                            <img src={`https://img.youtube.com/vi/${ytId}/mqdefault.jpg`} alt="Превью видео" />
                        </div>
                    )}

                    {/* БЛОК КОНСПЕКТА */}
                    <div style={{ marginTop: 24, marginBottom: 8, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <label className="form-label" style={{ margin: 0 }}>Конспект (страницы)</label>
                        <div style={{ display: 'flex', gap: 8 }}>
                            <input
                                type="file"
                                multiple
                                accept="image/*"
                                ref={fileInputRef}
                                style={{ display: 'none' }}
                                onChange={handleFileUpload}
                            />
                            <button
                                type="button"
                                className="btn btn-ghost btn-sm"
                                onClick={() => fileInputRef.current?.click()}
                                disabled={uploading}
                            >
                                {uploading ? 'Загрузка...' : '📁 Загрузить фото'}
                            </button>
                            <button type="button" className="btn btn-ghost btn-sm" onClick={addUrlField}>
                                + Ссылка
                            </button>
                        </div>
                    </div>

                    <div className="conspect-urls-list">
                        {conspectUrls.map((url, index) => (
                            <div key={index} className="conspect-url-item">
                                <div style={{ flex: 1 }}>
                                    <input
                                        className="form-input"
                                        placeholder="Вставьте ссылку на картинку"
                                        value={url}
                                        onChange={e => handleUrlChange(index, e.target.value)}
                                    />
                                    {url.trim().length > 5 && (
                                        <div className="conspect-img-preview">
                                            <img
                                                src={url}
                                                alt="Превью"
                                                onError={(e) => e.target.style.display = 'none'}
                                                onLoad={(e) => e.target.style.display = 'block'}
                                            />
                                        </div>
                                    )}
                                </div>
                                <button type="button" className="btn-icon btn-danger" onClick={() => removeUrlField(index)} title="Удалить страницу">
                                    ✕
                                </button>
                            </div>
                        ))}
                    </div>

                    {error && <p style={{ color: 'red', marginTop: 8 }}>{error}</p>}
                </div>
                <div className="modal-footer">
                    <button className="btn btn-ghost" onClick={onClose}>Отмена</button>
                    <button className="btn btn-primary" onClick={handleSubmit} disabled={saving || uploading}>
                        {saving ? 'Сохранение...' : 'Сохранить'}
                    </button>
                </div>
            </div>
        </div>
    )
}