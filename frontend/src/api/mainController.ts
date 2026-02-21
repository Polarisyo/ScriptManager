// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** GET /health/ */
export async function health(options?: { [key: string]: any }) {
  return request<string>('/health/', {
    method: 'GET',
    ...(options || {}),
  })
}
