<template>
  <div class="create-project-container">
    <div class="page-header">
      <h1>新建项目</h1>
      <p>创建一个新的AI视频分镜项目</p>
    </div>

    <div class="content-layout">
      <div class="project-tips">
        <div class="tips-section">
          <h3>💡 创建提示</h3>
          <ul>
            <li>项目名称建议简洁明了，便于识别</li>
            <li>选择合适的项目类型有助于后续管理</li>
            <li>填写项目描述可以帮助您和团队成员了解项目背景</li>
            <li>封面图片可以提升项目的视觉效果</li>
            <li>设置合理的目标时长可以更好地规划分镜数量</li>
          </ul>
        </div>

        <div class="quick-actions">
          <h3>⚡ 快速操作</h3>
          <div class="action-buttons">
            <el-button type="primary" :icon="MagicStick" @click="handleAIGenerate">
              AI一键生成分镜
            </el-button>
            <el-button :icon="Upload" @click="handleUploadScript"> 导入剧本 </el-button>
          </div>
        </div>
      </div>

      <div class="project-form-container">
        <el-form ref="formRef" :model="projectForm" :rules="formRules" class="project-form">
          <el-form-item label="项目名称" prop="projectName">
            <el-input
              v-model="projectForm.projectName"
              placeholder="请输入项目名称"
              :prefix-icon="FolderOpened"
              clearable
            />
          </el-form-item>

          <el-form-item label="项目类型" prop="projectType">
            <el-select
              v-model="projectForm.projectType"
              placeholder="请选择项目类型"
              style="width: 100%"
            >
              <el-option label="短视频" value="短视频" />
              <el-option label="广告" value="广告" />
              <el-option label="宣传片" value="宣传片" />
              <el-option label="MV" value="MV" />
              <el-option label="纪录片" value="纪录片" />
              <el-option label="其他" value="其他" />
            </el-select>
          </el-form-item>

          <el-form-item label="项目描述" prop="projectDescription">
            <el-input
              v-model="projectForm.projectDescription"
              type="textarea"
              :rows="4"
              placeholder="请输入项目描述（可选）"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>

          <el-form-item label="封面图片" prop="coverUrl">
            <div class="cover-upload">
              <el-upload
                class="cover-uploader"
                action="/api/upload"
                :show-file-list="false"
                :on-success="handleCoverSuccess"
                :before-upload="beforeCoverUpload"
              >
                <img v-if="projectForm.coverUrl" :src="projectForm.coverUrl" class="cover-image" />
                <div v-else class="cover-placeholder">
                  <el-icon :size="48"><Plus /></el-icon>
                  <p>点击上传封面</p>
                  <p class="tips">支持 JPG、PNG 格式，建议尺寸 400x250</p>
                </div>
              </el-upload>
            </div>
          </el-form-item>

          <el-form-item label="项目状态" prop="projectStatus">
            <el-radio-group v-model="projectForm.projectStatus">
              <el-radio label="draft">草稿</el-radio>
              <el-radio label="inProgress">进行中</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="公开项目" prop="isPublic">
            <el-switch v-model="projectForm.isPublic" />
            <span class="form-tip">设置为公开后，其他用户可以查看您的项目</span>
          </el-form-item>

          <el-form-item label="目标时长" prop="targetDuration">
            <el-input-number
              v-model="projectForm.targetDuration"
              :min="30"
              :max="7200"
              :step="30"
              controls-position="right"
              style="width: 90%"
            />
            <span class="unit">秒</span>
          </el-form-item>

          <el-form-item label="分镜数量" prop="shotCount">
            <el-input-number
              v-model="projectForm.shotCount"
              :min="1"
              :max="100"
              :step="1"
              controls-position="right"
              style="width: 90%"
            />
            <span class="unit">个</span>
          </el-form-item>

          <el-form-item>
            <div class="submit">
              <el-button
                type="primary"
                size="large"
                :loading="loading"
                class="submit-button"
                @click="handleSubmit"
              >
                创建项目
              </el-button>
              <el-button class="submit-button" size="large" @click="handleCancel"> 取消 </el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { FolderOpened, Plus, MagicStick, Upload } from '@element-plus/icons-vue'
import { useProjectStore } from '@/stores/project'
import { useUserStore } from '@/stores/user'

const router = useRouter()

const formRef = ref<FormInstance>()
const loading = ref(false)
const userStore = useUserStore()
const loginUser = userStore.userInfo

const projectForm = reactive<API.ProjectAddRequest>({
  projectName: '',
  projectType: '',
  projectDescription: '',
  coverUrl: '',
  isPublic: false,
  totalDuration: 300,
  shotCount: 5,
  userId: loginUser?.id,
})

