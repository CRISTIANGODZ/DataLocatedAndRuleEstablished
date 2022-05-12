<template>
  <div>
    <el-row>
      <el-col :span="2">
        <v-chip label> 任务状态: </v-chip>
      </el-col>
      <el-col>
        <el-tabs v-model="filterData.taskFOU" @tab-click="onFilterDataChange">
          <el-tab-pane label="全部" name="-1" />
          <el-tab-pane label="未完成" name="0" />
          <el-tab-pane label="已完成" name="1" />
        </el-tabs>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="2"> <v-chip label> 任务类别: </v-chip> </el-col>
      <el-col :span="24">
        <el-tabs
          v-model="filterData.taskCategoriesCurrent"
          @tab-click="onFilterDataChange"
        >
          <el-tab-pane
            v-for="taskCategory in jsonData.taskCategories"
            :label="taskCategory.title"
            :name="taskCategory.id + ''"
            :key="taskCategory.title"
          />
        </el-tabs>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4"
        ><div class="grid-content bg-purple">
          <el-select
            v-model="filterData.labelPerson"
            filterable
            placeholder="请选择标注人"
            @input="onFilterDataChange"
          >
            <el-option
              v-for="person in jsonData.labelPersons"
              :key="person.id"
              :label="person.username"
              :value="person.username"
            >
            </el-option>
          </el-select></div
      ></el-col>
      <el-col :span="4"
        ><div class="grid-content bg-purple">
          <el-input
            v-model="filterData.title"
            @input="onFilterDataChange"
            placeholder="请输入标题内容"
          ></el-input></div
      ></el-col>
      <el-col :span="4"
        ><div class="grid-content bg-purple">
          <el-select
            v-model="filterData.templateCategory"
            filterable
            placeholder="请选择模板"
            @change="onFilterDataChange"
          >
            <el-option
              v-for="category in jsonData.templatesCategories"
              :key="category.id"
              :label="category.title"
              :value="category.title"
            >
            </el-option
          ></el-select></div
      ></el-col>
      <el-col :span="4"
        ><div class="grid-content bg-purple">
          <el-date-picker
            v-model="filterData.startDate"
            align="right"
            type="date"
            placeholder="选择开始日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            @change="onFilterDataChange"
            :picker-options="pickerOptions"
          >
          </el-date-picker></div
      ></el-col>
      <el-col :span="4"
        ><div class="grid-content bg-purple">
          <el-date-picker
            v-model="filterData.endDate"
            align="right"
            type="date"
            placeholder="选择最后日期"
            @change="onFilterDataChange"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            :picker-options="pickerOptions"
          >
          </el-date-picker></div
      ></el-col>
      <el-col :span="4"
        ><div class="grid-content bg-purple">
          <el-button type="primary" @click="clearFilterData"
            >清空所有搜索项</el-button
          >
        </div></el-col
      >
    </el-row>
    <el-row>
      <el-table
        :data="jsonData.tasks"
        style="width: 100%; margin-top: 20px; margin-bottom: 20px"
        border
        :default-sort="{ prop: 'date', order: 'descending' }"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" width="50"> </el-table-column>
        <el-table-column prop="title" label="标题" sortable width="180">
        </el-table-column>
        <el-table-column prop="name" label="标注人"> </el-table-column>
        <el-table-column prop="updateTime" label="最近标注时间">
        </el-table-column>
        <el-table-column prop="taskCategory" label="任务类型">
        </el-table-column>
        <el-table-column prop="templateCategory" label="模板类型">
        </el-table-column>
        <el-table-column prop="status" label="完成状态">
          <template slot-scope="scope">
            <v-chip
              v-if="scope.row.doneState === 1"
              @click="handleTaskEditAction(scope.row)"
              color="green"
              filter
              label
            >
              已完成 <i class="el-icon-view" style="padding-left: 5px"></i>
            </v-chip>
            <v-chip
              v-else
              color="yellow"
              @click="handleTaskEditAction(scope.row)"
              filter
              label
            >
              未完成 <i class="el-icon-edit" style="padding-left: 5px"></i>
            </v-chip>
          </template>
        </el-table-column>
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
    <!-- <MissionAnnotate :dialogVisible="missionAnnotateVisiable" /> -->
  </div>
</template>

<script>
// import MissionAnnotate from "./MissionAnnotate";
// taskStatus 任务状态: -1: 所有, 0: 未完成 1: 已完成
// taskCategoriesCurrent 任务类别: -1 表示所有

