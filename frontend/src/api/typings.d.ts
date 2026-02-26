declare namespace API {
  type BaseResponseBoolean = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponseListProjectVO = {
    code?: number
    data?: ProjectVO[]
    message?: string
  }

  type BaseResponseListShotVO = {
    code?: number
    data?: ShotVO[]
    message?: string
  }

  type BaseResponseLong = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponseProjectVO = {
    code?: number
    data?: ProjectVO
    message?: string
  }

  type BaseResponseShotVO = {
    code?: number
    data?: ShotVO
    message?: string
  }

  type BaseResponseUserVO = {
    code?: number
    data?: UserVO
    message?: string
  }

  type DeleteRequest = {
    id?: number
    userId?: number
  }

  type PageShotMaterial = {
    records?: ShotMaterial[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type ProjectAddRequest = {
    projectName?: string
    projectDescription?: string
    projectType?: string
    coverUrl?: string
    shotCount?: number
    totalDuration?: number
    isPublic?: boolean
    userId?: number
  }

  type ProjectEditRequest = {
    id?: number
    projectName?: string
    projectDescription?: string
    projectType?: string
    coverUrl?: string
    shotCount?: number
    totalDuration?: number
    progressRate?: number
    isPublic?: boolean
    projectStatus?: string
    userId?: number
  }

  type ProjectQueryRequest = {
    projectName?: string
    projectType?: string
    isPublic?: boolean
    projectStatus?: string
    userId?: number
  }

  type ProjectVO = {
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
    user?: UserVO
    shotOrder?: number[]
  }

  type ShotAddRequest = {
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

  type ShotEditRequest = {
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

  type ShotMaterial = {
    materialId?: number
    shotId?: number
    materialType?: string
    materialRole?: string
    materialDesc?: string
    isDeleted?: boolean
    createTime?: string
    updateTime?: string
  }

  type shotmaterialGetInfoParams = {
    id: number
  }

  type shotmaterialPageParams = {
    page: PageShotMaterial
  }

  type shotmaterialRemoveParams = {
    id: number
  }

  type ShotQueryRequest = {
    userId?: number
    projectId?: number
    shotTitle?: string
    shotDesc?: string
    duration?: number
    orderIndex?: number
    shotStatus?: string
    scriptContent?: string
  }

  type ShotVO = {
    shotId?: number
    projectId?: number
    shotTitle?: string
    shotDesc?: string
    duration?: number
    orderIndex?: number
    shotStatus?: string
    scriptContent?: string
  }

  type SingleQueryRequest = {
    id?: number
    userId?: number
  }

  type UserEditRequest = {
    userId?: number
    userAccount?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
  }

  type UserLoginRequest = {
    userAccount?: string
    userPassword?: string
  }

  type UserRegisterRequest = {
    userAccount?: string
    userPassword?: string
    checkPassword?: string
  }

  type UserVO = {
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
}
