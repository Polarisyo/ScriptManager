import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { projectList, projectSave, projectEdit } from '@/api/projectController'
import { ElMessage } from 'element-plus'

export const useProjectStore = defineStore('project', () => {
  const projects = ref<API.ProjectVO[]>([])
  const currentProject = ref<API.ProjectVO | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  // Getters
  const totalProjects = computed(() => projects.value.length)
  const completedProjects = computed(
    () => projects.value.filter((p: API.ProjectVO) => p.projectStatus === 'completed').length,
  )
  const draftProjects = computed(
    () => projects.value.filter((p: API.ProjectVO) => p.projectStatus === 'draft').length,
  )

  // Actions
  const fetchProjects = async (params?: any) => {
    loading.value = true
    error.value = null
    try {
      const response = await projectList(params || {})
      if (response.data.code == 0 && response.data.data) {
        projects.value = response.data.data
        return projects.value
      }
      throw new Error('获取项目列表失败')
    } catch (err: any) {
      error.value = err.message || '获取项目列表失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  const createProject = async (data: any) => {
    loading.value = true
    error.value = null
    try {
      const response = await projectSave(data)
      if (response.data) {
        // await fetchProjects()
        ElMessage.success('创建项目成功')
        return response.data
      }
      throw new Error('创建项目失败')
    } catch (err: any) {
      error.value = err.message || '创建项目失败'
      ElMessage.error(err.message || '创建项目失败')
      throw err
    } finally {
      loading.value = false
    }
  }

  const updateProject = async (data: any) => {
    loading.value = true
    error.value = null
    try {
      const response = await projectEdit(data)
      if (response.data) {
        await fetchProjects()
        ElMessage.success('更新项目成功')
        return response.data
      }
      throw new Error('更新项目失败')
    } catch (err: any) {
      error.value = err.message || '更新项目失败'
      ElMessage.error(err.message || '更新项目失败')
      throw err
    } finally {
      loading.value = false
    }
  }

  const setCurrentProject = (project: API.ProjectVO | null) => {
    currentProject.value = project
  }

  const getProjectById = (id: number) => {
    return projects.value.find((p: API.ProjectVO) => p.id === id) || null
  }

  const deleteProject = async (id: number) => {
    loading.value = true
    error.value = null
    try {
      const response = await fetch('http://localhost:8123/api/project/remove', {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ id }),
      })

      const data = await response.json()

      if (data.code === 0) {
        await fetchProjects()
        ElMessage.success('删除项目成功')
        return true
      }
      throw new Error(data.message || '删除项目失败')
    } catch (err: any) {
      error.value = err.message || '删除项目失败'
      ElMessage.error(err.message || '删除项目失败')
      throw err
    } finally {
      loading.value = false
    }
  }

  const duplicateProject = async (id: number) => {
    const project = getProjectById(id)
    if (!project) throw new Error('项目不存在')

    const newProject = {
      ...project,
      id: undefined,
      projectName: `${project.projectName} - 副本`,
      projectStatus: 'draft',
    }

    return createProject(newProject)
  }

  const clearProjects = () => {
    projects.value = []
    currentProject.value = null
    error.value = null
  }

  return {
    projects,
    currentProject,
    loading,
    error,
    totalProjects,
    completedProjects,
    draftProjects,
    fetchProjects,
    createProject,
    updateProject,
    setCurrentProject,
    getProjectById,
    deleteProject,
    duplicateProject,
    clearProjects,
  }
})
