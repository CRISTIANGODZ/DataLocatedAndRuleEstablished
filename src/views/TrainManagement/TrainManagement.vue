<template>
  <el-container direction="vertical">
    <h2>训练管理</h2>
    <!-- <el-header></el-header> -->
    <el-row el-row type="flex" justify="between" :gutter="20">
      <el-col :span="4">
        <el-input v-model="filterData.keyWord" placeholder="输入模型名称搜索" @input="onFilterDataChange" />
      </el-col>
      <el-col :span="4">
        <el-select v-model="filterData.templateTitle" placeholder="请选择模板" @input="onFilterDataChange">
          <el-option v-for="template in jsonData.templateList" :key="template.id" :label="template.title"
            :value="template.title"></el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-select v-model="filterData.state" placeholder="请选择训练状态" @input="onFilterDataChange">
          <el-option v-for="state in stateList" :key="state.id" :label="state.title" :value="state.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <div class="grid-content bg-purple">
          <el-date-picker v-model="filterData.updateTime" align="right" type="date" placeholder="选择训练时间"
            format="yyyy-MM-dd" value-format="yyyy-MM-dd" @change="onFilterDataChange">
          </el-date-picker>
        </div>
      </el-col>
      <el-col :span="1"></el-col>
      <el-col :span="4">
        <div class="grid-content bg-purple">
          <el-button type="primary" @click="clearFilterData">清空所有搜索项</el-button>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-table :data="jsonData.datasets" border highlight-current-row style="width: 100%" @row-click="handleRowClick">
        <el-table-column prop="id" label="编号" width="103"> </el-table-column>
        <el-table-column prop="title" label="模型名称" width="220"></el-table-column>
        <el-table-column prop="state" label="模型状态" width="140">
          <template slot-scope="scope">
            <el-tag :type="scope.row.state === datasetStatus.UNTRAIN ? 'success' : 'info'">
              {{
                  scope.row.state === datasetStatus.UNTRAIN
                    ? datasetStatusZH.UNTRAIN
                    : datasetStatusZH.TRAINING
              }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="上次训练时间" width="180">
        </el-table-column>
        <el-table-column prop="amount" label="总训练次数" width="140">
        </el-table-column>
        <el-table-column prop="templateTitle" label="模板" width="140">
        </el-table-column>
        <el-table-column prop="doneTask" label="已标注数量" width="140">
        </el-table-column>
        <el-table-column prop="totalTask" label="总任务数量" width="140">
        </el-table-column>
        <el-table-column label="操作" width="140">
          <template slot-scope="scope">
            <el-button type="primary" disabled v-if="scope.row.state === datasetStatus.TRAINING">训 练
            </el-button>
            <el-button @click="handleTrainModalOperationVisible(scope.row)" type="primary" v-else>训 练
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
      <el-row el-row type="flex" justify="between" :gutter="20">
        <el-col :span="4">
          <p>最小置信度</p>
        </el-col>
        <el-col :span="10">
          <el-input v-model="trainSet.weight" placeholder="置信度在0到1之间,默认为0" @input="onTrainWeightChange" />
        </el-col>
      </el-row>
      <nobr>置信度大于 </nobr>
      <nobr class="el-data">{{ trainSet.weight }}</nobr>
      <nobr> 的任务有 </nobr>
      <nobr class="el-data">{{ trainSet.totalUsefulParams }}</nobr>
      <nobr> 条</nobr>
      <el-row el-row type="flex" justify="between" :gutter="20">
        <el-col :span="4">
          <p>训练条数</p>
        </el-col>
        <el-col :span="10">
          <el-input v-model="trainSet.param" />
        </el-col>
      </el-row>
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
// import { getDatasetList } from "@/api/dataset";
export default Vue.extend({
  data() {
    return {
      filterData: {
        roleName: "",
        keyWord: "",
        templateTitle: "",
        updateTime: "",
        state: "",
        pagination: {
          currentIndex: 1,
          pageSize: 10,
          total: "",
        },
      },
      filterDataVo: {
        state: "",
        templateTitle: "",
        title: "",
        updateTime: ""
      },
      stateList: [
        {
          id: 0,
          title: "未训练"
        },
        {
          id: 1,
          title: "训练中"
        }
      ],
      jsonData: {
        datasets: [] as Dataset[],
        templateList: [],
      },
      operation: {
        modalVisible: false,
        operationState: DatasetOperation.CHECK,
      },
      selectRole: {} as Dataset,
      trainDatasetModalVisible: false,
      trainSet: {
        templateId: "",
        modelId: "",
        weight: 0.0,
        totalUsefulParams: "",
        param: "",
      },
    };
  },
  components: {
    DatasetModal,
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
      this.filterData.updateTime = "";
      this.filterData.state = "";
      this.filterDataVo.title = "";
      this.filterDataVo.state = "";
      this.filterDataVo.templateTitle = "";
      this.filterDataVo.updateTime = "";
      this.getDataSets();
    },
    getDataSets() {
      this.filterDataVo.title = this.filterData.keyWord;
      this.filterDataVo.state = this.filterData.state;
      this.filterDataVo.templateTitle = this.filterData.templateTitle;
      this.filterDataVo.updateTime = this.filterData.updateTime;
      this.$http
        .post(
          "/model/pageModel/" +
          this.filterData.pagination.currentIndex +
          "/" +
          this.filterData.pagination.pageSize,
          this.filterDataVo
        )
        .then((response) => {
          if (response.data.code === 200) {
            this.jsonData.datasets = response.data.data.modelVoList;
            this.filterData.pagination.total = response.data.data.modelTotal;
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
    handleTrainModalOperationVisible(row) {
      this.trainSet.modelId = row.id;
      for (var i = 0; i < this.jsonData.templateList.length; i++) {
        if (this.jsonData.templateList[i].title === row.templateTitle) {
          this.trainSet.templateId = this.jsonData.templateList[i].id;
        }
      }
      this.getTrainParam();
      this.trainDatasetModalVisible = true;
    },
    hideTrainModalOperationVisible() {
      this.trainDatasetModalVisible = false;
    },
    onTrainWeightChange() {
      this.getTrainParam();
    },
    getTrainParam() {
      this.$http
        .get(
          "/model/trainParam/" +
          this.trainSet.weight +
          "/" +
          this.trainSet.templateId
        )
        .then((response) => {
          if (response.data.code === 200) {
            this.trainSet.totalUsefulParams = response.data.data;
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    async onSaveTrainModalOperationVisible() {
      if (this.trainSet.param > this.trainSet.totalUsefulParams) {
        this.$message({
          message: '训练数量应小于总任务数量',
          type: 'warning'
        });
        return;
      }
      if (this.trainSet.weight === "") {
        this.trainSet.weight = 0;
      }
      this.$http
        .post(
          "/model/train/",
          {
            templateId: this.trainSet.templateId,
            modelId: this.trainSet.modelId,
            weight: this.trainSet.weight,
            batchSize: this.trainSet.param,
          }
        )
        .then((response) => {
          if (response.data.code === 200) {
            this.$message({
              message: '模型训练成功！',
              type: 'success'
            });
            this.getDataSets();
            this.hideTrainModalOperationVisible();
          }
        })
        .catch((error) => {
          console.log(error);
        });
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
    this.getTemplateList();
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

.el-data {
  color: dodgerblue;
  font-weight: 1000;
}
</style>
