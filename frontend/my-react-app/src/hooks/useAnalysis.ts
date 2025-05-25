import { useQuery } from '@tanstack/react-query';
import { getAnalysis, type AnalysisResult } from '../api/analysis';

export function useAnalysis(id: string | null) {
    return useQuery<AnalysisResult>({
        queryKey: ['analysis', id],
        queryFn: () => getAnalysis(id as string),
        enabled: !!id,           // запрос выключен, пока id === null
        retry: 1,
        staleTime: 60_000,       // минута кеша
    });
}
