import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    host: '0.0.0.0',
    port: 5173,
    strictPort: true,
    proxy: {
      // все запросы к /files будут проксироваться на ваш Spring API
      '/files': {
        target: 'http://api-gateway:8080',  // ваш API-gateway или конкретный сервис
        changeOrigin: true,
        // если у вас в бекенде путь не точно '/files', а, например, '/api/files', то:
        // rewrite: path => path.replace(/^\/files/, '/api/files')
      },
      // и аналогично для скачивания
      '/load': {
        target: 'http://api-gateway:8080',
        changeOrigin: true,
        // rewrite: path => path.replace(/^\/download/, '/api/download')
      }
    }
  }
})