import { PersonType } from "@/views/PersonManagement/PersonType";
import { Permission } from "@/views/PermissionManagement/PermissionTypes";

interface LoginRoleVo {
  description: string;
  id: number;
  name: string;
  permissions: Array<PermissionType>;
}

export interface UserVo extends PersonType {}
export interface PermissionType extends Permission {
  fpath: string;
}

export interface LoginJSONData {
  code: number;
  ok: boolean;
  message: string;
  data: UserInfoType;
}

export interface UserInfoType {
  loginRoleVo: LoginRoleVo;
  token: string;
  userVo: UserVo;
}
