import { useState } from 'react';
import { useFiles } from './hooks/useFiles.ts';
import FileUploadButton from "./component/FileUploadButton";
import DuplicateAlert from './component/DuplicateAlert';
import FilesTable from './component/FilesTable/FilesTable';
import WordCloudModal from './component/WordCloudModal';
import AnalysisModal from "./component/AnalysisModal.tsx";

export default function App() {
    const { list, upload, remove } = useFiles();
    const files = list.data ?? [];

    /* ==== состояние UI ==== */
    const [duplicate, setDuplicate] = useState<string | null>(null);
    const [analysisId, setAnalysisId] = useState<string | null>(null);
    const [cloudId, setCloudId] = useState<string | null>(null);

    /* ==== события ==== */
    const handleUpload = (fl: FileList) => {
        const file = fl[0];
        if (!file) return;

        // проверяем дубликат по имени
        if (files.some((f) => f.originalName === file.name)) {
            setDuplicate(file.name);
        } else {
            upload.mutate(file);
        }
    };

    const handleOverride = () => {
        if (!duplicate) return;
        const inputEl = document.querySelector('input[type=file]') as HTMLInputElement | null;
        const file = inputEl?.files?.[0];
        if (file) upload.mutate(file); // сервер перезапишет
        setDuplicate(null);
    };

    const handleRename = () => {
        if (!duplicate) return;
        const inputEl = document.querySelector('input[type=file]') as HTMLInputElement | null;
        const file = inputEl?.files?.[0];
        if (!file) return;
        const ext = file.name.substring(file.name.lastIndexOf('.'));
        const renamed = new File([file], `${file.name.replace(ext, '')} (1)${ext}`, {
            type: file.type,
        });
        upload.mutate(renamed);
        setDuplicate(null);
    };

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Header */}
            <header className="h-16 bg-white shadow-sm flex items-center justify-between px-6">
                <h1 className="text-2xl font-semibold">Anti-Plagiarism</h1>
            </header>

            {/* Main */}
            <main className="container mx-auto px-6 py-8">
                <div className="flex items-center mb-6">
                    <FileUploadButton onUpload={handleUpload} />
                    {duplicate && (
                        <DuplicateAlert
                            filename={duplicate}
                            onOverride={handleOverride}
                            onRename={handleRename}
                        />
                    )}
                </div>

                <FilesTable
                    files={files}
                    onAnalyze={(id) => setAnalysisId(id)}
                    onWordCloud={(id) => setCloudId(id)}
                    onDelete={(id) => remove.mutate(id)}
                />
            </main>

            {/* Модальные окна */}
            <AnalysisModal
                file={files.find((f) => f.id === analysisId) ?? null}
                loading={false /* TODO: подвяжите к запросу анализа */}
                onClose={() => setAnalysisId(null)}
            />

            <WordCloudModal
                file={files.find((f) => f.id === cloudId) ?? null}
                loading={false /* TODO */}
                onClose={() => setCloudId(null)}
                data={[]} // результат API /analysis/{id}/wordcloud
            />
        </div>
    );
}
