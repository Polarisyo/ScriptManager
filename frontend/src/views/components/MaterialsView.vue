<template>
  <div class="materials-container">
    <div class="materials-header">
      <h2>素材库</h2>
      <div class="header-actions">
        <el-button type="primary" @click="handleUpload">
          <el-icon><Upload /></el-icon>
          上传素材
        </el-button>
        <el-button @click="handleAIGenerate">
          <el-icon><MagicStick /></el-icon>
          AI生成
        </el-button>
      </div>
    </div>

    <div class="materials-toolbar">
      <el-radio-group v-model="filterType" @change="handleFilter">
        <el-radio-button label="all">全部</el-radio-button>
        <el-radio-button label="image">图片</el-radio-button>
        <el-radio-button label="video">视频</el-radio-button>
        <el-radio-button label="audio">音频</el-radio-button>
      </el-radio-group>

      <el-input
        v-model="searchQuery"
        placeholder="搜索素材..."
        prefix-icon="Search"
        clearable
        @change="handleSearch"
      />
    </div>

    <div v-loading="loading" class="materials-grid">
      <div v-for="material in filteredMaterials" :key="material.id" class="material-card">
        <div class="material-preview" @click="handlePreview(material)">
          <el-image
            v-if="material.materialType === 'image'"
            :src="material.materialUrl || defaultImage"
            fit="cover"
            lazy
          >
            <template #error>
              <div class="image-error">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
          <div v-else-if="material.materialType === 'video'" class="video-preview">
            <el-icon class="play-icon"><VideoPlay /></el-icon>
            <div class="video-duration">{{ formatDuration(material.duration) }}</div>
          </div>
          <div v-else class="audio-preview">
            <el-icon><Headset /></el-icon>
          </div>
        </div>

        <div class="material-info">
          <h4 class="material-name">{{ material.materialName }}</h4>
          <span class="material-type">{{ getTypeLabel(material.materialType) }}</span>
          <div class="material-meta">
            <span class="meta-text">{{
              material.createdAt ? formatDate(material.createdAt) : ''
            }}</span>
          </div>
        </div>

        <div class="material-actions">
          <el-button text @click="handlePreview(material)">预览</el-button>
          <el-button text type="danger" @click="handleDelete(material)">删除</el-button>
        </div>
      </div>
    </div>

    <div v-if="filteredMaterials.length === 0 && !loading" class="empty-state">
      <el-empty description="暂无素材" />
    </div>

    <el-dialog
      v-model="previewDialogVisible"
      title="素材预览"
      width="80%"
      @close="previewDialogVisible = false"
    >
      <div v-if="currentMaterial" class="preview-content">
        <el-image
          v-if="currentMaterial.materialType === 'image'"
          :src="currentMaterial.materialUrl"
          fit="contain"
        />
        <video
          v-else-if="currentMaterial.materialType === 'video'"
          :src="currentMaterial.materialUrl"
          controls
        />
        <div v-else class="audio-content">
          <el-icon class="audio-icon" :size="80"><Headset /></el-icon>
          <p>{{ currentMaterial.materialName }}</p>
        </div>
      </div>
    </el-dialog>

    <el-dialog v-model="uploadDialogVisible" title="上传素材" width="500px">
      <el-upload
        drag
        :auto-upload="false"
        :on-change="handleFileChange"
        accept="image/*,video/*,audio/*"
      >
        <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip">支持 JPG、PNG、GIF、MP4、MP3 格式，单个文件不超过 500MB</div>
        </template>
      </el-upload>
      <template #footer>
        <el-button @click="uploadDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="uploading" @click="handleConfirmUpload">
          上传
        </el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="aiGenerateDialogVisible" title="AI生成素材" width="600px">
      <el-form ref="aiFormRef" :model="aiForm" :rules="aiRules" label-width="100px">
        <el-form-item label="生成类型" prop="type">
          <el-select v-model="aiForm.type" placeholder="选择生成类型">
            <el-option label="场景图" value="image" />
            <el-option label="视频片段" value="video" />
          </el-select>
        </el-form-item>

        <el-form-item label="风格" prop="style">
          <el-input v-model="aiForm.style" placeholder="例如：赛博朋克、写实、卡通" />
        </el-form-item>

        <el-form-item label="数量" prop="count">
          <el-input-number v-model="aiForm.count" :min="1" :max="10" :step="1" />
        </el-form-item>

        <el-form-item label="描述" prop="description">
          <el-input
            v-model="aiForm.description"
            type="textarea"
            :rows="4"
            placeholder="详细描述你要生成的素材内容"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="aiGenerateDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="generating" @click="handleConfirmGenerate">
          生成
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Upload,
  MagicStick,
  Search,
  Picture,
  VideoPlay,
  Headset,
  UploadFilled,
} from '@element-plus/icons-vue'

interface Material {
  id?: number
  materialName?: string
  materialType?: string
  materialUrl?: string
  createdAt?: string
  duration?: number
}

const emit = defineEmits<{
  refresh: []
}>()

const loading = ref(false)
const uploadDialogVisible = ref(false)
const previewDialogVisible = ref(false)
const aiGenerateDialogVisible = ref(false)
const uploading = ref(false)
const generating = ref(false)
const searchQuery = ref('')
const filterType = ref('all')

