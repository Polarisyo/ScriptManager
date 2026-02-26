/**
 * 用户认证相关类型定义
 *
 * 定义了用户登录、注册、查询等相关的TypeScript接口类型
 */

export interface LoginRequest {
  userAccount: string // 用户账号/邮箱
  userPassword: string // 用户密码
}

export interface RegisterRequest {
  userAccount: string // 用户账号/邮箱
  userPassword: string // 用户密码
  checkPassword: string // 确认密码
}

export interface UserInfo {
  id?: number
  userAccount?: string
  userName?: string
  userAvatar?: string
  userProfile?: string
  userRole?: string
  subscriptionPlan?: string
  aiCredits?: number
  lastLoginTime?: string
}

export type UserLoginRequest = {
  userAccount?: string
  userPassword?: string
}

export type UserRegisterRequest = {
  userAccount?: string
  userPassword?: string
  checkPassword?: string
}

export type UserEditRequest = {
  userId?: number
  userAccount?: string
  userName?: string
  userAvatar?: string
  userProfile?: string
}

/**
 * 项目添加请求类型
 * 用于创建新项目时提交的数据
 */
export interface ProjectAddRequest {
  projectName?: string // 项目名称
  projectDescription?: string // 项目描述
  projectType?: string // 项目类型（宣传片/教程/短视频等）
  coverUrl?: string // 项目封面URL
  shotCount?: number // 镜头数量
  totalDuration?: number // 总时长（秒）
  isPublic?: boolean // 是否公开
}

/**
 * 项目编辑请求类型
 * 用于编辑项目时提交的数据
 */
export interface ProjectEditRequest {
  projectName?: string // 项目名称
  projectDescription?: string // 项目描述
  projectType?: string // 项目类型
  coverUrl?: string // 封面URL
  shotCount?: number // 镜头数量
  totalDuration?: number // 总时长
  progressRate?: number // 完成进度（0-100）
  isPublic?: boolean // 是否公开
  projectStatus?: string // 项目状态（进行中/已完成/草稿）
}

/**
 * 项目查询请求类型
 * 用于查询项目列表时的筛选条件
 */
export interface ProjectQueryRequest {
  projectName?: string // 项目名称（用于模糊搜索）
  projectType?: string // 项目类型
  isPublic?: boolean // 是否公开
  projectStatus?: string // 项目状态
  userId?: number // 用户ID
}

/**
 * 项目视图对象
 * 后端返回的完整项目信息
 */
export interface Project {
  id?: number // 项目ID
  projectName?: string // 项目名称
  projectDescription?: string // 项目描述
  projectType?: string // 项目类型
  projectStatus?: string // 项目状态
  userId?: number // 用户ID
  coverUrl?: string // 封面URL
  shotCount?: number // 镜头数量
  totalDuration?: number // 总时长
  progressRate?: number // 完成进度
  isPublic?: boolean // 是否公开
  user?: UserInfo // 创建用户信息
  createTime?: string // 创建时间
  editTime?: string // 编辑时间
}

/**
 * 分镜剧本类型
 */
export interface Shot {
  id?: number // 分镜ID
  shotName?: string // 镜头名称
  sceneDescription?: string // 场景描述
  cameraAngle?: string // 摄像机角度
  character?: string // 角色
  actionDescription?: string // 动作描述
  dialogue?: string // 台词
  lightingAtmosphere?: string // 灯光与氛围
  notes?: string // 备注
  duration?: number // 时长（秒）
  status?: string // 状态
  sceneImageUrls?: string[] // 场景图片URL列表
  videoDraftUrl?: string // 视频草稿URL
}

export interface ShotVO {
  shotId?: number
  projectId?: number
  shotTitle?: string
  shotDesc?: string
  duration?: number
  orderIndex?: number
  shotStatus?: string
  scriptContent?: string
}

export interface ShotAddRequest {
  userId?: number
  projectId?: number
  shotTitle?: string
  shotDesc?: string
  duration?: number
  orderIndex?: number
  shotStatus?: string
  scriptContent?: string
  shotOrder?: number[]
}

export interface ShotEditRequest {
  shotId?: number
  userId?: number
  projectId?: number
  shotTitle?: string
  shotDesc?: string
  duration?: number
  orderIndex?: number
  shotStatus?: string
  scriptContent?: string
  shotOrder?: number[]
}

export interface ShotQueryRequest {
  userId?: number
  projectId?: number
  shotTitle?: string
  shotDesc?: string
  duration?: number
  orderIndex?: number
  shotStatus?: string
  scriptContent?: string
}

/**
 * 素材类型
 */
export interface Material {
  id?: number // 素材ID
  materialName?: string // 素材名称
  materialType?: string // 素材类型（图片/视频/音频）
  materialUrl?: string // 素材URL
  createdAt?: string // 创建时间
  usageCount?: number // 使用次数
  tags?: string[] // 标签
  status?: string // 状态
}

/**
 * 视频草稿类型
 */
export interface VideoDraft {
  id?: number
  projectId?: number
  shotId?: number
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

/**
 * AI服务类型
 */
export interface AIService {
  id?: number
  serviceType?: string
  serviceName?: string
  creditsUsed?: number
  createdAt?: string
  result?: string
}

/**
 * 通用响应类型
 */
export interface ProgressResponse {
  code?: number
  data?: number
  message?: string
}
