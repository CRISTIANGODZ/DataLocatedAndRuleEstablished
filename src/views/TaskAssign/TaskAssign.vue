<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-input
          v-model="filterData.keyWord"
          placeholder="输入关键字搜索"
          @input="onFilterDataChange"
        />
      </el-col>
      <el-col :span="8">
        <el-select
          v-model="taskAssignList.personId"
          multiple
          placeholder="请选择标注人"
        >
          <el-option
            v-for="item in options"
            :key="item.id"
            :label="item.username"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="6" class="btn-assign">
        <el-button type="primary" round @click="handleAssigin"
          >分配任务</el-button
        >
      </el-col>
    </el-row>
    <el-table
      ref="taskTableRef"
      :data="tableData"
      lazy
      @selection-change="handleSelectionChange"
      style="width: 100%"
      :row-style="{ height: '80px' }"
      @select="handleToggleRowSelection"
      @select-all="handleToggleAllSelection"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="上传时间" sortable prop="uploadTime">
      </el-table-column>
      <el-table-column label="Title" sortable prop="title"> </el-table-column>
      <el-table-column label="内容" sortable prop="content">
        <template slot-scope="scope">
          <div>
            {{
              ("" + scope.row.content).substring(0, 20) +
              (("" + scope.row.content).length >= 20 ? "..." : "")
            }}
          </div>
          <el-button type="text" @click="checkTextContent(scope.row)">
            点击查看更多内容</el-button
          >
        </template>
      </el-table-column>
      <el-table-column align="right" fixed="right">
        <template slot-scope="scope">
          <div style="display: flex; overflow: hidden; flex: 1">
            <!-- <span>{{scope.row}}</span> -->
            <TaskSelector
              :data="scope.row"
              :taskCategories="taskCategories"
              v-on:taskSelected="taskSelectedByValue"
            />
            <TemplateSelector
              :data="scope.row"
              :templateIds="templateIds"
              v-on:templateSelected="templateSelectedByValue"
            />
          </div>
        </template>
      </el-table-column>
    </el-table>
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
    <el-dialog
      title="文本详情"
      :visible.sync="dialogVisible"
      width="70%"
      top="3%"
      :before-close="handleClose"
    >
      <h2>{{ checkTextRow.title }}</h2>
      <p style="color: grey">{{ checkTextRow.uploadTime }}</p>
      <span v-html="checkTextRow.content"></span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import TaskSelector from "./TaskSelector.vue";
import TemplateSelector from "./TemplateSelector.vue";

