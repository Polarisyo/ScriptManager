// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 POST /project/add */
export async function projectSave(body: API.ProjectAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/project/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /project/edit */
export async function projectEdit(body: API.ProjectEditRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseProjectVO>('/project/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /project/list */
export async function projectList(body: API.ProjectQueryRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseListProjectVO>('/project/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
