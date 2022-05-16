<template>
  <el-container direction="vertical">
    <h2>人员管理</h2>
    <el-row el-row type="flex" justify="between" :gutter="20">
      <el-col>
        <el-input
          v-model="filterData.userName"
          placeholder="请输入人员名称"
        ></el-input>
      </el-col>
      <el-col>
        <el-input
          v-model="filterData.userRole"
          placeholder="请输入人员角色"
        ></el-input>
      </el-col>

      <el-col>
        <el-button type="primary" @click="() => {}">添加角色</el-button>
      </el-col>
    </el-row>
    <el-row>
      <el-table
        :data="jsonData.roles"
        border
        highlight-current-row
        style="width: 100%"
        @row-click="handleRowClick"
      >
        <el-table-column prop="id" label="编号" width="180"> </el-table-column>
        <el-table-column
          prop="name"
          label="角色名称"
          width="180"
        ></el-table-column>
        <el-table-column prop="description" label="角色描述" width="180">
        </el-table-column>

        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleCheckModalOperationVisible"
              >查看</el-button
            >
            <el-button
              size="mini"
              @click="handleEditModalOperationVisible"
              type="primary"
              >编辑</el-button
            >
            <el-button
              size="mini"
              type="danger"
              @click="handleDeleteModalOperationVisible"
              ref="deleteRoleReference"
              >删除</el-button
            >
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
  </el-container>
</template>

<script lang="ts">
import Vue from "vue";
export default Vue.extend({
  data() {
    return {
      filterData: {
        userName: "",
        userRole: "",
        pagination: {
          currentIndex: 1,
          pageSize: 10,
          total: 3,
        },
      },
      jsonData: {
        users: [],
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
  },
});
</script>

<style scoped></style>
