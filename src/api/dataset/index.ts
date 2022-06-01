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

export function addDataset(data: Dataset): Promise<AxiosResponse<any>> {
  console.log("dataset:", data);
  return request.post("/datasets", data);
}

export function updateDataset(data: Dataset): Promise<AxiosResponse<any>> {
  console.log("dataset:", data);
  return request.post("/datasets/update", data);
}