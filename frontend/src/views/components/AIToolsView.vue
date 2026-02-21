<template>
  <div class="ai-tools-container">
    <div class="ai-tools-header">
      <h2>AI工具箱</h2>
      <div class="header-info">
        <el-tag type="success">积分: {{ aiCredits }}</el-tag>
      </div>
    </div>

    <div class="ai-tools-content">
      <div class="ai-tools-grid">
        <div class="ai-tool-card" @click="handleOpenTool('script')">
          <div class="tool-icon script-icon">
            <el-icon><Document /></el-icon>
          </div>
          <h3>剧本分析</h3>
          <p>AI辅助剧本分析与优化</p>
          <div class="tool-tags">
            <el-tag size="small">情感分析</el-tag>
            <el-tag size="small">节奏分析</el-tag>
            <el-tag size="small">人物分析</el-tag>
          </div>
        </div>

        <div class="ai-tool-card" @click="handleOpenTool('shots')">
          <div class="tool-icon shots-icon">
            <el-icon><VideoCamera /></el-icon>
          </div>
          <h3>分镜生成</h3>
          <p>根据剧本自动生成分镜</p>
          <div class="tool-tags">
            <el-tag size="small">智能生成</el-tag>
            <el-tag size="small">批量处理</el-tag>
          </div>
        </div>

        <div class="ai-tool-card" @click="handleOpenTool('materials')">
          <div class="tool-icon materials-icon">
            <el-icon><Picture /></el-icon>
          </div>
          <h3>素材生成</h3>
          <p>AI生成场景图片素材</p>
          <div class="tool-tags">
            <el-tag size="small">场景图</el-tag>
            <el-tag size="small">多种风格</el-tag>
          </div>
        </div>

        <div class="ai-tool-card" @click="handleOpenTool('video')">
          <div class="tool-icon video-icon">
            <el-icon><VideoPlay /></el-icon>
          </div>
          <h3>视频生成</h3>
          <p>AI生成视频草稿</p>
          <div class="tool-tags">
            <el-tag size="small">快速生成</el-tag>
            <el-tag size="small">多种风格</el-tag>
          </div>
        </div>

        <div class="ai-tool-card" @click="handleOpenTool('summary')">
          <div class="tool-icon summary-icon">
            <el-icon><Star /></el-icon>
          </div>
          <h3>智能总结</h3>
          <p>AI智能总结剧情要点</p>
          <div class="tool-tags">
            <el-tag size="small">智能摘要</el-tag>
            <el-tag size="small">关键提取</el-tag>
          </div>
        </div>

        <div class="ai-tool-card" @click="handleOpenTool('review')">
          <div class="tool-icon review-icon">
            <el-icon><ChatDotRound /></el-icon>
          </div>
          <h3>剧本评审</h3>
          <p>AI自动评审剧本质量</p>
          <div class="tool-tags">
            <el-tag size="small">专业评审</el-tag>
            <el-tag size="small">优化建议</el-tag>
          </div>
        </div>
      </div>

      <div class="ai-section">
        <el-collapse v-model="activePanel">
          <el-collapse-item title="生成参数配置" name="settings">
            <div class="ai-settings">
              <el-form label-width="120px">
                <el-form-item label="通用风格">
                  <el-select v-model="settings.style" placeholder="选择风格">
                    <el-option label="电影感" value="电影感" />
                    <el-option label="动画风" value="动画风" />
                    <el-option label="写实风" value="写实风" />
                    <el-option label="赛博朋克" value="赛博朋克" />
                    <el-option label="水墨风格" value="水墨风格" />
                  </el-select>
                </el-form-item>

                <el-form-item label="详细程度">
                  <el-slider v-model="settings.detailedLevel" :min="1" :max="5" />
                  <div class="slider-labels">
                    <span>基础</span>
                    <span>中等</span>
                    <span>详细</span>
                  </div>
                </el-form-item>

                <el-form-item label="自动保存">
                  <el-switch v-model="settings.autoSave" />
                </el-form-item>
              </el-form>
            </div>
          </el-collapse-item>

          <el-collapse-item title="任务队列" name="queue">
            <div class="ai-queue">
              <div v-if="taskQueue.length === 0" class="queue-empty">
                <el-empty description="暂无进行中的任务" />
              </div>

              <div v-else class="queue-list">
                <div v-for="task in taskQueue" :key="task.id" class="queue-item">
                  <div class="queue-info">
                    <el-icon class="queue-icon" :class="task.status">
                      <component :is="getTaskIcon(task.taskType || 'other')" />
                    </el-icon>
                    <div class="queue-details">
                      <h4>{{ task.taskName }}</h4>
                      <p>{{ task.taskType }}</p>
                    </div>
                  </div>
                  <div class="queue-progress">
                    <el-progress
                      :percentage="task.progress"
                      :status="task.status === '完成' ? 'success' : undefined"
                    />
                    <span class="queue-status">{{ task.status }}</span>
                  </div>
                  <div class="queue-actions">
                    <el-button text type="danger" @click="handleCancelTask(task)"> 取消 </el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-collapse-item>

          <el-collapse-item title="历史记录" name="history">
            <div class="ai-history">
              <div v-if="historyRecords.length === 0" class="history-empty">
                <el-empty description="暂无历史记录" />
              </div>

              <div v-else class="history-list">
                <div v-for="record in historyRecords" :key="record.id" class="history-item">
                  <div class="history-info">
                    <el-icon><Clock /></el-icon>
                    <div class="history-details">
                      <h4>{{ record.taskName }}</h4>
                      <p>{{ record.generateTime }}</p>
                    </div>
                  </div>
                  <div class="history-actions">
                    <el-button text @click="handleViewRecord(record)"> 查看 </el-button>
                    <el-button text @click="handleDownloadRecord(record)"> 下载 </el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>

    <el-dialog
      v-model="toolDialogVisible"
      :title="`${getToolTitle(currentToolType || '')} - 参数配置`"
      width="600px"
    >
      <div v-if="currentTool" class="tool-config">
        <el-form label-width="120px">
          <el-form-item label="选择镜头">
            <el-select v-model="currentTool.shotId" placeholder="选择镜头">
              <el-option
                v-for="shot in shots"
                :key="shot.id"
                :label="shot.shotName"
                :value="shot.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="风格设置">
            <el-select v-model="currentTool.style" placeholder="选择风格">
              <el-option label="电影感" value="电影感" />
              <el-option label="动画风" value="动画风" />
              <el-option label="写实风" value="写实风" />
            </el-select>
          </el-form-item>

          <el-form-item label="详细程度">
            <el-slider v-model="currentTool.detailedLevel" :min="1" :max="5" />
          </el-form-item>

          <el-form-item label="数量">
            <el-input-number v-model="currentTool.count" :min="1" :max="10" :step="1" />
          </el-form-item>
        </el-form>
      </div>
      <div v-if="currentTool" class="tool-config">
        <el-form label-width="120px">
          <el-form-item label="选择镜头">
            <el-select v-model="currentTool.shotId" placeholder="选择镜头">
              <el-option
                v-for="shot in shots"
                :key="shot.id"
                :label="shot.shotName"
                :value="shot.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="风格设置">
            <el-select v-model="currentTool.style" placeholder="选择风格">
              <el-option label="电影感" value="电影感" />
              <el-option label="动画风" value="动画风" />
              <el-option label="写实风" value="写实风" />
            </el-select>
          </el-form-item>

          <el-form-item label="详细程度">
            <el-slider v-model="currentTool.detailedLevel" :min="1" :max="5" />
          </el-form-item>

          <el-form-item label="数量">
            <el-input-number v-model="currentTool.count" :min="1" :max="10" :step="1" />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <el-button @click="toolDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="processing" @click="handleConfirmTool">
          开始生成
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Document,
  VideoCamera,
  Picture,
  VideoPlay,
  Star,
  ChatDotRound,
  Clock,
  Check,
  Loading,
  Close,
} from '@element-plus/icons-vue'

