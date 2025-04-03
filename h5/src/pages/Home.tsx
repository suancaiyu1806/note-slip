import React, { useEffect, useState } from 'react';
import NoteCard from '../components/NoteCard';
// 更新导入路径
import styles from '../styles/home.module.scss';

const Home: React.FC = () => {
  const [notes, setNotes] = useState<{ key: string; value: string; createTime: string }[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedNotes = localStorage.getItem('notes');
    if (storedNotes) {
      setNotes(JSON.parse(storedNotes));
    }
    setLoading(false);
  }, []);

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h1 className={styles.title}>我的便签</h1>
      </div>
      {loading ? (
        <div className={styles.loading}>加载中...</div>
      ) : notes.length === 0 ? (
        <div className={styles.empty}>暂无便签，请创建新便签</div>
      ) : (
        <div className={styles.notesGrid}>
          {notes.map((note) => (
            <NoteCard key={note.key} note={note} />
          ))}
        </div>
      )}
    </div>
  );
};

export default Home;