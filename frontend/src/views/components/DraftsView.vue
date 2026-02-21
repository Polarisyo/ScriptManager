<template>
  <div class="drafts-container">
    <div class="drafts-header">
      <h2>视频草稿</h2>
      <div class="header-actions">
        <el-button type="primary" @click="handleGenerateDraft">
          <el-icon><VideoCamera /></el-icon>
          生成新草稿
        </el-button>
      </div>
    </div>

    <div v-loading="loading" class="drafts-content">
      <div v-if="drafts.length === 0" class="empty-state">
        <el-empty description="暂无草稿，点击上方按钮生成新草稿" />
      </div>

      <div v-else class="drafts-list">
        <div
          v-for="draft in drafts"
          :key="draft.id"
          class="draft-card"
          @click="handleViewDraft(draft)"
        >
          <div class="draft-preview">
            <div class="draft-thumbnail">
              <el-icon class="video-icon"><VideoPlay /></el-icon>
              <div class="draft-info">
                <h4>{{ draft.draftVersion }}.0 - {{ draft.draftUrl ? '已生成' : '生成中' }}</h4>
                <p class="draft-duration">{{ formatDuration(draft.duration) }}</p>
              </div>
            </div>
            <div class="draft-status" :class="draft.status">
              {{ draft.status }}
            </div>
          </div>

          <div class="draft-meta">
            <span class="meta-text">{{ formatDate(draft.generatedAt) }}</span>
            <span class="meta-text">{{ draft.quality || '1080P' }}</span>
          </div>

          <div class="draft-actions">
            <el-button v-if="draft.status === '生成完成'" text @click.stop="handleDownload(draft)">
              下载
            </el-button>
            <el-button text @click.stop="handleRegenerate(draft)"> 重新生成 </el-button>
            <el-dropdown
              trigger="click"
              @command="(command: any) => handleDraftCommand(command, draft)"
            >
              <el-button text>
                <el-icon><More /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :command="['share', draft]">分享</el-dropdown-item>
                  <el-dropdown-item :command="['settings', draft]">设置</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="generateDialogVisible" title="生成视频草稿" width="600px">
      <el-form ref="formRef" :model="generateForm" :rules="generateRules" label-width="100px">
        <el-form-item label="视频风格" prop="style">
          <el-select v-model="generateForm.style" placeholder="选择视频风格">
            <el-option label="电影感" value="电影感" />
            <el-option label="动画风" value="动画风" />
            <el-option label="写实风" value="写实风" />
            <el-option label="短视频风格" value="短视频风格" />
          </el-select>
        </el-form-item>

        <el-form-item label="时长" prop="duration">
          <el-input-number v-model="generateForm.duration" :min="5" :max="120" :step="5" />
          <span style="margin-left: 8px">秒</span>
        </el-form-item>

        <el-form-item label="帧率" prop="fps">
          <el-select v-model="generateForm.fps" placeholder="选择帧率">
            <el-option label="24 fps" value="24" />
            <el-option label="30 fps" value="30" />
            <el-option label="60 fps" value="60" />
          </el-select>
        </el-form-item>

        <el-form-item label="分辨率" prop="resolution">
          <el-select v-model="generateForm.resolution" placeholder="选择分辨率">
            <el-option label="720P" value="720P" />
            <el-option label="1080P" value="1080P" />
            <el-option label="2K" value="2K" />
            <el-option label="4K" value="4K" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="generateDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="generating" @click="handleConfirmGenerate">
          生成
        </el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="previewDialogVisible"
      title="草稿预览"
      width="80%"
      @close="previewDialogVisible = false"
    >
      <div v-if="currentDraft" class="preview-content">
        <video
          :src="currentDraft.draftUrl"
          controls
          ref="videoPlayer"
          @timeupdate="handleTimeUpdate"
        />
        <div class="preview-info">
          <h3>{{ currentDraft.draftVersion }}.0 版本</h3>
          <p>{{ currentDraft.status }}</p>
          <p>时长: {{ formatDuration(currentDraft.duration) }}</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { VideoCamera, VideoPlay, More } from '@element-plus/icons-vue'

interface VideoDraft {
  id?: number
  draftVersion?: number
  draftUrl?: string
  status?: string
  generatedAt?: string
  duration?: number
  quality?: string
  parameters?: {
    style?: string
    duration?: number
    fps?: number
    resolution?: string
  }
}

