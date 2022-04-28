<template>
  <v-container fluid>
    <v-row align="center">
      <el-select v-model="taskValue" filterable placeholder="请选择任务类型" @change="taskSelectedClick" style="margin-right: 15px">
          <el-option
            v-for="item in taskCategorys"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        <!-- <span>{{name}}</span> -->
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  data: () => ({
    taskCategorys: [
        {
          value: 1,
          label: '任务1'
        },
        {
          value: 2,
          label: '任务2'
        },
        {
          value: 3,
          label: '任务3'
        },
      ],
      taskValue: null,
  }),
  props: ['data'],
  methods: {
    taskSelectedClick() {
      // console.log('taskSelector 子组件触发')
      this.$emit('taskSelected', {
        taskValue: this.taskValue,
        currentName: this.data.name,
        currentDate: this.data.date
      })
    }
  },
});
</script>

<style></style>
