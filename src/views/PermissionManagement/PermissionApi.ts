import { requestl } from "@/http";
import { AxiosResponse } from "axios";
import { RoleListJsonData, RoleFilterDataType } from "./PermissionTypes";

export function getRoleList(
  filterData: RoleFilterDataType = {} as RoleFilterDataType
): Promise<AxiosResponse<RoleListJsonData>> {
  return requestl.get("/roles", {
    ...filterData,
  });
}
