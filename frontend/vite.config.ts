import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), vueDevTools()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    port: 5173,
    proxy: {
      '/user': {
        target: 'http://localhost:8123',
        changeOrigin: true,
        rewrite: (path) => `/api${path}`,
      },
      '/project': {
        target: 'http://localhost:8123',
        changeOrigin: true,
        rewrite: (path) => `/api${path}`,
      },
      '/heath': {
        target: 'http://localhost:8123',
        changeOrigin: true,
        rewrite: (path) => path,
      },
    },
  },
})
