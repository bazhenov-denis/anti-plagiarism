import type { FileInfo } from '../types/FileInfo';
import type { AnalysisResult } from '../api/analysis';

interface Props {
    file: FileInfo | null;
    analysis: AnalysisResult | null;
    loading: boolean;
    onClose(): void;
}

export default function AnalysisModal({ file, analysis, loading, onClose }: Props) {
    if (!file) return null;

    const stats = analysis?.stats;
    const sim   = analysis?.similarity ?? [];

    return (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg shadow-xl w-4/5 max-w-4xl max-h-[90vh] overflow-auto relative">
                {/* close btn */}
                <button
                    onClick={onClose}
                    className="absolute top-4 right-4 text-gray-400 hover:text-gray-600"
                >
                    <i className="fas fa-times" />
                </button>

                {/* title */}
                <h2 className="text-xl font-semibold px-8 pt-8">{file.originalName}</h2>

                {/* body */}
                {loading && (
                    <div className="flex flex-col items-center justify-center py-20">
                        <div className="w-12 h-12 border-4 border-blue-200 border-t-blue-600 rounded-full animate-spin" />
                        <p className="mt-4 text-gray-600">Анализируем…</p>
                    </div>
                )}

                {!loading && analysis && (
                    <div className="p-8 space-y-6">
                        {/* --- статистика --- */}
                        <div className="grid grid-cols-3 gap-4 text-center">
                            <Stat label="Слова" value={stats?.words} />
                            <Stat label="Абзацы" value={stats?.paragraphs} />
                            <Stat label="Символы" value={stats?.chars} />
                        </div>

                        {/* --- таблица похожих --- */}
                        <div>
                            <h3 className="text-lg font-medium mb-2">Похожие документы</h3>
                            {sim.length === 0 ? (
                                <p className="text-gray-500">Совпадений не найдено.</p>
                            ) : (
                                <table className="min-w-full divide-y divide-gray-200">
                                    <thead className="bg-gray-50">
                                    <tr>
                                        <th className="px-4 py-2 text-left text-xs text-gray-500">ID</th>
                                        <th className="px-4 py-2 text-right text-xs text-gray-500">Совпадение, %</th>
                                    </tr>
                                    </thead>
                                    <tbody className="divide-y divide-gray-200">
                                    {sim.map((s) => (
                                        <tr key={s.fileId}>
                                            <td className="px-4 py-2">{s.fileId}</td>
                                            <td className="px-4 py-2 text-right">
                                                {(s.score * 100).toFixed(1)}
                                            </td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            )}
                        </div>
                    </div>
                )}

                {!loading && !analysis && (
                    <p className="p-8 text-red-600">Не удалось получить результат анализа.</p>
                )}
            </div>
        </div>
    );
}

/* маленький подкомпонент карточки статистики */
function Stat({ label, value }: { label: string; value?: number }) {
    return (
        <div className="bg-gray-100 rounded-lg py-4">
            <div className="text-2xl font-bold">{value ?? '—'}</div>
            <div className="text-xs text-gray-500">{label}</div>
        </div>
    );
}
