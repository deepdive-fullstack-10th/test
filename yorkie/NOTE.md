## 📝필요한 기술들만 정리

---

### Deactivate
```javascript
window.addEventListener('beforeunload', () => {
  client.deactivate({keepalive: true});
});
```

- `await client.deactivate();`은 그냥 client 연결을 끊어준다.
  - 코드 작성이 끝나고 code space 종료 할 때, yorkie에 client를 deactivate 해주면 됨!
- 위 코드 처럼, beforeunload 이벤트가 발생할 경우, deactivate 를 보장해주는 옵션이 keepalive이다.

---

### Attaching

```javascript
await client.attach(doc, {
  initialPresence: { color: 'blue', cursor: { x: 0, y: 0 } },
  syncMode: SyncMode.Manual,
});
```

- client가 yorkie 문서에 연결하는 방법이다.
- initialPresence 옵션을 사용한다면, Json 정보로 각 Client의 정보를 공유할 수 있다.
- syncMode는 매우 복잡한 작업이 아니라면 Realtime을 사용하면 되는데 Default이다.
  - Realtime = 자동 동기화
  - Menual = 수동 동기화 처리
  - 그 외 2개 더있는데 아래에 작성.
- Client 별 커서 및 캐럿 처리에 유용할듯

```javascript
await client.attach(doc, {
  initialRoot: {
    list: [1, 2, 3],
    counter: new yorkie.Counter(yorkie.IntType, 0),
  },
});
```

- initialRoot도 있는데 list 정보랑 counter 정보를 공유할 수 있다.
  - 정확히는 그냥 객체들을 공유하는 것
  - Presence랑 같다고 볼 수도 있음.
  - Presence는 Document에 연결된 동안 임시 데이터, Root는 영구 데이터
- Counter의 자세한 내용은 Docs를 보면 되곘지만, 우리 프로젝트에서는 필요 없다.

---

### Updating

```javascript
doc.update((root, presence) => {
  presence.set({ cursor: { x: 1, y: 1 } });
});
```
- update는 callback parameter로 root랑 presence 정보를 받을 수 있다.
- root에는 아까 initialRoot 정보가 포함되는데 initialRoot에는 Default로 message가 숨겨져있다.
  - 즉, root를 통해 message가 업데이트 되는거임.
  - 키보드 입력 -> doc.update((root) => {root.message = 입력 값})
- presence는 initialPresence 정보로 Json 형태로 저장된 내용을 가져 올 수 있음.
  - update 될 때 마다 presence로 좌표 정보로 carrot이나 cursor에 사용자별 프로필 사진 띄우든지 처리 가능할듯

### Getting

```javascript
// 아래 두개는 같은거
doc.getPresence(client.getID());
doc.getMyPresence();

// 현재 document에 있는 모든 사용자 정보
const users = doc.getPresences();
for (const { clientID, presence } of users ) {
  // Do something
}
```
- 위 방식들을 통해 IDE 탭 별 연결된 사용자 정보를 가져올 수 있을듯
- presence에 UserInfo 같은걸 넣어서 처리 가능할듯

---

### Editing

```javascript
const message = 'update document for test';
doc.update((root) => {
  root.todos = [];
  root.todos.push('todo-1');
  root.obj = {
    name: 'yorkie',
    age: 14,
  };
  root.counter = new yorkie.Counter(yorkie.IntType, 0);
  root.counter.increase(1);
}, message);
```

- 단순 보기에는 Updating과 동일
- Updating은 Presence 정보를 최신화
- Editing은 Remote에 존재하는 Document를 수정
- `doc.getRoot()`로 root의 하위 정보들을 가져올 수 있음.

---

### Subscribing

|             |             |         | ||
|:-----------:|:-----------:|:-------:|:-:|:-:|
|  presence   | my-presence | others  |none |$.path|
| connection  |    sync     | status  |auth-error |all|

- 총 10가지 방법이 존재
- 필요없는 내용
  - none이랑 presence, $.path 빼고 다 필요없음.
  - 추후 고도화 작업 시 더 필요한 내용들은 존재

