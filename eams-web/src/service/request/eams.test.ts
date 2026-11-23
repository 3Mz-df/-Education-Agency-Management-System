import { createServer } from 'node:http';
import type { Server } from 'node:http';
import type { AddressInfo } from 'node:net';
import { afterAll, beforeAll, describe, expect, it } from 'vite-plus/test';
import { createEamsClient } from './eams';

let server: Server;
let baseURL = '';

beforeAll(async () => {
  server = createServer((req, res) => {
    res.setHeader('Content-Type', 'application/json');
    if (req.url === '/unauthorized') res.statusCode = 401;
    if (req.url === '/invalid') {
      res.end(JSON.stringify({ code: 400, message: '课程名称不能为空', data: null }));
    } else if (req.url === '/unauthorized' || req.url === '/system/user/login') {
      res.end(JSON.stringify({ code: 401, message: '未登录或登录已过期', data: null }));
    } else if (req.url === '/empty') {
      res.end(JSON.stringify({ code: 200, message: '操作成功', data: null }));
    } else {
      res.end(JSON.stringify({ code: 200, message: '操作成功', data: { authorization: req.headers.authorization } }));
    }
  });
  await new Promise<void>(resolve => server.listen(0, '127.0.0.1', resolve));
  baseURL = `http://127.0.0.1:${(server.address() as AddressInfo).port}`;
});

afterAll(async () => {
  await new Promise<void>((resolve, reject) => server.close(error => (error ? reject(error) : resolve())));
});

describe('EAMS 请求协议', () => {
  it('将 JWT 放入请求头，并取出成功响应中的 data', async () => {
    const request = createEamsClient({ baseURL, getToken: () => 'test-token' });
    const result = await request<{ authorization: string }>({ url: '/current' });
    expect(result.error).toBeNull();
    expect(result.data).toEqual({ authorization: 'Bearer test-token' });
  });

  it('HTTP 200 下的业务校验失败仍返回后端 message', async () => {
    const messages: string[] = [];
    const request = createEamsClient({ baseURL, onError: message => messages.push(message) });
    const result = await request({ url: '/invalid' });
    expect(result.error).toBe('课程名称不能为空');
    expect(result.data).toBeNull();
    expect(messages).toEqual(['课程名称不能为空']);
  });

  it('HTTP 401 会清理登录状态', async () => {
    let logoutCount = 0;
    const request = createEamsClient({
      baseURL,
      onUnauthorized: () => {
        logoutCount += 1;
      }
    });
    const result = await request({ url: '/unauthorized' });
    expect(result.error).toBe('未登录或登录已过期');
    expect(logoutCount).toBe(1);
  });

  it('登录失败的业务 401 只提示错误，不触发登出重定向', async () => {
    let logoutCount = 0;
    const request = createEamsClient({
      baseURL,
      onUnauthorized: () => {
        logoutCount += 1;
      }
    });
    const result = await request({ url: '/system/user/login', method: 'post' });
    expect(result.error).toBe('未登录或登录已过期');
    expect(logoutCount).toBe(0);
  });

  it('旧请求返回 401 时，不会清理用户刚登录得到的新 JWT', async () => {
    let token = 'old-token';
    let logoutCount = 0;
    const request = createEamsClient({
      baseURL,
      getToken: () => token,
      onUnauthorized: () => {
        logoutCount += 1;
      }
    });
    const pending = request({ url: '/unauthorized' });
    token = 'new-token';
    const result = await pending;
    expect(result.error).toBe('未登录或登录已过期');
    expect(logoutCount).toBe(0);
  });

  it('修改、删除返回的 null 也属于成功结果', async () => {
    const request = createEamsClient({ baseURL });
    expect(await request({ url: '/empty' })).toEqual({ data: null, error: null });
  });
});
