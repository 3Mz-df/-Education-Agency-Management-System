import { create, isAxiosError } from 'axios';
import type { AxiosRequestConfig } from 'axios';

interface EamsResponse<T> {
  code: number;
  message: string;
  data: T;
  timestamp?: number;
}

export type RequestResult<T> = { data: T; error: null } | { data: null; error: string };

interface ClientOptions {
  baseURL: string;
  getToken?: () => string;
  onError?: (message: string) => void;
  onUnauthorized?: () => void | Promise<void>;
}

/** 将 EAMS 的响应协议统一转换成页面使用的结果。 */
export function createEamsClient(options: ClientOptions) {
  const instance = create({ baseURL: options.baseURL, timeout: 10000 });

  return async function request<T>(config: AxiosRequestConfig): Promise<RequestResult<T>> {
    const token = options.getToken?.();

    function fail(message: string, unauthorized = false): RequestResult<T> {
      // 登录输错密码时只显示提示；其他接口的 401 才清理登录状态。
      if (unauthorized && config.url !== '/system/user/login' && options.getToken?.() === token) {
        void options.onUnauthorized?.();
      }
      options.onError?.(message);
      return { data: null, error: message };
    }

    try {
      const response = await instance.request<EamsResponse<T>>({
        ...config,
        headers: { ...config.headers, ...(token ? { Authorization: `Bearer ${token}` } : {}) }
      });

      // HTTP 成功不等于业务成功，例如参数校验失败时后端仍可能返回 HTTP 200。
      if (response.data.code !== 200) {
        return fail(response.data.message || '操作失败', response.data.code === 401);
      }

      return { data: response.data.data, error: null };
    } catch (error: unknown) {
      if (isAxiosError<Partial<EamsResponse<unknown>>>(error)) {
        const message = error.response?.data?.message;
        if (message) return fail(message, error.response?.status === 401);
        if (error.response?.status === 401) return fail('登录已过期，请重新登录', true);
        if (error.code === 'ECONNABORTED') return fail('请求超时，请稍后重试');
        return fail(error.response ? `请求失败（HTTP ${error.response.status}）` : '无法连接服务，请确认后端已启动');
      }
      return fail('请求失败，请稍后重试');
    }
  };
}
