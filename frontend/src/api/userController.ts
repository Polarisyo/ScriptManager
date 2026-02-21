// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** GET /user/getInfo/${id} */
export async function getInfo(params: API.getInfoParams, options?: { [key: string]: any }) {
  const { id } = params
  return request<API.User>(`/user/getInfo/${id}`, {
    method: 'GET',
    ...(options || {}),
  })
}

/** GET /user/list */
export async function list(query?: { [key: string]: any }, options?: { [key: string]: any }) {
  return request<API.User[]>('/user/list', {
    method: 'GET',
    params: query,
    ...(options || {}),
  })
}

/** POST /user/login */
export async function login(body: API.UserLoginRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseUserVO>('/user/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** GET /user/page */
export async function page(
  page?: { pageNumber?: number; pageSize?: number },
  options?: { [key: string]: any },
) {
  return request<API.PageUser>('/user/page', {
    method: 'GET',
    params: page,
    ...(options || {}),
  })
}

/** POST /user/register */
export async function register(body: API.UserRegisterRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** DELETE /user/remove/${id} */
export async function remove(params: API.removeParams, options?: { [key: string]: any }) {
  const { id } = params
  return request<boolean>(`/user/remove/${id}`, {
    method: 'DELETE',
    ...(options || {}),
  })
}

/** PUT /user/update */
export async function update(body: API.User, options?: { [key: string]: any }) {
  return request<boolean>('/user/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
