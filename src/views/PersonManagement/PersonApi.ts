import { requestl } from "@/http";
import { request } from "@/http";
import { AxiosResponse } from "axios";
import {
  PersonFilterDataType,
  PersonJsonDataType,
  UserInfoFilterDataType,
  UserInfoJsonDataType,
} from "./PersonType";

export function getPersonList(
  filterData: PersonFilterDataType
): Promise<AxiosResponse<PersonJsonDataType>> {
  return requestl.get("/users", {
    ...filterData,
  });
}

export function getUserInfoCondition(
  filterData: PersonFilterDataType
): Promise<AxiosResponse<UserInfoJsonDataType>> {
  return request.post(
    "/user/getUserInfoCondition/" +
      filterData.pagination.currentIndex +
      "/" +
      filterData.pagination.pageSize +
      "/",
    {
      roleId: filterData.userRole,
      username: filterData.userName,
    }
  );
}


