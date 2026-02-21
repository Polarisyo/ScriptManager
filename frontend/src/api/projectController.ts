// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** POST /project/edit */
export async function edit(body: API.ProjectEditRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseProjectVO>('/project/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** POST /project/list */
export async function list(body: API.ProjectQueryRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseListProjectVO>('/project/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** DELETE /project/remove/${id} */
export async function remove(params: API.removeParams, options?: { [key: string]: any }) {
  const { id } = params
  return request<boolean>(`/project/remove/${id}`, {
    method: 'DELETE',
    ...(options || {}),
  })
}

/** POST /project/save */
export async function save(body: API.ProjectAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/project/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
