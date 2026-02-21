import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/projects',
  },
  {
    path: '/user/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: { title: '登录' },
  },
  {
    path: '/user/register',
    name: 'Register',
    component: () => import('@/views/RegisterView.vue'),
    meta: { title: '注册' },
  },
  {
    path: '/projects',
    name: 'ProjectList',
    component: () => import('@/views/ProjectListView.vue'),
    meta: {
      title: '项目列表',
      requiresAuth: true,
    },
  },
  {
    path: '/projects/:id',
    name: 'ProjectWorkspace',
    component: () => import('@/views/ProjectWorkspaceView.vue'),
    meta: {
      title: '项目工作空间',
      requiresAuth: true,
    },
    props: true,
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  document.title = `${to.meta.title || 'AI视频分镜剧本管理'} - AI Script Manager`

  // 更宽松的登录检查：token或userInfo任一存在即可
  const isLoggedIn = userStore.token || userStore.userInfo

  if (to.meta.requiresAuth && !isLoggedIn) {
    next({
      path: '/user/login',
      query: { redirect: to.fullPath },
    })
  } else if ((to.path === '/user/login' || to.path === '/user/register') && isLoggedIn) {
    next('/projects')
  } else {
    next()
  }
})

export default router