export default {
  // components: { MissionAnnotate },
  data() {
    return {
      missionAnnotateVisiable: false,
      filterData: {
        taskFOU: "-1",
        taskCategoriesCurrent: "-1",
        labelPerson: "",
        title: "",
        templateCategory: "",
        startDate: "",
        endDate: "",
        pagination: {
          currentIndex: 1,
          total: 3,
          pageSize: 5,
        },
      },
      defaultTaskCategories: [{ id: -1, title: "全部", name: "all" }],
      jsonData: {
        taskCategories: [
          { id: -1, title: "全部", name: "all" },
          { id: 1, title: "外科", name: "surgical" },
          { id: 2, title: "内科", name: "internal" },
          { id: 3, title: "精神科", name: "spiritual" },
          { id: 4, title: "骨科", name: "bone" },
        ],
        defaultTaskCatogory: 0,
        labelPersons: [
          { id: 0, username: "李四" },
          { id: 1, username: "王五" },
          { id: 2, username: "赵六" },
          { id: 6, username: "周期" },
        ],
        templatesCategories: [
          { id: 1, title: "模板1" },
          { id: 2, title: "模板2" },
          { id: 3, title: "模板3" },
        ],
        tasks: [
          {
            id: 0,
            doneState: 1,
            title: "title1",
            textId: 1,
            updateTime: "2019-01-01",
            templateCategoryId: 1,
            templateCategory: "模板1",
            labelPersonId: 1,
            name: "李四",
            taskCategoryId: 1,
            taskCategory: "外科",
          },
          {
            id: 1,
            doneState: 1,
            title: "title2",
            textId: 2,
            updateTime: "2019-01-02",
            templateCategoryId: 2,
            templateCategory: "模板2",
            labelPersonId: 2,
            name: "王五",
            taskCategoryId: 2,
            taskCategory: "内科",
          },
          {
            id: 2,
            doneState: 2,
            title: "title3",
            textId: 3,
            updateTime: "2019-01-03",
            templateCategoryId: 3,
            templateCategory: "模板3",
            labelPersonId: 3,
            name: "赵六",
            taskCategoryId: 3,
            taskCategory: "精神科",
          },
        ],
        pagination: {
          currentIndex: 1,
          total: 3,
          pageSize: 10,
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
    };
  },
  methods: {
    handleClick({ index, name }, event) {
      console.log(index, name, event);
    },
    // 选择任务状态
    onTaskFOUChange({ index, name }, event) {
      console.log("任务状态: ", name);
      this.filterData.taskFOU = name;
    },
    // 选择任务类别
    onTaskCategoryChange({ index, name }, event) {
      console.log("任务类型: ", name);
      this.filterData.taskCategoriesCurrent = name;
    },
    // 选择标注人
    onLabelPersonChange(e) {
      console.log("标注人: ", e);
      this.filterData.labelPerson = e;
    },
    onTitleChange(e) {
      console.log("标题: ", e);
      this.filterData.title = e;
    },
    onFilterDataChange() {
      console.log("filterData: ", this.filterData);
      this.getTasks();
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
    handleTaskEditAction(row) {
      console.log("编辑的row:", row);
      this.missionAnnotateVisiable = true;
      this.$router
        .push({
          path: "/missionAnnotate",
          query: {
            taskId: row.id,
          },
        })
        .catch((_) => {});
    },
    // 使用上面 handleCurrentChange 方法即可
    // handlePrevClick(val) {
    //   console.log(`当前页: ${val}`);
    //   this.filterData.pagination.currentIndex = val;
    //   // this.onFilterDataChange();
    // },
    //  handleNextClick(val) {
    //   console.log(`当前页: ${val}`);
    //   this.filterData.pagination.currentIndex = val;
    //   // this.onFilterDataChange();
    // },
    handleRowClick(row, event, column) {
      console.log(row, event, column);
    },
    clearFilterData() {
      this.filterData = {
        taskFOU: "-1",
        taskCategoriesCurrent: "-1",
        labelPerson: "",
        title: "",
        templateCategory: "",
        startDate: "",
        endDate: "",
        pagination: this.filterData.pagination,
      };
      this.getTasks();
    },
    // 获取后端数据
    getTasks() {
      this.$http
        .post(
          "/task/pageTask/" +
            this.filterData.pagination.currentIndex +
            "/" +
            this.filterData.pagination.pageSize,
          {
            doneState:
              this.filterData.taskFOU == "-1" ? null : this.filterData.taskFOU,
            taskCategoryId:
              this.filterData.taskCategoriesCurrent == "-1"
                ? null
                : this.filterData.taskCategoriesCurrent,
            username: this.filterData.labelPerson,
            textTitle: this.filterData.title,
            templateTitle: this.filterData.templateCategory,
            fromTime: this.filterData.startDate,
            toTime: this.filterData.endDate,
            // pageIndex: this.filterData.pagination.currentIndex,
            // pageSize: this.filterData.pagination.pageSize,
          }
        )
        .then((res) => {
          console.log("res: ", res);
          // this.filterData.tasks = res.data.data.tasks;
          this.jsonData.taskCategories = this.defaultTaskCategories.concat(
            res.data.data.taskCategoryList
          );
          this.jsonData.labelPersons = res.data.data.userList;
          this.jsonData.templatesCategories = res.data.data.templateList;
          this.jsonData.tasks = res.data.data.taskVoList;
          this.filterData.pagination.total = res.data.data.total;
        });
    },
    getTaskCategories() {},
    getTemplatesCategories() {},
    getLabelPersons() {},
  },
  mounted() {
    this.getTasks();
  },
};
</script>

<style scoped>
/* .el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
} */
</style>
