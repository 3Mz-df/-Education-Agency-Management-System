import { defineConfig } from 'vite-plus/test/config';

export default defineConfig({
  test: {
    include: ['src/service/request/eams.test.ts'],
    environment: 'node'
  }
});
