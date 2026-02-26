<template>
  <div class="workspace-container">
    <div class="workspace-header">
      <div class="header-left">
        <el-button :icon="ArrowLeft" text @click="handleBack"> 返回 </el-button>
        <div class="project-info">
          <h1 class="project-name">{{ currentProject?.projectName || '项目名称' }}</h1>
          <div class="project-meta">
            <el-tag :type="getStatusType(currentProject?.projectStatus)" size="small">
              {{ getStatusText(currentProject?.projectStatus) }}
            </el-tag>
            <span class="meta-divider">|</span>
            <span>版本 {{ currentProject?.currentVersion }}</span>
            <span class="meta-divider">|</span>
            <span>{{ currentProject?.shotCount || 0 }} 分镜</span>
            <span class="meta-divider">|</span>
            <span>{{ formatDuration(currentProject?.totalDuration || 0) }}</span>
          </div>
        </div>
      </div>

      <div class="header-actions">
        <el-button type="primary" :icon="Plus" @click="handleAddShot"> 添加分镜 </el-button>
        <el-button :icon="Download"> 导出项目 </el-button>
        <el-dropdown @command="handleCommand">
          <el-button :icon="MoreFilled">
            更多操作
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="duplicate">
                <el-icon><CopyDocument /></el-icon>
                复制项目
              </el-dropdown-item>
              <el-dropdown-item command="export">
                <el-icon><Download /></el-icon>
                导出项目
              </el-dropdown-item>
              <el-dropdown-item command="archive" divided>
                <el-icon><FolderDelete /></el-icon>
                归档项目
              </el-dropdown-item>
              <el-dropdown-item command="delete" style="color: #f56c6c">
                <el-icon><Delete /></el-icon>
                删除项目
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <div class="workspace-content">
      <div class="sidebar">
        <div class="sidebar-section">
          <div class="section-header">
            <h3>项目信息</h3>
            <el-button text :icon="Edit" @click="handleEditProject" />
          </div>

          <div class="project-detail">
            <div class="detail-item">
              <span class="label">项目类型</span>
              <span class="value">{{ currentProject?.projectType || '未设置' }}</span>
            </div>
            <div class="detail-item">
              <span class="label">完成进度</span>
              <div class="progress-with-text">
                <el-progress
                  :percentage="calculateProgress()"
                  :show-text="false"
                  :color="getProgressColor()"
                />
                <span class="progress-text"> {{ calculateProgress() }}% </span>
              </div>
            </div>
            <div class="detail-item">
              <span class="label">描述</span>
              <p class="description">{{ currentProject?.projectDescription || '暂无描述' }}</p>
            </div>
          </div>
        </div>

        <div class="sidebar-section">
          <div class="section-header">
            <h3>版本历史</h3>
            <el-button text :icon="RefreshRight" />
          </div>

          <div class="version-list">
            <div
              v-for="version in versions"
              :key="version.version"
              class="version-item"
              :class="{ active: version.active }"
              @click="handleVersionClick(version)"
            >
              <div class="version-info">
                <span class="version-number">v{{ version.version }}</span>
                <span class="version-date">{{ formatDate(version.date) }}</span>
              </div>
              <div class="version-activities">
                <span class="activity">{{ version.activity }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="sidebar-section">
          <div class="section-header">
            <h3>素材库</h3>
            <el-button text :icon="Upload" @click="handleUploadMaterial" />
          </div>

          <div class="material-stats">
            <div class="stat-item">
              <el-icon><Picture /></el-icon>
              <span>图片: {{ imageCount }}</span>
            </div>
            <div class="stat-item">
              <el-icon><VideoCamera /></el-icon>
              <span>视频: {{ videoCount }}</span>
            </div>
            <div class="stat-item">
              <el-icon><Headset /></el-icon>
              <span>音频: {{ audioCount }}</span>
            </div>
          </div>

          <div class="material-tags">
            <div class="tag-item" @click="handleFilterByRole('background')">
              <el-icon><Picture /></el-icon>
              <span>背景</span>
            </div>
            <div class="tag-item" @click="handleFilterByRole('character')">
              <el-icon><User /></el-icon>
              <span>角色</span>
            </div>
            <div class="tag-item" @click="handleFilterByRole('prop')">
              <el-icon><Box /></el-icon>
              <span>道具</span>
            </div>
            <div class="tag-item" @click="handleFilterByRole('audio')">
              <el-icon><Headset /></el-icon>
              <span>音效</span>
            </div>
          </div>
        </div>

        <div class="sidebar-section">
          <div class="section-header">
            <h3>AI 工具</h3>
          </div>

          <div class="ai-tools">
            <div class="tool-item" @click="handleAIGenerateShots">
              <div class="tool-icon">
                <el-icon><MagicStick /></el-icon>
              </div>
              <div class="tool-content">
                <h4>文本生成分镜</h4>
                <p>输入剧本文本自动生成分镜</p>
              </div>
              <el-tag size="small" type="primary">5积分</el-tag>
            </div>

            <div class="tool-item" @click="handleAIPolishScript">
              <div
                class="tool-icon"
                style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)"
              >
                <el-icon><EditPen /></el-icon>
              </div>
              <div class="tool-content">
                <h4>AI 润色剧本</h4>
                <p>提升剧本语言质量</p>
              </div>
              <el-tag size="small" type="success">3积分</el-tag>
            </div>

            <div class="tool-item" @click="handleAIGenerateImage">
              <div
                class="tool-icon"
                style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%)"
              >
                <el-icon><Picture /></el-icon>
              </div>
              <div class="tool-content">
                <h4>AI 生成图片</h4>
                <p>根据描述生成场景图片</p>
              </div>
              <el-tag size="small" type="warning">10积分</el-tag>
            </div>

            <div class="tool-item" @click="handleAIGenerateVideo">
              <div
                class="tool-icon"
                style="background: linear-gradient(135deg, #f83600 0%, #f9d423 100%)"
              >
                <el-icon><VideoPlay /></el-icon>
              </div>
              <div class="tool-content">
                <h4>AI 生成视频</h4>
                <p>基于图片生成视频片段</p>
              </div>
              <el-tag size="small" type="danger">50积分</el-tag>
            </div>
          </div>
        </div>
      </div>

      <div class="main-content">
        <div class="shot-list" v-loading="shotsLoading">
          <div class="shot-list-header">
            <div class="header-left">
              <h2>分镜列表</h2>
              <div class="header-meta">
                <span>共 {{ filteredShots.length }} 个分镜</span>
              </div>
            </div>
            <div class="header-actions">
              <el-button type="primary" size="small" :icon="Plus" @click="handleAddShot">
                添加分镜
              </el-button>
              <el-button size="small" :icon="Refresh" @click="fetchShots"> 刷新 </el-button>
            </div>
          </div>

          <div class="shot-items">
            <div
              v-for="shot in filteredShots"
              :key="shot.shotId"
              class="shot-item"
              draggable="true"
              @dragstart="handleDragStart($event, shot)"
              @dragover="handleDragOver($event)"
              @drop="handleDrop($event, shot)"
            >
              <div class="shot-drag-handle">
                <el-icon><Rank /></el-icon>
              </div>

              <div class="shot-status">
                <el-tag :type="getShotStatusType(shot.shotStatus)" size="small">
                  {{ getShotStatusText(shot.shotStatus) }}
                </el-tag>
              </div>

              <div class="shot-content">
                <h4 class="shot-title">{{ shot.shotTitle || `分镜 ${shot.orderIndex + 1}` }}</h4>
                <p class="shot-description">{{ shot.shotDesc || '暂无描述' }}</p>
                <div class="shot-meta">
                  <span class="meta-item">
                    <el-icon><Timer /></el-icon>
                    {{ shot.duration || 0 }}s
                  </span>
                  <span class="meta-divider">|</span>
                  <span class="meta-item">
                    <el-icon><Document /></el-icon>
                    {{ shot.scriptContent?.length || 0 }} 字
                  </span>
                </div>
              </div>

              <div class="shot-actions">
                <el-button text :icon="Edit" @click.stop="handleEditShot(shot)"> 编辑 </el-button>
                <el-button
                  text
                  :icon="Delete"
                  @click.stop="handleDeleteShot(shot)"
                  style="color: #f56c6c"
                >
                  删除
                </el-button>
              </div>
            </div>
          </div>

          <div v-if="filteredShots.length === 0" class="empty-shots">
            <el-empty description="暂无分镜" />
            <el-button type="primary" :icon="Plus" @click="handleAddShot">
              添加第一个分镜
            </el-button>
          </div>
        </div>
      </div>

      <div class="right-panel">
        <div class="panel-section">
          <div class="panel-header">
            <h3>进度跟踪</h3>
            <el-button text :icon="Setting">配置</el-button>
          </div>

          <div class="progress-section">
            <div class="progress-circle">
              <el-progress
                type="circle"
                :percentage="calculateProgress()"
                :color="getProgressColor()"
                :width="120"
              />
            </div>

            <div class="progress-stats">
              <div class="stat-row">
                <span class="stat-label">总进度</span>
                <span class="stat-value">{{ calculateProgress() }}%</span>
              </div>
              <div class="stat-row">
                <span class="stat-label">分镜数</span>
                <span class="stat-value">
                  {{ completedShots.length }} / {{ allShots.length }}
                </span>
              </div>
              <div class="stat-row">
                <span class="stat-label">总时长</span>
                <span class="stat-value">{{ formatDuration(totalDuration) }}</span>
              </div>
              <div class="stat-row">
                <span class="stat-label">目标时长</span>
                <span class="stat-value">{{ formatDuration(targetDuration) }}</span>
              </div>
              <div class="stat-row">
                <span class="stat-label">剩余时间</span>
                <span class="stat-value">{{ remainingTime }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="panel-section">
          <div class="panel-header">
            <h3>最近活动</h3>
            <el-button text :icon="RefreshRight" @click="fetchActivities" />
          </div>

          <div class="activity-list">
            <div v-for="activity in activities" :key="activity.id" class="activity-item">
              <div class="activity-icon">
                <el-icon>
                  <component :is="getActivityIcon(activity.type)" />
                </el-icon>
              </div>
              <div class="activity-content">
                <p class="activity-text">{{ activity.text }}</p>
                <span class="activity-time">{{ activity.time }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 分镜编辑对话框 -->
    <el-dialog
      v-model="shotDialogVisible"
      :title="editingShot ? '编辑分镜' : '添加分镜'"
      width="600px"
      @close="handleShotDialogClose"
    >
      <el-form ref="shotFormRef" :model="shotForm" :rules="shotFormRules" label-width="100px">
        <el-form-item label="分镜标题" prop="shotTitle">
          <el-input v-model="shotForm.shotTitle" placeholder="请输入分镜标题" />
        </el-form-item>

        <el-form-item label="分镜描述" prop="shotDesc">
          <el-input
            v-model="shotForm.shotDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入分镜描述"
          />
        </el-form-item>

        <el-form-item label="分镜时长" prop="duration">
          <el-input-number
            v-model="shotForm.duration"
            :min="0"
            :max="300"
            :step="1"
            controls-position="right"
          />
          <span class="unit">秒</span>
        </el-form-item>

        <el-form-item label="分镜状态" prop="shotStatus">
          <el-radio-group v-model="shotForm.shotStatus">
            <el-radio label="draft">草稿</el-radio>
            <el-radio label="inProgress">进行中</el-radio>
            <el-radio label="completed">已完成</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="剧本内容" prop="scriptContent">
          <el-input
            v-model="shotForm.scriptContent"
            type="textarea"
            :rows="5"
            placeholder="请输入剧本内容"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="shotDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleShotSubmit"> 确定 </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  ArrowLeft,
  MoreFilled,
  CopyDocument,
  Download,
  FolderDelete,
  Delete,
  Plus,
  Refresh,
  RefreshRight,
  Upload,
  Edit,
  Setting,
  MagicStick,
  EditPen,
  VideoPlay,
  Picture,
  User,
  Box,
  Headset,
  Rank,
  Document,
  VideoCamera,
  Timer,
} from '@element-plus/icons-vue'
import { useProjectStore } from '@/stores/project'

const router = useRouter()
const route = useRoute()
const projectStore = useProjectStore()

const currentProject = projectStore.projects
const versions = ref<any[]>([])
const shots = ref<any[]>([])
const filteredShots = computed(() => shots.value)
const allShots = computed(() => shots.value)
const completedShots = computed(() => shots.value.filter((s) => s.shotStatus === 'completed'))
const activities = ref<any[]>([])

const shotsLoading = ref(false)
const shotDialogVisible = ref(false)
const editingShot = ref<any>(null)
const shotFormRef = ref<FormInstance>()

const shotForm = ref<API.ShotAddRequest>({
  shotId: null,
  projectId: projec,
  shotStatus: 'draft',
  scriptContent: '',
})

const shotFormRules: FormRules = {
  shotTitle: [{ required: true, message: '请输入分镜标题', trigger: 'blur' }],
  shotDesc: [{ required: true, message: '请输入分镜描述', trigger: 'blur' }],
  duration: [{ required: true, message: '请输入分镜时长', trigger: 'blur' }],
}

const imageCount = ref(15)
const videoCount = ref(5)
const audioCount = ref(3)

const totalDuration = computed(() => {
  return shots.value.reduce((sum, shot) => sum + (shot.duration || 0), 0)
})

const targetDuration = ref(300)
const remainingTime = computed(() => {
  const remaining = targetDuration.value - totalDuration.value
  if (remaining > 0) return `还剩 ${Math.ceil(remaining / 60)} 分钟`
  return '已超出目标时长'
})

const projectId = computed(() => route.params.id as string)

onMounted(() => {
  if (projectId.value) {
    fetchProject()
    fetchShots()
    fetchVersions()
    fetchActivities()
  }
})

const fetchProject = async () => {
  try {
    const response = await fetch(`http://localhost:8123/api/project/getInfo`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ id: projectId.value }),
    })

    const data = await response.json()
    if (data.code === 0 && data.data) {
      currentProject.value = data.data
    }
  } catch (error) {
    console.error('获取项目信息失败:', error)
  }
}

