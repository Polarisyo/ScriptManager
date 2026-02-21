<template>
  <div class="project-workspace-container">
    <div class="workspace-header">
      <div class="header-left">
        <el-button text @click="handleBack">
          <el-icon><ArrowLeft /></el-icon>
        </el-button>
        <div class="project-info">
          <h2>{{ currentProject?.projectName || '项目工作空间' }}</h2>
          <p>{{ currentProject?.projectDescription }}</p>
        </div>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleSaveProject"> 保存 </el-button>
      </div>
    </div>

    <div class="workspace-tabs">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="分镜剧本" name="script">
          <ShotScriptView
            v-if="currentProject"
            :project-id="currentProject.id"
            @refresh="loadProject"
          />
        </el-tab-pane>

        <el-tab-pane label="素材库" name="materials">
          <MaterialsView
            v-if="currentProject"
            :project-id="currentProject.id"
            @refresh="loadProject"
          />
        </el-tab-pane>

        <el-tab-pane label="视频草稿" name="drafts">
          <DraftsView
            v-if="currentProject"
            :project-id="currentProject.id"
            @refresh="loadProject"
          />
        </el-tab-pane>

        <el-tab-pane label="AI工具" name="ai">
          <AIToolsView
            v-if="currentProject"
            :project-id="currentProject.id"
            @refresh="loadProject"
          />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { useProjectStore } from '@/stores/project'

const router = useRouter()
const route = useRoute()
const projectStore = useProjectStore()

const activeTab = ref('script')

const currentProject = ref<any>(null)

const loadProject = async () => {
  const projectId = route.params.id as string
  if (!projectId) return

  const project = projectStore.projects.find((p) => p.id === Number(projectId))
  if (project) {
    currentProject.value = project
  } else {
    try {
      const res = await projectStore.editProjectAction(Number(projectId), {})
      if (res) {
        currentProject.value = projectStore.projects.find((p) => p.id === Number(projectId))
      }
    } catch (error: any) {
      ElMessage.error(error.message || '加载项目失败')
    }
  }
}

const handleBack = () => {
  router.push('/projects')
}

const handleSaveProject = async () => {
  try {
    if (currentProject.value) {
      await projectStore.editProjectAction(currentProject.value.id, currentProject.value)
      ElMessage.success('保存成功')
    }
  } catch (error: any) {
    ElMessage.error(error.message || '保存失败')
  }
}

onMounted(() => {
  loadProject()
})

watch(
  () => route.params.id,
  () => {
    loadProject()
  },
)
</script>

<style scoped>
.project-workspace-container {
  min-height: 100vh;
  background: #f5f7fa;
  display: flex;
  flex-direction: column;
}

.workspace-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
  flex: 1;
}

.project-info {
  flex: 1;
}

.project-info h2 {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 4px 0;
}

.project-info p {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.workspace-tabs {
  flex: 1;
  overflow: hidden;
}

.workspace-tabs :deep(.el-tabs__content) {
  height: calc(100vh - 120px);
  overflow-y: auto;
}

.workspace-tabs :deep(.el-tab-pane) {
  height: 100%;
}
</style>
