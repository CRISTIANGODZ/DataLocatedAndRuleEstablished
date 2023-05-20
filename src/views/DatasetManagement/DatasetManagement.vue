<template>
  <el-container direction="vertical">
    <h2>病历管理</h2>
    <el-row el-row type="flex" justify="between" :gutter="20">
      <el-col>
        <el-button type="primary" @click="showAddDatasetModal">导入病历</el-button>
        <el-button type="primary" @click="Download">导出病历</el-button>
      </el-col>
    </el-row>
    <el-row el-row type="flex" justify="between" :gutter="10">
      <el-col :span="4">
        <div class="searchName">
          <el-input v-model="filterData.keyWord" placeholder="请输入病人姓名搜索" @input="onFilterDataChange"/>
          <el-button @click="searchName">搜索</el-button>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <!-- 放在了jsonData。datasets数组中 -->
      <el-table :data="jsonData.datasets" border highlight-current-row style="width: 100%" @row-click="handleRowClick">
        <el-table-column type="index" width="110" label="编号" align="center"></el-table-column>
        <el-table-column prop="name" width="110" label="姓名" align="center"></el-table-column>
        <el-table-column prop="title" width="200" label="title" align="center">
        </el-table-column>
        <el-table-column prop="content" label="content" width="400" align="center">
        </el-table-column>
        <el-table-column prop="username" label="上传者" width="200" align="center">
        </el-table-column>
        <el-table-column prop="uploadTime" label="上传时间" width="200" align="center">
        </el-table-column>
        <el-table-column label="操作" width="333" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" round @click="Check(scope.row)">查看</el-button>
            <el-button size="mini" type="warning" round @click="Edit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" round @click="Delete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-row>
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="filterData.pagination.currentIndex" :page-sizes="[5, 10, 15, 20]"
        :page-size="filterData.pagination.pageSize" layout="total, sizes, prev, pager, next, jumper"
        :total="filterData.pagination.total">
      </el-pagination>
    </el-row>
    <!--  -->
    <el-dialog title="查看病历" :visible.sync="checkDatasetModalVisible">
      <el-row>
        <el-table :data="list" border highlight-current-row style="width: 100%" @row-click="handleRowClick">
          <el-table-column type="index" label="textId" width="100" align="center"></el-table-column>
          <el-table-column prop="title" label="title" width="200" align="center">
          </el-table-column>
          <el-table-column prop="content" label="content" align="center">
            <template slot-scope="scope">
              <div>
                {{
                    ("" + scope.row.content).substring(0, 25) +
                    (("" + scope.row.content).length >= 25 ? "..." : "")
                }}
              </div>
              <el-button type="text" @click="checkTextContent(scope.row)">点击查看更多内容</el-button>
            </template>
          </el-table-column>
          <el-table-column type="index" width="200" label="updatetime" align="center"> </el-table-column>
        </el-table>
      </el-row>
      <el-row>
        <el-pagination background @size-change="handleTextSizeChange" @current-change="handleTextCurrentChange"
          :current-page="filterText.pagination.currentIndex" :page-sizes="[5, 10, 15, 20]"
          :page-size="filterText.pagination.pageSize" layout="total, sizes, prev, pager, next, jumper"
          :total="filterText.pagination.total">
        </el-pagination>
      </el-row>
    </el-dialog>
    <el-dialog title="编辑病历" :visible.sync="editDatasetModalVisible">
      <el-form :model="editDataSet">
        <el-form-item label="content">
          <el-input v-model="editDataSet.content"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="hideEdit">取 消</el-button>
        <el-button type="primary" @click="onSaveEdit">保 存</el-button>
      </div>
    </el-dialog>
    <dataset-modal></dataset-modal>
    <add-dataset-modal-vue :dialogVisible="addDatasetModalVisible" :closeModal="hideAddDatasetModal">
    </add-dataset-modal-vue>
  </el-container>
</template>

