let editor;
let yorkieClient;
let yorkieDoc;

const urlParams = new URLSearchParams(window.location.search);
const roomId = urlParams.get('roomId');
document.getElementById('roomIdDisplay').textContent = roomId;

function initMonaco() {
    return new Promise(resolve => {
        require.config({
            paths: {'vs': 'https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.52.0/min/vs'}
        });

        require(['vs/editor/editor.main'], () => {
            editor = monaco.editor.create(document.getElementById('editor-container'), {
                value: '// 여기에 코드를 입력하세요',
                language: 'javascript',
                theme: 'vs-dark',
                automaticLayout: true,
                minimap: {
                    enabled: false
                }
            });
            resolve();
        });
    });
}

function updateClientsInfo() {
    if (yorkieDoc) {
        const presences = yorkieDoc.getPresences();
        const clientsDiv = document.getElementById('clients');
        clientsDiv.innerHTML = '연결된 클라이언트: ' +
            Array.from(presences).map(presence => presence.clientID).join(', ');
    }
}

async function testConnection() {
    try {
        yorkieClient = new yorkie.Client('http://localhost:11101');
        document.getElementById('status').textContent = '클라이언트 생성됨';

        await yorkieClient.activate();
        const clientId = yorkieClient.getID();
        document.getElementById('status').textContent = '연결 성공! Client ID: ' + clientId;

        yorkieDoc = new yorkie.Document(roomId);

        // presence 정보 추가
        await yorkieClient.attach(yorkieDoc, {
            initialPresence: {
                clientId: clientId
            }
        });

        yorkieDoc.update((root) => {
            if (!root.content) {
                root.content = new yorkie.Text();
            }
        });

        // presence 변경 구독
        yorkieDoc.subscribe('presence', (event) => {
            updateClientsInfo();
        });

        yorkieDoc.subscribe((event) => {
            console.log('Document event detail:', {
                type: event.type,
                value: event.value,
                operations: event.value?.operations
            });

            if (event.type === 'remote-change') {
                // operations 처리
                const operations = event.value?.operations;
                if (operations && operations.length > 0) {
                    console.log('Remote operations:', operations);

                        // 현재 문서의 전체 내용 가져오기
                    const currentContent = yorkieDoc.getRoot().content;
                    console.log('Current document content:', currentContent?.toString());

                    // 에디터 업데이트
                    if (currentContent) {
                        const position = editor.getPosition();
                        editor.setValue(currentContent.toString());
                        if (position) {
                            editor.setPosition(position);
                        }
                    }
                }
            }
        });

        let isUpdating = false;  // 업데이트 중복 방지
        editor.onDidChangeModelContent((event) => {
            if (event.isFlush || isUpdating) return;

            const content = editor.getValue();
            console.log('Local change:', {
                content: content,
                changes: event.changes
            });

            try {
                isUpdating = true;
                yorkieDoc.update((root) => {
                    if (!root.content) {
                        root.content = new yorkie.Text();
                    }

                    // 변경사항 적용
                    root.content.edit(0, root.content.length, content);

                    console.log('Document after update:', {
                        content: root.content.toString(),
                        length: root.content.length
                    });
                });
            } finally {
                isUpdating = false;
            }
        });

// 초기 클라이언트 정보 업데이트
        updateClientsInfo();

    } catch (error) {
        document.getElementById('status').textContent = '연결 실패: ' + error.message;
        console.error('Yorkie 연결 에러:', error);
    }
}

window.addEventListener('beforeunload', async () => {
    if (yorkieDoc && yorkieClient) {
        await yorkieClient.detach(yorkieDoc);
        await yorkieClient.deactivate();
    }
});

async function init() {
    if (!roomId) {
        alert('방 ID가 없습니다!');
        window.location.href = 'index.html';
        return;
    }
    await initMonaco();
    await testConnection();
}

init();