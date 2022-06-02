<template>
  <el-container direction="vertical">
    <h2>数据管理</h2>
    <!-- <el-header></el-header> -->
    <el-row el-row type="flex" justify="between" :gutter="20">
      <el-col>
        <el-button type="primary" @click="showAddDatasetModal">添加数据集</el-button>
      </el-col>
    </el-row>
    <el-row el-row type="flex" justify="between" :gutter="20">
      <el-col :span="4">
        <el-input v-model="filterData.keyWord" placeholder="输入数据集名称搜索" @input="onFilterDataChange" />
      </el-col>
      <el-col :span="4">
        <el-select v-model="filterData.templateTitle" clearable placeholder="请选择模板" @input="onFilterDataChange">
          <el-option v-for="template in jsonData.templateList" :key="template.id" :label="template.title"
            :value="template.title"></el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-select v-model="filterData.personName" clearable placeholder="请选择上传者" @input="onFilterDataChange">
          <el-option v-for="item in options" :key="item.id" :label="item.username" :value="item.username">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <div class="grid-content bg-purple">
          <el-button type="primary" @click="clearFilterData">清空所有搜索项</el-button>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-table :data="jsonData.datasets" border highlight-current-row style="width: 100%" @row-click="handleRowClick">
        <el-table-column type="index" width="83" label="编号"> </el-table-column>
        <el-table-column prop="title" label="数据集名称" width="200"></el-table-column>
        <!-- <el-table-column prop="templateName" label="模板名称" width="180">
        </el-table-column> -->
        <el-table-column prop="description" label="数据集描述" width="210">
          <template slot-scope="scope">
            <div>
              {{
                  ("" + scope.row.description).substring(0, 25) +
                  (("" + scope.row.description).length >= 25 ? "..." : "")
              }}
            </div>
            <el-button type="text" @click="checkTextContent(scope.row)">
              点击查看更多内容</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="上传者" width="180">
        </el-table-column>
        <el-table-column prop="uploadTime" label="上传时间" width="180">
        </el-table-column>
        <el-table-column prop="templateTitle" label="模板" width="120">
        </el-table-column>
        <el-table-column prop="total" label="文本数量" width="120">
        </el-table-column>
        <el-table-column label="操作" width="250">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleCheckModalOperationVisible(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEditModalOperationVisible(scope.row)">编辑
            </el-button>
            <el-button size="mini" type="danger" @click="handleDeleteModalOperationVisible(scope.row)">删除</el-button>
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
    <!-- <role-permission-modal-vue
      :allPermissions="jsonData.permissions"
      :closeModal="handleCloseModalOperation"
      :visible="operation.modalVisible"
      :roleInfo="selectRole"
      :confirmDelete=handleConfirmDelete
      :state="operation.operationState"
    ></role-permission-modal-vue> -->
    <el-dialog title="编辑数据集" :visible.sync="editDatasetModalVisible">
      <el-form :model="editDataSet">
        <el-form-item label="名称">
          <el-input v-model="editDataSet.title"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="editDataSet.description"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="hideEditModalOperationVisible">取 消</el-button>
        <el-button type="primary" @click="onSaveEditModalOperationVisible">保 存</el-button>
      </div>
    </el-dialog>
    <el-dialog title="数据集详情" :visible.sync="checkDatasetModalVisible">
      <!-- <el-row el-row type="flex" justify="between" :gutter="20">
        <el-col>
          <el-input v-model="filterText.keyWord" placeholder="请输入文本名称" @input="onFilterTextChange" />
        </el-col>
      </el-row> -->
      <el-row>
        <el-table :data="jsonData.textlist" border highlight-current-row style="width: 100%">
          <el-table-column prop="id" width="100" label="编号"> </el-table-column>
          <el-table-column prop="title" label="文本名称" width="300"></el-table-column>
          <el-table-column prop="content" label="文本内容">
            <template slot-scope="scope">
              <div>
                {{
                    ("" + scope.row.content).substring(0, 25) +
                    (("" + scope.row.content).length >= 25 ? "..." : "")
                }}
              </div>
              <el-button type="text" @click="checkTextContent(scope.row)">
                点击查看更多内容</el-button>
            </template>
          </el-table-column>
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
    <dataset-modal></dataset-modal>
    <add-dataset-modal-vue :dialogVisible="addDatasetModalVisible" :closeModal="hideAddDatasetModal">
    </add-dataset-modal-vue>
  </el-container>
</template>

<script lang="ts">
import Vue from "vue";
import DatasetModal from "./DatasetModal.vue";
import {
  DatasetType,
  Dataset,
  DatasetOperation,
  DatasetStatus,
  DatasetStatusZH,
} from "./DatasetTypes";
import { updateDataset } from "@/api/dataset";
import AddDatasetModalVue from "./AddDatasetModal.vue";
export default Vue.extend({
  data() {
    return {
      filterData: {
        keyWord: "",
        templateTitle: "",
        personName: "",
        pagination: {
          currentIndex: 1,
          pageSize: 10,
          total: "",
        },
      },
      filterDataVo: {
        title: "",
        username: "",
        templateTitle: "",
      },
      filterText: {
        datasetId: "",
        pagination: {
          currentIndex: 1,
          pageSize: 10,
          total: "",
        },
      },
      options: [],
      jsonData: {
        datasets: [] as Dataset[],
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
      editDataSet: {} as Dataset,
      addDatasetModalVisible: false,
      checkDatasetModalVisible: false,
      editDatasetModalVisible: false,
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
    clearFilterData() {
      this.filterData.keyWord = "";
      this.filterData.templateTitle = "";
      this.filterData.personName = "";
      this.filterDataVo.title = "";
      this.filterDataVo.username = "";
      this.filterDataVo.templateTitle = "";
      this.getDataSets();
    },
    getDataSets() {
      this.filterDataVo.title = this.filterData.keyWord;
      this.filterDataVo.username = this.filterData.personName;
      this.filterDataVo.templateTitle = this.filterData.templateTitle;
      this.$http
        .post(
          "/data/pageData/" +
          this.filterData.pagination.currentIndex +
          "/" +
          this.filterData.pagination.pageSize,
          this.filterDataVo
        )
        .then((response) => {
          if (response.data.code === 200) {
            console.log(response.data);
            this.jsonData.datasets = response.data.data.dataVoList;
            this.filterData.pagination.total = response.data.data.dataTotal;
            this.filterData.pagination.currentIndex =
              response.data.data.current;
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
      console.log("filterText: ", this.filterText);
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
    handleCheckModalOperationVisible(row) {
      this.handleModalOperationVisible();
      this.operation.operationState = DatasetOperation.CHECK;
      this.filterText.datasetId = row.id;
      this.onFilterTextChange();
      this.checkDatasetModalVisible = true;
    },
    handleEditModalOperationVisible(row) {
      this.handleModalOperationVisible();
      this.operation.operationState = DatasetOperation.EDIT;
      this.editDataSet = {
        id: row.id,
        title: row.title,
        description: row.description,
      };
      this.editDatasetModalVisible = true;
    },
    hideEditModalOperationVisible() {
      this.editDatasetModalVisible = false;
    },
    async onSaveEditModalOperationVisible() {
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
    // handleAddModalOperationVisible() {
    //   this.operation.operationState = DatasetOperation.ADD;
    //   this.handleModalOperationVisible();
    //   this.selectRole = {
    //     id: 0,
    //     name: "",
    //     description: "",
    //     permissions: [],
    //   };
    // },
    handleDeleteModalOperationVisible(row) {
      console.log(row.id);
      this.$confirm("确定要删除这个数据集吗？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          //点击删除
          this.handleModalOperationVisible();
          this.operation.operationState = DatasetOperation.DELETE;
          this.$http.delete("/data/deleteDataSet/" + row.id).then((res) => {
            console.log("res: ", res);
            this.$message({
              type: "success",
              message: "删除成功",
            });
            this.getData();
            // this.getTasks();
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
  },
  mounted() {
    this.getDataSets();
    this.getLabelPersons();
    this.getTemplateList();
    // this.getData();
  },
  computed: {
    datasetStatus() {
      return DatasetStatus;
    },
    datasetStatusZH() {
      return DatasetStatusZH;
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
</style>