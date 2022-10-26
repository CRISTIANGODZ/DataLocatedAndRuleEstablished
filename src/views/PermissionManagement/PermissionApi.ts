import { request } from "@/http";
import { AxiosResponse } from "axios";
import {
  RoleListJsonData,
  RoleFilterDataType,
  PermissionListJsonData,
  RoleInfo,
  OPStatus,
} from "./PermissionTypes";

export function getRoleList(
  filterData: RoleFilterDataType = {} as RoleFilterDataType
): Promise<AxiosResponse<RoleListJsonData>> {
  return request.get(
    "/permission/selectAllRole/" +
      filterData.currentIndex +
      "/" +
      filterData.pageSize
  );
}

export function getPermissionList(): Promise<
  AxiosResponse<PermissionListJsonData>
> {
  return request.get("/permission/selectAllPermission/");
}

export function updateRole(role: RoleInfo): Promise<AxiosResponse<OPStatus>> {
  return request.post("/permission/updateRole/", role);
}

export function addRole(role: RoleInfo): Promise<AxiosResponse<OPStatus>> {
  return request.post("/permission/addRole/", role);
}
