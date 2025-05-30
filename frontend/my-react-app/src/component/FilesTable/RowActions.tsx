// src/component/FilesTable/RowActions.tsx
interface Props {
    onAnalyze(): void;
    onWordCloud(): void;
    onDelete(): void;
    onDownload(): void;
}

export function RowActions({ onAnalyze, onWordCloud, onDelete, onDownload }: Props) {
    return (
        <div className="flex space-x-2 justify-end">
            <button
                className="w-8 h-8 bg-blue-50 rounded-full"
                onClick={onAnalyze}
                title="Анализировать"
            >
                <i className="fas fa-chart-bar text-blue-600"/>
            </button>

            <button
                className="w-8 h-8 bg-green-50 rounded-full"
                onClick={onWordCloud}
                title="Облако слов"
            >
                <i className="fas fa-cloud text-green-600"/>
            </button>

            <button
                className="w-8 h-8 bg-emerald-50 rounded-full"
                onClick={onDownload}
                title="Скачать"
            >
                <i className="fas fa-download text-emerald-600"/>
            </button>

            <button
                className="w-8 h-8 bg-red-50 rounded-full"
                onClick={onDelete}
                title="Удалить"
            >
                <i className="fas fa-trash-alt text-red-600"/>
            </button>
        </div>
    );
}
