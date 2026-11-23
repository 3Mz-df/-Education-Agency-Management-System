/** 与后端 SysUser 对应；密码只用于提交，接口不会返回密码。 */
export interface SystemUser {
  id: number;
  username: string;
  nickname: string | null;
  realName: string | null;
  userType: number | null;
  institutionId: number | null;
  phone: string | null;
  email: string | null;
  avatar: string | null;
  status: number | null;
  createTime: string | null;
}

export interface UserInput {
  id?: number;
  username: string;
  password?: string;
  nickname: string;
  realName: string;
  userType: number | null;
  institutionId: number | null;
  phone: string;
  email: string;
  avatar: string;
  status: number | null;
}

export interface UserQuery {
  pageNum: number;
  pageSize: number;
  keyword?: string;
  userType?: number;
  status?: number;
}

export interface PageResult<T> {
  pageNum: number;
  pageSize: number;
  total: number;
  pages: number;
  list: T[];
}

export interface Course {
  id: number;
  courseName: string;
  description: string | null;
  status: number;
  createTime: string | null;
  updateTime: string | null;
}

export interface CourseAddDTO {
  courseName: string;
  description: string;
}
