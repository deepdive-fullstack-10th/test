// js/managers/YorkieManager.js
export class YorkieManager {
    constructor(serverUrl) {
        this.serverUrl = serverUrl;
        this.client = null;
        this.doc = null;
    }

    async initialize(roomId, language) {
        try {
            this.client = new yorkie.Client(this.serverUrl);
            await this.client.activate();

            // 문서 키에 언어 정보 포함
            const docKey = `${roomId}-${language}`;
            this.doc = new yorkie.Document(docKey);

            // presence 정보를 포함하여 attach
            await this.client.attach(this.doc, {
                initialPresence: {
                    clientId: this.client.getID(),
                    userName: `User ${Math.floor(Math.random() * 1000)}`, // 임시 사용자 이름
                    color: '#' + Math.floor(Math.random()*16777215).toString(16) // 임시 색상
                }
            });

            return {
                clientId: this.client.getID(),
                docId: this.doc.getKey()
            };
        } catch (error) {
            console.error('Yorkie initialization error:', error);
            throw error;
        }
    }

    onDocumentChange(callback) {
        if (this.doc) {
            this.doc.subscribe(callback);
        }
    }

    onPresenceChange(callback) {
        if (this.doc) {
            this.doc.subscribe('presence', (event) => {
                if (event.type !== 'presence-changed') {
                    callback(this.getPresences());
                }
            });
        }
    }

    updateDocument(content) {
        if (this.doc) {
            this.doc.update((root) => {
                if (!root.content) {
                    root.content = new yorkie.Text();
                }
                root.content.edit(0, root.content.length, content);
            });
        }
    }

    getPresences() {
        if (!this.doc) return [];
        const presences = this.doc.getPresences();
        return Array.from(presences).map(presence => ({
            clientId: presence.clientID,
            userName: presence.presence.userName || presence.clientID,
            color: presence.presence.color || '#000000'
        }));
    }

    async cleanup() {
        if (this.doc && this.client) {
            await this.client.detach(this.doc);
            await this.client.deactivate();
        }
    }

}