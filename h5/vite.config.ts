import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  base: "./",
  plugins: [react()],
  css: {
    modules: {
      // 开启 CSS Modules 并指定生成的类名格式
      localsConvention: "camelCase",
    },
  },
  build: {
    rollupOptions: {
      input: {
        main: "./index.html",
        serviceWorker: "./service-worker.js",
      },
    },
  },
});
