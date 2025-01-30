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
        if (this.clientsElement) {
            this.clientsElement.innerHTML = '연결된 클라이언트: ' + clients.join(', ');
        }
    }

    updateRoomInfo(roomId, language) {
        if (this.roomIdElement) {
            this.roomIdElement.textContent = `${roomId} (${language})`;
        }
    }

}