import { writeFile } from 'node:fs/promises';

const base = process.env.TODOMARKET_API_BASE_URL || 'http://localhost:8184';
if (!['http:', 'https:'].includes(new URL(base).protocol)) {
  throw new Error('TODOMARKET_API_BASE_URL debe usar HTTP o HTTPS.');
}

await writeFile(new URL('../public/config.js', import.meta.url),
  'window.TODOMARKET_CONFIG = ' + JSON.stringify({ apiBaseUrl: base.replace(/\/+$/, '') }) + ';\n'
);
