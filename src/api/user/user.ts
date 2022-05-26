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
};

function updateUserInfo(
  updateUserInfo: PersonType
): Promise<AxiosResponse<any>> {
  console.log("updateUserInfo:", updateUserInfo);
  return request.post("/user/updateUserInfo/", {
    ...updateUserInfo,
  });
}

function registerUserInfo(
  registerUserInfo: PersonType
): Promise<AxiosResponse<any>> {
  console.log("updateUserInfo:", registerUserInfo);
  return request.post("/user/register/", {
    ...registerUserInfo,
  });
}
