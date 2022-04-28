<template>
  <div>
    <el-row :gutter="20">
        <el-col :span="6">
          <el-input
          v-model="search"
          size="large"
          placeholder="输入关键字搜索"/>
        </el-col>
        <el-col :span="6">
          <el-select v-model="value" placeholder="请选择标注人">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="6" class="btn-assign">
          <el-button type="primary" round @click="handleAssigin">分配任务</el-button>
        </el-col>
      </el-row>
  <el-table
    :data="tableData.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
    @selection-change="handleSelectionChange"
    style="width: 100%">
    <el-table-column
      type="selection"
      width="55" />
    <el-table-column
      label="上传时间"
      sortable
      prop="date">
    </el-table-column>
    <el-table-column
      label="Title"
      sortable
      prop="name">
    </el-table-column>
    <el-table-column
      align="right">
      <template slot-scope="scope">
        <div style="display: flex">
          <!-- <span>{{scope.row}}</span> -->
          <TaskSelector :data="scope.row" v-on:taskSelected="taskSelectedByValue" />
          <TemplateSelector :data="scope.row" v-on:templateSelected="templateSelectedByValue" />
        </div>
      </template>
    </el-table-column>
  </el-table>
  </div>
</template>

<script lang="ts">
import TaskSelector from './TaskSelector.vue'
import TemplateSelector from './TemplateSelector.vue'

export default {
  components: {TaskSelector, TemplateSelector},
  data() {
    return {
      tableData: [{
        date: '2022-05-02',
        name: '精神病标注',
        taskValue: '',
        templateValue: '',
      }, {
        date: '2022-05-04',
        name: '心脏病标注',
        taskValue: '',
        templateValue: '',
      }, {
        date: '2022-05-01',
        name: '高血压标注',
        taskValue: '',
        templateValue: '',
      }, {
        date: '2022-05-03',
        name: '新冠肺炎标注',
        taskValue: '',
        templateValue: '',
      }],
      search: '',
      options: [{
        value: '0',
        label: '全选'
      }, {
        value: '张三-id',
        label: '张三'
      }, {
        value: '李四-id',
        label: '李四'
      }, {
        value: '王五-id',
        label: '王五'
      }],
      value: '',
      taskValue: [],
      multipleSelection: [],
      sendValues: []
    }
  },
  methods: {
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },
    taskSelectedByValue(taskValues) {
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
        if (item.date === taskValues.currentDate && item.name === taskValues.currentName) {
          this.tableData[index].taskValue = taskValues.taskValue
        }
      })
      // console.log('after', this.tableData);
      // console.log('taskSelectedByValue执行结束');
      
    },
    templateSelectedByValue(templateValues) {
      this.tableData.forEach((item, index) => {
        if (item.date === templateValues.currentDate && item.name === templateValues.currentName) {
          this.tableData[index].templateValue = templateValues.templateValue
        }
      })
    },
    handleAssigin(index, row) {
      console.log('分配任务button点击')
      console.log('所选择的标注人：', this.value)
      console.log('所选择的要分配的任务数组：', this.sendValues)
       this.$alert(`确定将这些任务分配给${this.value}吗？`, '任务分配', {
          confirmButtonText: '确定',
          callback: action => {
            this.$message({
              type: 'info',
              message: `任务分配: ${ action === 'cancel' ? '失败' : '成功' }`
            });
          }
        });
    },
    handleSelectionChange(val) {
      this.sendValues = val
      this.multipleSelection = val;
    },
    toggleSelection(rows) {
        if (rows) {
          rows.forEach(row => {
            this.$refs.multipleTable.toggleRowSelection(row);
          });
        } else {
          this.$refs.multipleTable.clearSelection();
        }
      },
  },
  }
</script>

<style>
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
</style>
