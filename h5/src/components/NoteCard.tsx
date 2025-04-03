import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/noteCard.module.scss';

interface NoteCardProps {
  note: { key: string; value: string; createTime: string };
}

const NoteCard: React.FC<NoteCardProps> = ({ note }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/note-edit/${note.key}`);
  };

  return (
    <div className={styles.noteCard} onClick={handleClick}>
      <div className={styles.noteContent}>{note.value || '空便签'}</div>
      <div className={styles.noteDate}>{note.createTime}</div>
    </div>
  );
};

export default NoteCard;