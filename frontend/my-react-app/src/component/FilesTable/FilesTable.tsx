// src/component/FilesTable/FilesTable.tsx
import type { FileInfo } from '../../types/FileInfo';
import { RowActions } from './RowActions';

interface Props {
    files: FileInfo[];
    onAnalyze(id: string): void;
    onWordCloud(id: string): void;
    onDelete(id: string): void;
}

export default function FilesTable({
                                       files,
                                       onAnalyze,
                                       onWordCloud,
                                       onDelete,
                                   }: Props) {
    return (
        <div className="bg-white rounded-lg shadow-md overflow-hidden">
            <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                <tr>
                    <th className="px-6 py-3 text-left text-xs text-gray-500">
                        Имя файла
                    </th>
                    <th className="px-6 py-3 text-right text-xs text-gray-500">
                        Действия
                    </th>
                </tr>
                </thead>

                <tbody className="divide-y divide-gray-200">
                {files.map((f, i) => (
                    <tr key={f.id} className={i % 2 ? 'bg-gray-50' : ''}>
                        <td className="px-6 py-4">{f.originalName}</td>
                        <td className="px-6 py-4">
                            <RowActions
                                onAnalyze={() => onAnalyze(f.id)}
                                onWordCloud={() => onWordCloud(f.id)}
                                onDelete={() => onDelete(f.id)}
                            />
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
