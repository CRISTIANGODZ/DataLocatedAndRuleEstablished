import { AxiosRequestConfig } from "axios";

export interface PersonType {
  id: number;
  username: string;
  email: string;
  password: string;
  ucount: string;
  phone: string;
  address: string;
  userRole: string;
  userRoleId: number;
  description: string;
  avatar: string;
}

export interface Pagination {
  currentIndex: number;
  total: number;
  pageSize: number;
}

export interface PersonFilterDataType extends AxiosRequestConfig {
  userName: string;
  userRole: string;
  pagination: Pagination;
}

export interface PersonJsonDataType {
  code: number;
  data: {
    pagination: Pagination;
    users: PersonType[];
  };
}

export enum PersonOperation {
  CHECK = 0,
  EDIT = 1,
  DELETE = 2,
  ADD = 3,
}

export interface PersonLoginJSONDataType {
  code: number;
  message: string;
  data: {
    userVo: PersonType;
    token: string;
  };
  ok: boolean;
}

export interface PersonJsonDataType {
  code: number;
}

export interface UserInfoFilterDataType extends AxiosRequestConfig {
  current: number;
  size: number;
  roleId: number;
  roleName: string;
}


export interface  UserInfoJsonDataType {
  code: number;
  message: string;
  ok: boolean;
  data: {
    current: number;
    size: number;
    total: number;
    userList: PersonType[];
  };
}