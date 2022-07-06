<template>
  <el-container direction="vertical">
    <h2>模型管理</h2>
    <!-- <el-header></el-header> -->
    <div v-if="showDetails">
      <el-row>
        <el-button type="primary" icon="el-icon-arrow-left" @click="returnList">返回</el-button>
      </el-row>
      <el-row>
        <el-table :data="jsonData.historicList" border highlight-current-row :style="tableWidth(76.01)"
          empty-text="暂无数据" @row-click="handleRowClick">
          <el-table-column type="index" :width="remSize(4)"></el-table-column>
          <el-table-column prop="updateTime" label="上次训练时间" :width="remSize(9)">
            <template slot-scope="scope">
              <p>
                {{
                    scope.row.updateTime == null
                      ? "暂无"
                      : scope.row.updateTime
                }}</p>
            </template>
          </el-table-column>
          <el-table-column prop="state" label="模型状态" :width="remSize(8)">
            <template slot-scope="scope">
              <el-tag :type="scope.row.state === datasetStatus.UNTRAIN ? 'success' : 'info'">
                {{
                    scope.row.state === datasetStatus.UNTRAIN
                      ? datasetStatusZH.UNTRAIN
                      : datasetStatusZH.TRAINING
                }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="useWeight" label="上次训练最小权重" :width="remSize(8)">
            <template slot-scope="scope">
              <p v-text="scope.row.useWeight * 100 + '%'"></p>
            </template>
          </el-table-column>
          <el-table-column prop="useBatch" label="上次训练任务数量" :width="remSize(8)">
          </el-table-column>
          <el-table-column label="准确率" :width="remSize(8)">
            <template slot-scope="scope">
              <p v-text="percentage(scope.row.acc, 10000) + '%'"></p>
            </template>
          </el-table-column>
          <el-table-column label="F1-score" :width="remSize(8)">
            <template slot-scope="scope">
              <p v-text="percentage(scope.row.score, 100)"></p>
            </template>
          </el-table-column>
          <el-table-column prop="preModel" label="选用模型" :width="remSize(10)">
            <template slot-scope="scope">
              <el-button :type="scope.row.preModel === 2 ? 'success' : scope.row.preModel === 1 ? 'info' : ''"
                style="width:120px" :disabled="scope.row.state == '1'" @click="setPredictiveModel(scope.row)"
                v-text="scope.row.preModel === 0 ? '设为预测模型' : scope.row.preModel === 2 ? '预测模型' : '正在加载模型'">
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="操作" :width="remSize(13)">
            <template slot-scope="scope">
              <el-button type="primary" :disabled="scope.row.state == '1'"
                @click="handleTrainModalOperationVisible(scope.row)">训练
              </el-button>
              <el-button type="danger" :disabled="scope.row.state == '1'"
                @click="handleDeleteModalOperationVisible(scope.row)">删除
              </el-button>
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
    </div>
    <div v-else>
      <el-row el-row type="flex" justify="between" :gutter="20">
        <el-col :span="4">
          <el-input v-model="filterData.keyWord" placeholder="输入模型名称搜索" @input="onFilterDataChange" />
        </el-col>
        <el-col :span="4">
          <el-select v-model="filterData.templateTitle" clearable placeholder="请选择模板" @input="onFilterDataChange">
            <el-option v-for="template in jsonData.templateList" :key="template.id" :label="template.title"
              :value="template.title"></el-option>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="filterData.state" clearable placeholder="请选择训练状态" @input="onFilterDataChange">
            <el-option v-for="state in stateList" :key="state.id" :label="state.title" :value="state.id">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="4">
          <div class="grid-content bg-purple">
            <el-date-picker v-model="filterData.updateTime" align="right" clearable type="date" placeholder="选择训练时间"
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
        <el-table :data="jsonData.datasets" border highlight-current-row empty-text="暂无" :style="tableWidth(75.01)"
          @row-click="handleRowClick">
          <el-table-column type="index" label="编号" :width="remSize(6)"> </el-table-column>
          <el-table-column prop="title" label="模型名称" :width="remSize(10)"></el-table-column>
          <el-table-column prop="state" label="模型状态" :width="remSize(8)">
            <template slot-scope="scope">
              <el-tag :type="scope.row.state === datasetStatus.UNTRAIN ? 'success' : 'info'">
                {{
                    scope.row.state === datasetStatus.UNTRAIN
                      ? datasetStatusZH.UNTRAIN
                      : datasetStatusZH.TRAINING
                }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="updateTime" label="上次训练时间" :width="remSize(10)">
          </el-table-column>
          <el-table-column prop="amount" label="总训练次数" :width="remSize(8)">
          </el-table-column>
          <el-table-column prop="templateTitle" label="模板" :width="remSize(8)">
          </el-table-column>
          <el-table-column prop="doneTask" label="已标注数量" :width="remSize(8)">
          </el-table-column>
          <el-table-column prop="totalTask" label="总任务数量" :width="remSize(8)">
          </el-table-column>
          <el-table-column label="操作" :width="remSize(9)">
            <template slot-scope="scope">
              <el-button type="primary" @click="handleHistoricVersionOperationVisible(scope.row)">详情
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
    </div>
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
          <p>最小权重</p>
        </el-col>
        <el-col :span="10">
          <el-select v-model="trainSet.weight" filterable allow-create clearable default-first-option
            placeholder="权重在 0 到 100 之间" style="width:250px" @change="onTrainWeightChange">
            <el-option v-for="item in confidence" :key="item" :label="item" :value="item">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <nobr>权重大于 </nobr>
      <nobr class="el-data">{{ String(trainSet.weight) == "" ? 0 : trainSet.weight }} %</nobr>
      <nobr> 的任务有 </nobr>
      <nobr class="el-data">{{ trainSet.totalUsefulParams }}</nobr>
      <nobr> 条</nobr>
      <el-row el-row type="flex" justify="between" :gutter="20">
        <el-col :span="4">
          <p>训练条数 (不能小于10条)</p>
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
      filterText: {
        pagination: {
          currentIndex: 1,
          pageSize: 10,
          total: "",
        },
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
      confidence: [0, 20, 50, 70, 90],
      jsonData: {
        datasets: [],
        templateList: [],
        historicList: [],
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
      showDetails: false,
      trainDatasetModalVisible: false,
      predictiveModelOperateLock: true,
      trainSet: {
        state: "",
        templateId: "",
        modelId: "",
        modelHistoryId: "",
        preModelId: "",
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
      this.getHistoricVersions();
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
    handleRowClick(row, column, event) {
      this.selectRole = row;
      console.log(row);
      console.log(column);
    },
    returnList() {
      this.getDataSets();
      this.showDetails = false;
    },
    handleHistoricVersionOperationVisible(row) {
      this.trainSet.modelId = row.id;
      this.trainSet.state = row.state;
      this.filterText.pagination.pageSize = 10;
      this.filterText.pagination.currentIndex = 1;
      for (var i = 0; i < this.jsonData.templateList.length; i++) {
        if (this.jsonData.templateList[i].title === row.templateTitle) {
          this.trainSet.templateId = this.jsonData.templateList[i].id;
        }
      }
      this.getHistoricVersions();
      this.showDetails = true;
    },
    handleTrainModalOperationVisible(row) {
      this.trainSet.weight = 0.0;
      this.trainSet.totalUsefulParams = 0;
      this.trainSet.param = 10;
      this.trainSet.modelHistoryId = row.id;
      this.getTrainParam();
      this.trainDatasetModalVisible = true;
    },
    hideTrainModalOperationVisible() {
      this.trainDatasetModalVisible = false;
    },
    handleDeleteModalOperationVisible(row) {
      this.$confirm("确定要删除这个历史版本吗？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$http.delete("/model/deleteModelHistory/" + row.id + "/")
            .then((response) => {
              if (response.data.code === 200) {
                this.$message({
                  message: '删除成功！',
                  type: 'success'
                });
                this.getHistoricVersions();
              }
            })
            .catch((error) => {
              console.log(error);
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    onTrainWeightChange() {
      this.getTrainParam();
    },
    trainSetWeightFilterChange(column) {
      this.trainSet.weight = column;
      this.onTrainWeightChange();
    },
    getHistoricVersions() {
      this.$http
        .get(
          "/model/modelHistory/" +
          this.trainSet.modelId + "/" +
          this.filterText.pagination.currentIndex + "/" +
          this.filterText.pagination.pageSize
        )
        .then((response) => {
          if (response.data.code === 200) {
            var index = -1;
            let list = response.data.data.modelHistoryList
            for (var i = 0; i < list.length; i++) {
              if (list[i].state == "1") {
                index = i;
              }
              if (list[i].preModel != 0) {
                this.trainSet.preModelId = list[i].id;
              }
            }
            for (; index > 0; index--) {
              [list[index], list[index - 1]] = [list[index - 1], list[index]];
            }
            this.jsonData.historicList = list;
            this.filterText.pagination.total = response.data.data.modelHistoryTotal;
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getTrainParam() {
      this.$http
        .get(
          "/model/trainParam/" +
          this.trainSet.weight / 100.0 +
          "/" +
          this.trainSet.templateId +
          "/" +
          this.trainSet.modelHistoryId
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
    postTrainSet() {
      this.$http
        .post(
          "/model/train/",
          {
            templateId: this.trainSet.templateId,
            modelId: this.trainSet.modelId,
            weight: this.trainSet.weight / 100.0,
            batchSize: this.trainSet.param,
            modelHistoryId: this.trainSet.modelHistoryId,
          }
        )
        .then((response) => {
          if (response.data.code === 200) {
            this.$message({
              message: '模型训练完成！',
              type: 'success'
            });
          } else if (response.data.code === 201) {
            this.$message({
              message: '模型训练失败',
              type: 'error'
            });
          }
          this.getDataSets();
          this.getHistoricVersions();
          return 1;
        })
        .catch((error) => {
          console.log(error);
          return 0;
        });
    },
    onSaveTrainModalOperationVisible() {
      if (this.trainSet.param <= 0) {
        this.$message({
          message: '请输入正整数！',
          type: 'warning'
        });
        return;
      } else if (this.trainSet.param <= 10) {
        this.$message({
          message: '训练条数不能小于10条！',
          type: 'warning'
        });
        return;
      }
      if (this.trainSet.param > this.trainSet.totalUsefulParams) {
        this.$message({
          message: '没有这么多数据！训练数量应小于总任务数量',
          type: 'warning'
        });
        return;
      }
      // if (this.jsonData.historicList.length >= 5) {
      //   this.$alert("最多保存五个历史版本，请先删除一个历史版本再训练", "警告", {
      //     confirmButtonText: "确定",
      //     type: "warning",
      //   }).then(() => {
      //     this.hideTrainModalOperationVisible();
      //   }).catch(() => {
      //     this.hideTrainModalOperationVisible();
      //   });
      //   return;
      // }
      if (this.trainSet.weight === "") {
        this.trainSet.weight = 0;
      }
      var res = this.postTrainSet();
      if (res == 0) {
        this.postTrainSet();
      }
      this.hideTrainModalOperationVisible();
      var delayInMilliseconds = 500; //等待500毫秒
      setTimeout(this.getHistoricVersions, delayInMilliseconds);
    },
    setPredictiveModel(row) {
      if (row.preModel != 0) { return; }
      if (this.predictiveModelOperateLock == true) {
        this.predictiveModelOperateLock = false;
      } else {
        this.$message({
          message: '模型正在加载，请稍等',
          type: 'warning'
        });
        return;
      }
      this.$http
        // .get(
        //   "/model/setPredictModel/" +
        //   // this.trainSet.preModelId + "/" +
        //   row.id
        // )
        .post(
          "/model/setPredictModel/",
          {
            id: row.id
          }
        )
        .then((response) => {
          if (response.data.code === 200) {
            this.$message({
              message: '模型更改成功！',
              type: 'success'
            });
          } else {
            this.$message({
              message: '模型更改失败！',
              type: 'error'
            });
          }
          this.getHistoricVersions();
          this.predictiveModelOperateLock = true;
        })
        .catch((error) => {
          console.log(error);
        });
      // this.getHistoricVersions();
      var delayInMilliseconds = 700; //等待700毫秒
      setTimeout(this.getHistoricVersions, delayInMilliseconds);
    },
    percentage(num, multiple) {
      return Math.round(num * multiple) / 100;
    },
    remSize(size) {
      //获取设备宽度
      var deviceWidth = document.documentElement.clientWidth || window.innerWidth;
      return Math.floor(deviceWidth * size / 100) + "px";
    },
    tableWidth(size) {
      var width = this.remSize(size);
      return "width:" + width;
    },
  },
  mounted() {
    this.getDataSets();
    this.getTemplateList();
    // window.onresize = function () {
    //   var deviceWidth = document.body.clientWidth || window.innerWidth;
    //   document.documentElement.style.fontSize = Math.round((deviceWidth / 100)) + 'px';
    //   document.querySelector('body').style.fontSize = 0.3 + 'rem'
    //   console.log("*************");
    // }
  },
  // watch: {
  //   onresize
  // },
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