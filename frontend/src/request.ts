import axios from 'axios'
import { ElMessage } from 'element-plus'

const myAxios = axios.create({
  //前端解决跨域问题配置
  // baseURL: '',
  baseURL: '/api',
  timeout: 60000,
  withCredentials: true,
})

export default myAxios

// 添加请求拦截器
myAxios.interceptors.request.use(
  function (config) {
    // 在发送请求之前做些什么
    return config
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
  },
)

// 全局响应拦截器
myAxios.interceptors.response.use(
  function (response) {
    const { data } = response
    // 未登录
    if (data.code === 40100) {
      // 不是登录的请求，并且用户目前不是已经在用户登录页面，则跳转到登录页面
      // 具体哪些可以不登录就访问就看是否返回40100
      if (
        !response.request.responseURL.includes('user/get/login') &&
        !window.location.pathname.includes('/user/login')
      ) {
        ElMessage.warning('请先登录')
        window.location.href = `/user/login?redirect=${window.location.href}`
      }
    }
    return response
  },
  function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    return Promise.reject(error)
  },
)