interface AIRecord {
  id?: number
  taskName?: string
  taskType?: string
  generateTime?: string
  status?: string
}

interface TaskQueueItem {
  id?: number
  taskName?: string
  taskType?: string
  progress?: number
  status?: string
}

interface Props {
  projectId?: number
}

const props = defineProps<Props>()
const emit = defineEmits<{
  refresh: []
}>()

const aiCredits = ref(150)
const activePanel = ref(['settings'])
const toolDialogVisible = ref(false)
const processing = ref(false)
const currentToolType = ref('')
const currentTool = ref<any>(null)

const settings = reactive({
  style: '电影感',
  detailedLevel: 3,
  autoSave: true,
})

const shots = ref([
  { id: 1, shotName: '镜头1: 开场' },
  { id: 2, shotName: '镜头2: 产品展示' },
  { id: 3, shotName: '镜头3: 功能演示' },
])

const taskQueue = ref<TaskQueueItem[]>([
  {
    id: 1,
    taskName: '生成场景图',
    taskType: '场景图生成',
    progress: 75,
    status: '进行中',
  },
])

const historyRecords = ref<AIRecord[]>([
  {
    id: 1,
    taskName: '剧本分析',
    taskType: '剧本分析',
    generateTime: '2024-01-15 14:30',
    status: '完成',
  },
])