const fetchShots = async () => {
  shotsLoading.value = true
  try {
    const response = await fetch(`http://localhost:8123/api/shot/list`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        projectId: projectId.value,
      }),
    })

    const data = await response.json()
    if (data.code === 0 && data.data) {
      shots.value = data.data.sort((a: any, b: any) => a.orderIndex - b.orderIndex)
    }
  } catch (error) {
    console.error('获取分镜列表失败:', error)
  } finally {
    shotsLoading.value = false
  }
}

const fetchVersions = async () => {
  // 模拟版本数据
  versions.value = [
    { version: 2, date: '2024-02-15', activity: '编辑了分镜1', active: true },
    { version: 1, date: '2024-01-20', activity: 'AI生成分镜', active: false },
  ]
}

const fetchActivities = async () => {
  // 模拟活动数据
  activities.value = [
    { id: 1, type: 'create', text: '创建了项目', time: '5分钟前' },
    { id: 2, type: 'edit', text: '编辑了分镜1', time: '10分钟前' },
    { id: 3, type: 'upload', text: '上传了素材', time: '30分钟前' },
  ]
}

const handleBack = () => {
  router.push('/project')
}

const handleCreateProject = () => {
  router.push('/project/create')
}

const handleEditProject = () => {
  // TODO: 实现项目编辑
  ElMessage.info('项目编辑功能开发中')
}