<script lang="ts">
import Vue from "vue";
import DatasetModal from "./DatasetModal.vue";
import axios from 'axios';
import {
  DatasetType,
  Dataset,
  DatasetOperation,
  DatasetStatus,
  DatasetStatusZH,
} from "./DatasetTypes";
import { updateDataset } from "@/api/dataset";
import AddDatasetModalVue from "./AddDatasetModal.vue";
import Papa from "papaparse"
import { url } from "inspector";
import { title } from "process";

export default Vue.extend({
  data() {
    return {
      filterData: {
        keyWord: "",
        templateTitle: "",
        personName: "",
        startDate: "",
        pagination: {
          currentIndex: 1,
          pageSize: 10,
          total: "",
        },
      },
      filterDataVo: {
        name: "",
        title: "",
        content: "",
        username: "",
        templateTitle: "",
        uploadTime: "",
      },
      filterText: {
        datasetId: "",
        pagination: {
          currentIndex: 1,
          pageSize: 10,
          total: "",
        },
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        shortcuts: [
          {
            text: "今天",
            onClick(picker) {
              picker.$emit("pick", new Date());
            },
          },
          {
            text: "昨天",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit("pick", date);
            },
          },
          {
            text: "一周前",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            },
          },
        ],
      },
      options: [],
      //数据集中
      jsonData: {
        datasets: [],
        textlist: [],
        templateList: [],
      },
      operation: {
        modalVisible: false,
        operationState: DatasetOperation.CHECK,
      },
      formLabelAlign: {
        name: "",
        region: "",
        type: "",
      },
      selectRole: {} as Dataset,
      editDataSet: [],
      addDatasetModalVisible: false,
      checkDatasetModalVisible: false,
      editDatasetModalVisible: false,
      header: [{ label: "textId" },{ label: "title" }, { label: "content" },{ label: "updatetime" }],
      list: [],
      //  [
      //   {
      //     textId: 1,
      //     title: "病史",
      //     content: "曾患心脏病，糖尿病",
      //     updatetime: 20201016
      //   },
      //   {
      //     textId: 2,
      //     title: "主诉",
      //     content: "检查肝功能是否正常",
      //     updatetime: 20201015
      //   },
      //   {
      //     textId: 3,
      //     title: "既往史",
      //     content: "做过心脏搭桥手术",
      //     updatetime: 20201014
      //   },
      //   {
      //     textId: 4,
      //     title: "个人史",
      //     content: "曾患一级尿毒症",
      //     updatetime: 20201013
      //   },
      //   {
      //     textId: 5,
      //     title: "家族史",
      //     content: "父亲患尿毒症去世",
      //     updatetime: 20201012
      //   },
      //   {
      //     textId: 6,
      //     title: "其他情况",
      //     content: "良好",
      //     updatetime: 20201011
      //   },
      // ],
      fileName: "电子病历.csv",
    };
  },
  components: {
    DatasetModal,
    AddDatasetModalVue,
  },
  methods: {
    getTemplateList() {
      this.$http.get("/template/get/").then((response) => {
        if (response.data.code === 200) {
          this.jsonData.templateList = response.data.data;
          console.log("模板:", this.jsonData.templateList);
        }
      });
    },
    getLabelPersons() {
      this.$http.get("/taskDistribution/userList").then((response) => {
        if (response.data.code === 200) {
          this.options = response.data.data;
          console.log("标注人:", this.options);
        }
      });
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.filterData.pagination.pageSize = val;
      this.onFilterDataChange();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.filterData.pagination.currentIndex = val;
      this.onFilterDataChange();
    },
    onFilterDataChange() {
      console.log("filterData: ", this.filterData);
      this.getDataSets();
    },
    //搜索
    searchName(){
      this.$http.get("http://121.4.93.222:8081/electronic/case/patient/text/get",{
        params:{
          name:'李四'//this.filterData.keyWord,
        }
      })
      .then((resolves)=>{
        console.log("我在里面");
        console.log(resolves.data.patientThemeTextList);
        this.list = resolves.data.patientThemeTextList;
      })

      this.getDataSets();
    },
    getDataSets() {
      //修改操作
      this.filterDataVo.name = this.filterData.keyWord;
      this.filterDataVo.title = this.filterData.title;
      this.filterDataVo.content = this.filterData.content;
      this.filterDataVo.username = this.filterData.personName;
      this.filterDataVo.templateTitle = this.filterData.templateTitle;
      this.filterDataVo.uploadTime = this.filterData.startDate;
      //请求操作
      this.$http
        .post(
          "/data/pageData/" +
          this.filterData.pagination.currentIndex +
          "/" +
          this.filterData.pagination.pageSize,
          this.filterDataVo
          ,""
        )
        .then((response) => {
          if (response.data.code === 200) {
            console.log(response.data);
            //返回的数组
            this.jsonData.datasets = response.data.data.dataVoList;
            this.filterData.pagination.total = response.data.data.dataTotal;
            this.filterData.pagination.currentIndex = response.data.data.current;
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    handleTextSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.filterText.pagination.pageSize = val;
      this.onFilterTextChange();
    },
    handleTextCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.filterText.pagination.currentIndex = val;
      this.onFilterTextChange();
    },
    onFilterTextChange() {
      // console.log("filterText: ", this.filterText);
      this.getTexts();
    },
    getTexts() {
      this.$http
        .get(
          "/data/viewDetail/" +
          this.filterText.datasetId +
          "/" +
          this.filterText.pagination.currentIndex +
          "/" +
          this.filterText.pagination.pageSize,
        )
        .then((response) => {
          if (response.data.code === 200) {
            this.jsonData.textlist = response.data.data;
            this.filterText.pagination.total = response.data.data.length;
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    handleLabelStateClick(row) {
      console.log("handleLabelStateClick");
      console.log(row);
    },
    customColorMethod(percentage) {
      if (percentage < 30) {
        return "#909399";
      } else if (percentage < 70) {
        return "#e6a23c";
      } else {
        return "#67c23a";
      }
    },
    checkTextContent(row) {
      if (row.description)
        this.$alert(row.description, "",);
      else
        this.$alert(row.content, "",);
    },
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },
    handleLabelStateMouseover(row) {
      console.log("handleLabelStateMouseover");
      console.log(row);
    },
    handleModalOperationVisible() {
      console.log("dddd");
      this.operation.modalVisible = true;
    },
    handleRowClick(row, column, event) {
      this.selectRole = row;
      console.log(row);
      console.log(column);
    },

    //查看详情的时候请求
    Check(row) {
      console.log("你猜猜这个数据是什么",row);
      this.handleModalOperationVisible();
      this.operation.operationState = DatasetOperation.CHECK;
      this.filterText.datasetId = row.id;
      this.onFilterTextChange();
      this.checkDatasetModalVisible = true;
      //请求的方式http://121.4.93.222:8081/electronic/case/patient/text/get
      //这个单个病史病例
      this.$http.get("http://121.4.93.222:8081/electronic/case/patient/get/single/text",{
        params:{
          textId:12//this.filterData.keyWord,
        }
      })
      .then((resolves)=>{
        console.log("我在0.0里面");
        console.log(resolves.data.patientThemeTextList);
        //this.list = resolves.data.patientThemeTextList;
      })
      //这是病人全部的病史
      // this.$http.get("http://121.4.93.222:8081/electronic/case/patient/text/get",{
      //   params:{
      //     name:'李四'//this.filterData.keyWord,
      //   }
      // })
      // .then((resolves)=>{
      //   console.log("我在里面");
      //   console.log(resolves.data.patientThemeTextList);
      //   this.list = resolves.data.patientThemeTextList;
      // })
      // this.$http.get('/electronic/case/patient/text/update',{
      //   method:'GET',
      //   param:{
      //    textIdId:row.id 
      //   }
      // }).then((res)=>{
      //   return res.json()
      // }).then((data)=>{
      //    this.list =  data.patientThemeTextList
      // })
      //过滤器的方式
      // this.fliterData(row,id)
    },
    fliterData(id){
      this.filterDataVo = this.list.filters((res)=>{
        return res.templateId === id 
      })
    },
    Delete(row) {
      console.log(row.id);
      this.$confirm("确定要删除这个病历吗？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
      .then(() => {
        this.handleModalOperationVisible();
        this.operation.operationState = DatasetOperation.DELETE;
        this.$http.delete("/data/deleteDataSet/" + row.id).then((res) => {
          console.log("res: ", res);
          this.$message({
            type: "success",
            message: "删除成功",
          });
          this.getDataSets();
        });
      })
      .catch(() => {
        this.$message({
          type: "info",
          message: "已取消删除",
        });
      });
    },
    showAddDatasetModal() {
      this.addDatasetModalVisible = true;
    },
    hideAddDatasetModal() {
      this.addDatasetModalVisible = false;
    },
    handleConfirmDelete() {
      this.$success("删除成功");
      this.handleCloseModalOperation();
    },
    handleCloseModalOperation() {
      this.operation.modalVisible = false;
      if (this.operation.operationState !== DatasetOperation.EDIT) {
        this.getRoles();
      }
    },
    // async getData() {
    //   const dataset_data = (await getDatasetList(this.filterData)).data;
    //   console.log("dataset_data: ", dataset_data);
    //   if (dataset_data.code === 200) {
    //     this.jsonData.datasets = dataset_data.data.datasets;
    //     this.filterData.pagination.total = dataset_data.data.pagination.total;
    //   }
    // },
    Edit(row) {
      this.handleModalOperationVisible();
      this.operation.operationState = DatasetOperation.EDIT;
      this.editDataSet = {
        patientId: row.patientId,
        textId: row.textId,
        content: row.content,
      };
      this.filterText.datasetId = row.textId;
      this.onFilterTextChange();
      this.editDatasetModalVisible = true;
    },
    hideEdit() {
      this.editDatasetModalVisible = false;
    },
    async onSaveEdit() {
      const res = (await updateDataset(this.editDataSet)).data;
      if (res.code === 200) {
        this.editDataSet = {} as Dataset;
        this.$success("修改成功");
        this.editDatasetModalVisible = false;
        this.getDataSets();
      } else {
        this.$error(res.msg);
      }
    },
    Download(){
      var that = this;
      console.log('第一');
      
      axios.get('http://192.168.43.62:8081/electronic/case/get/csv',{
        params:{
          name:'李四'
        }
      })
      .then(function(response){
        console.log('response',response);     
        that.list = response.data.data.datasets;
      },function(err){})
      var csv = Papa.unparse(that.list);
      const url = this.genUrl(csv, {});
      let a = document.createElement("a");
      document.body.appendChild(a);
      a.href = url;
      a.download = this.fileName;
      a.click();
      window.URL.revokeObjectURL(url);
    },
    genUrl(encoded, options) {
      const dataBlob = new Blob([`\ufeff${encoded}`], {
        type: "text/plain;charset=utf-8",
      }); 
      return window.URL.createObjectURL(dataBlob);
    },
  },
  mounted() {
    this.getDataSets();
    this.getLabelPersons();
    this.getTemplateList();
  },
  computed: {
    datasetStatus() {
      return DatasetStatus;
    },
    datasetStatusZH() {
      return DatasetStatusZH;
    },
    headerLabel: function() {
      let result;
      result = this.header.map((item) => {
        return item.label;
      });
      result = result.join(",");
      return result;
    },
    headerProp: function() {
      let result;
      result = this.header.map((item) => {
        return item.label;
      });
      return result;
    },
  },
});
</script>

<style scoped>
.el-row {
  margin-top: 10px;
  margin-bottom: 10px;
}

.el-table {
  margin-top: 10px;
  margin-bottom: 10px;
}

.searchName{
  display: flex;
}
</style>