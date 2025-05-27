import type { AnalysisResult } from '../api/analysis';
import type { FileInfo } from '../types/FileInfo';

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
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
            <div className="relative bg-white rounded-lg p-6 w-[90vw] max-w-3xl">
                <button className="absolute top-2 right-2 text-xl" onClick={onClose}>✕</button>

                <h2 className="text-xl font-semibold mb-4">Анализ файла «{file.originalName}»</h2>

                {loading ? (
                    <p className="text-center py-20">Загрузка…</p>
                ) : (
                    <>
                        {stats && (
                            <div className="grid grid-cols-3 gap-4 mb-6 text-center">
                                <div className="bg-gray-50 p-4 rounded-lg">
                                    <p className="text-sm text-gray-500">Символов</p>
                                    <p className="text-2xl font-bold">{stats.chars}</p>
                                </div>
                                <div className="bg-gray-50 p-4 rounded-lg">
                                    <p className="text-sm text-gray-500">Слов</p>
                                    <p className="text-2xl font-bold">{stats.words}</p>
                                </div>
                                <div className="bg-gray-50 p-4 rounded-lg">
                                    <p className="text-sm text-gray-500">Параграфов</p>
                                    <p className="text-2xl font-bold">{stats.paragraphs}</p>
                                </div>
                            </div>
                        )}

                        <h3 className="font-medium mb-2">Похожие файлы</h3>
                        <div className="overflow-x-auto">
                            <table className="min-w-full divide-y divide-gray-200 text-sm">
                                <thead className="bg-gray-50">
                                <tr>
                                    <th className="px-4 py-2 text-left text-gray-500">ID файла</th>
                                    <th className="px-4 py-2 text-right text-gray-500">Score</th>
                                </tr>
                                </thead>
                                <tbody className="divide-y divide-gray-200">
                                {sim.length === 0 ? (
                                    <tr>
                                        <td colSpan={2} className="px-4 py-6 text-center text-gray-500">Нет похожих файлов</td>
                                    </tr>
                                ) : (
                                    sim.map((s) => (
                                        <tr key={s.fileId}>
                                            <td className="px-4 py-2">{s.fileId}</td>
                                            <td className="px-4 py-2 text-right">{s.score.toFixed(2)}</td>
                                        </tr>
                                    ))
                                )}
                                </tbody>
                            </table>
                        </div>
                    </>
                )}
            </div>
        </div>
    );
}