import { request } from '../request';
import type { RequestResult } from '../request/eams';
import { fetchCurrentUser } from './user';

/** 后端返回 JWT 字符串，将它转换成登录状态使用的对象。 */
export async function fetchLogin(username: string, password: string): Promise<RequestResult<Api.Auth.LoginToken>> {
  const result = await request<string>({ url: '/system/user/login', method: 'post', data: { username, password } });
  if (result.error !== null) return result;
  return { data: { token: result.data }, error: null };
}

/** 将真实用户映射到模板需要的用户信息，不把 userType 擅自解释为角色。 */
export async function fetchGetUserInfo(): Promise<RequestResult<Api.Auth.UserInfo>> {
  const result = await fetchCurrentUser();
  if (result.error !== null) return result;
  if (!result.data) return { data: null, error: '当前用户不存在，请重新登录' };

  return {
    data: {
      userId: String(result.data.id),
      userName: result.data.username,
      roles: [],
      buttons: []
    },
    error: null
  };
}
