<template>
  <v-container fluid>
    <v-row align="center">
      <el-select v-model="templateValue" filterable @change="templateSelectedClick" placeholder="请选择模版类型">
          <el-option
            v-for="item in templateIds"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  data: () => ({
    templateIds: [
        {
          value: 1,
          label: '模版1'
        },
        {
          value: 2,
          label: '模版2'
        },
        {
          value: 3,
          label: '模版3'
        },
      ],
      templateValue: null
  }),
  props: ['data'],
  methods: {
    templateSelectedClick() {
      // console.log('templateSelector 子组件触发')  
      this.$emit('templateSelected', {
        templateValue: this.templateValue,
        currentName: this.data.name,
        currentDate: this.data.date
      })
    }
  },
});
</script>

<style></style>
