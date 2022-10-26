import { request } from "@/http";
import { AxiosResponse } from "axios";
import { PersonType } from "../PersonManagement/PersonType";

export function updateUserInfo(
  updateUserInfo: PersonType
): Promise<AxiosResponse<any>> {
  return request.post("/user/updateUserInfo/", {
    ...updateUserInfo,
  });
}
