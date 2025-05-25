interface Props {
    filename: string;
    onOverride(): void;
    onRename(): void;
}
export default function DuplicateAlert({ filename, onOverride, onRename }: Props) {
    return (
        <div className="ml-4 bg-yellow-50 border-l-4 border-yellow-400 p-4 flex items-center">
            <p className="text-sm flex-1">Файл «{filename}» уже существует.</p>
            <button onClick={onOverride} className="px-3 py-1 bg-yellow-600 text-white rounded mr-2">
                Заменить
            </button>
            <button onClick={onRename} className="px-3 py-1 bg-gray-200 rounded">
                Переименовать
            </button>
        </div>
    );
}
