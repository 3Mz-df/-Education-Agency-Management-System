/** 后端返回的 LocalDateTime 只调整显示，不擅自转换时区。 */
export function formatDateTime(value: string | null | undefined) {
  return value ? value.replace('T', ' ').slice(0, 19) : '—';
}

export function displayText(value: string | number | null | undefined) {
  return value === null || value === undefined || value === '' ? '—' : String(value);
}

export function userStatusText(status: number | null) {
  if (status === 1) return '启用';
  if (status === 0) return '禁用';
  return displayText(status);
}
