// js/services/api.js
const API_CONFIG = {
    BASE_URL: 'http://localhost:8080/api',
    ENDPOINTS: {
        USER: '/user/current',
        LANGUAGES: '/languages',
        TABS: '/rooms'
    }
};

export class ApiService {
    static async getAvailableLanguages() {
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.LANGUAGES}`);
            return await response.json();
        } catch (error) {
            console.error('Failed to fetch languages:', error);
            // 기본값 반환
            return [
                { id: 'javascript', name: 'JavaScript' },
                { id: 'python', name: 'Python' },
                { id: 'java', name: 'Java' }
            ];
        }
    }

    static async getUserTabs(roomId) {
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.TABS}/${roomId}/tabs`);
            return await response.json();
        } catch (error) {
            console.error('Failed to fetch user tabs:', error);
            // 임시 데이터 반환
            return [
                { userId: 'user1', userName: 'User 1', color: '#FF6B6B' },
                { userId: 'user2', userName: 'User 2', color: '#4ECDC4' },
                { userId: 'user3', userName: 'User 3', color: '#45B7D1' },
                { userId: 'user4', userName: 'User 4', color: '#96CEB4' }
            ];
        }
    }
}