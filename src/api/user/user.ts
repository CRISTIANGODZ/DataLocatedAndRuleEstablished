import { request } from "@/http";
import { AxiosResponse } from "axios";
import { LoginJSONData } from "@/views/Login/LoginTypes";
import { PersonType } from "@/views/PersonManagement/PersonType";

function login(
  ucount: string,
  password: string
): Promise<AxiosResponse<LoginJSONData>> {
  console.log("login");
  console.log("ucount: " + ucount);
  console.log("password: " + password);
  return request.post("/user/login/", {
    ucount,
    password,
  });
}

export default {
  login,
  updateUserInfo,
  registerUserInfo,
  deleteUserInfo,
};

function updateUserInfo(
  updateUserInfo: PersonType
): Promise<AxiosResponse<any>> {
  console.log("updateUserInfo:", updateUserInfo);
  return request.post("/user/updateUserInfo/", {
    ...updateUserInfo,
    roleId: updateUserInfo.userRoleId,
  });
}

function registerUserInfo(
  registerUserInfo: PersonType
): Promise<AxiosResponse<any>> {
  console.log("registerUserInfo:", registerUserInfo);
  return request.post("/user/register/", {
    ...registerUserInfo,
  });
}

function deleteUserInfo(userId: number): Promise<AxiosResponse<any>> {
  return request.delete("/user/deleteUserInfo/" + userId + "/");
}
