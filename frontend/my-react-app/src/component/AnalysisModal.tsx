import type { FileInfo } from '../types/FileInfo';

interface Props {
    file: FileInfo | null;
    loading: boolean;
    onClose(): void;
    // сюда же можно передать данные анализа
}
export default function AnalysisModal({ file, loading, onClose }: Props) {
    if (!file) return null;

    return (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg shadow-xl w-4/5 max-w-4xl max-h-[90vh] overflow-auto relative">
                <button
                    onClick={onClose}
                    className="absolute top-4 right-4 text-gray-400 hover:text-gray-600"
                >
                    <i className="fas fa-times" />
                </button>

                {loading ? (
                    <div className="flex flex-col items-center justify-center py-20">
                        <div className="w-12 h-12 border-4 border-blue-200 border-t-blue-600 rounded-full animate-spin" />
                        <p className="mt-4 text-gray-600">Анализ документа…</p>
                    </div>
                ) : (
                    <div>результат анализа</div>
                )}
            </div>
        </div>
    );
}
