declare namespace API {
  type BaseResponseListProjectVO = {
    code?: number
    data?: ProjectVO[]
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

  type BaseResponseUserVO = {
    code?: number
    data?: UserVO
    message?: string
  }

  type getInfoParams = {
    id: number
  }

  type pageParams = {
    page: PageUser
  }

  type PageUser = {
    records?: User[]
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
  }

  type removeParams = {
    id: number
  }

  type User = {
    id?: number
    userAccount?: string
    userPassword?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    editTime?: string
    createTime?: string
    updateTime?: string
    isDelete?: number
    subscriptionPlan?: string
    aiCredits?: number
    lastLoginTime?: string
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
