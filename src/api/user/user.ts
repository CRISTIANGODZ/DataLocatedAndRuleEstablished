import { request } from "@/http";
import { AxiosResponse } from "axios";
import { PersonLoginJSONDataType } from "@/views/PersonManagement/PersonType";

function login(
  ucount: string,
  password: string
): Promise<AxiosResponse<PersonLoginJSONDataType>> {
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
