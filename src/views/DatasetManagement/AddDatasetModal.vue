<template>
  <el-dialog
    title="上传数据集"
    :visible.sync="dialogVisible"
    width="40%"
    :before-close="closeModal"
  >
    <el-row>
      <el-form
        :label-position="labelPosition"
        label-width="100px"
        :model="formLabelAlign"
      >
        <el-form-item label="数据集名称">
          <el-input v-model="uploadDatasetData.name"></el-input>
        </el-form-item>
        <el-form-item label="上传文件">
          <el-upload
            class="upload-demo"
            drag
            :on-success="handleUploadSuccess"
            action="https://jsonplaceholder.typicode.com/posts/"
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <div class="el-upload__tip" slot="tip">
              只能上传规定格式的json文件
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="模板">
          <el-select
            v-model="uploadDatasetData.templateId"
            filterable
            clearable
            reserve-keyword
            placeholder="选择模板"
          >
            <el-option
              v-for="template in templates"
              :key="template.id"
              :label="template.title"
              :value="template.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="详情">
          <el-input
            v-model="uploadDatasetData.description"
            type="textarea"
            rows="5"
            placeholder="请输入数据集详情"
          ></el-input>
        </el-form-item>
      </el-form>
    </el-row>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeModal">取 消</el-button>
      <el-button type="primary" @click="closeModal">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { Dataset } from "./DatasetTypes";
import template from "@/api/template";

export default {
  data() {
    return {
      // dialogVisible: false,
      formLabelAlign: {
        name: "",
        region: "",
        type: "",
      },
      labelPosition: "left",
      // uploadDatasetData: {
      //   fileName: "",
      //   filePath: "",
      //   uploaderId: 1,
      //   uploadTime: "",
      //   description: "",
      //   templateId: 1,
      //   templateName: "",
      // },
      uploadDatasetData: {} as Dataset,
      templates: [],
    };
  },
  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    },
    closeModal: {
      type: Function,
      default: () => {},
    },
  },
  methods: {
    async getTemplateList() {
      const res = (await template.getTemplateList()).data;
      console.log("template list:", res);
      if (res.code === 200) {
        this.templates = res.data;
      }
    },
    handleUploadSuccess(response, file, fileList) {
      console.log("response: ", response);
      console.log("file: ", file);
      console.log("fileList: ", fileList);
      this.uploadDatasetData.filePath = response.data.filePath;
      this.uploadDatasetData.fileName = file.name;
    },
  },
  mounted() {
    this.getTemplateList();
  },
};
</script>

<style scoped></style>
