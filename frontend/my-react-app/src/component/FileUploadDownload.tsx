import React, { useState, useEffect, type ChangeEvent, type FormEvent } from 'react';
import axios from 'axios';
import './FileUploadDownload.css';

const FileUploadDownload: React.FC = () => {
    const [selectedFile, setSelectedFile] = useState<File | null>(null);
    const [files, setFiles] = useState<string[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetchFiles();
    }, []);

    const fetchFiles = async (): Promise<void> => {
        try {
            const response = await axios.get<string[] | { files: string[] }>('/files');
            const data = response.data;
            if (Array.isArray(data)) {
                setFiles(data);
            } else if ('files' in data && Array.isArray(data.files)) {
                setFiles(data.files);
            } else {
                console.error('Unexpected files response:', data);
                setFiles([]);
                setError('Failed to load file list.');
            }
        } catch (err) {
            console.error('Error fetching files:', err);
            setError('Error fetching files.');
        }
    };

    const handleFileChange = (event: ChangeEvent<HTMLInputElement>): void => {
        const fileList = event.target.files;
        setSelectedFile(fileList && fileList.length > 0 ? fileList[0] : null);
    };

    const handleUpload = async (event: FormEvent<HTMLFormElement>): Promise<void> => {
        event.preventDefault();
        if (!selectedFile) return;
        const formData = new FormData();
        formData.append('file', selectedFile);

        try {
            await axios.post('/files', formData);
            setSelectedFile(null);
            await fetchFiles();
        } catch (err) {
            console.error('Error uploading file:', err);
            setError('Error uploading file.');
        }
    };

    return (
        <div>
            <header className="files">
                <h3 className="title">Загрузка файла</h3>
            </header>
            <section className="formdata">
                <form className="upload-form" onSubmit={handleUpload}>
                    <label htmlFor="file" className="upload-label">
                        Выберите файл
                    </label>
                    <input
                        type="file"
                        id="file"
                        className="file-input"
                        onChange={handleFileChange}
                        required
                    />
                    <button type="submit" className="upload-button">
                        Загрузить
                    </button>
                </form>
            </section>

            <section className="tabledata">
                {error && <div className="error-message">{error}</div>}
                <table className="file-table">
                    <thead>
                    <tr>
                        <th>File</th>
                        <th>Download</th>
                    </tr>
                    </thead>
                    <tbody>
                    {files.length > 0 ? (
                        files.map((fileName) => (
                            <tr key={fileName}>
                                <td>{fileName}</td>
                                <td>
                                    <a href={`files/load/${fileName}`} className="download-button" download>
                                        ↓
                                    </a>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan={2}>No files available</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </section>
        </div>
    );
};

export default FileUploadDownload;