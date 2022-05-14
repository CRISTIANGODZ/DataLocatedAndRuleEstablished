<template>
  <el-container direction="vertical">
    <!-- <el-header></el-header> -->
    <el-row el-row type="flex" justify="between" :gutter="20">
      <el-col>
        <el-input
          v-model="filterData.dataset"
          placeholder="请输入数据集名称"
        ></el-input>
      </el-col>
      <el-col>
        <el-input
          v-model="filterData.dataset"
          placeholder="请输入标注类别"
        ></el-input>
      </el-col>
      <el-col>
        <el-input
          v-model="filterData.dataset"
          placeholder="请输入数据集名称"
        ></el-input>
      </el-col>
      <el-col>
        <el-input
          v-model="filterData.dataset"
          placeholder="请输入数据集名称"
        ></el-input>
      </el-col>
      <el-col>
        <el-button type="primary">查看模型配置</el-button>
      </el-col>
    </el-row>
    <el-row>
      <el-table
        :data="datasets.data"
        border
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column prop="id" label="编号" width="180"> </el-table-column>
        <el-table-column prop="name" label="数据集名称" width="180">
        </el-table-column>
        <el-table-column prop="textCategory" label="数据集类别">
        </el-table-column>
        <el-table-column
          prop="{isTraing,labeledTextCount,textCount}"
          label="标注状态"
        >
          <template slot-scope="scope">
            <el-progress
              @click="handleLabelStateClick(scope.row)"
              :text-inside="true"
              :stroke-width="15"
              :color="customColorMethod"
              :percentage="
                (100 * scope.row.labeledTextCount) / scope.row.textCount
              "
              @focus="handleLabelStateMouseover(scope.row)"
            ></el-progress>
          </template>
        </el-table-column>
        <!-- <el-table-column>
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"
              >Edit</el-button
            >
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
              >Delete</el-button
            >
            <el-progress
              @click="handleLabelStateClick(scope.row)"
              :text-inside="true"
              :stroke-width="10"
              :color="customColorMethod"
              :percentage="40"
            ></el-progress>
          </template>
        </el-table-column> -->
      </el-table>
    </el-row>
    <el-row>
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="filterData.pagination.currentIndex"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="filterData.pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="filterData.pagination.total"
      >
      </el-pagination>
    </el-row>
  </el-container>
</template>

<script lang="ts">
import Vue from "vue";
const enum TrainingState {
  Untraing = 0,
  Training = 1,
  TrainingSuccess = 2,
  TrainingFail = 3,
}
export default Vue.extend({
  data() {
    return {
      input1: "",
      input2: "",
      input3: "",
      input4: "",
      filterData: {
        dataset: "",
        pagination: {
          currentIndex: 1,
          pageSize: 10,
          total: 3,
        },
      },
      modelParams: {
        circleTime: "1800",
        circleNum: "1",
        startTime: "2022-01-01 00:00:00",
        dataLimit: 1000,
        isTraining: TrainingState.Training,
        modelName: "心脏病",
        textInfo: {
          totalTextCount: 1000,
          labeledTextCount: 900,
          textCategory: "心脏病",
        },
      },
      datasets: {
        pagination: {
          currentIndex: 1,
          pageSize: 10,
          total: 3,
        },
        data: [
          {
            id: 0,
            name: "心脏病",
            textCount: 1000,
            labeledTextCount: 900,
            textCategory: "心脏病",
            isTraing: TrainingState.Training,
          },
          {
            id: 1,
            name: "胃病",
            textCount: 1000,
            labeledTextCount: 0,
            textCategory: "胃病",
            isTraing: TrainingState.Untraing,
          },
          {
            id: 2,
            name: "心理疾病",
            textCount: 2000,
            labeledTextCount: 200,
            textCategory: "心理疾病",
            isTraing: TrainingState.TrainingSuccess,
          },
        ],
      },
      labelPosition: "left",
      formLabelAlign: {
        name: "",
        region: "",
        type: "",
      },
    };
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
  },
});
</script>
<style scoped></style>
