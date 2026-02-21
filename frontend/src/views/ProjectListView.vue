<template>
  <div class="project-list-container">
    <div class="project-header">
      <h1>项目列表</h1>
      <div class="header-actions">
        <el-button type="primary" @click="handleCreateProject">
          <el-icon><Plus /></el-icon>
          新建项目
        </el-button>
      </div>
    </div>

    <div class="project-content">
      <div class="filter-section">
        <el-input
          v-model="searchQuery"
          placeholder="搜索项目..."
          prefix-icon="Search"
          clearable
          @change="handleSearch"
        />
        <el-select
          v-model="statusFilter"
          placeholder="项目状态"
          clearable
          @change="handleFilter"
          style="margin-left: 12px"
        >
          <el-option label="全部" value="" />
          <el-option label="进行中" value="进行中" />
          <el-option label="已完成" value="已完成" />
          <el-option label="草稿" value="草稿" />
        </el-select>
        <el-select
          v-model="typeFilter"
          placeholder="项目类型"
          clearable
          @change="handleFilter"
          style="margin-left: 12px"
        >
          <el-option label="全部" value="" />
          <el-option label="宣传片" value="宣传片" />
          <el-option label="教程" value="教程" />
          <el-option label="短视频" value="短视频" />
          <el-option label="广告" value="广告" />
        </el-select>
      </div>

      <div v-loading="loading" class="project-grid">
        <div
          v-for="project in filteredProjects"
          :key="project.id"
          class="project-card"
          @click="handleProjectClick(project)"
        >
          <div class="project-cover">
            <el-image
              :src="project.coverUrl || defaultCover"
              :preview-src-list="[project.coverUrl]"
              fit="cover"
              lazy
            >
              <template #error>
                <div class="image-error">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
            <div class="project-status" :class="project.projectStatus">
              {{ project.projectStatus }}
            </div>
          </div>

          <div class="project-info">
            <h3 class="project-title">{{ project.projectName }}</h3>
            <p class="project-description">{{ project.projectDescription }}</p>

            <div class="project-stats">
              <div class="stat-item">
                <el-icon><Film /></el-icon>
                <span>镜头数: {{ project.shotCount || 0 }}</span>
              </div>
              <div class="stat-item">
                <el-icon><Timer /></el-icon>
                <span>时长: {{ formatDuration(project.totalDuration) }}</span>
              </div>
            </div>

            <div class="project-meta">
              <span class="meta-text">{{ formatDate(project.createTime) }}</span>
            </div>
          </div>

          <div class="project-actions">
            <el-dropdown
              trigger="click"
              @command="(command: any) => handleAction(command, project)"
            >
              <el-button text>
                <el-icon><More /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :command="['edit', project]">编辑</el-dropdown-item>
                  <el-dropdown-item :command="['duplicate', project]">复制</el-dropdown-item>
                  <el-dropdown-item divided :command="['delete', project]">删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>

      <div v-if="filteredProjects.length === 0 && !loading" class="empty-state">
        <el-empty description="暂无项目" />
      </div>
    </div>

    <el-dialog
      v-model="createDialogVisible"
      title="新建项目"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form ref="createFormRef" :model="createForm" :rules="createRules" label-width="80px">
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="createForm.projectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目类型" prop="projectType">
          <el-select v-model="createForm.projectType" placeholder="请选择项目类型">
            <el-option label="宣传片" value="宣传片" />
            <el-option label="教程" value="教程" />
            <el-option label="短视频" value="短视频" />
            <el-option label="广告" value="广告" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目描述" prop="projectDescription">
          <el-input
            v-model="createForm.projectDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入项目描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="creating" @click="handleCreateConfirm">
          创建
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Picture, Film, Timer, More } from '@element-plus/icons-vue'
import { useProjectStore } from '@/stores/project'

const router = useRouter()
const projectStore = useProjectStore()

const loading = ref(false)
const creating = ref(false)
const searchQuery = ref('')
const statusFilter = ref('')
const typeFilter = ref('')
const createDialogVisible = ref(false)
const defaultCover = 'https://via.placeholder.com/300x200?text=Project+Cover'

const createFormRef = ref()
const createForm = reactive({
  projectName: '',
  projectDescription: '',
  projectType: '',
  coverUrl: '',
  shotCount: 0,
  totalDuration: 0,
  isPublic: false,
})

