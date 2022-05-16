export const permission_links = [
  // super_admin_links:
  [
    ["mdi-inbox-arrow-down", "任务大厅", "/main"],
    ["mdi-dresser", "任务分配", "taskAssign"],
    ["mdi-send", "标签管理", "labels"],
    ["mdi-camera-account", "个人信息", "profile"],
    ["mdi-account-cog", "人员管理", "personManagement"],
    ["mdi-account-wrench", "权限管理", "permissionManagement"],
    ["mdi-package-variant", "模型管理", "modelManagement"],
  ],
  // regular_admin_links:
  [
    ["mdi-inbox-arrow-down", "任务大厅", "main"],
    ["mdi-alert", "任务分配", "taskAssign"],
    ["mdi-send", "标签管理", "labels"],
    ["mdi-camera-account", "个人信息", "profile"],
  ],
  // regular_person_link:
  [
    ["mdi-inbox-arrow-down", "任务大厅", "main"],
    ["mdi-camera-account", "个人信息", "profile"],
  ],
] as const;

export const permission_roles = ["超级管理员", "管理员", "普通用户"] as const;

export enum ROLES {
  SUPER_ADMIN = "0",
  ADMIN = "1",
  USER = "2",
}
