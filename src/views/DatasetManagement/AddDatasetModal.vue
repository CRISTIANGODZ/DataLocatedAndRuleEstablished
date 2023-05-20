<template>
  <el-dialog title="添加病历" :visible.sync="dialogVisible" width="40%" :before-close="closeModal">
    <el-row>
      <el-form :label-position="labelPosition" label-width="100px" :model="formLabelAlign" enctype="multipart/form-data"> 
        <el-form-item label="姓名">
          <el-input v-model="uploadDatasetData.name"></el-input>
        </el-form-item>
        <el-form-item label="上传文件">
          <div class="el-tips-head">
            <div class="tips-demo">
              <i class="el-icon-question"></i>
              <div class="realtip">
                <img src="../../assets/images/detail.jpg" alt="" width="100%" height="100%">
              </div>
            </div>
            <div class="download-format-file">
              <span>提示</span>
            </div>
          </div>

          <el-upload class="upload-demo" accept=".csv" drag ref="upload" :auto-upload="false" :limit="1" action=""
            :http-request="uploadFile">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <div class="el-upload__tip" slot="tip">
              只能上传规定格式的csv文件
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="title">
          <el-input v-model="uploadDatasetData.title" type="textarea" rows="5" placeholder="请输入title"></el-input>
      
        </el-form-item>
        <el-form-item label="content">
          <el-input v-model="uploadDatasetData.content" type="textarea" rows="5" placeholder="请输入content"></el-input>
        </el-form-item>
      </el-form>
    </el-row>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeModal">取 消</el-button>
      <el-button type="primary" @click="onSavaDataset">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { Dataset } from "./DatasetTypes";
import template from "@/api/template";
import { addDataset } from "@/api/dataset";

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
      uploadDatasetData: {
        name: "",
        title: "",
        templateId: 0,
        content: "",
        userId: 2,
      },
      templates: [],
      uploading: false,
      processPercent: 5,
      processStatus: "success",
      processVisible: false,
      tipVisible: false,
      tipTitle: "",
      timer: null,
    };
  },
  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    },
    closeModal: {
      type: Function,
      default: () => { },
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
    async onSavaDataset() {
      this.$refs.upload.submit();
      const res = (await addDataset(this.uploadDatasetData)).data;
      console.log("response: ", res);
      if (res.code === 200) {
        this.$message.success("上传成功");
        this.closeModal();
        this.uploadDatasetData = {} as Dataset;
      }
    },
    
    //上传
    async uploadFile(param) {
      let dataset = {
        name: this.uploadDatasetData.name,
        title: this.uploadDatasetData.title,
        content: this.uploadDatasetData.content,
        templateId: this.uploadDatasetData.templateId,
        userId: this.userId,
      };
      console.log("dataset: ", dataset);
      const formData = new FormData();
      console.log('真是个测试',param);
      
      formData.set("uploadFile", param);
      formData.set("dataSet", JSON.stringify(dataset));

      console.log("formData: ", formData);
      const res = (await addDataset(formData)).data;
      console.log("response: ", res);
      if (res.code === 200) {
        this.uploadDatasetData = {} as Dataset;
        this.processPercent = 100;
        this.closeModal();
        this.$refs.upload.clearFiles();
        this.$success("上传成功");
      } else {
        this.$error(res.msg);
      }
    },
  },
  mounted() {
    this.getTemplateList();
  },
  computed: {
    userId: function () {
      return this.$store.state.user.userInfo.userVo.id;
    },
  },
};
</script>

<style scoped>
.el-tips-head{
  display: flex;
}
.tips-demo{
  position: relative;
  width: 20px;
}
.realtip{
  position: absolute;
  z-index: 2022;
  right: -800px;
  width: 1000px;
  height: 400px;
  opacity: 0;
  transform: scale(0);
  transform-origin: top left;
  -webkit-transition: all .4s ease-in-out;
  -o-transition: all .4s ease-in-out;
  transition: all .4s ease-in-out;
}
.tips-demo:hover .realtip{
  opacity: 1;
  transform: scale(1);
}
</style>
