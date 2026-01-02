import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import path from "path";

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "src"),
    },
  },
  // --- 新增以下 server 配置 ---
  server: {
    port: 5173, // 前端端口（默认就是这个，可以不写）
    proxy: {
      "/api": {
        target: "http://localhost:8080", // 后端服务地址
        changeOrigin: true, // 允许跨域
        // 如果后端接口本身就包含 /api 前缀（看你的Controller代码是包含的），则不需要 rewrite
        // rewrite: (path) => path.replace(/^\/api/, ""), 
      },
    },
  },
});