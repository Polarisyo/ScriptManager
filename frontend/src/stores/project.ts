import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Project, ProjectAddRequest, ProjectEditRequest, ProjectQueryRequest } from '@/types'
import {
  save as saveProject,
  edit as editProject,
  list,
  remove as deleteProject,
} from '@/api/projectController'

export const useProjectStore = defineStore('project', () => {
  const projects = ref<Project[]>([])
  const currentProject = ref<Project | undefined>(undefined)
  const isLoading = ref(false)
  const total = ref(0)

  const filteredProjects = computed(() => projects.value)
  const projectCount = computed(() => projects.value.length)

  const setProjects = (projectList: Project[]) => {
    projects.value = projectList
  }

  const setCurrentProject = (project: Project | undefined) => {
    currentProject.value = project
  }

  const setLoading = (loading: boolean) => {
    isLoading.value = loading
  }

  const addProject = async (projectData: ProjectAddRequest) => {
    try {
      const res = await saveProject(projectData)
      if (res.data?.code === 0 && res.data?.data) {
        const newProject: Project = {
          ...projectData,
          id: res.data.data,
          userId: 0,
          user: undefined,
          shotCount: 0,
          totalDuration: 0,
          progressRate: 0,
          projectStatus: '草稿',
          createTime: new Date().toISOString(),
          editTime: new Date().toISOString(),
        }
        projects.value.unshift(newProject)
        return newProject
      }
      throw new Error(res.data?.message || '创建项目失败')
    } catch (error: any) {
      throw error
    }
  }

  const editProjectAction = async (projectId: number, projectData: ProjectEditRequest) => {
    try {
      const res = await editProject(projectData)
      const data = res.data as any
      if (data?.code === 0) {
        const index = projects.value.findIndex((p) => p.id === projectId)
        if (index !== -1) {
          projects.value[index] = {
            ...projects.value[index],
            ...projectData,
          }
        }
        if (currentProject.value?.id === projectId) {
          currentProject.value = {
            ...currentProject.value,
            ...projectData,
          }
        }
        return true
      }
      throw new Error(res.data?.message || '更新项目失败')
    } catch (error: any) {
      throw error
    }
  }

  const fetchProjects = async (query?: ProjectQueryRequest) => {
    setLoading(true)
    try {
      const res = await list(query || {})
      const data = res.data as any
      if (data?.code === 0 && data?.data) {
        projects.value = data.data
        total.value = data.data.length
      }
    } catch (error: any) {
      throw error
    } finally {
      setLoading(false)
    }
  }

  const deleteProjectAction = async (projectId: number) => {
    try {
      const res = await deleteProject({ id: projectId })
      const data = res.data as any
      if (data?.code === 0) {
        projects.value = projects.value.filter((p) => p.id !== projectId)
        if (currentProject.value?.id === projectId) {
          currentProject.value = undefined
        }
        return true
      }
      throw new Error(data?.message || '删除项目失败')
    } catch (error: any) {
      throw error
    }
  }

  return {
    projects,
    currentProject,
    isLoading,
    total,
    filteredProjects,
    projectCount,
    setProjects,
    setCurrentProject,
    setLoading,
    addProject,
    editProjectAction,
    fetchProjects,
    deleteProjectAction,
  }
})