const formRules: FormRules = {
  projectName: [
    { required: true, message: '请输入项目名称', trigger: 'blur' },
    { min: 2, max: 50, message: '项目名称长度在2到50个字符', trigger: 'blur' },
  ],
  projectType: [{ required: true, message: '请选择项目类型', trigger: 'change' }],
  projectDescription: [{ max: 500, message: '描述不能超过500个字符', trigger: 'blur' }],
  projectStatus: [{ required: true, message: '请选择项目状态', trigger: 'change' }],
  targetDuration: [{ required: true, message: '请输入目标时长', trigger: 'blur' }],
  shotCount: [{ required: true, message: '请输入分镜数量', trigger: 'blur' }],
}

const handleCoverSuccess = (response: any, file: any) => {
  if (response.code === 0) {
    projectForm.coverUrl = response.data.url
    ElMessage.success('封面上传成功')
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

const beforeCoverUpload = (file: any) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('封面图片必须是图片格式！')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('封面图片大小不能超过5MB！')
    return false
  }
  return true
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      const res = await useProjectStore().createProject(projectForm)
      // 调用项目创建API

      if (res.code === 0 && res.data) {
        ElMessage.success('项目创建成功')
        router.push(`/project/${res.data}`)
      } else {
        throw new Error(res.message || '创建失败')
      }
    } catch (error: any) {
      ElMessage.error(error.message || '创建项目失败')
      console.error('创建项目失败:', error)
    } finally {
      loading.value = false
    }
  })
}

const handleCancel = () => {
  router.push('/project')
}

const handleAIGenerate = () => {
  ElMessage.info('AI一键生成分镜功能开发中')
}

const handleUploadScript = () => {
  ElMessage.info('导入剧本功能开发中')
}
</script>

<style scoped>
.create-project-container {
  width: 100%;
  min-height: 100vh;
  background: var(--bg-page);
  padding: 32px;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  overflow-x: hidden;
}

.page-header {
  margin-bottom: 32px;
}

.page-header h1 {
  font-size: 28px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 8px 0;
}

.page-header p {
  font-size: 14px;
  color: var(--text-secondary);
  margin: 0;
}

.content-layout {
  display: flex;
  gap: 32px;
  flex: 1;
  align-items: flex-start;
}

.project-tips {
  width: 320px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.project-form-container {
  flex: 1;
  display: flex;
  justify-content: center;
  max-width: 800px;
  margin: 0 auto;
  width: 100%;
}

.project-form {
  width: 100%;
  max-width: 600px;
  padding: 32px;
  background: var(--bg-card);
  border-radius: 16px;
  box-shadow: var(--shadow-base);
}

.cover-upload {
  width: 100%;
  height: 250px;
  border: 2px dashed var(--border-base);
  border-radius: 12px;
  overflow: hidden;
}

.cover-uploader {
  width: 100%;
  height: 100%;
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: var(--text-secondary);
  background: var(--bg-hover);
}

.cover-placeholder .el-icon {
  font-size: 48px;
  color: var(--primary-color);
  margin-bottom: 16px;
}

.cover-placeholder p {
  margin: 4px 0;
  font-size: 14px;
}

.cover-placeholder .tips {
  font-size: 12px;
  color: var(--text-placeholder);
  max-width: 80%;
}

.form-tip {
  /* margin-left: 12px; */
  font-size: 13px;
  color: var(--text-secondary);
}

.unit {
  margin-left: 8px;
  font-size: 13px;
  color: var(--text-regular);
}

.submit-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  margin: 0 0;
}
.submit {
  width: 100%;
  height: 48px;
  font-size: 16px;
  margin: 10px auto;
  text-align: center;
}

.tips-section,
.quick-actions {
  background: var(--bg-card);
  border-radius: 12px;
  padding: 20px;
  box-shadow: var(--shadow-base);
}

.tips-section h3,
.quick-actions h3 {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 0 0;
}

.tips-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.tips-section li {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 8px;
  padding-left: 16px;
  position: relative;
}

.tips-section li::before {
  content: '•';
  position: absolute;
  left: 0;
  color: var(--primary-color);
}

.quick-actions .action-buttons {
  display: flex;
  gap: 12px;
}

.quick-actions .action-buttons .el-button {
  flex: 1;
}

@media (max-width: 768px) {
  .create-project-container {
    padding: 16px;
  }

  .content-layout {
    flex-direction: column;
  }

  .project-tips {
    width: 100%;
  }
}
</style>
