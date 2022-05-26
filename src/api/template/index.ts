import { request } from "@/http";
import { AxiosResponse } from "axios";

export default {
  getTemplateList,
};

function getTemplateList(): Promise<AxiosResponse<any>> {
  return request.get("/taskDistribution/templateList");
}

