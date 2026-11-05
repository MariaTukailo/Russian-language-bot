import { useState } from 'react'
import { createTest, updateTest } from '../api/tests'

export default function TestModal({ test, topics, onClose, onSaved }) {
    const [title, setTitle] = useState(test?.title || '')
    const [topicId, setTopicId] = useState(test?.topicId || (topics[0]?.id || ''))
    const [questions, setQuestions] = useState(test?.questions || [
        { text: '', options: [{ text: '', correct: true }, { text: '', correct: false }] }
    ])
    const [saving, setSaving] = useState(false)
    const [error, setError] = useState(null)

    const handleQuestionChange = (qIndex, value) => {
        const newQ = [...questions]
        newQ[qIndex].text = value
        setQuestions(newQ)
    }

    const handleOptionChange = (qIndex, oIndex, field, value) => {
        const newQ = [...questions]
        newQ[qIndex].options[oIndex][field] = value
        setQuestions(newQ)
    }

    const addQuestion = () => {
        setQuestions([...questions, { text: '', options: [{ text: '', correct: true }, { text: '', correct: false }] }])
    }

    const removeQuestion = (qIndex) => setQuestions(questions.filter((_, i) => i !== qIndex))

    const addOption = (qIndex) => {
        const newQ = [...questions]
        newQ[qIndex].options.push({ text: '', correct: false })
        setQuestions(newQ)
    }

    const removeOption = (qIndex, oIndex) => {
        const newQ = [...questions]
        newQ[qIndex].options = newQ[qIndex].options.filter((_, i) => i !== oIndex)
        setQuestions(newQ)
    }

    const handleSubmit = () => {
        if (!title.trim() || !topicId) {
            setError('Заполните название и выберите тему')
            return
        }
        setSaving(true)

        const payload = {
            title: title.trim(),
            topicId: Number(topicId),
            questions: questions.map(q => ({
                text: q.text,
                options: q.options.map(o => ({ text: o.text, correct: o.correct }))
            }))
        }

        // При создании передаем topicId в URL, при обновлении - просто в body
        const promise = test
            ? updateTest(test.id, payload)
            : createTest(payload, Number(topicId))

        promise
            .then(() => { setSaving(false); onSaved() })
            .catch(err => { console.error(err); setError('Ошибка при сохранении'); setSaving(false) })
    }

    // ... (JSX код модалки остается без изменений, как я присылал ранее) ...
    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal modal-large" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>{test ? 'Редактировать тест' : 'Создать тест'}</h2>
                    <button className="modal-close" onClick={onClose}>×</button>
                </div>
                <div className="modal-body">
                    <label className="form-label">Тема</label>
                    <select className="form-input" value={topicId} onChange={e => setTopicId(e.target.value)}>
                        <option value="">— Выберите тему —</option>
                        {topics.map(t => <option key={t.id} value={t.id}>{t.title}</option>)}
                    </select>

                    <label className="form-label">Название теста</label>
                    <input className="form-input" value={title} onChange={e => setTitle(e.target.value)} placeholder="Например: Базовый тест" />

                    <hr className="divider" />

                    <div className="questions-header">
                        <h3>Вопросы</h3>
                        <button className="btn btn-primary btn-sm" onClick={addQuestion}>+ Вопрос</button>
                    </div>

                    {questions.map((q, qIndex) => (
                        <div key={qIndex} className="question-block">
                            <div className="question-header">
                                <span>Вопрос {qIndex + 1}</span>
                                {questions.length > 1 && (
                                    <button className="btn-icon btn-danger" onClick={() => removeQuestion(qIndex)}>✕</button>
                                )}
                            </div>
                            <input className="form-input" placeholder="Текст вопроса" value={q.text} onChange={e => handleQuestionChange(qIndex, e.target.value)} />
                            <div className="options-list">
                                {q.options.map((opt, oIndex) => (
                                    <div key={oIndex} className="option-row">
                                        <input type="checkbox" checked={opt.correct} onChange={e => handleOptionChange(qIndex, oIndex, 'correct', e.target.checked)} title="Отметить как правильный" />
                                        <input className="form-input" placeholder="Вариант ответа" value={opt.text} onChange={e => handleOptionChange(qIndex, oIndex, 'text', e.target.value)} />
                                        {q.options.length > 2 && (
                                            <button className="btn-icon btn-danger" onClick={() => removeOption(qIndex, oIndex)}>✕</button>
                                        )}
                                    </div>
                                ))}
                                <button className="btn btn-ghost btn-sm" onClick={() => addOption(qIndex)} style={{ alignSelf: 'flex-start', marginTop: '8px' }}>
                                    + Добавить вариант
                                </button>
                            </div>
                        </div>
                    ))}
                    {error && <p style={{ color: 'red', marginTop: 8 }}>{error}</p>}
                </div>
                <div className="modal-footer">
                    <button className="btn btn-ghost" onClick={onClose}>Отмена</button>
                    <button className="btn btn-primary" onClick={handleSubmit} disabled={saving}>
                        {saving ? 'Сохранение...' : 'Сохранить'}
                    </button>
                </div>
            </div>
        </div>
    )
}