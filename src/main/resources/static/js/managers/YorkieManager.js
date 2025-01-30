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

            const docKey = `${roomId}-${language}`;
            this.doc = new yorkie.Document(docKey);

            // presence 정보를 포함하여 attach
            await this.client.attach(this.doc, {
                initialPresence: {
                    clientId: this.client.getID(),
                    status: 'active'
                }
            });

            // 초기 presence 동기화를 위한 지연
            await new Promise(resolve => setTimeout(resolve, 100));

            // 문서 초기화
            this.doc.update((root) => {
                if (!root.content) {
                    root.content = new yorkie.Text();
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
        this.doc.subscribe(callback);
    }

    updateDocument(content) {
        this.doc.update((root) => {
            if (root.content) {
                root.content.edit(0, root.content.length, content);
            }
        });
    }

    getPresences() {
        const presences = this.doc.getPresences();
        console.log('Current presences:', presences); // 디버깅용
        return presences;
    }

    onPresenceChange(callback) {
        this.doc.subscribe('presence', (event) => {
            console.log('Presence event:', event); // 디버깅용
            callback(this.getPresences());
        });
    }

    async cleanup() {
        if (this.doc && this.client) {
            await this.client.detach(this.doc);
            await this.client.deactivate();
        }
    }

}