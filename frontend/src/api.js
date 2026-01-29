const API_BASE = 'http://localhost:8082/api';

export const bookmarkApi = {
    async getAll(page = 0, size = 10) {
        const res = await fetch(`${API_BASE}/bookmarks?page=${page}&size=${size}`);
        return res.json();
    },

    async getById(id) {
        const res = await fetch(`${API_BASE}/bookmarks/${id}`);
        return res.json();
    },

    async create(bookmark) {
        const res = await fetch(`${API_BASE}/bookmarks`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bookmark),
        });
        return res.json();
    },

    async update(id, bookmark) {
        const res = await fetch(`${API_BASE}/bookmarks/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bookmark),
        });
        return res.json();
    },

    async delete(id) {
        await fetch(`${API_BASE}/bookmarks/${id}`, { method: 'DELETE' });
    },

    async search(keyword, page = 0, size = 10) {
        const res = await fetch(`${API_BASE}/bookmarks/search?keyword=${encodeURIComponent(keyword)}&page=${page}&size=${size}`);
        return res.json();
    },

    async getByTag(tagName, page = 0, size = 10) {
        const res = await fetch(`${API_BASE}/bookmarks/tag/${encodeURIComponent(tagName)}?page=${page}&size=${size}`);
        return res.json();
    },
};

export const tagApi = {
    async getAll() {
        const res = await fetch(`${API_BASE}/tags`);
        return res.json();
    },
};
