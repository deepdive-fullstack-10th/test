// js/managers/UIManager.js
import { store } from '../config/store.js';

export class UIManager {
    constructor() {
        this.statusElement = document.getElementById('status');
        this.clientsElement = document.getElementById('clients');
        this.roomIdElement = document.getElementById('roomIdDisplay');
    }

    updateStatus(status) {
        if (this.statusElement) {
            this.statusElement.textContent = status;
        }
    }

    updateClients(clients) {
        if (this.clientsElement && Array.isArray(clients)) {
            this.clientsElement.innerHTML = clients.map(client => `
            <div class="client-item">
                <div class="client-color" style="background-color: ${client.color}"></div>
                <div class="client-info">
                    ${client.userName} (${client.clientId})
                </div>
            </div>
        `).join('');
        }
    }

    updateRoomInfo(roomId, language) {
        if (this.roomIdElement) {
            this.roomIdElement.textContent = `${roomId} (${language})`;
        }
    }

    showError(message) {
        this.updateStatus(`Error: ${message}`);
        console.error(message);
    }

}