const materials = ref<Material[]>([
  {
    id: 1,
    materialName: '未来城市夜景.jpg',
    materialType: 'image',
    materialUrl: 'https://via.placeholder.com/400x300?text=Future+City',
    createdAt: '2024-01-15',
    duration: 0,
  },
  {
    id: 2,
    materialName: '角色特写.mp4',
    materialType: 'video',
    materialUrl: 'https://via.placeholder.com/400x300?text=Video+Clip',
    createdAt: '2024-01-14',
    duration: 30,
  },
])

const filteredMaterials = computed(() => {
  return materials.value.filter((material) => {
    const matchType = filterType.value === 'all' || material.materialType === filterType.value
    const matchSearch =
      !searchQuery.value ||
      material.materialName?.toLowerCase().includes(searchQuery.value.toLowerCase())
    return matchType && matchSearch
  })
})

const currentMaterial = ref<Material | null>(null)
const aiFormRef = ref()

const aiForm = reactive({
  type: 'image',
  style: '',
  count: 4,
  description: '',
})

const aiRules = {
  type: [{ required: true, message: '请选择生成类型', trigger: 'change' }],
  description: [{ required: true, message: '请输入描述', trigger: 'blur' }],
}

const defaultImage = 'https://via.placeholder.com/400x300?text=Material'

const getTypeLabel = (type?: string) => {
  const labels: Record<string, string> = {
    image: '图片',
    video: '视频',
    audio: '音频',
  }
  return type ? labels[type] || '未知' : '未知'
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

const loadMaterials = async () => {
  loading.value = true
  try {
    // 这里可以从API加载素材数据
  } catch (error: any) {
    ElMessage.error(error.message || '加载素材失败')
  } finally {
    loading.value = false
  }
}

const handleFilter = () => {
  loadMaterials()
}

const handleSearch = () => {
  loadMaterials()
}

const handlePreview = (material: Material) => {
  currentMaterial.value = material
  previewDialogVisible.value = true
}

const handleUpload = () => {
  uploadDialogVisible.value = true
}

const handleAIGenerate = () => {
  aiGenerateDialogVisible.value = true
}

const handleDelete = async (material: Material) => {
  try {
    await ElMessageBox.confirm(`确定要删除素材"${material.materialName}"吗？`, '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    materials.value = materials.value.filter((m) => m.id !== material.id)
    ElMessage.success('删除成功')
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

const handleFileChange = (file: any) => {
  console.log('File selected:', file)
}

const handleConfirmUpload = async () => {
  uploading.value = true
  try {
    // 这里处理上传逻辑
    ElMessage.success('上传成功')
    uploadDialogVisible.value = false
    emit('refresh')
  } catch (error: any) {
    ElMessage.error(error.message || '上传失败')
  } finally {
    uploading.value = false
  }
}

const handleConfirmGenerate = async () => {
  await aiFormRef.value?.validate(async (valid: boolean) => {
    if (!valid) return

    generating.value = true
    try {
      ElMessage.success('AI生成任务已提交，请稍候')
      aiGenerateDialogVisible.value = false
    } catch (error: any) {
      ElMessage.error(error.message || '生成失败')
    } finally {
      generating.value = false
    }
  })
}

onMounted(() => {
  loadMaterials()
})
</script>

<style scoped>
.materials-container {
  padding: 24px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.materials-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.materials-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.materials-toolbar {
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.materials-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}

.material-card {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
}

.material-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.material-preview {
  position: relative;
  width: 100%;
  height: 150px;
  overflow: hidden;
  background: #f5f7fa;
}

.image-error {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 40px;
}

.video-preview {
  position: relative;
  width: 100%;
  height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #409eff;
}

.video-preview:hover .play-icon {
  font-size: 64px;
}

.play-icon {
  font-size: 48px;
  transition: all 0.3s;
}

.video-duration {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.audio-preview {
  width: 100%;
  height: 150px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #67c23a;
}

.audio-preview :deep(.el-icon) {
  font-size: 48px;
  margin-bottom: 12px;
}

.material-info {
  padding: 12px;
}

.material-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.material-type {
  display: inline-block;
  padding: 2px 8px;
  background: #ecf5ff;
  color: #409eff;
  border-radius: 12px;
  font-size: 12px;
  margin-bottom: 8px;
}

.material-meta {
  font-size: 12px;
  color: #909399;
}

.meta-text {
  display: flex;
  align-items: center;
  gap: 4px;
}

.material-actions {
  display: flex;
  justify-content: space-between;
  padding: 8px 12px;
  background: #fafafa;
  border-top: 1px solid #ebeef5;
}

.preview-content {
  max-width: 800px;
  margin: 0 auto;
  text-align: center;
}

.preview-content :deep(.el-image) {
  max-height: 600px;
  max-width: 100%;
}

.audio-content {
  padding: 40px;
  text-align: center;
}

.audio-icon {
  color: #67c23a;
  margin-bottom: 16px;
}

.audio-content p {
  color: #606266;
  margin: 0;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

:deep(.el-upload-dragger) {
  padding: 40px;
}
</style>
