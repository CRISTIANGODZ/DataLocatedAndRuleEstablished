<template>
  <el-container direction="vertical">
    <h2>训练管理</h2>
    <!-- <el-header></el-header> -->
    <el-row el-row type="flex" justify="between" :gutter="20">
      <el-col>
        <el-input v-model="filterData.keyWord" placeholder="请输入数据集名称" @input="onFilterDataChange" />
      </el-col>
    </el-row>
    <el-row>
      <el-table :data="jsonData.datasets" border highlight-current-row style="width: 100%" @row-click="handleRowClick">
        <el-table-column type="index" label="编号" width="103"> </el-table-column>
        <el-table-column prop="name" label="数据集名称" width="260"></el-table-column>
        <el-table-column prop="status" label="数据集状态" width="140">
          <template slot-scope="scope">
            <el-tag :type="
              scope.row.status === datasetStatus.UNTRAIN
                ? ''
                : scope.row.status === datasetStatus.TRAINING
                  ? 'info'
                  : 'success'
            ">
              {{
                  scope.row.status === datasetStatus.UNTRAIN
                    ? datasetStatusZH.UNTRAIN
                    : scope.row.status === datasetStatus.TRAINING
                      ? datasetStatusZH.TRAINING
                      : datasetStatusZH.TRAINED
              }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastTrainTime" label="上次训练时间" width="140">
        </el-table-column>
        <el-table-column prop="trainedTimes" label="总训练次数" width="140">
        </el-table-column>
        <el-table-column prop="templateName" label="模板" width="140">
        </el-table-column>
        <el-table-column prop="labeledTaskCount" label="已标注数量" width="140">
        </el-table-column>
        <el-table-column prop="totalTaskCount" label="总任务数量" width="140">
        </el-table-column>
        <el-table-column label="操作" width="140">
          <template slot-scope="scope">
            <el-button type="primary" disabled v-if="scope.row.status">训 练
            </el-button>
            <el-button @click="handleTrainModalOperationVisible" type="primary" v-else>训 练
            </el-button>
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
    <el-dialog title="训练" :visible.sync="trainDatasetModalVisible">
      <el-form :model="trainSet">
        <el-form-item label="最小置信度" :label-width="formLabelWidth">
          <el-input v-model="trainSet.minConfidence"></el-input>
        </el-form-item>
        <el-form-item label="训练条数">
          <el-input v-model="trainSet.trainBars"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="hideTrainModalOperationVisible">取 消</el-button>
        <el-button type="primary" @click="onSaveTrainModalOperationVisible">确 定</el-button>
      </div>
    </el-dialog>
    <dataset-modal></dataset-modal>
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
export default Vue.extend({
  data() {
    return {
      filterData: {
        roleName: "",
        keyWord: "",
        pagination: {
          currentIndex: 1,
          pageSize: 10,
          total: 2,
        },
      },
      jsonData: {
        // datasets: [] as Dataset[],
        datasets: [
          {
            id: 111,
            name: "2月前患者无明显诱因开始出现",
            status: DatasetStatus.UNTRAIN,
            lastTrainTime: "2022-05-02",
            trainedTimes: 3,
            templateName: "影像检查",
            labeledTaskCount: 1000,
            totalTaskCount: 2000,
          },
          {
            id: 222,
            name: "2月前患者无明显诱因开始出现",
            status: DatasetStatus.TRAINING,
            lastTrainTime: "2022-05-02",
            trainedTimes: 2,
            templateName: "影像检查",
            labeledTaskCount: 1000,
            totalTaskCount: 2000,
          },
        ]
      },
      operation: {
        modalVisible: false,
        operationState: DatasetOperation.CHECK,
      },
      selectRole: {} as Dataset,
      trainDatasetModalVisible: false,
      trainSet: {
        minConfidence: 0.7,
        trainBars: 100,
      },
    };
  },
  components: {
    DatasetModal,
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
    onFilterDataChange() {
      console.log("filterData: ", this.filterData);
      this.getDataSets();
    },
    getDataSets() {
      this.$http
        .post(
          "/datasets/pageText/" +
          this.filterData.pagination.currentIndex +
          "/" +
          this.filterData.pagination.pageSize,
          {
            textTitle: this.filterData.keyWord,
          }
        )
        .then((response) => {
          if (response.data.code === 200) {
            this.jsonData.datasets = response.data.data;
            this.filterData.pagination.total = response.data.data.total;
            this.filterData.pagination.currentIndex =
              response.data.data.current;
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
    // handleEdit(index, row) {
    //   console.log(index, row);
    // },
    // handleDelete(index, row) {
    //   console.log(index, row);
    // },
    // handleLabelStateMouseover(row) {
    //   console.log("handleLabelStateMouseover");
    //   console.log(row);
    // },
    // handleModalOperationVisible() {
    //   console.log("dddd");
    //   this.operation.modalVisible = true;
    // },
    handleRowClick(row, column, event) {
      this.selectRole = row;
      console.log(row);
      console.log(column);
    },
    handleTrainModalOperationVisible() {
      this.trainDatasetModalVisible = true;
    },
    hideTrainModalOperationVisible() {
      this.trainDatasetModalVisible = false;
    },
    async onSaveTrainModalOperationVisible() {
      this.$http
        .post(
          "/datasets/train/",
          {
            minConfidence: this.trainSet.minConfidence,
            trainBars: this.trainSet.trainBars,
          }
        )
        .then((response) => {
          if (response.data.code === 200) {
            this.getData();
            this.hideTrainModalOperationVisible();
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    // handleEditModalOperationVisible() {
    //   this.handleModalOperationVisible();
    //   this.operation.operationState = DatasetOperation.EDIT;
    // },
    // handleCheckModalOperationVisible() {
    //   this.handleModalOperationVisible();
    //   this.operation.operationState = DatasetOperation.CHECK;
    // },
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
    // handleDeleteModalOperationVisible() {
    //   this.handleModalOperationVisible();
    //   this.operation.operationState = DatasetOperation.DELETE;
    // },
    // handleConfirmDelete() {
    //   this.$success("删除成功");
    //   this.handleCloseModalOperation();
    // },
    // handleCloseModalOperation() {
    //   this.operation.modalVisible = false;
    //   if (this.operation.operationState !== DatasetOperation.EDIT) {
    //     this.getRoles();
    //   }
    // },
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
