import { EditorManager } from './managers/EditorManager.js';
import { YorkieManager } from './managers/YorkieManager.js';
import { UIManager } from './managers/UIManager.js';
import { SUPPORTED_LANGUAGES } from './config/languages.js';

export class CollaborativeIDE {

    constructor() {
        this.editorManager = new EditorManager('editor-container');
        this.yorkieManager = new YorkieManager('http://localhost:11101');
        this.uiManager = new UIManager();
        this.isUpdating = false;
    }

    async initialize() {
        const urlParams = new URLSearchParams(window.location.search);
        const roomId = urlParams.get('roomId');
        const language = urlParams.get('language') || 'javascript';

        if (!roomId) {
            alert('방 ID가 없습니다!');
            window.location.href = '/index.html';
            return;
        }

        try {
            // 에디터 초기화 시 언어 전달
            await this.editorManager.initialize(language);

            // 언어 선택 UI 업데이트
            const languageSelect = document.getElementById('languageSelect');
            if (languageSelect) {
                languageSelect.value = language;
                languageSelect.disabled = false;  // 선택 가능하도록 활성화
            }

            // Yorkie 초기화
            const connectionInfo = await this.yorkieManager.initialize(roomId, language);
            this.uiManager.updateStatus('연결 성공! Client ID: ' + connectionInfo.clientId);
            this.uiManager.updateRoomInfo(roomId, SUPPORTED_LANGUAGES[language].name);

            this.setupEventHandlers();
        } catch (error) {
            this.uiManager.updateStatus('연결 실패: ' + error.message);
            console.error('초기화 에러:', error);
        }
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

        this.yorkieManager.onPresenceChange((presences) => {
            const clientIds = Array.from(presences).map(presence => presence.clientID);
            this.uiManager.updateClients(clientIds);
        });

        // 초기 클라이언트 정보 업데이트 추가
        const initialPresences = this.yorkieManager.getPresences();
        const initialClientIds = Array.from(initialPresences).map(presence => presence.clientID);
        this.uiManager.updateClients(initialClientIds);
    }
}

// 페이지 언로드 시 정리
window.addEventListener('beforeunload', () => {
    app.yorkieManager.cleanup();
});