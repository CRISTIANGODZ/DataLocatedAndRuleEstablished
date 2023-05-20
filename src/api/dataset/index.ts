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
  // const config = {
  //   headers: {
  //     "Content-Type": "multipart/form-data",
  //   },
  // };
  return request({
    url:"http://121.4.93.222:8081/electronic/case/submit/csv",
    method:'post',
    headers:{
        "Content-Type": "multipart/form-data;boundary=----WebKitFormBoundaryIsWvYgQMdGdA0emB",
    },
    data

  })
  // return request.post("http://121.4.93.222:8081/electronic/case/submit/csv",data,{
  //   headers:{
  //     "Content-Type": "multipart/form-data",
  //   }
  // });
}


// export function addDataset(data: any): Promise<AxiosResponse<any>> {
//   console.log("dataset:", data);
//   const config = {
//     headers: {
//       "Content-Type": "multipart/form-data",
//     },
//   };
//   return request.post("/data/upload/", data, config);
// }

export function updateDataset(data: Dataset): Promise<AxiosResponse<any>> {
  console.log("dataset:", data);
  return request.post("/electronic/case/patient/text/update", data);
}
