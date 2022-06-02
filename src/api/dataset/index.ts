import { requestl, request } from "@/http";
import { AxiosResponse } from "axios";
import {
  DatasetJSONData,
  Dataset,
  DatasetFilterDataType,
} from "@/views/DatasetManagement/DatasetTypes";

export function getDatasetList(
  filterData: DatasetFilterDataType = {} as DatasetFilterDataType
): Promise<AxiosResponse<DatasetJSONData>> {
  return requestl.get("/datasets", {
    ...filterData,
  });
}

export function addDataset(data: any): Promise<AxiosResponse<any>> {
  console.log("dataset:", data);
  const config = {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  };
  return request.post("/data/upload/", data, config);
}

export function updateDataset(data: Dataset): Promise<AxiosResponse<any>> {
  console.log("dataset:", data);
  return request.post("/data/update", data);
}