export default {
  components: { TaskSelector, TemplateSelector },
  data() {
    return {
      dialogVisible: false,
      filterData: {
        keyWord: "",
        pagination: {
          currentIndex: 1,
          total: 3,
          pageSize: 5,
        },
      },
      checkTextRow: {
        title: "",
        id: "",
        content: "",
      },
      tableData: [
        {
          id: 1,
          uploadTime: "2022-05-02",
          title: "精神病标注",
          taskValue: "",
          templateValue: "",
        },
        // {
        //   id: 2,
        //   uploadTime: "2022-05-04",
        //   title: "心脏病标注",
        //   taskValue: "",
        //   templateValue: "",
        // },
        // {
        //   id: 3,
        //   uploadTime: "2022-05-01",
        //   title: "高血压标注",
        //   taskValue: "",
        //   templateValue: "",
        // },
        // {
        //   id: 4,
        //   uploadTime: "2022-05-03",
        //   title: "新冠肺炎标注",
        //   taskValue: "",
        //   templateValue: "",
        // },
      ],
      search: "",
      options: [
        {
          value: "0",
          label: "全选",
        },
        {
          value: "张三-id",
          label: "张三",
        },
        {
          value: "李四-id",
          label: "李四",
        },
        {
          value: "王五-id",
          label: "王五",
        },
      ],
      taskAssignList: {
        personId: [],
        taskList: [{ textId: "", templateId: "", taskCategoryId: "" }],
      },
      value: "",
      taskValue: [],
      multipleSelection: [],
      sendValues: [],
      templateIds: [],
      taskCategories: [],
    };
  },
  methods: {
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
          this.checkTextRow = {
            title: "",
            id: "",
            content: "",
          };
        })
        .catch((_) => {});
    },
    checkTextContent(row) {
      this.dialogVisible = true;
      this.checkTextRow.id = row.id;
      this.checkTextRow.title = row.title;
      this.checkTextRow.uploadTime = row.uploadTime;
      this.checkTextRow.content = row.content || "";

      // this.checkTextRow.content.replaceAll(/\n|\r/g, "<br/>");
      if (!this.checkTextRow.content.includes("<\/p>")) {
        this.checkTextRow.content = this.checkTextRow.content
          .split(/\n|\r/g)
          .map((p) => {
            return `<p>${p}</p>`;
          })
          .join("");
      }
      console.log("row:", row);
    },
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },
    taskSelectedByValue(taskValues) {
      console.log("taskValues:", taskValues);
      // this.tableData.map(item => {
      //   if (item.date === taskValues.currentDate && item.name === taskValues.currentName) {
      //     console.log('找到！');
      //     console.log({...item, taskValue: taskValues.taskValue});
      //     return {...item, taskValue: taskValues.taskValue}
      //   } else {
      //     return item
      //   }
      // })
      this.tableData.forEach((item, index) => {
        if (item.id === taskValues.textId) {
          this.tableData[index].taskValue = taskValues.taskValue;
        }
      });
      // console.log('after', this.tableData);
      // console.log('taskSelectedByValue执行结束');
    },
    templateSelectedByValue(templateValues) {
      console.log("templateValues:", templateValues);
      this.tableData.forEach((item, index) => {
        if (item.id === templateValues.textId) {
          this.tableData[index].templateValue = templateValues.templateValue;
        }
      });
    },
    handleAssigin(index, row) {
      if (this.taskAssignList.personId.length === 0) {
        this.$message({
          message: "请选择人员",
          type: "warning",
          showClose: true,
        });
        return;
      }
      let assginPersons = [],
        usernames = [];
      this.options.forEach((v) => {
        this.taskAssignList.personId.forEach((item) => {
          if (v.id === item) {
            assginPersons.push({ id: item, username: v.username });
            usernames.push(v.username);
          }
        });
      });
      console.log("assginPersons:", assginPersons);
      // this.taskAssignList.personId.forEach((item) => {
      //   assginPersons.push({
      //     personId: item,
      //     taskCategoryId: this.taskAssignList.taskList[0].taskCategoryId,
      //     textId: row.id,
      //     templateId: this.taskAssignList.taskList[0].templateId,
      //   });
      // });
      console.log("分配任务button点击");
      console.log("所选择的标注人：", this.value);
      console.log("所选择的要分配的任务数组：", this.sendValues);
      console.log("分配的任务为: ", this.taskAssignList);
      this.$confirm(
        `确定将这些任务分配给 ${usernames.join(", ")} 吗？`,
        "任务分配",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          // callback: (action) => {
          //   this.$message({
          //     type: "info",
          //     message: `任务分配: ${action === "cancel" ? "失败" : "成功"}`,
          //   });
          // },
        }
      )
        .then(() => {
          this.$http
            .post("/taskDistribution/distribute", {
              userId: this.taskAssignList.personId,
              idForDistribution: this.taskAssignList.taskList,
            })
            .then((res) => {
              if (res.data.code === 200) {
                this.$message({
                  type: "success",
                  message: "任务分配成功!",
                });
              } else {
                this.$message({
                  type: "error",
                  message: "任务分配失败!",
                });
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消任务分配",
          });
        });
    },
    handleSelectionChange(val) {
      this.sendValues = val;
      this.multipleSelection = val;
      console.log("val:", val);
      this.taskAssignList.taskList = val.map((v) => ({
        textId: v.id,
        templateId: v.templateValue,
        taskCategoryId: v.taskValue,
      }));
    },

    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleToggleRowSelection(selection, row) {
      console.log(row, selection);
      if (row.taskValue === "") {
        this.$message({
          message: "请先选择任务类别，再进行任务分配",
          type: "warning",
          showClose: true,
        });
        this.$refs.taskTableRef.toggleRowSelection(row, false);
      }
      if (row.templateValue === "") {
        this.$message({
          message: "请先选择任务模板，再进行任务分配",
          type: "warning",
          showClose: true,
          offset: 50,
        });
        this.$refs.taskTableRef.toggleRowSelection(row, false);
      }
    },
    handleToggleAllSelection(selection) {
      console.log("selection:", selection);
      for (const row of selection) {
        if (row.taskValue === "" || row.templateValue === "") {
          this.$message({
            message: "请先选择任务类别和任务模板，再进行任务分配",
            type: "warning",
            showClose: true,
          });
          this.$refs.taskTableRef.clearSelection();
          return;
        }
      }
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
      this.getTexts();
    },
    getTexts() {
      this.$http
        .post(
          "/taskDistribution/pageText/" +
            this.filterData.pagination.currentIndex +
            "/" +
            this.filterData.pagination.pageSize,
          {
            textTitle: this.filterData.keyWord,
          }
        )
        .then((response) => {
          if (response.data.code === 200) {
            this.tableData = response.data.data.textList.map((v) => ({
              ...v,
              templateValue: "",
              taskValue: "",
            }));
            this.filterData.pagination.total = response.data.data.total;
            this.filterData.pagination.currentIndex =
              response.data.data.current;
          }
        })
        .catch((error) => {
          console.log(error);
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
    getTaskCategories() {
      this.$http.get("/taskDistribution/taskList").then((response) => {
        if (response.data.code === 200) {
          this.taskCategories = response.data.data;
          console.log("任务类型:", this.taskCategories);
        }
      });
    },
    getTemplateCategories() {
      this.$http.get("/taskDistribution/templateList").then((response) => {
        if (response.data.code === 200) {
          this.templateIds = response.data.data;
          console.log("模板类型:", this.templateIds);
        }
      });
    },
  },
  created() {
    this.getTexts();
    this.getLabelPersons();
    this.getTemplateCategories();
    this.getTaskCategories();
  },
};
</script>

<style scoped>
@import "~prismjs/themes/prism-dark.css";

.container {
  max-height: calc(100vh - 64px);
  overflow: hidden;
}
.btn-assign {
  position: absolute;
  right: 30px;
}
.el-input--mini .el-input__inner {
  height: 40px !important;
  line-height: 40px !important;
}
.el-table__header {
  table-layout: auto !important;
}
</style>
