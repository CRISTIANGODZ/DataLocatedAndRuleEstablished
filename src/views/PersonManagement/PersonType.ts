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
