<template>
  <el-container direction="vertical">
    <h2>训练管理</h2>
    <!-- <el-header></el-header> -->
    <el-row el-row type="flex" justify="between" :gutter="20">
      <el-col>
        <el-input v-model="filterData.roleName" placeholder="请输入数据集名称"></el-input>
      </el-col>

      <el-col>
        <el-button type="primary" @click="showAddDatasetModal">添加数据集</el-button>
      </el-col>
    </el-row>
    <el-row>
      <el-table :data="jsonData.datasets" border highlight-current-row style="width: 100%" @row-click="handleRowClick">
        <el-table-column prop="id" label="编号" width="180"> </el-table-column>
        <el-table-column prop="name" label="数据集名称" width="180"></el-table-column>
        <el-table-column prop="templateName" label="模板名称" width="180">
        </el-table-column>
        <el-table-column prop="description" label="数据集描述" width="180">
        </el-table-column>
        <el-table-column prop="status" label="数据集状态" width="180">
          <template slot-scope="scope">
            <el-tag :type="
              scope.row.status === datasetStatus.UNTRAIN
                ? ''
                : scope.row.status === datasetStatus.TRAINING
                  ? 'info'
                  : 'success'
            ">{{
    scope.row.status === datasetStatus.UNTRAIN
      ? datasetStatusZH.UNTRAIN
      : scope.row.status === datasetStatus.TRAINING
        ? datasetStatusZH.TRAINING
        : datasetStatusZH.TRAINED
}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="uploader" label="上传者" width="180">
        </el-table-column>
        <el-table-column prop="uploadTime" label="上传时间" width="180">
        </el-table-column>
        <el-table-column prop="labeledTaskCount" label="已标注任务数量" width="180">
        </el-table-column>
        <el-table-column prop="totalTaskCount" label="总任务数量" width="180">
        </el-table-column>
        <el-table-column prop="textCount" label="总病例数量" width="180">
        </el-table-column>

        <el-table-column label="操作" width="250">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleCheckModalOperationVisible">查看</el-button>
            <el-button size="mini" @click="handleEditModalOperationVisible" type="primary">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDeleteModalOperationVisible">删除</el-button>
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
import { getDatasetList } from "@/api/dataset";
import AddDatasetModalVue from "./AddDatasetModal.vue";
export default Vue.extend({
  data() {
    return {
      filterData: {
        roleName: "",
        pagination: {
          currentIndex: 1,
          pageSize: 10,
          total: 3,
        },
      },
      jsonData: {
        datasets: [] as Dataset[],
      },
      operation: {
        modalVisible: false,
        operationState: DatasetOperation.CHECK,
      },
      selectRole: {} as Dataset,
      addDatasetModalVisible: false,
    };
  },
  components: {
    DatasetModal,
    AddDatasetModalVue,
  },
  methods: {
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
    handleEditModalOperationVisible() {
      this.handleModalOperationVisible();
      this.operation.operationState = DatasetOperation.EDIT;
    },
    handleCheckModalOperationVisible() {
      this.handleModalOperationVisible();
      this.operation.operationState = DatasetOperation.CHECK;
    },
    handleAddModalOperationVisible() {
      this.operation.operationState = DatasetOperation.ADD;
      this.handleModalOperationVisible();
      this.selectRole = {
        id: 0,
        name: "",
        description: "",
        permissions: [],
      };
    },
    handleDeleteModalOperationVisible() {
      this.handleModalOperationVisible();
      this.operation.operationState = DatasetOperation.DELETE;
    },

    showAddDatasetModal() {
      this.addDatasetModalVisible = true;
    },
    hideAddDatasetModal() {
      console.log('ccccccccccccccccccccc');
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
    async getData() {
      const dataset_data = (await getDatasetList(this.filterData)).data;
      console.log("dataset_data: ", dataset_data);
      if (dataset_data.code === 200) {
        this.jsonData.datasets = dataset_data.data.datasets;
        this.filterData.pagination.total = dataset_data.data.pagination.total;
      }
    },
  },
  mounted() {
    this.getData();
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