const emit = defineEmits<{
  refresh: []
}>()

const loading = ref(false)
const generating = ref(false)
const generateDialogVisible = ref(false)
const previewDialogVisible = ref(false)
const videoPlayer = ref()

const drafts = ref<VideoDraft[]>([
  {
    id: 1,
    draftVersion: 1,
    status: '生成完成',
    generatedAt: '2024-01-15',
    duration: 180,
    quality: '1080P',
    draftUrl: 'https://via.placeholder.com/800x450?text=Draft+Preview',
    parameters: {
      style: '电影感',
      duration: 180,
      fps: 30,
      resolution: '1080P',
    },
  },
])

const currentDraft = ref<VideoDraft | null>(null)

const generateForm = reactive({
  style: '电影感',
  duration: 60,
  fps: '30',
  resolution: '1080P',
})

const generateRules = {
  style: [{ required: true, message: '请选择视频风格', trigger: 'change' }],
  duration: [{ required: true, message: '请输入时长', trigger: 'blur' }],
  fps: [{ required: true, message: '请选择帧率', trigger: 'change' }],
  resolution: [{ required: true, message: '请选择分辨率', trigger: 'change' }],
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

const loadDrafts = async () => {
  loading.value = true
  try {
    // 这里可以从API加载草稿数据
  } catch (error: any) {
    ElMessage.error(error.message || '加载草稿失败')
  } finally {
    loading.value = false
  }
}

const handleGenerateDraft = () => {
  generateForm.style = '电影感'
  generateForm.duration = 60
  generateForm.fps = '30'
  generateForm.resolution = '1080P'
  generateDialogVisible.value = true
}

const handleConfirmGenerate = async () => {
  generating.value = true
  try {
    // 这里处理生成逻辑
    ElMessage.success('草稿生成任务已提交，请稍候')
    generateDialogVisible.value = false
    loadDrafts()
  } catch (error: any) {
    ElMessage.error(error.message || '生成失败')
  } finally {
    generating.value = false
  }
}

const handleViewDraft = (draft: VideoDraft) => {
  currentDraft.value = draft
  previewDialogVisible.value = true
}

const handleDownload = (draft: VideoDraft) => {
  ElMessage.success('下载开始')
}

const handleRegenerate = (draft: VideoDraft) => {
  currentDraft.value = draft
  handleGenerateDraft()
}

const handleDraftCommand = (command: string | string[], draft: VideoDraft) => {
  if (command === 'share') {
    ElMessage.success('分享链接已复制')
  } else if (command === 'settings') {
    ElMessage.info('打开设置面板')
  }
}

const handleTimeUpdate = () => {
  // 处理时间更新
}

onMounted(() => {
  loadDrafts()
})
</script>

<style scoped>
.drafts-container {
  padding: 24px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.drafts-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.drafts-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.drafts-content {
  min-height: 400px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.drafts-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.draft-card {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
}

.draft-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.draft-preview {
  position: relative;
  height: 180px;
  background: #f5f7fa;
}

.video-icon {
  font-size: 48px;
  color: #409eff;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.draft-thumbnail {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.draft-info {
  text-align: center;
}

.draft-info h4 {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 4px 0;
}

.draft-info p {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.draft-status {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  color: white;
  background: rgba(0, 0, 0, 0.6);
}

.draft-status:empty {
  display: none;
}

.draft-status:has([class]) {
  background: #67c23a;
}

.draft-meta {
  padding: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fafafa;
  border-top: 1px solid #ebeef5;
}

.meta-text {
  font-size: 13px;
  color: #606266;
}

.draft-actions {
  display: flex;
  justify-content: space-between;
  padding: 8px 12px;
}

.preview-content {
  max-width: 800px;
  margin: 0 auto;
  text-align: center;
}

.preview-content :deep(video) {
  max-width: 100%;
  max-height: 600px;
  margin: 0 auto;
  border-radius: 8px;
}

.preview-info {
  margin-top: 20px;
}

.preview-info h3 {
  font-size: 18px;
  color: #303133;
  margin: 0 0 12px 0;
}

.preview-info p {
  color: #909399;
  margin: 8px 0;
}
</style>
