// js/config/store.js
class Store {

    constructor() {
        this.state = {
            currentRoom: null,
            currentUser: null,
            currentLanguage: null,
            userTabs: []
        };
        this.listeners = new Set();
    }

    subscribe(listener) {
        this.listeners.add(listener);
        return () => this.listeners.delete(listener);
    }

    setState(newState) {
        this.state = { ...this.state, ...newState };
        this.listeners.forEach(listener => listener(this.state));
    }

    getState() {
        return this.state;
    }

}

export const store = new Store();