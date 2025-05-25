import type {ChangeEvent} from 'react';

interface Props {
    onUpload(files: FileList): void;
}
export default function FileUploadButton({ onUpload }: Props) {
    return (
        <label className="h-14 px-6 bg-blue-600 text-white flex items-center rounded-md cursor-pointer hover:bg-blue-700">
            <i className="fas fa-upload mr-3" />
            Загрузить файл
            <input
                type="file"
                className="hidden"
                onChange={(e: ChangeEvent<HTMLInputElement>) =>
                    e.target.files && onUpload(e.target.files)
                }
            />
        </label>
    );
}
