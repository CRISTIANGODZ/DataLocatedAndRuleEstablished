<template>
  <v-container fluid>
    <v-row align="center">
      <el-select
        v-model="templateValue"
        filterable
        clearable
        @change="templateSelectedClick"
        placeholder="请选择模版类型"
        @focus="templateFocus"
      >
        <el-option
          v-for="item in templateIds"
          :key="item.id"
          :label="item.title"
          :value="item.id"
        >
        </el-option>
      </el-select>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  data: function () {
    return {
      templateCategories: this.templateIds,
      // templateIds: [
      //     {
      //       value: 1,
      //       label: '模版1'
      //     },
      //     {
      //       value: 2,
      //       label: '模版2'
      //     },
      //     {
      //       value: 3,
      //       label: '模版3'
      //     },
      //   ],
      templateValue: null,
    };
  },
  props: ["data", "templateIds"],
  methods: {
    templateSelectedClick() {
      // console.log('templateSelector 子组件触发')
      this.$emit("templateSelected", {
        templateValue: this.templateValue,
        currentName: this.data.name,
        currentDate: this.data.date,
        textId: this.data.id,
      });
    },
    templateFocus() {
      this.templateCategories = this.templateIds;
    },
  },
});
</script>

<style></style>