const toolConfig: Record<string, { title: string; icon: any }> = {
  script: { title: '剧本分析', icon: Document },
  shots: { title: '分镜生成', icon: VideoCamera },
  materials: { title: '素材生成', icon: Picture },
  video: { title: '视频生成', icon: VideoPlay },
  summary: { title: '智能总结', icon: Star },
  review: { title: '剧本评审', icon: ChatDotRound },
}

const aiToolsGrid = [
  { type: 'script', label: '剧本分析', description: 'AI辅助剧本分析与优化' },
  { type: 'shots', label: '分镜生成', description: '根据剧本自动生成分镜' },
  { type: 'materials', label: '素材生成', description: 'AI生成场景图片素材' },
  { type: 'video', label: '视频生成', description: 'AI生成视频草稿' },
  { type: 'summary', label: '智能总结', description: 'AI智能总结剧情要点' },
  { type: 'review', label: '剧本评审', description: 'AI自动评审剧本质量' },
]

const handleOpenTool = (type: string) => {
  currentToolType.value = type
  currentTool.value = {
    shotId: 1,
    style: '电影感',
    detailedLevel: 3,
    count: 4,
  } as any
  toolDialogVisible.value = true
}

const getToolTitle = (type: string | undefined) => {
  return type ? toolConfig[type]?.title || 'AI工具' : 'AI工具'
}

const getTaskIcon = (taskType: string) => {
  if (taskType.includes('图片') || taskType.includes('场景')) {
    return Picture
  } else if (taskType.includes('视频') || taskType.includes('分镜')) {
    return VideoCamera
  }
  return Document
}

const handleCancelTask = (task: TaskQueueItem) => {
  task.status = '已取消'
  ElMessage.success('任务已取消')
}

const handleViewRecord = (record: AIRecord) => {
  ElMessage.info('查看历史记录')
}

const handleDownloadRecord = (record: AIRecord) => {
  ElMessage.success('下载开始')
}

const handleConfirmTool = async () => {
  processing.value = true
  try {
    ElMessage.success('AI生成任务已提交')
    toolDialogVisible.value = false
    emit('refresh')
  } catch (error: any) {
    ElMessage.error(error.message || '生成失败')
  } finally {
    processing.value = false
  }
}
</script>

<style scoped>
.ai-tools-container {
  padding: 24px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.ai-tools-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.ai-tools-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.header-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.ai-tools-content {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.ai-tools-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.ai-tool-card {
  border: 1px solid #ebeef5;
  border-radius: 12px;
  padding: 24px;
  cursor: pointer;
  transition: all 0.3s;
  background: #fafafa;
}

.ai-tool-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.tool-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
}

.tool-icon :deep(.el-icon) {
  font-size: 32px;
}

.script-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.shots-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.materials-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.video-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: white;
}

.summary-icon {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  color: white;
}

.review-icon {
  background: linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%);
  color: white;
}

.ai-tool-card h3 {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
}

.ai-tool-card p {
  font-size: 14px;
  color: #909399;
  margin: 0 0 16px 0;
}

.tool-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.ai-section {
  background: #fafafa;
  border-radius: 8px;
  padding: 20px;
}

.ai-section :deep(.el-collapse-item__header) {
  font-weight: 600;
  color: #303133;
}

.ai-settings,
.ai-queue,
.ai-history {
  padding: 20px 0;
}

.queue-empty,
.history-empty {
  text-align: center;
  padding: 40px 0;
}

.queue-list,
.history-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.queue-item,
.history-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.queue-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.queue-icon {
  font-size: 32px;
}

.queue-icon.check {
  color: #67c23a;
}

.queue-icon.loading {
  color: #409eff;
  animation: rotate 1s linear infinite;
}

.queue-icon.close {
  color: #f56c6c;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.queue-details {
  display: flex;
  flex-direction: column;
}

.queue-details h4 {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 4px 0;
}

.queue-details p {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.queue-progress {
  flex: 1;
  max-width: 200px;
  margin: 0 20px;
}

.queue-status {
  margin-left: 20px;
  font-size: 14px;
  color: #606266;
}

.queue-actions {
  display: flex;
  gap: 8px;
}

.history-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.history-details {
  display: flex;
  flex-direction: column;
}

.history-details h4 {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 4px 0;
}

.history-details p {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.history-actions {
  display: flex;
  gap: 8px;
}

.tool-config {
  padding: 20px 0;
}

.tool-config :deep(.el-form-item) {
  margin-bottom: 20px;
}

.slider-labels {
  display: flex;
  justify-content: space-between;
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
}
</style>
