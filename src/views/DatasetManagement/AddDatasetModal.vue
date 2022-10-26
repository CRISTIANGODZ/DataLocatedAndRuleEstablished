<template>
  <el-dialog title="上传数据集" :visible.sync="dialogVisible" width="40%" :before-close="closeModal">
    <el-row>
      <el-form :label-position="labelPosition" label-width="100px" :model="formLabelAlign">
        <el-form-item label="数据集名称">
          <el-input v-model="uploadDatasetData.name"></el-input>
        </el-form-item>
        <el-form-item label="上传文件">
          <el-upload class="upload-demo" accept=".json" drag ref="upload" :auto-upload="false" :limit="1" action="#"
            :http-request="uploadFile">
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
          <el-select v-model="uploadDatasetData.templateId" filterable clearable reserve-keyword placeholder="选择模板">
            <el-option v-for="template in templates" :key="template.id" :label="template.title" :value="template.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="详情">
          <el-input v-model="uploadDatasetData.description" type="textarea" rows="5" placeholder="请输入数据集详情"></el-input>
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
      uploadDatasetData: {} as Dataset,
      // uploadDatasetData: {
      //   title: "",
      //   templateId: 0,
      //   description: "",
      //   userId: 2,
      // },
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
    onSavaDataset() {
      this.$refs.upload.submit();
      // const res = (await addDataset(this.uploadDatasetData)).data;
      // console.log("response: ", res);
      // if (res.code === 200) {
      //   this.$message.success("上传成功");
      //   this.closeModal();
      //   this.uploadDatasetData = {} as Dataset;
      // }
    },
    async uploadFile(param) {
      let dataset = {
        title: this.uploadDatasetData.name,
        description: this.uploadDatasetData.description,
        templateId: this.uploadDatasetData.templateId,
        userId: this.userId,
      };
      console.log("dataset: ", dataset);
      const formData = new FormData();
      formData.set("uploadFile", param.file);
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
/deep/ .el-upload {
  width: 100%;
}

/deep/ .el-upload-dragger {
  width: 100%;
}
</style>