const filteredProjects = computed(() => {
  return projectStore.projects.filter((project) => {
    const matchSearch =
      !searchQuery.value ||
      project.projectName?.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      project.projectDescription?.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchStatus = !statusFilter.value || project.projectStatus === statusFilter.value
    const matchType = !typeFilter.value || project.projectType === typeFilter.value
    return matchSearch && matchStatus && matchType
  })
})

const createRules = {
  projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  projectType: [{ required: true, message: '请选择项目类型', trigger: 'change' }],
}

const formatDuration = (duration?: number) => {
  if (!duration) return '0:00'
  const minutes = Math.floor(duration / 60)
  const seconds = duration % 60
  return `${minutes}:${seconds.toString().padStart(2, '0')}`
}

const formatDate = (dateStr?: string) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

const loadProjects = async () => {
  loading.value = true
  try {
    const query: any = {}
    if (searchQuery.value) query.projectName = searchQuery.value
    if (statusFilter.value) query.projectStatus = statusFilter.value
    if (typeFilter.value) query.projectType = typeFilter.value

    await projectStore.fetchProjects(query)
  } catch (error: any) {
    ElMessage.error(error.message || '加载项目失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  loadProjects()
}

const handleFilter = () => {
  loadProjects()
}

const handleProjectClick = (project: any) => {
  router.push(`/projects/${project.id}`)
}

const handleAction = async (command: string | string[], project: any) => {
  if (command === 'delete') {
    try {
      await ElMessageBox.confirm(`确定要删除项目"${project.projectName}"吗？`, '删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
      await projectStore.deleteProjectAction(project.id!)
      ElMessage.success('删除成功')
    } catch (error: any) {
      if (error !== 'cancel') {
        ElMessage.error(error.message || '删除失败')
      }
    }
  } else if (command === 'edit') {
    router.push(`/projects/${project.id}`)
  } else if (command === 'duplicate') {
    handleDuplicate(project)
  }
}

const handleDuplicate = async (project: any) => {
  try {
    await projectStore.addProject({
      projectName: `${project.projectName} - 副本`,
      projectDescription: project.projectDescription,
      projectType: project.projectType,
      coverUrl: project.coverUrl,
      shotCount: 0,
      totalDuration: 0,
      isPublic: false,
    })
    ElMessage.success('复制成功')
  } catch (error: any) {
    ElMessage.error(error.message || '复制失败')
  }
}

const handleCreateProject = () => {
  createFormRef.value?.resetFields()
  createForm.coverUrl = ''
  createDialogVisible.value = true
}

const handleDialogClose = () => {
  createFormRef.value?.resetFields()
}

const handleCreateConfirm = async () => {
  if (!createFormRef.value) return

  await createFormRef.value.validate(async (valid: boolean) => {
    if (!valid) return

    creating.value = true
    try {
      await projectStore.addProject(createForm)
      createDialogVisible.value = false
      ElMessage.success('创建成功')
      loadProjects()
    } catch (error: any) {
      ElMessage.error(error.message || '创建失败')
    } finally {
      creating.value = false
    }
  })
}

onMounted(() => {
  loadProjects()
})
</script>

<style scoped>
.project-list-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.project-header h1 {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.project-content {
  padding: 24px;
}

.filter-section {
  margin-bottom: 24px;
  display: flex;
  align-items: center;
}

.project-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.project-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.project-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
}

.project-cover {
  position: relative;
  width: 100%;
  height: 180px;
  overflow: hidden;
}

.project-cover :deep(.el-image) {
  width: 100%;
  height: 100%;
}

.image-error {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #909399;
  font-size: 40px;
}

.project-status {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  color: white;
  background: rgba(0, 0, 0, 0.6);
}

.project-status:empty {
  display: none;
}

.project-info {
  padding: 16px;
}

.project-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.project-description {
  font-size: 14px;
  color: #909399;
  margin: 0 0 12px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  height: 42px;
}

.project-stats {
  display: flex;
  gap: 16px;
  margin-bottom: 12px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #606266;
}

.project-meta {
  font-size: 12px;
  color: #909399;
}

.meta-text {
  display: flex;
  align-items: center;
  gap: 4px;
}

.project-actions {
  position: absolute;
  top: 12px;
  left: 12px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}
</style>
