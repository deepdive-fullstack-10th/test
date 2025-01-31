// js/config/languages.js
export const SUPPORTED_LANGUAGES = [
    {
        id: 'javascript',
        name: 'JavaScript',
        extension: 'js',
        defaultContent: '// JavaScript code here\n\nfunction main() {\n\t\n}',
        mimeType: 'application/javascript'
    },
    {
        id: 'python',
        name: 'Python',
        extension: 'py',
        defaultContent: '# Python code here\n\ndef main():\n\tpass\n\nif __name__ == "__main__":\n\tmain()',
        mimeType: 'text/x-python'
    },
    {
        id: 'java',
        name: 'Java',
        extension: 'java',
        defaultContent: 'public class Main {\n\tpublic static void main(String[] args) {\n\t\t\n\t}\n}',
        mimeType: 'text/x-java-source'
    }
];