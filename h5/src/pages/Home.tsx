import React, { useEffect, useState } from 'react';
import NoteCard from '../components/NoteCard';
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

    const initNFC = async () => {
      if ('NDEFReader' in window) {
        const reader = new (window as any).NDEFReader();
        try {
          await reader.scan();
          console.log('开始搜索NFC设备');

          reader.onreading = async (event: any) => {
            console.log('发现NFC设备：', event);
            const nfcId = event.message.records[0].data;
            // 假设这里有一个函数可以根据nfcId获取便签key
            const key = await getNoteKeyByNFCId(nfcId);
            if (key) {
              // 假设这里有一个函数可以根据key打开便签编辑页
              openNoteEditPage(key);
            }
          };

          reader.onreadingerror = (error: any) => {
            console.error('读取NFC标签失败：', error);
          };
        } catch (error) {
          console.error('NFC初始化失败：', error);
        }
      } else {
        console.error('当前浏览器不支持Web NFC API');
      }
    };

    initNFC();
  }, []);

  // 模拟根据nfcId获取便签key的函数
  const getNoteKeyByNFCId = async (nfcId: string) => {
    // 这里需要实现与后端交互获取便签key的逻辑
    // 示例中简单返回一个硬编码的key
    return 'exampleKey';
  };

  // 模拟打开便签编辑页的函数
  const openNoteEditPage = (key: string) => {
    // 这里需要实现打开便签编辑页的逻辑
    console.log('打开便签编辑页，key:', key);
  };

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