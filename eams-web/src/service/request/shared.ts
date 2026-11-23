import type { RequestInstanceState } from './type';

/** 同一时间的相同错误只显示一次，避免多个请求同时失败时重复弹出。 */
export function showErrorMsg(state: RequestInstanceState, message: string) {
  if (state.errMsgStack.includes(message)) return;

  state.errMsgStack.push(message);
  window.$message?.error(message);
  window.setTimeout(() => {
    state.errMsgStack = state.errMsgStack.filter(item => item !== message);
  }, 3000);
}
