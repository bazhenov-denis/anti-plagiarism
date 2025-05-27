import { useEffect, useState } from 'react';
import '@fortawesome/fontawesome-free/css/all.min.css';

import { useFiles } from './hooks/useFiles';
import { useAnalysis } from './hooks/useAnalysis';
import { useWordCloudPng } from './hooks/useWordCloud';

import FileUploadButton from './component/FileUploadButton';
import DuplicateAlert from './component/DuplicateAlert';
import FilesTable from './component/FilesTable/FilesTable';
import AnalysisModal from './component/AnalysisModal';
import WordCloudModal from './component/WordCloudModal';
import {downloadFile} from "./api/files.ts";

export default function App() {
    const { list, upload, remove } = useFiles();
    const files = list.data ?? [];

    const [duplicate, setDuplicate] = useState<string | null>(null);
    const [analysisId, setAnalysisId] = useState<string | null>(null);
    const [cloudId, setCloudId] = useState<string | null>(null);


    const handleDownload = (id: string) => {
        const file = files.find((f) => f.id === id);
        if (file) downloadFile(id, file.originalName);
    };

    const { data: analysis, isLoading: loadingAnalysis } = useAnalysis(analysisId);
    const { src: cloudSrc, loading: loadingCloud, load: loadCloud } = useWordCloudPng();

    // При смене cloudId загружаем PNG
    useEffect(() => {
        if (cloudId) {
            loadCloud(cloudId);
        }
    }, [cloudId, loadCloud]);

    const handleUpload = (fl: FileList) => {
        const file = fl[0];
        if (!file) return;
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
        if (file) upload.mutate(file);
        setDuplicate(null);
    };

    const handleRename = () => {
        if (!duplicate) return;
        const inputEl = document.querySelector('input[type=file]') as HTMLInputElement | null;
        const file = inputEl?.files?.[0];
        if (!file) return;
        const ext = file.name.substring(file.name.lastIndexOf('.'));
        const renamed = new File([file], `${file.name.replace(ext, '')} (1)${ext}`, { type: file.type });
        upload.mutate(renamed);
        setDuplicate(null);
    };

    return (
        <div className="min-h-screen bg-gray-50">
            <header className="h-16 bg-white shadow-sm flex items-center justify-between px-6">
                <h1 className="text-2xl font-semibold">Антиплагиат ВШЭ</h1>
            </header>
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
                    onDownload={handleDownload}
                />
            </main>

            <AnalysisModal
                file={files.find((f) => f.id === analysisId) ?? null}
                analysis={analysis ?? null}
                loading={loadingAnalysis}
                onClose={() => setAnalysisId(null)}
            />

            <WordCloudModal
                file={files.find((f) => f.id === cloudId) ?? null}
                loading={loadingCloud}
                src={cloudSrc}
                onClose={() => setCloudId(null)}
            />
        </div>
    );
}