import { getServiceBaseURL } from '@/utils/service';
import { localStg } from '@/utils/storage';
import { useAuthStore } from '@/store/modules/auth';
import { showErrorMsg } from './shared';
import { createEamsClient } from './eams';
import type { RequestInstanceState } from './type';

const isHttpProxy = import.meta.env.DEV && import.meta.env.VITE_HTTP_PROXY === 'Y';
const { baseURL, otherBaseURL } = getServiceBaseURL(import.meta.env, isHttpProxy);
const state: RequestInstanceState = { errMsgStack: [] };

const options = {
  getToken: () => localStg.get('token') || '',
  onError: (message: string) => showErrorMsg(state, message),
  async onUnauthorized() {
    // 同时失败的多个请求只由第一个请求触发退出。
    if (localStg.get('token')) await useAuthStore().resetStore();
  }
};

/** 系统服务：登录、当前用户和用户管理。 */
export const request = createEamsClient({ ...options, baseURL });

/** 课程服务：开发时经 Vite 代理转发至 8085。 */
export const courseRequest = createEamsClient({ ...options, baseURL: otherBaseURL.eams });
