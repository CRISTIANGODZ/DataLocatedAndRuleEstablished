import { AxiosRequestConfig } from "axios";

export interface DatasetType {}

export interface Dataset {
  id: number;
  name: string;
  description: string;
  uploadTime: string;
  uploader: string;
  uploaderId: number;
  status: number;
  labeledCount: number;
  unlabeledCount: number;
  totalCount: number;
}
export enum DatasetOperation {
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

export enum DatasetStatus {
  UNTRAIN = 0,
  TRAINING = 1,
  TRAINED = 2,
}

export interface DatasetJSONData extends AxiosRequestConfig {
  code: number;
  data: {
    pagination: Pagination;
    datasets: Dataset[];
  };
}

export interface DatasetFilterDataType extends AxiosRequestConfig {
  datasetName: string;
  pagination: Pagination;
}
