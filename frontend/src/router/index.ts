/**
 * Vue Router 路由配置
 *
 * 使用 Vue Router 5 进行路由管理
 */

import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import ACCESS_ENUM from '@/access/accessEnum'

// ==================== 路由定义 ====================

/**
 * 路由配置数组
 * 定义所有页面的路由规则
 */
const routes: RouteRecordRaw[] = [
  /**
   * 根路径，重定向到项目列表页
   */
  {
    path: '/',
    redirect: '/project',
  },

  /**
   * 登录页路由
   */
  {
    path: '/user/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: { title: '登录' },
  },

  /**
   * 注册页路由
   */
  {
    path: '/user/register',
    name: 'Register',
    component: () => import('@/views/RegisterView.vue'),
    meta: { title: '注册' },
  },

  /**
   * 项目创建页路由
   */
  {
    path: '/project/create',
    name: 'ProjectCreate',
    component: () => import('@/views/ProjectCreateView.vue'),
    meta: {
      title: '创建项目',
      access: ACCESS_ENUM.USER, // 需要登录才能访问
    },
  },

  /**
   * 项目列表页路由
   */
  {
    path: '/project',
    name: 'ProjectList',
    component: () => import('@/views/ProjectListView.vue'),
    meta: {
      title: '项目列表',
      access: ACCESS_ENUM.USER, // 需要登录才能访问
    },
  },

  /**
   * 项目工作空间路由
   * 动态路由，接收项目ID作为参数
   */
  {
    path: '/project/:id',
    name: 'ProjectWorkspace',
    component: () => import('@/views/ProjectWorkspaceView.vue'),
    meta: {
      title: '项目工作空间',
      access: ACCESS_ENUM.USER, // 需要登录才能访问
    },
    props: true, // 将路由参数作为props传递给组件
  },
]

// ==================== 路由实例 ====================

/**
 * 创建路由实例
 */
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

// ==================== 暴露 ====================

export default router
