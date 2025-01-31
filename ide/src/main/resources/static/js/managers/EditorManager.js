// js/managers/EditorManager.js
import { SUPPORTED_LANGUAGES } from '../config/languages.js';

export class EditorManager {

    constructor(containerId) {
        this.editor = null;
        this.containerId = containerId;
        this.currentLanguage = 'javascript';
        this.isUpdating = false;
    }

    setupEditor(initialLanguage) {
        this.currentLanguage = initialLanguage;
        const defaultLang = SUPPORTED_LANGUAGES.find(lang => lang.id === initialLanguage);

        this.editor = monaco.editor.create(document.getElementById(this.containerId), {
            value: defaultLang.defaultContent,
            language: this.currentLanguage,
            theme: 'vs-dark',
            automaticLayout: true,
            minimap: { enabled: false },
            fontSize: 14,
            tabSize: 4,
            insertSpaces: true,
            formatOnPaste: true,
            formatOnType: true
        });
    }

    setupLanguageSupport() {
        SUPPORTED_LANGUAGES.forEach(lang => {
            monaco.languages.registerCompletionItemProvider(lang.id, {
                provideCompletionItems: (model, position) => {
                    return { suggestions: [] };
                }
            });
        });
    }

    setLanguage(languageId) {
        const langConfig = SUPPORTED_LANGUAGES.find(lang => lang.id === languageId);
        if (!langConfig) {
            throw new Error(`Unsupported language: ${languageId}`);
        }

        this.currentLanguage = languageId;

        // 현재 내용이 있는지 확인
        const currentContent = this.getValue().trim();

        // 모델 언어 변경
        const model = this.editor.getModel();
        monaco.editor.setModelLanguage(model, languageId);

        // 에디터가 비어있다면 해당 언어의 기본 내용으로 설정
        if (!currentContent) {
            this.setValue(langConfig.defaultContent);
        }

        // 언어별 설정 적용
        this.editor.updateOptions({
            tabSize: languageId === 'python' ? 4 : 2,
            insertSpaces: true
        });
    }

    async initialize(language) {
        return new Promise(resolve => {
            require.config({
                paths: {'vs': 'https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.52.0/min/vs'}
            });

            require(['vs/editor/editor.main'], () => {
                this.setupEditor(language);  // 선택된 언어 전달
                this.setupLanguageSupport();
                resolve();
            });
        });
    }

    getValue() {
        return this.editor.getValue();
    }

    setValue(content) {
        this.isUpdating = true;
        this.editor.setValue(content);
        this.isUpdating = false;
    }

    getPosition() {
        return this.editor.getPosition();
    }

    setPosition(position) {
        if (position) {
            this.editor.setPosition(position);
        }
    }

    onContentChange(callback) {
        return this.editor.onDidChangeModelContent((event) => {
            if (!this.isUpdating) {
                callback(event);
            }
        });
    }

}