import { useCallback, useState } from 'react';
import { fetchWordCloudPng } from '../api/analysis';

export function useWordCloudPng() {
    const [src, setSrc] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    const load = useCallback(async (id: string) => {
        setLoading(true);
        try {
            const url = await fetchWordCloudPng(id);
            setSrc(url);
        } finally {
            setLoading(false);
        }
    }, []);

    return { src, loading, load };
}