// js/app.js
import { EditorManager } from './managers/EditorManager.js';
import { YorkieManager } from './managers/YorkieManager.js';
import { UIManager } from './managers/UIManager.js';
import { TabManager } from './managers/TabManager.js';
import { store } from "./config/store.js";
import { SUPPORTED_LANGUAGES } from './config/languages.js';

export class CollaborativeIDE {

    constructor() {
        this.editorManager = new EditorManager('editor-container');
        this.yorkieManager = new YorkieManager('http://localhost:11101');
        this.uiManager = new UIManager();
        this.tabManager = new TabManager(this.yorkieManager);
        this.isUpdating = false;
    }

    async initialize() {
        try {
            const urlParams = new URLSearchParams(window.location.search);
            const roomId = urlParams.get('roomId');
            const language = urlParams.get('language') || 'javascript';

            if (!roomId) {
                throw new Error('Room ID is required');
            }

            // 초기 상태 설정
            store.setState({
                currentRoom: roomId,
                currentLanguage: language
            });

            // 언어 선택기 설정
            this.setupLanguageSelector(language);

            // 에디터 및 탭 초기화
            await Promise.all([
                this.editorManager.initialize(language),
                this.tabManager.initialize(roomId)
            ]);

            this.uiManager.updateRoomInfo(roomId, language);
            this.setupEventHandlers();

        } catch (error) {
            this.uiManager.showError(error.message);
        }
    }

    setupLanguageSelector(currentLanguage) {
        const select = document.getElementById('languageSelect');
        SUPPORTED_LANGUAGES.forEach(lang => {
            const option = document.createElement('option');
            option.value = lang.id;
            option.textContent = lang.name;
            option.selected = lang.id === currentLanguage;
            select.appendChild(option);
        });

        select.addEventListener('change', (e) => {
            this.editorManager.setLanguage(e.target.value);
            store.setState({ currentLanguage: e.target.value });
        });
    }

    setupEventHandlers() {
        // 원격 변경사항 처리
        this.yorkieManager.onDocumentChange((event) => {
            if (event.type === 'remote-change') {
                const operations = event.value?.operations;
                if (operations && operations.length > 0) {
                    const currentContent = this.yorkieManager.doc.getRoot().content;
                    if (currentContent) {
                        const position = this.editorManager.getPosition();
                        this.editorManager.setValue(currentContent.toString());
                        this.editorManager.setPosition(position);
                    }
                }
            }
        });

        // 로컬 변경사항 처리
        this.editorManager.onContentChange((event) => {
            if (!this.isUpdating) {
                const content = this.editorManager.getValue();
                this.yorkieManager.updateDocument(content);
            }
        });

        // presence 변경 처리
        this.yorkieManager.onPresenceChange((presences) => {
            this.uiManager.updateClients(presences);
            // 상태 업데이트도 추가
            if (presences.length > 0) {
                this.uiManager.updateStatus('연결됨');
            }
        });

        // 초기 상태 업데이트
        const initialPresences = this.yorkieManager.getPresences();
        if (initialPresences.length > 0) {
            this.uiManager.updateStatus('연결됨');
            this.uiManager.updateClients(initialPresences);
        }
    }

}

// 페이지 언로드 시 정리
window.addEventListener('beforeunload', () => {
    app.yorkieManager.cleanup();
});