```javascript
// none
const unsubscribe = doc.subscribe((event) => {
  if (event.type === 'snapshot') {
    // `snapshot` delivered when the entire document is updated from the server.
  } else if (event.type === 'local-change') {
    // `local-change` delivered when calling document.update from the current client.
  } else if (event.type === 'remote-change') {
    // `remote-change` delivered when the document is updated from other clients.
    const { message, operations } = event.value;

    // You can access the operations that have been applied to the document.
    for (const op of operations) {
      // e.g.) { type: 'increase', value: 1, path: '$.counter' }
      switch (op.type) {
        case 'increase':
          // ...
          break;
      }
    }
  }
});
```

- 개발 환경에서 remote-change 옵션만 활용될 것
  - yorkie document 정보만 활용하면 될 것으로 보임.
```javascript
// 예시
doc.subscribe((event) => {
  if (event.type === 'remote-change') {
    messageEl.value = doc.getRoot().message;
    // doc.getPresence(); 정보도 활용가능
  }
});
```
- 고도화 시 offline 환경에서 local-change 옵션이랑 connection, sync 방식으로 online 환경으로 전환 시 동기화 되도록 처리 가능할듯

```javascript
//presence
const unsubscribe = doc.subscribe('presence', (event) => {
  if (event.type === 'initialized') {
    // event.value: Array of users currently participating in the document
  }

  if (event.type === 'watched') {
    // event.value: A user has joined the document editing in online
  }

  if (event.type === 'unwatched') {
    // event.value: A user has left the document editing
  }

  if (event.type === 'presence-changed') {
    // event.value: A user has updated their presence
  }
});
```

- initialized 옵션으로 Document session에 접속되어있는 다른 사용자들 정보를 가져올 수 있음.
  - 탭 별 연결된 사용자 정보 업데이트 가능
  - 옵션명대로 초기에 사용자 정보를 불러오므로 추가로 내 정보도 업데이트 해줘야 함.
- watched로 새로 Document session에 연결된 사용자 정보를 구독 가능
  - 기존 사용자 정보들은 있을테니 거기에 추가된 사용자 정보만 업데이트 하면 됨
- unwatched로 Document session에서 나간 사용자 정보를 구독 가능
  - 사용자 정보 리스트에서 나간 사람만 제외해주면 됨
- precense-changed는 다른 client 및 내가 작업을 할 때 변경된 precense 정보를 구독
  - 잘 쓰려나 모르겠음. 활용성에 대해 잘 떠오르지 않음.

```javascript
// The event is triggered when the value of the path("$.todos") is changed.
const unsubscribe = doc.subscribe('$.todos', (event) => {
  // You can access the updated value of the path.
  const target = doc.getValueByPath('$.todos');
});
```

- initialRoot에 저장된 정보로 최상위 root부터 key값 들을 하나의 노드로 처리함.
- $.todos는 root: { todos: {} } 와 같은 형식일 떄, todos만 구독하는 것
- 그 자식도 가능 root: { todos: { dummy: {} } } -> $.todos.dummy
- 영구적으로 보관해야 될 정보가 있을 때 사용, 현재는 message 말고는 안 떠오르긴 함.

```javascript
// auth-error
const unsubscribe = doc.subscribe('auth-error', (event) => {
  console.log(event.value);
  // event.value contains:
  // - reason: string
  // - method: 'PushPull' | 'WatchDocuments'
});
```

- auth-error는 webHook을 통해 server에서 token 처리를 할 때 사용할 수 있음
  - 현재 구현할 지 고민 중, 추가 학습이 필요

---

### Sync Changing

- SyncMode는 두 가지의 옵션이 더 있음.
  - SyncMode.RealtimePushOnly
  - SyncMode.RealtimeSyncOff
- `await client.changeSyncMode(doc, option)`으로 설정 가능
- 우리는 Realtime이면 충분하다고 판단 됨.

---

### Detaching

`await client.detach(doc);`

- deactivate는 클라이언트를 완전히 종료하는 방식임.
- 즉, 다시 연결하려면 network를 타고 yorkie에 연결해야 됨.
  - network overhead 발생
  - detach로 yorkie에는 연결하고 해당 document에 관련된 세션만 끊을 수 있음.
- 탭 전환시 사용하면 좋음. detach -> new tab attach 반복

### ETC

- 네가지가 더 남음
  - Custom CRDT
  - Counter
  - TS Support
  - Logger Options
- counter는 사용할 일이 없고, custom crdt는 필요할 지 상황이 돼바야 알 것 같음.
- Ts는 문외한.
- 나머지 정보들은 되게 적은 양만 존재하므로 문서를 찾아보면 좋을듯
