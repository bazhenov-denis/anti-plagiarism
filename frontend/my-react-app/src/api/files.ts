// src/api/files.ts
import axios from 'axios';
import type { FileInfo } from '../types/FileInfo';

const base = axios.create({
    baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8080',
});

export async function listFiles(): Promise<FileInfo[]> {
    const r = await base.get<FileInfo[]>('/files');
    return r.data;
}

export async function uploadFile(file: File): Promise<FileInfo> {
    const data = new FormData();
    data.append('file', file);
    const r = await base.post<FileInfo>('/files', data, {
        headers: { 'Content-Type': 'multipart/form-data' },
    });
    return r.data;
}

export async function deleteFile(id: string): Promise<void> {
    await base.delete(`/files/${id}`);
}

export function downloadUrl(id: string): string {
    // <a href={downloadUrl(id)} download />
    return `${base.defaults.baseURL}/files/${id}`;
}
export default base;