// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 GET /heath/ */
export async function mainHealth(options?: { [key: string]: any }) {
  return request<string>('/heath/', {
    method: 'GET',
    ...(options || {}),
  })
}
