import { request } from "@/http";
import { AxiosResponse } from "axios";
import { LoginJSONData } from "@/views/Login/LoginTypes";

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
};
