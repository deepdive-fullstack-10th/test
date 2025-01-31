// js/managers/TabManager.js
import { store } from '../config/store.js';

const MOCK_TABS = [
    { userId: 'user1', userName: 'User 1', color: '#FF6B6B' },
    { userId: 'user2', userName: 'User 2', color: '#4ECDC4' },
    { userId: 'user3', userName: 'User 3', color: '#45B7D1' },
    { userId: 'user4', userName: 'User 4', color: '#96CEB4' }
];

export class TabManager {
    constructor(yorkieManager) {
        this.yorkieManager = yorkieManager;
        this.activeTab = null;
        this.container = document.getElementById('tabs-container');
    }

    async initialize(roomId) {
        // API 호출 대신 임시 데이터 사용
        const tabs = MOCK_TABS;
        store.setState({ userTabs: tabs });
        this.renderTabs(tabs);

        if (tabs.length > 0) {
            await this.activateTab(tabs[0].userId);
        }
    }

    renderTabs(tabs) {
        this.container.innerHTML = tabs.map(tab => `
            <div class="tab ${tab.userId === this.activeTab ? 'active' : ''}"
                 style="border-top: 3px solid ${tab.color}"
                 data-user="${tab.userId}">
                ${tab.userName}
            </div>
        `).join('');

        // 탭 클릭 이벤트 설정
        this.container.querySelectorAll('.tab').forEach(tab => {
            tab.addEventListener('click', () => this.activateTab(tab.dataset.user));
        });
    }

    async activateTab(userId) {
        if (this.activeTab === userId) return;

        // 이전 탭의 Yorkie 연결 해제
        if (this.activeTab) {
            await this.yorkieManager.cleanup();
        }

        this.activeTab = userId;

        // 새 탭의 Yorkie 연결
        const state = store.getState();
        await this.yorkieManager.initialize(
            `${state.currentRoom}-${userId}`,
            state.currentLanguage
        );

        // UI 업데이트
        this.container.querySelectorAll('.tab').forEach(tab => {
            tab.classList.toggle('active', tab.dataset.user === userId);
        });
    }
}