const handleAddShot = () => {
  editingShot.value = null
  shotForm.value = {
    shotId: null,
    projectId: projectId.value,
    shotTitle: '',
    shotDesc: '',
    duration: 5,
    shotStatus: 'draft',
    scriptContent: '',
  }
  shotDialogVisible.value = true
}

const handleEditShot = (shot: any) => {
  editingShot.value = shot
  shotForm.value = {
    ...shot,
    projectId: projectId.value,
  }
  shotDialogVisible.value = true
}

const handleDeleteShot = (shot: any) => {
  ElMessageBox.confirm('确定要删除该分镜吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      try {
        await fetch(`http://localhost:8123/api/shot/remove`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ id: shot.shotId }),
        })

        ElMessage.success('删除成功')
        fetchShots()
      } catch (error) {
        console.error('删除失败:', error)
      }
    })
    .catch(() => {})
}

const handleShotSubmit = async () => {
  if (!shotFormRef.value) return

  await shotFormRef.value.validate(async (valid) => {
    if (!valid) return

    try {
      const method = shotForm.value.shotId ? 'PUT' : 'POST'
      const url = shotForm.value.shotId
        ? `http://localhost:8123/api/shot/edit`
        : `http://localhost:8123/api/shot/add`

      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(shotForm.value),
      })

      const data = await response.json()
      if (data.code === 0) {
        ElMessage.success(shotForm.value.shotId ? '更新成功' : '创建成功')
        shotDialogVisible.value = false
        fetchShots()
      }
    } catch (error) {
      console.error('操作失败:', error)
    }
  })
}

