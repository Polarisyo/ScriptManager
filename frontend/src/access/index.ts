import router from '@/router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import checkAccess from '@/access/checkAccess'
import ACCESS_ENUM from '@/access/accessEnum'

router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  
  // 1. 尝试获取当前登录用户信息（仅获取一次，避免重复请求）
  // 注意：即使未登录，这个调用也应该返回一个明确的“未登录”状态对象
  if (!userStore.userInfo?.userRole) {
    await userStore.getLoginUserInfo()
  }
  
  const loginUser = userStore.userInfo
  const needAccess = (to.meta?.access as string) ?? ACCESS_ENUM.NOT_LOGIN
  const userAccess = loginUser?.userRole ?? ACCESS_ENUM.NOT_LOGIN
  
  console.log(`路由守卫: 前往[${to.path}], 需要权限[${needAccess}], 用户权限[${userAccess}]`)
  
  // 2. 如果目标路由不需要登录，直接放行（如登录页、注册页、公开页面）
  if (needAccess === ACCESS_ENUM.NOT_LOGIN) {
    next()
    return
  }
  
  // 3. 需要登录才能访问的路由
  // 检查用户是否已登录
  if (!loginUser || userAccess === ACCESS_ENUM.NOT_LOGIN) {
    // 未登录，重定向到登录页
    // 注意：登录页的 meta.access 必须设置为 ACCESS_ENUM.NOT_LOGIN
    ElMessage.warning('请先登录')
    next('/user/login')
    return
  }
  
  // 4. 已登录，检查是否有权限访问目标路由
  if (!checkAccess(loginUser, needAccess)) {
    ElMessage.error('没有访问权限')
    // 可以重定向到无权限页面或首页
    // next('/noAuth') // 确保 /noAuth 路由存在且可访问
    return
  }
  
  // 5. 权限检查通过，正常放行
  next()
})