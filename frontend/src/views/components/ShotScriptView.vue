<template>
  <div class="shot-script-container">
    <div class="script-header">
      <h2>{{ form.shotName || '新建镜头剧本' }}</h2>
      <div class="script-actions">
        <el-button @click="handleSave">保存</el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>
    </div>

    <div class="script-content">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" class="script-form">
        <el-form-item label="镜头名称" prop="shotName">
          <el-input v-model="form.shotName" placeholder="请输入镜头名称" />
        </el-form-item>

        <el-form-item label="场景描述" prop="sceneDescription">
          <el-input
            v-model="form.sceneDescription"
            type="textarea"
            :rows="4"
            placeholder="描述场景的主要内容和环境"
          />
        </el-form-item>

        <el-form-item label="摄像机角度" prop="cameraAngle">
          <el-select v-model="form.cameraAngle" placeholder="选择摄像机角度">
            <el-option label="平视" value="平视" />
            <el-option label="俯视" value="俯视" />
            <el-option label="仰视" value="仰视" />
            <el-option label="侧视" value="侧视" />
            <el-option label="特写" value="特写" />
            <el-option label="远景" value="远景" />
          </el-select>
        </el-form-item>

        <el-form-item label="角色" prop="character">
          <el-input v-model="form.character" placeholder="请输入角色名称" />
        </el-form-item>

        <el-form-item label="动作描述" prop="actionDescription">
          <el-input
            v-model="form.actionDescription"
            type="textarea"
            :rows="4"
            placeholder="描述角色的动作和移动"
          />
        </el-form-item>

        <el-form-item label="台词" prop="dialogue">
          <el-input v-model="form.dialogue" type="textarea" :rows="3" placeholder="角色的对白" />
        </el-form-item>

        <el-form-item label="灯光与氛围" prop="lightingAtmosphere">
          <el-input
            v-model="form.lightingAtmosphere"
            type="textarea"
            :rows="3"
            placeholder="描述灯光和氛围"
          />
        </el-form-item>

        <el-form-item label="备注" prop="notes">
          <el-input v-model="form.notes" type="textarea" :rows="2" placeholder="其他备注信息" />
        </el-form-item>

        <el-form-item label="镜头时长（秒）" prop="duration">
          <el-input-number
            v-model="form.duration"
            :min="0"
            :max="600"
            :step="1"
            placeholder="请输入时长"
          />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { Shot } from '@/types'

interface Props {
  projectId?: number
}

const props = defineProps<Props>()
const emit = defineEmits<{
  refresh: []
}>()

const formRef = ref()
const form = reactive<Partial<Shot>>({
  shotName: '',
  sceneDescription: '',
  cameraAngle: '',
  character: '',
  actionDescription: '',
  dialogue: '',
  lightingAtmosphere: '',
  notes: '',
  duration: 0,
})

const rules = {
  shotName: [{ required: true, message: '请输入镜头名称', trigger: 'blur' }],
  sceneDescription: [{ required: true, message: '请输入场景描述', trigger: 'blur' }],
  cameraAngle: [{ required: true, message: '请选择摄像机角度', trigger: 'change' }],
  duration: [{ required: true, message: '请输入镜头时长', trigger: 'blur' }],
}

const loadShot = () => {
  // 这里可以加载镜头数据
  form.shotName = '镜头1: 开场'
  form.sceneDescription = '展示未来城市的夜景，霓虹灯闪烁'
  form.cameraAngle = '平视'
  form.character = '主角'
  form.actionDescription = '主角从远处走来'
  form.dialogue = ''
  form.lightingAtmosphere = '赛博朋克风格，霓虹灯光'
  form.notes = ''
  form.duration = 5
}

const handleSave = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid: boolean) => {
    if (!valid) return
    ElMessage.success('保存成功')
    emit('refresh')
  })
}

const handleReset = () => {
  formRef.value?.resetFields()
  loadShot()
}

onMounted(() => {
  loadShot()
})
</script>

<style scoped>
.shot-script-container {
  padding: 24px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.script-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.script-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.script-actions {
  display: flex;
  gap: 12px;
}

.script-content {
  max-width: 900px;
}

.script-form {
  margin-top: 20px;
}

.script-form :deep(.el-form-item) {
  margin-bottom: 24px;
}
</style>
