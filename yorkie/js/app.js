let client;
let doc;
const statusEl = document.getElementById('status');
const userCountEl = document.getElementById('userCount');
const messageEl = document.getElementById('message');

async function main() {
  // 1. Client 생성 및 연결
  client = new yorkie.Client('http://localhost:11101');

  await client.activate();
  statusEl.textContent = 'Connected';

  // 2. Document 생성 및 연결
  doc = new yorkie.Document('demo-doc');
  await client.attach(doc, { initialPresence: {} });

  // 3. Document 초기 상태 설정
  doc.update((root) => {
    if (!root.message) {
      root.message = '';
    }
  });

  // 4. Document 변경 구독
  doc.subscribe((event) => {
    if (event.type === 'remote-change') {
      messageEl.value = doc.getRoot().message;
    }
  });

  // 5. Presence 구독
  doc.subscribe('presence', () => {
    const users = doc.getPresences();
    userCountEl.textContent = users.length;
  });

  // 초기 메시지 표시
  messageEl.value = doc.getRoot().message;
  const users = doc.getPresences();
  userCountEl.textContent = users.length;
}

// 메시지 업데이트 함수
messageEl.addEventListener('input', () => {
  doc.update((root) => {
    root.message = messageEl.value;
  });
});

main().catch(console.error);
