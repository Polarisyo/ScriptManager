<template>
  <div class="project-list-container">
    <div class="page-header">
      <div class="page-title">
        <el-icon :size="28"><FolderOpened /></el-icon>
        <h1>项目列表</h1>
      </div>

      <div class="header-actions">
        <el-button type="primary" :icon="Plus" size="large" @click="handleCreateProject">
          新建项目
        </el-button>
      </div>
    </div>

    <div class="page-content">
      <div class="filters-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索项目..."
          :prefix-icon="Search"
          clearable
          style="width: 200px; margin-right: 12px"
        />

        <el-select
          v-model="projectType"
          placeholder="项目类型"
          clearable
          style="width: 150px; margin-right: 12px"
        >
          <el-option label="全部" value="" />
          <el-option label="短视频" value="短视频" />
          <el-option label="广告" value="广告" />
          <el-option label="宣传片" value="宣传片" />
          <el-option label="MV" value="MV" />
        </el-select>

        <el-select
          v-model="projectStatus"
          placeholder="项目状态"
          clearable
          style="width: 150px; margin-right: 12px"
        >
          <el-option label="全部" value="" />
          <el-option label="草稿" value="draft" />
          <el-option label="进行中" value="inProgress" />
          <el-option label="已完成" value="completed" />
          <el-option label="已归档" value="archived" />
        </el-select>

        <el-select v-model="sortBy" placeholder="排序方式" style="width: 150px">
          <el-option label="最新创建" value="createTime" />
          <el-option label="最近更新" value="updateTime" />
          <el-option label="分镜数量" value="shotCount" />
        </el-select>
      </div>

      <div v-loading="loading" class="projects-grid">
        <div v-if="projects.length === 0" class="empty-state">
          <el-icon :size="80" color="#dcdfe6"><FolderOpened /></el-icon>
          <p>暂无项目</p>
          <el-button type="primary" :icon="Plus" @click="handleCreateProject">
            创建第一个项目
          </el-button>
        </div>

        <div
          v-for="project in projects"
          :key="project.id"
          class="project-card"
          @click="handleProjectClick(project)"
        >
          <div class="project-cover">
            <el-image :src="project.coverUrl || defaultCover" fit="cover" class="cover-image">
              <template #placeholder>
                <div class="image-placeholder">
                  <el-icon :size="48"><VideoCamera /></el-icon>
                </div>
              </template>
            </el-image>
            <div class="project-status">
              <el-tag :type="getStatusType(project.projectStatus)" size="small">
                {{ getStatusText(project.projectStatus) }}
              </el-tag>
            </div>
          </div>

          <div class="project-info">
            <h3 class="project-name">{{ project.projectName }}</h3>
            <p class="project-description">{{ project.projectDescription || '暂无描述' }}</p>

            <div class="project-meta">
              <div class="meta-item">
                <el-icon><VideoCamera /></el-icon>
                <span>{{ project.shotCount || 0 }} 分镜</span>
              </div>
              <div class="meta-item">
                <el-icon><Timer /></el-icon>
                <span>{{ formatDuration(project.totalDuration || 0) }}</span>
              </div>
            </div>

            <div class="project-progress">
              <el-progress
                :percentage="calculateProgress(project)"
                :color="getProgressColor(project)"
                :show-text="false"
              />
              <span class="progress-text"> {{ calculateProgress(project) }}% </span>
            </div>
          </div>

          <div class="project-actions">
            <el-button
              type="primary"
              size="small"
              :icon="Edit"
              @click.stop="handleEditProject(project)"
            >
              编辑
            </el-button>
            <el-dropdown @command="(cmd) => handleCommand(cmd, project)">
              <el-button size="small" :icon="MoreFilled">
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="duplicate">
                    <el-icon><CopyDocument /></el-icon>
                    复制项目
                  </el-dropdown-item>
                  <el-dropdown-item command="delete" divided>
                    <el-icon><Delete /></el-icon>
                    删除项目
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>

      <div v-if="!loading" class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="totalProjects"
          :page-sizes="[12, 24, 48]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchProjects"
          @current-change="fetchProjects"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, type Action } from 'element-plus'
import {
  Plus,
  Search,
  Edit,
  MoreFilled,
  CopyDocument,
  Delete,
  VideoCamera,
  Timer,
  Clock,
  FolderOpened,
} from '@element-plus/icons-vue'
import { useProjectStore } from '@/stores/project'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const projectStore = useProjectStore()
const userStore = useUserStore()
const loginUser = ref(userStore.userInfo)

const { projects, totalProjects } = projectStore
const searchKeyword = ref('')
const projectType = ref('')
const projectStatus = ref('')
const sortBy = ref('createTime')
const currentPage = ref(1)
const pageSize = ref(12)
const loading = ref(false)

