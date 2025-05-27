import base from './files'; // axios instance for JSON API

export interface Similarity { fileId: string; score: number; }
export interface FileStats { words: number; paragraphs: number; chars: number; }
export interface AnalysisResult { stats: FileStats; similarity: Similarity[]; }

export async function getAnalysis(id: string): Promise<AnalysisResult> {
    const res = await base.get<AnalysisResult>(`/analysis/${id}`);
    return res.data;
}

export type WordCloudItem = { name: string; value: number };

export async function fetchWordCloudPng(id: string): Promise<string> {
    const resp = await fetch(`/analysis/${id}/wordcloud`);
    if (!resp.ok) throw new Error(`Word-cloud ${id} — ${resp.status}`);
    const blob = await resp.blob();
    return URL.createObjectURL(blob);
}