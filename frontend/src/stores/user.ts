import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import {
  userUpdate,
  userGetLoginUser,
  userLogin,
  userRegister,
  userRemove,
} from '@/api/userController'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<API.UserVO | null>(null)
  const token = ref<string>(localStorage.getItem('token') || '')

  // Getters
  const isLoggedIn = computed(() => !!userInfo.value)
  const userName = computed(() => userInfo.value?.userName || '')
  const userAvatar = computed(() => userInfo.value?.userAvatar || '')
  const userAccount = computed(() => userInfo.value?.userAccount || '')
  const subscriptionPlan = computed(() => userInfo.value?.subscriptionPlan || 'free')
  const aiCredits = computed(() => userInfo.value?.aiCredits || 0)
  const userRole = computed(() => userInfo.value?.userRole || 'user')

  // Actions
  const setUserInfo = (info: API.UserVO | null) => {
    userInfo.value = info
  }

  const setToken = (tokenValue: string) => {
    token.value = tokenValue
    localStorage.setItem('token', tokenValue)
  }

  const clearUser = () => {
    userInfo.value = null
    token.value = ''
    localStorage.removeItem('token')
  }

  const login = async (userAccount: string, userPassword: string) => {
    try {
      const response = await userLogin({ userAccount, userPassword })

      if (response.data.code === 0 && response.data.data) {
        setUserInfo(response.data.data)
        setToken('your-jwt-token-here') // 实际项目中应该从响应中获取
        ElMessage.success('登录成功')
        return response.data.data
      }
      throw new Error(response.data.message || '登录失败')
    } catch (error: any) {
      ElMessage.error(error.message || '登录失败，请检查账号密码')
      throw error
    }
  }

  const getLoginUserInfo = async () => {
    if (userInfo.value) return userInfo.value

    try {
      const response = await userGetLoginUser()

      if (response.data.code === 0 && response.data.data) {
        setUserInfo(response.data.data)
        return response.data.data
      }
      return null
    } catch (error: any) {
      console.error('获取用户信息失败:', error)
      return null
    }
  }

  const logout = async () => {
    try {
      await fetch('http://localhost:8123/api/user/logout', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
      })
    } catch (error) {
      console.error('登出请求失败:', error)
    } finally {
      clearUser()
      ElMessage.success('已安全退出')
    }
  }

  const removeUser = async (userId: number) => {
    try {
      await userRemove({ userId })

      clearUser()
      ElMessage.success('删除成功')
      return true
    } catch (error: any) {
      ElMessage.error(error.message || '删除失败')
      throw error
    }
  }

  const editUserInfo = async (data: API.UserEditRequest) => {
    try {
      const response = await userUpdate(data)

      if (response.data.code === 0 && response.data.data) {
        setUserInfo(response.data.data)
        ElMessage.success('更新成功')
        return response.data.data
      }
      throw new Error(response.data.message || '更新失败')
    } catch (error: any) {
      ElMessage.error(error.message || '更新失败')
      throw error
    }
  }

  const register = async (userAccount: string, userPassword: string, checkPassword: string) => {
    try {
      const response = await userRegister({ userAccount, userPassword, checkPassword })

      if (response.data.code === 0 && response.data.data) {
        ElMessage.success('注册成功，请登录')
        return response.data.data
      }
      throw new Error(response.data.message || '注册失败')
    } catch (error: any) {
      ElMessage.error(error.message || '注册失败')
      throw error
    }
  }

  return {
    userInfo,
    token,
    isLoggedIn,
    userName,
    userAvatar,
    userAccount,
    subscriptionPlan,
    aiCredits,
    userRole,
    setUserInfo,
    setToken,
    clearUser,
    login,
    getLoginUserInfo,
    logout,
    editUserInfo,
    register,
    removeUser,
  }
})
