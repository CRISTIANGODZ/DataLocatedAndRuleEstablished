import { AxiosRequestConfig } from "axios";

export interface RoleInfo {
  id: number;
  name: string;
  title: string;
  description: string;
  permissions: number[];
}
export interface Permission {
  id: number;
  icon: string;
  title: string;
  path: string;
}
export enum RoleOperation {
  CHECK = 0,
  EDIT = 1,
  DELETE = 2,
  ADD = 3,
}

export interface Pagination {
  currentIndex: number;
  total: number;
  pageSize: number;
}

export interface RoleListJsonData {
  code: number;
  data: {
    pagination: Pagination;
    roles: RoleInfo[];
  };
}

export interface RoleFilterDataType extends AxiosRequestConfig {
  roleName: string;
  pagination: Pagination;
}
