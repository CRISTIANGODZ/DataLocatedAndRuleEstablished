<template>
  <v-container fluid>
    <v-row align="center">
      <el-form>
        <el-form-item :required="true">
          <el-select
            v-model="taskValue"
            filterable
            clearable
            loading-text="获取任务类型中..."
            placeholder="请选择任务类型"
            @change="taskSelectedClick"
            style="margin-right: 15px"
          >
            <el-option
              v-for="item in taskCategories"
              :key="item.id"
              :label="item.title"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
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
        label: "任务1",
      },
      {
        value: 2,
        label: "任务2",
      },
      {
        value: 3,
        label: "任务3",
      },
    ],
    taskValue: null,
  }),
  props: ["data","taskCategories"],
  methods: {
    taskSelectedClick() {
      // console.log('taskSelector 子组件触发')
      this.$emit("taskSelected", {
        taskValue: this.taskValue,
        currentName: this.data.name,
        currentDate: this.data.date,
        textId: this.data.id
      });
    },
  },
  mounted(){
    console.log("this.data:", this.data);
    // console.log("this.taskCategorys:", this.taskCategorys);
  }
});
</script>

<style></style>
