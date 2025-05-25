// src/main.tsx (Vite)
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './index.css';

import {
    QueryClient,
    QueryClientProvider,
} from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'; // опционально

// один общий клиент на всё приложение
const queryClient = new QueryClient();

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        {/*    ⬇️  оборачиваем App */}
        <QueryClientProvider client={queryClient}>
            <App />
            {/* DevTools можно убрать в проде */}
            <ReactQueryDevtools initialIsOpen={false} />
        </QueryClientProvider>
    </React.StrictMode>,
);
