// src/hooks/useFiles.ts
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import * as api from '../api/files';

export function useFiles() {
    const qc = useQueryClient();

    const list = useQuery({
        queryKey: ['files'],
        queryFn: api.listFiles,
    });

    const upload = useMutation({
        mutationFn: api.uploadFile,
        onSuccess: () => qc.invalidateQueries({ queryKey: ['files'] }),
    });

    const remove = useMutation({
        mutationFn: api.deleteFile,
        onSuccess: () => qc.invalidateQueries({ queryKey: ['files'] }),
    });

    return { list, upload, remove };
}
