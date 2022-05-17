import { requestl } from "@/http";
import { AxiosResponse } from "axios";
import { PersonFilterDataType, PersonJsonDataType } from "./PersonType";

export function getPersonList(
  filterData: PersonFilterDataType
): Promise<AxiosResponse<PersonJsonDataType>> {
  return requestl.get("/users", {
    ...filterData,
  });
}


