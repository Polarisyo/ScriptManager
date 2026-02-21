export interface LoginRequest {
  userAccount: string
  userPassword: string
}

export interface RegisterRequest {
  userAccount: string
  userPassword: string
  checkPassword: string
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

export interface ProjectAddRequest {
  projectName?: string
  projectDescription?: string
  projectType?: string
  coverUrl?: string
  shotCount?: number
  totalDuration?: number
  isPublic?: boolean
}

export interface ProjectEditRequest {
  projectName?: string
  projectDescription?: string
  projectType?: string
  coverUrl?: string
  shotCount?: number
  totalDuration?: number
  progressRate?: number
  isPublic?: boolean
  projectStatus?: string
}

export interface ProjectQueryRequest {
  projectName?: string
  projectType?: string
  isPublic?: boolean
  projectStatus?: string
}

export interface Project {
  id?: number
  projectName?: string
  projectDescription?: string
  projectType?: string
  projectStatus?: string
  userId?: number
  coverUrl?: string
  shotCount?: number
  totalDuration?: number
  progressRate?: number
  isPublic?: boolean
  user?: UserInfo
  createTime?: string
  editTime?: string
}

export interface Shot {
  id?: number
  shotName?: string
  sceneDescription?: string
  cameraAngle?: string
  character?: string
  actionDescription?: string
  dialogue?: string
  lightingAtmosphere?: string
  notes?: string
  duration?: number
  status?: string
  sceneImageUrls?: string[]
  videoDraftUrl?: string
}

export interface Material {
  id?: number
  materialName?: string
  materialType?: string
  materialUrl?: string
  createdAt?: string
  usageCount?: number
  tags?: string[]
  status?: string
}

export interface VideoDraft {
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

export interface AIService {
  id?: number
  serviceType?: string
  serviceName?: string
  creditsUsed?: number
  createdAt?: string
  result?: string
}

export interface ProgressResponse {
  code?: number
  data?: number
  message?: string
}