const handleShotDialogClose = () => {
  shotFormRef.value?.resetFields()
}

const handleCommand = async (command: string) => {
  switch (command) {
    case 'duplicate':
      try {
        await ElMessageBox.confirm('确定要复制该项目吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info',
        })
        ElMessage.info('复制功能开发中')
      } catch (error) {
        if (error !== 'cancel') {
          console.error('复制失败:', error)
        }
      }
      break

    case 'export':
      ElMessage.info('导出功能开发中')
      break

    case 'archive':
      try {
        await ElMessageBox.confirm('确定要归档该项目吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
        ElMessage.success('已归档')
      } catch (error) {
        if (error !== 'cancel') {
          console.error('归档失败:', error)
        }
      }
      break

    case 'delete':
      try {
        await ElMessageBox.confirm(
          `确定要删除项目"${currentProject.value?.projectName}"吗？此操作不可恢复！`,
          '警告',
          {
            confirmButtonText: '确定删除',
            cancelButtonText: '取消',
            type: 'warning',
          },
        )
        ElMessage.success('删除成功')
        router.push('/project')
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
        }
      }
      break
  }
}

const handleVersionClick = (version: any) => {
  ElMessage.info(`切换到版本 v${version.version}`)
}

const handleUploadMaterial = () => {
  ElMessage.info('上传功能开发中')
}

