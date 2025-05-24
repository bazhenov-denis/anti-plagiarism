// The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work.

import React, { useState } from 'react';
import * as echarts from 'echarts';

interface File {
    id: number;
    name: string;
    date: string;
    status: 'Не проанализировано' | 'Проанализировано';
    analyzed: boolean;
}

const App: React.FC = () => {
    const [files, setFiles] = useState<File[]>([
        { id: 1, name: 'Документ1.txt', date: '22.05.2025 10:30', status: 'Не проанализировано', analyzed: false },
        { id: 2, name: 'Курсовая.txt', date: '21.05.2025 15:45', status: 'Проанализировано', analyzed: true },
        { id: 3, name: 'Диссертация.txt', date: '20.05.2025 09:15', status: 'Проанализировано', analyzed: true }
    ]);

    const [showDuplicateAlert, setShowDuplicateAlert] = useState(false);
    const [duplicateFileName, setDuplicateFileName] = useState('');
    const [showAnalysisModal, setShowAnalysisModal] = useState(false);
    const [showWordCloudModal, setShowWordCloudModal] = useState(false);
    const [currentFileId, setCurrentFileId] = useState<number | null>(null);
    const [isLoading, setIsLoading] = useState(false);

    const handleFileUpload = () => {
        // Симуляция загрузки файла
        const newFileName = 'Новый документ.txt';

        if (files.some(file => file.name === newFileName)) {
            setDuplicateFileName(newFileName);
            setShowDuplicateAlert(true);
        } else {
            const newFile: File = {
                id: files.length + 1,
                name: newFileName,
                date: '22.05.2025 12:00',
                status: 'Не проанализировано',
                analyzed: false
            };

            setFiles([...files, newFile]);
        }
    };

    const handleOverride = () => {
        const updatedFiles = files.map(file =>
            file.name === duplicateFileName
                ? { ...file, date: '22.05.2025 12:00', status: 'Не проанализировано', analyzed: false }
                : file
        );

        setFiles(updatedFiles);
        setShowDuplicateAlert(false);
    };

    const handleRename = () => {
        const newName = `${duplicateFileName.split('.')[0]} (1).txt`;

        const newFile: File = {
            id: files.length + 1,
            name: newName,
            date: '22.05.2025 12:00',
            status: 'Не проанализировано',
            analyzed: false
        };

        setFiles([...files, newFile]);
        setShowDuplicateAlert(false);
    };

    const handleDelete = (id: number) => {
        setFiles(files.filter(file => file.id !== id));
    };

    const handleAnalyze = (id: number) => {
        setCurrentFileId(id);
        setShowAnalysisModal(true);

        const file = files.find(f => f.id === id);

        if (file && !file.analyzed) {
            setIsLoading(true);

            // Симуляция анализа
            setTimeout(() => {
                setIsLoading(false);
                setFiles(files.map(f =>
                    f.id === id
                        ? { ...f, status: 'Проанализировано', analyzed: true }
                        : f
                ));
            }, 2000);
        }
    };

    const handleWordCloud = (id: number) => {
        setCurrentFileId(id);
        setShowWordCloudModal(true);
        setIsLoading(true);

        // Симуляция загрузки облака слов
        setTimeout(() => {
            setIsLoading(false);

            // Инициализация облака слов с помощью echarts
            const chartDom = document.getElementById('wordcloud-chart');
            if (chartDom) {
                const myChart = echarts.init(chartDom);
                const option = {
                    animation: false,
                    series: [{
                        type: 'wordCloud',
                        shape: 'circle',
                        left: 'center',
                        top: 'center',
                        width: '90%',
                        height: '90%',
                        right: null,
                        bottom: null,
                        sizeRange: [12, 60],
                        rotationRange: [-90, 90],
                        rotationStep: 45,
                        gridSize: 8,
                        drawOutOfBound: false,
                        textStyle: {
                            fontFamily: 'sans-serif',
                            fontWeight: 'bold',
                            color: function () {
                                return 'rgb(' + [
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160)
                                ].join(',') + ')';
                            }
                        },
                        emphasis: {
                            focus: 'self',
                            textStyle: {
                                shadowBlur: 10,
                                shadowColor: '#333'
                            }
                        },
                        data: [
                            { name: 'анализ', value: 100 },
                            { name: 'текст', value: 85 },
                            { name: 'документ', value: 70 },
                            { name: 'плагиат', value: 65 },
                            { name: 'проверка', value: 60 },
                            { name: 'система', value: 55 },
                            { name: 'работа', value: 50 },
                            { name: 'исследование', value: 45 },
                            { name: 'метод', value: 40 },
                            { name: 'результат', value: 38 },
                            { name: 'данные', value: 36 },
                            { name: 'информация', value: 34 },
                            { name: 'контент', value: 32 },
                            { name: 'источник', value: 30 },
                            { name: 'алгоритм', value: 28 },
                            { name: 'сравнение', value: 26 },
                            { name: 'оригинальность', value: 24 },
                            { name: 'процент', value: 22 },
                            { name: 'совпадение', value: 20 },
                            { name: 'отчет', value: 18 }
                        ]
                    }]
                };

                option && myChart.setOption(option);
            }
        }, 1500);
    };

    const currentFile = currentFileId ? files.find(f => f.id === currentFileId) : null;

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Header */}
            <header className="h-16 bg-white shadow-sm flex items-center justify-between px-6">
                <div className="flex items-center">
                    <h1 className="text-2xl font-semibold text-gray-800">Anti-Plagiarism</h1>
                </div>
                <div className="flex items-center">
                    <button className="w-10 h-10 rounded-full bg-blue-50 text-blue-600 flex items-center justify-center cursor-pointer hover:bg-blue-100 transition-colors">
                        <i className="fas fa-question"></i>
                    </button>
                </div>
            </header>

            {/* Main Content */}
            <main className="container mx-auto px-6 py-8">
                <div className="flex items-center mb-6 relative">
                    <button
                        onClick={handleFileUpload}
                        className="!rounded-button whitespace-nowrap h-14 px-6 bg-blue-600 text-white font-medium flex items-center justify-center shadow-md hover:shadow-lg hover:bg-blue-700 transition-all transform hover:scale-[1.02] cursor-pointer"
                    >
                        <i className="fas fa-upload mr-3"></i>
                        Загрузить файл
                    </button>

                    {/* Duplicate Alert */}
                    {showDuplicateAlert && (
                        <div className="ml-4 bg-yellow-50 border-l-4 border-yellow-400 p-4 flex items-center">
                            <div className="flex-1">
                                <p className="text-sm text-yellow-700">
                                    Файл с именем "{duplicateFileName}" уже существует.
                                </p>
                            </div>
                            <div className="flex space-x-2">
                                <button
                                    onClick={handleOverride}
                                    className="!rounded-button whitespace-nowrap px-3 py-1 bg-yellow-600 text-white text-sm cursor-pointer hover:bg-yellow-700"
                                >
                                    Заменить
                                </button>
                                <button
                                    onClick={handleRename}
                                    className="!rounded-button whitespace-nowrap px-3 py-1 bg-gray-200 text-gray-700 text-sm cursor-pointer hover:bg-gray-300"
                                >
                                    Переименовать
                                </button>
                            </div>
                        </div>
                    )}
                </div>

                {/* Files Table */}
                <div className="bg-white rounded-lg shadow-md overflow-hidden">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                        <tr>
                            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Имя файла
                            </th>
                            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Дата загрузки
                            </th>
                            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Статус
                            </th>
                            <th scope="col" className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Действия
                            </th>
                        </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                        {files.map((file, index) => (
                            <tr key={file.id} className={index % 2 === 0 ? 'bg-white' : 'bg-gray-50'}>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    <div className="flex items-center">
                                        <i className="fas fa-file-alt text-gray-400 mr-3"></i>
                                        <div className="text-sm font-medium text-gray-900">{file.name}</div>
                                    </div>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    <div className="text-sm text-gray-500">{file.date}</div>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                        file.status === 'Проанализировано'
                            ? 'bg-green-100 text-green-800'
                            : 'bg-gray-100 text-gray-800'
                    }`}>
                      {file.status}
                    </span>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                    <div className="flex justify-end space-x-2">
                                        <button
                                            onClick={() => handleAnalyze(file.id)}
                                            className="!rounded-button whitespace-nowrap w-8 h-8 bg-blue-50 text-blue-600 rounded-full flex items-center justify-center cursor-pointer hover:bg-blue-100 transition-colors"
                                            title="Анализировать и создать отчет"
                                        >
                                            <i className="fas fa-chart-bar"></i>
                                        </button>
                                        <button
                                            onClick={() => handleWordCloud(file.id)}
                                            className="!rounded-button whitespace-nowrap w-8 h-8 bg-green-50 text-green-600 rounded-full flex items-center justify-center cursor-pointer hover:bg-green-100 transition-colors"
                                            title="Облако слов"
                                        >
                                            <i className="fas fa-cloud"></i>
                                        </button>
                                        <button
                                            onClick={() => handleDelete(file.id)}
                                            className="!rounded-button whitespace-nowrap w-8 h-8 bg-red-50 text-red-600 rounded-full flex items-center justify-center cursor-pointer hover:bg-red-100 transition-colors"
                                            title="Удалить"
                                        >
                                            <i className="fas fa-trash-alt"></i>
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </main>

            {/* Analysis Modal */}
            {showAnalysisModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg shadow-xl w-4/5 max-w-4xl max-h-[90vh] overflow-auto">
                        <div className="p-6 border-b border-gray-200 flex justify-between items-center">
                            <h3 className="text-lg font-semibold text-gray-900">
                                Анализ: {currentFile?.name}
                            </h3>
                            <button
                                onClick={() => setShowAnalysisModal(false)}
                                className="!rounded-button whitespace-nowrap text-gray-400 hover:text-gray-500 cursor-pointer"
                            >
                                <i className="fas fa-times"></i>
                            </button>
                        </div>

                        <div className="p-6">
                            {isLoading ? (
                                <div className="flex flex-col items-center justify-center py-12">
                                    <div className="w-12 h-12 border-4 border-blue-200 border-t-blue-600 rounded-full animate-spin"></div>
                                    <p className="mt-4 text-gray-600">Анализ документа...</p>
                                </div>
                            ) : (
                                <>
                                    <div className="grid grid-cols-3 gap-6 mb-8">
                                        <div className="bg-gray-50 rounded-lg p-4 text-center">
                                            <p className="text-sm text-gray-500 mb-1">Абзацев</p>
                                            <p className="text-2xl font-bold text-gray-800">24</p>
                                        </div>
                                        <div className="bg-gray-50 rounded-lg p-4 text-center">
                                            <p className="text-sm text-gray-500 mb-1">Слов</p>
                                            <p className="text-2xl font-bold text-gray-800">1,245</p>
                                        </div>
                                        <div className="bg-gray-50 rounded-lg p-4 text-center">
                                            <p className="text-sm text-gray-500 mb-1">Символов</p>
                                            <p className="text-2xl font-bold text-gray-800">8,732</p>
                                        </div>
                                    </div>

                                    <div className="text-center mb-8">
                                        <p className="text-sm text-gray-500 mb-1">Общий процент совпадения</p>
                                        <p className="text-5xl font-bold text-red-600">32%</p>
                                    </div>

                                    <h4 className="text-lg font-medium text-gray-800 mb-4">Совпадения с другими документами</h4>

                                    <div className="border rounded-lg overflow-hidden">
                                        <table className="min-w-full divide-y divide-gray-200">
                                            <thead className="bg-gray-50">
                                            <tr>
                                                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                    Документ
                                                </th>
                                                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                    Совпадение
                                                </th>
                                                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                    Фрагмент
                                                </th>
                                            </tr>
                                            </thead>
                                            <tbody className="bg-white divide-y divide-gray-200">
                                            <tr>
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <div className="text-sm font-medium text-gray-900">Документ1.txt</div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <div className="text-sm text-red-600 font-semibold">24%</div>
                                                </td>
                                                <td className="px-6 py-4">
                                                    <div className="text-sm text-gray-900">
                                                        <p className="line-clamp-2">Анализ текста на предмет плагиата является важным этапом проверки оригинальности работы...</p>
                                                        <button className="mt-1 text-blue-600 text-xs hover:underline cursor-pointer">
                                                            Показать полностью
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr className="bg-gray-50">
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <div className="text-sm font-medium text-gray-900">Курсовая.txt</div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <div className="text-sm text-orange-600 font-semibold">18%</div>
                                                </td>
                                                <td className="px-6 py-4">
                                                    <div className="text-sm text-gray-900">
                                                        <p className="line-clamp-2">Методология исследования основана на сравнительном анализе текстовых фрагментов с использованием алгоритмов...</p>
                                                        <button className="mt-1 text-blue-600 text-xs hover:underline cursor-pointer">
                                                            Показать полностью
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </>
                            )}
                        </div>
                    </div>
                </div>
            )}

            {/* Word Cloud Modal */}
            {showWordCloudModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg shadow-xl w-3/5 max-w-3xl">
                        <div className="p-6 border-b border-gray-200 flex justify-between items-center">
                            <h3 className="text-lg font-semibold text-gray-900">
                                Облако слов: {currentFile?.name}
                            </h3>
                            <button
                                onClick={() => setShowWordCloudModal(false)}
                                className="!rounded-button whitespace-nowrap text-gray-400 hover:text-gray-500 cursor-pointer"
                            >
                                <i className="fas fa-times"></i>
                            </button>
                        </div>

                        <div className="p-6">
                            {isLoading ? (
                                <div className="flex flex-col items-center justify-center py-12">
                                    <div className="w-12 h-12 border-4 border-green-200 border-t-green-600 rounded-full animate-spin"></div>
                                    <p className="mt-4 text-gray-600">Генерация облака слов...</p>
                                </div>
                            ) : (
                                <div className="h-[400px] w-full" id="wordcloud-chart"></div>
                            )}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default App;
