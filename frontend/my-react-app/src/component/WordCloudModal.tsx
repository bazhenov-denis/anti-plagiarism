// src/components/WordCloudModal.tsx
import { useEffect } from 'react';
import * as echarts from 'echarts';
import type { FileInfo } from '../types/FileInfo';

interface Props {
    file: FileInfo | null;
    loading: boolean;
    onClose(): void;
    data: { name: string; value: number }[];
}
export default function WordCloudModal({ file, loading, data }: Props) {
    useEffect(() => {
        if (!loading && file) {
            const dom = document.getElementById('wordcloud-chart');
            if (dom) {
                echarts.dispose(dom); // очищаем старый экземпляр
                const chart = echarts.init(dom);
                chart.setOption({
                    series: [{ type: 'wordCloud', data }],
                });
            }
        }
    }, [loading, file, data]);

    if (!file) return null;
    return (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
            {/* аналогично контент */}
        </div>
    );
}