const defaultCover = 'https://via.placeholder.com/400x250/667eea/ffffff?text=AI视频分镜'

const fetchProjects = async () => {
  if (loading.value) return
  loading.value = true
  try {
    await projectStore.fetchProjects({
      projectName: searchKeyword.value,
      projectType: projectType.value,
      projectStatus: projectStatus.value,
      sortBy: sortBy.value,
      current: currentPage.value,
      size: pageSize.value,
      userId: loginUser.value?.id,
    })
  } catch (error) {
    console.error('获取项目列表失败:', error)
  } finally {
    loading.value = false
    console.log(projects.values)
  }
}

const handleCreateProject = () => {
  router.push('/project/create')
}

const handleProjectClick = (project: any) => {
  router.push(`/project/${project.id}`)
}

const handleEditProject = (project: any) => {
  router.push(`/project/${project.id}/edit`)
}

const handleCommand = async (command: any, project: any) => {
  switch (command) {
    case 'duplicate':
      try {
        await ElMessageBox.confirm('确定要复制该项目吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info',
        })
        await projectStore.duplicateProject(project.id)
        ElMessage.success('复制成功')
      } catch (error: any) {
        if (error !== 'cancel') {
          console.error('复制失败:', error)
        }
      }
      break

    case 'delete':
      try {
        await ElMessageBox.confirm(
          `确定要删除项目"${project.projectName}"吗？此操作不可恢复！`,
          '警告',
          {
            confirmButtonText: '确定删除',
            cancelButtonText: '取消',
            type: 'warning',
          },
        )
        await projectStore.deleteProject(project.id)
      } catch (error: any) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
        }
      }
      break
  }
}

const getStatusType = (status?: string) => {
  const statusMap: Record<string, any> = {
    draft: 'info',
    inProgress: 'primary',
    completed: 'success',
    archived: 'warning',
  }
  return statusMap[status || ''] || 'info'
}

const getStatusText = (status?: string) => {
  const statusMap: Record<string, string> = {
    draft: '草稿',
    inProgress: '进行中',
    completed: '已完成',
    archived: '已归档',
  }
  return statusMap[status || ''] || status || '未知'
}

const calculateProgress = (project: any) => {
  if (!project.shotCount) return 0
  return Math.round((project.shotCount / 10) * 100)
}

const getProgressColor = (project: any) => {
  const progress = calculateProgress(project)
  if (progress === 100) return '#67c23a'
  if (progress >= 50) return '#667eea'
  return '#e6a23c'
}

const formatDuration = (seconds: number) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

const formatDate = (date: string) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN')
}

onMounted(() => {
  fetchProjects()
})
</script>

<style scoped>
.project-list-container {
  width: 100%;
  height: 100vh;
  background: var(--bg-page);
  display: flex;
  flex-direction: column;
}

.page-header {
  padding: 24px 32px;
  background: var(--bg-card);
  border-bottom: 1px solid var(--border-base);
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-shrink: 0;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-title h1 {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.page-content {
  flex: 1;
  overflow-y: auto;
  padding: 24px 32px;
}

.filters-bar {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
  padding: 16px 20px;
  background: var(--bg-card);
  border-radius: 8px;
  box-shadow: var(--shadow-base);
  flex-wrap: wrap;
  gap: 12px;
}

.projects-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
  min-height: 200px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  color: var(--text-secondary);
}

.empty-state p {
  font-size: 16px;
  margin-top: 16px;
  margin-bottom: 24px;
}

.project-card {
  background: var(--bg-card);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: var(--shadow-base);
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid var(--border-base);
  display: flex;
  flex-direction: column;
  height: 100%;
}

.project-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: var(--primary-color);
}

.project-cover {
  position: relative;
  width: 100%;
  height: 180px;
  overflow: hidden;
  background: var(--bg-hover);
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.project-card:hover .cover-image {
  transform: scale(1.05);
}

.image-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  background: var(--bg-hover);
  color: var(--text-placeholder);
}

.project-status {
  position: absolute;
  top: 12px;
  right: 12px;
}

.project-info {
  padding: 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.project-name {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.project-description {
  font-size: 14px;
  color: var(--text-secondary);
  margin: 0 0 12px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.project-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 12px;
  font-size: 13px;
  color: var(--text-regular);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.project-progress {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: auto;
}

.progress-text {
  font-size: 12px;
  color: var(--text-regular);
  white-space: nowrap;
}

.project-actions {
  padding: 12px 16px;
  border-top: 1px solid var(--border-base);
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  background: var(--bg-hover);
}

.pagination-container {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style>
