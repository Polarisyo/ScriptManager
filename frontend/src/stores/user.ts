import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { UserInfo, LoginRequest, RegisterRequest } from '@/types'
import { login, register, getInfo } from '@/api/userController'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<UserInfo | null>(null)
  const token = ref<string>('')
  const isLoggedIn = computed(() => !!token.value && !!userInfo.value)

  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
  }

  const setToken = (newToken: string) => {
    token.value = newToken
  }

  const clearUserInfo = () => {
    userInfo.value = null
    token.value = ''
  }

  const loginAction = async (loginData: LoginRequest) => {
    try {
      const res = await login(loginData)
      const data = res.data as any
      if (data?.code === 0 && data?.data) {
        // 根据后端返回的数据格式判断
        if (typeof data.data === 'string') {
          // 如果data是字符串，可能是token
          token.value = data.data
        } else {
          // 如果data是对象，可能是用户信息
          userInfo.value = data.data as UserInfo
          token.value = '' // 没有token时设置为空字符串
        }
        return res.data
      }
      throw new Error(data?.message || '登录失败')
    } catch (error: any) {
      throw error
    }
  }

  const registerAction = async (registerData: RegisterRequest) => {
    try {
      const res = await register(registerData)
      if (res.data?.code === 0) {
        return res.data
      }
      throw new Error(res.data?.message || '注册失败')
    } catch (error: any) {
      throw error
    }
  }

  const getUserInfo = async () => {
    try {
      const id = userInfo.value?.id || 0
      const res = await getInfo({ id })
      const data = res.data as any
      if (data?.code === 0 && data?.data) {
        userInfo.value = data.data as UserInfo
        return data.data as UserInfo
      }
      throw new Error('获取用户信息失败')
    } catch (error: any) {
      throw error
    }
  }

  return {
    userInfo,
    token,
    isLoggedIn,
    setUserInfo,
    setToken,
    clearUserInfo,
    loginAction,
    registerAction,
    getUserInfo,
  }
})
