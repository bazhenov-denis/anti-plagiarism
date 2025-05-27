import type { FileInfo } from '../types/FileInfo';

interface Props {
    file: FileInfo | null;
    loading: boolean;
    src: string | null;
    onClose(): void;
}

export default function WordCloudModal({ file, loading, src, onClose }: Props) {
    if (!file) return null;

    return (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
            <div className="relative bg-white rounded-lg p-4 max-w-[80vw] max-h-[80vh]">
                <button
                    className="absolute top-2 right-2 text-xl"
                    onClick={onClose}
                >
                    ✕
                </button>
                {loading ? (
                    <p className="text-center mt-20">Загрузка…</p>
                ) : (
                    <img
                        src={src ?? ''}
                        alt="Word cloud"
                        className="w-full h-full object-contain"
                    />
                )}
            </div>
        </div>
    );
}