const handleFilterByRole = (role: string) => {
  ElMessage.info(`筛选${role}类型素材`)
}

const handleAIGenerateShots = () => {
  ElMessage.info('AI文本生成分镜功能开发中')
}

const handleAIPolishScript = () => {
  ElMessage.info('AI润色剧本功能开发中')
}

const handleAIGenerateImage = () => {
  ElMessage.info('AI生成图片功能开发中')
}

const handleAIGenerateVideo = () => {
  ElMessage.info('AI生成视频功能开发中')
}

const calculateProgress = () => {
  if (!currentProject.value?.shotCount) return 0
  return Math.round((currentProject.value.shotCount / 10) * 100)
}

const getProgressColor = () => {
  const progress = calculateProgress()
  if (progress === 100) return '#67c23a'
  if (progress >= 50) return '#667eea'
  return '#e6a23c'
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
  return statusMap[status || ''] || status
}

const getShotStatusType = (status: string) => {
  const statusMap: Record<string, any> = {
    draft: 'info',
    inProgress: 'primary',
    completed: 'success',
  }
  return statusMap[status] || 'info'
}

const getShotStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    draft: '草稿',
    inProgress: '进行中',
    completed: '已完成',
  }
  return statusMap[status] || status
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

const getActivityIcon = (type: string) => {
  const iconMap: Record<string, any> = {
    create: 'Plus',
    edit: 'Edit',
    upload: 'Upload',
    delete: 'Delete',
    download: 'Download',
  }
  return iconMap[type] || 'InfoFilled'
}

const handleDragStart = (event: DragEvent, shot: any) => {
  event.dataTransfer?.setData('shotId', shot.shotId)
}

const handleDragOver = (event: DragEvent) => {
  event.preventDefault()
}

const handleDrop = async (event: DragEvent, targetShot: any) => {
  event.preventDefault()
  const sourceId = event.dataTransfer?.getData('shotId')
  if (!sourceId || sourceId === targetShot.shotId) return

  try {
    ElMessage.success('分镜排序已更新')
    // TODO: 调用API更新排序
  } catch (error) {
    console.error('排序失败:', error)
  }
}
</script>

<style scoped>
.workspace-container {
  width: 100%;
  height: 100vh;
  background: var(--bg-page);
  display: flex;
  flex-direction: column;
}

.workspace-header {
  padding: 16px 24px;
  background: var(--bg-card);
  border-bottom: 1px solid var(--border-base);
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-shrink: 0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.project-info h1 {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.project-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--text-secondary);
  font-size: 14px;
}

.meta-divider {
  color: var(--text-placeholder);
}

