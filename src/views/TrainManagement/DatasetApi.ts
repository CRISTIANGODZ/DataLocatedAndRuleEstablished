import { requestl } from "@/http";
import { AxiosResponse } from "axios";
import { DatasetJSONData, DatasetFilterDataType } from "./DatasetTypes";

export function getDatasetList(
  filterData: DatasetFilterDataType = {} as DatasetFilterDataType
): Promise<AxiosResponse<DatasetJSONData>> {
  return requestl.get("/datasets", {
    ...filterData,
  });
}
