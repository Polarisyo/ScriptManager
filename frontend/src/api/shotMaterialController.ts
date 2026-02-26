// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 GET /shotMaterial/getInfo/${param0} */
export async function shotmaterialGetInfo(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.shotmaterialGetInfoParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.ShotMaterial>(`/shotMaterial/getInfo/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /shotMaterial/list */
export async function shotmaterialList(options?: { [key: string]: any }) {
  return request<API.ShotMaterial[]>('/shotMaterial/list', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /shotMaterial/page */
export async function shotmaterialPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.shotmaterialPageParams,
  options?: { [key: string]: any }
) {
  return request<API.PageShotMaterial>('/shotMaterial/page', {
    method: 'GET',
    params: {
      ...params,
      page: undefined,
      ...params['page'],
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 DELETE /shotMaterial/remove/${param0} */
export async function shotmaterialRemove(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.shotmaterialRemoveParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<boolean>(`/shotMaterial/remove/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /shotMaterial/save */
export async function shotmaterialSave(body: API.ShotMaterial, options?: { [key: string]: any }) {
  return request<boolean>('/shotMaterial/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /shotMaterial/update */
export async function shotmaterialUpdate(body: API.ShotMaterial, options?: { [key: string]: any }) {
  return request<boolean>('/shotMaterial/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