.header-actions {
  display: flex;
  gap: 12px;
}

.workspace-content {
  flex: 1;
  overflow: hidden;
  display: flex;
}

.sidebar {
  width: 280px;
  padding: 16px;
  background: var(--bg-card);
  border-right: 1px solid var(--border-base);
  overflow-y: auto;
  flex-shrink: 0;
}

.sidebar-section {
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.section-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.project-detail {
  padding: 12px;
  background: var(--bg-hover);
  border-radius: 8px;
}

.detail-item {
  margin-bottom: 12px;
}

.detail-item:last-child {
  margin-bottom: 0;
}

.detail-item .label {
  display: block;
  font-size: 13px;
  color: var(--text-secondary);
  margin-bottom: 4px;
}

.detail-item .value {
  font-size: 14px;
  color: var(--text-primary);
}

.progress-with-text {
  display: flex;
  align-items: center;
  gap: 8px;
}

.progress-text {
  font-size: 13px;
  color: var(--text-regular);
  white-space: nowrap;
}

.detail-item p.description {
  margin: 0;
  font-size: 13px;
  color: var(--text-secondary);
  line-height: 1.5;
}

.version-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.version-item {
  padding: 12px;
  background: var(--bg-hover);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.version-item:hover {
  background: var(--bg-active);
}

.version-item.active {
  border-color: var(--primary-color);
  background: var(--primary-light);
}

.version-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.version-number {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
}

.version-date {
  font-size: 12px;
  color: var(--text-secondary);
}

.version-activities .activity {
  font-size: 13px;
  color: var(--text-regular);
}

.material-stats {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 16px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--text-regular);
}

.material-tags {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 8px;
}

.tag-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  background: var(--bg-hover);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tag-item:hover {
  background: var(--bg-active);
}

.ai-tools {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.tool-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: var(--bg-hover);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.tool-item:hover {
  border-color: var(--primary-color);
  background: var(--bg-active);
}

.tool-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.tool-content {
  flex: 1;
}

.tool-content h4 {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 4px 0;
}

.tool-content p {
  font-size: 12px;
  color: var(--text-secondary);
  margin: 0;
}

.main-content {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}

.shot-list {
  min-height: 100%;
}

.shot-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.shot-list-header h2 {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.header-meta {
  color: var(--text-secondary);
  font-size: 14px;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.shot-items {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.shot-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: var(--bg-card);
  border-radius: 8px;
  box-shadow: var(--shadow-base);
  transition: all 0.3s ease;
  border: 1px solid var(--border-base);
}

.shot-item:hover {
  box-shadow: var(--shadow-lg);
  border-color: var(--primary-color);
}

.shot-drag-handle {
  color: var(--text-placeholder);
  cursor: move;
  flex-shrink: 0;
}

.shot-status {
  flex-shrink: 0;
}

.shot-content {
  flex: 1;
  min-width: 0;
}

.shot-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 4px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.shot-description {
  font-size: 13px;
  color: var(--text-secondary);
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.shot-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 12px;
  color: var(--text-regular);
}

.shot-meta .meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.shot-meta .meta-divider {
  color: var(--text-placeholder);
}

.shot-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

.empty-shots {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  color: var(--text-secondary);
}

.right-panel {
  width: 280px;
  padding: 16px;
  background: var(--bg-card);
  border-left: 1px solid var(--border-base);
  overflow-y: auto;
  flex-shrink: 0;
}

.panel-section {
  margin-bottom: 24px;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.panel-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.progress-section {
  text-align: center;
}

.progress-circle {
  margin-bottom: 16px;
}

.progress-stats {
  display: flex;
  flex-direction: column;
  gap: 12px;
  text-align: left;
}

.stat-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-label {
  font-size: 13px;
  color: var(--text-secondary);
}

.stat-value {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.activity-item {
  display: flex;
  gap: 12px;
  padding: 12px;
  background: var(--bg-hover);
  border-radius: 8px;
}

.activity-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--primary-light);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-color);
  flex-shrink: 0;
}

.activity-content {
  flex: 1;
  min-width: 0;
}

.activity-text {
  font-size: 14px;
  color: var(--text-primary);
  margin: 0 0 4px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.activity-time {
  font-size: 12px;
  color: var(--text-secondary);
}
</style>
