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

export async function downloadFile(id: string, originalName: string) {
    const resp = await fetch(`/files/${id}`);
    if (!resp.ok) throw new Error(`Download ${id} — ${resp.status}`);
    const blob = await resp.blob();
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = originalName;
    document.body.appendChild(link);
    link.click();
    link.remove();
    URL.revokeObjectURL(url);
}

export default base;