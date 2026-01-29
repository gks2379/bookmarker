import { useState, useEffect } from 'react';
import { bookmarkApi, tagApi } from './api';
import './App.css';

function App() {
  const [bookmarks, setBookmarks] = useState([]);
  const [tags, setTags] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [selectedTag, setSelectedTag] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [editingBookmark, setEditingBookmark] = useState(null);
  const [form, setForm] = useState({ url: '', title: '', description: '', tagNames: '' });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const [bookmarkRes, tagRes] = await Promise.all([
        bookmarkApi.getAll(),
        tagApi.getAll()
      ]);
      setBookmarks(bookmarkRes.content || []);
      setTags(tagRes || []);
    } catch (error) {
      console.error('Failed to load data:', error);
    }
    setLoading(false);
  };

  const handleSearch = async () => {
    if (!searchKeyword.trim()) {
      loadData();
      return;
    }
    setLoading(true);
    try {
      const res = await bookmarkApi.search(searchKeyword);
      setBookmarks(res.content || []);
      setSelectedTag(null);
    } catch (error) {
      console.error('Search failed:', error);
    }
    setLoading(false);
  };

  const handleTagFilter = async (tagName) => {
    if (selectedTag === tagName) {
      setSelectedTag(null);
      loadData();
      return;
    }
    setSelectedTag(tagName);
    setLoading(true);
    try {
      const res = await bookmarkApi.getByTag(tagName);
      setBookmarks(res.content || []);
    } catch (error) {
      console.error('Tag filter failed:', error);
    }
    setLoading(false);
  };

  const openCreateModal = () => {
    setEditingBookmark(null);
    setForm({ url: '', title: '', description: '', tagNames: '' });
    setShowModal(true);
  };

  const openEditModal = (bookmark) => {
    setEditingBookmark(bookmark);
    setForm({
      url: bookmark.url,
      title: bookmark.title,
      description: bookmark.description || '',
      tagNames: bookmark.tagNames?.join(', ') || ''
    });
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setEditingBookmark(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const data = {
      url: form.url,
      title: form.title,
      description: form.description,
      tagNames: form.tagNames.split(',').map(t => t.trim()).filter(t => t)
    };

    try {
      if (editingBookmark) {
        await bookmarkApi.update(editingBookmark.id, data);
      } else {
        await bookmarkApi.create(data);
      }
      closeModal();
      loadData();
    } catch (error) {
      console.error('Save failed:', error);
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('정말 삭제하시겠습니까?')) return;
    try {
      await bookmarkApi.delete(id);
      loadData();
    } catch (error) {
      console.error('Delete failed:', error);
    }
  };

  const formatDate = (dateStr) => {
    return new Date(dateStr).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  return (
    <div className="container">
      <h1>📑 Bookmark Manager</h1>
      <p className="subtitle">나만의 북마크를 태그로 관리하세요</p>

      <div className="controls">
        <input
          type="text"
          className="search-input"
          placeholder="검색어를 입력하세요..."
          value={searchKeyword}
          onChange={(e) => setSearchKeyword(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
        />
        <button className="btn btn-ghost" onClick={handleSearch}>검색</button>
        <button className="btn btn-primary" onClick={openCreateModal}>+ 새 북마크</button>
      </div>

      {tags.length > 0 && (
        <div className="tags-filter">
          {tags.map(tag => (
            <span
              key={tag.id}
              className={`tag-chip ${selectedTag === tag.name ? 'active' : ''}`}
              onClick={() => handleTagFilter(tag.name)}
            >
              #{tag.name}
            </span>
          ))}
        </div>
      )}

      {loading ? (
        <div className="loading">로딩 중...</div>
      ) : bookmarks.length === 0 ? (
        <div className="empty-state">
          <p>북마크가 없습니다.</p>
          <p>새 북마크를 추가해보세요!</p>
        </div>
      ) : (
        <div className="bookmark-list">
          {bookmarks.map(bookmark => (
            <div key={bookmark.id} className="bookmark-card">
              <div className="bookmark-header">
                <div>
                  <div className="bookmark-title">{bookmark.title}</div>
                  <a href={bookmark.url} target="_blank" rel="noopener noreferrer" className="bookmark-url">
                    {bookmark.url}
                  </a>
                </div>
                <div className="bookmark-actions">
                  <button className="btn btn-ghost" onClick={() => openEditModal(bookmark)}>수정</button>
                  <button className="btn btn-danger" onClick={() => handleDelete(bookmark.id)}>삭제</button>
                </div>
              </div>
              {bookmark.description && (
                <p className="bookmark-description">{bookmark.description}</p>
              )}
              {bookmark.tagNames?.length > 0 && (
                <div className="bookmark-tags">
                  {bookmark.tagNames.map(tag => (
                    <span key={tag} className="bookmark-tag">#{tag}</span>
                  ))}
                </div>
              )}
              <div className="bookmark-date">{formatDate(bookmark.createdAt)}</div>
            </div>
          ))}
        </div>
      )}

      {showModal && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h2>{editingBookmark ? '북마크 수정' : '새 북마크 추가'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>URL</label>
                <input
                  type="url"
                  required
                  value={form.url}
                  onChange={(e) => setForm({ ...form, url: e.target.value })}
                  placeholder="https://example.com"
                />
              </div>
              <div className="form-group">
                <label>제목</label>
                <input
                  type="text"
                  required
                  value={form.title}
                  onChange={(e) => setForm({ ...form, title: e.target.value })}
                  placeholder="북마크 제목"
                />
              </div>
              <div className="form-group">
                <label>설명 (선택)</label>
                <textarea
                  value={form.description}
                  onChange={(e) => setForm({ ...form, description: e.target.value })}
                  placeholder="간단한 설명을 입력하세요"
                />
              </div>
              <div className="form-group">
                <label>태그 (쉼표로 구분)</label>
                <input
                  type="text"
                  value={form.tagNames}
                  onChange={(e) => setForm({ ...form, tagNames: e.target.value })}
                  placeholder="dev, frontend, react"
                />
              </div>
              <div className="form-actions">
                <button type="button" className="btn btn-ghost" onClick={closeModal}>취소</button>
                <button type="submit" className="btn btn-primary">
                  {editingBookmark ? '수정' : '추가'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;
