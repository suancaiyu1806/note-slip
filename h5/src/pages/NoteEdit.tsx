import React, { useEffect, useState } from 'react';
import styles from '../styles/noteEdit.module.scss';

const NoteEdit: React.FC<{ noteKey?: string }> = ({ noteKey }) => {
  const [content, setContent] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (noteKey) {
      const storedNotes = localStorage.getItem('notes');
      if (storedNotes) {
        const notes = JSON.parse(storedNotes);
        const note = notes.find((n: { key: string }) => n.key === noteKey);
        if (note) {
          setContent(note.value);
        }
      }
    }
    setLoading(false);
  }, [noteKey]);

  const saveNote = () => {
    const storedNotes = localStorage.getItem('notes');
    const notes = storedNotes ? JSON.parse(storedNotes) : [];
    if (noteKey) {
      const index = notes.findIndex((n: { key: string }) => n.key === noteKey);
      if (index !== -1) {
        notes[index].value = content;
      }
    } else {
      const newNote = {
        key: Date.now().toString(),
        value: content,
        createTime: new Date().toLocaleString(),
      };
      notes.push(newNote);
    }
    localStorage.setItem('notes', JSON.stringify(notes));
    alert('保存成功');
  };

  const clearNote = () => {
    if (window.confirm('确定要清空当前便签内容吗？')) {
      setContent('');
      if (noteKey) {
        const storedNotes = localStorage.getItem('notes');
        if (storedNotes) {
          const notes = JSON.parse(storedNotes);
          const index = notes.findIndex((n: { key: string }) => n.key === noteKey);
          if (index !== -1) {
            notes[index].value = '';
            localStorage.setItem('notes', JSON.stringify(notes));
          }
        }
      }
      alert('清空成功');
    }
  };

  return (
    <div className={styles.container}>
      {loading ? (
        <div className={styles.loading}>加载中...</div>
      ) : (
        <>
          <textarea
            className={styles.noteEditor}
            value={content}
            onChange={(e) => setContent(e.target.value)}
            placeholder="请输入便签内容..."
          />
          <div className={styles.buttonGroup}>
            <button className={styles.saveBtn} onClick={saveNote}>
              保存
            </button>
            <button className={styles.clearBtn} onClick={clearNote}>
              清空
            </button>
          </div>
        </>
      )}
    </div>
  );
};

export default NoteEdit;