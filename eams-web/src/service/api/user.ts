import { request } from '../request';
import type { PageResult, SystemUser, UserInput, UserQuery } from './types';

export function fetchCurrentUser() {
  return request<SystemUser>({ url: '/system/user/current' });
}

export function fetchUserPage(params: UserQuery) {
  return request<PageResult<SystemUser>>({ url: '/system/user/list', params });
}

export function fetchUserDetail(id: number) {
  return request<SystemUser | null>({ url: `/system/user/${id}` });
}

export function fetchAddUser(data: UserInput) {
  return request<number>({ url: '/system/user', method: 'post', data });
}

export function fetchUpdateUser(data: UserInput & { id: number }) {
  return request<null>({ url: '/system/user', method: 'put', data });
}

export function fetchDeleteUser(id: number) {
  return request<null>({ url: `/system/user/${id}`, method: 'delete' });
}
