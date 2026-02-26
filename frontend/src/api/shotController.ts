// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 POST /shot/add */
export async function shotAdd(body: API.ShotAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/shot/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /shot/edit */
export async function shotEdit(body: API.ShotEditRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseShotVO>('/shot/edit', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /shot/getInfo */
export async function shotGetInfo(body: API.SingleQueryRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseShotVO>('/shot/getInfo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /shot/list */
export async function shotList(body: API.ShotQueryRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseListShotVO>('/shot/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /shot/remove */
export async function shotRemove(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/shot/remove', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
