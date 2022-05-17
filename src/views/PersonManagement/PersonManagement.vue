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
        :data="jsonData.data.users"
        border
        highlight-current-row
        style="width: 100%"
        @row-click="handleRowClick"
      >
        <el-table-column prop="id" label="编号" width="180"> </el-table-column>
        <el-table-column prop="ucount" label="ucount" width="180">
        </el-table-column>
        <el-table-column
          prop="username"
          label="人员名称"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="phone"
          label="电话"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="address"
          label="地址"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="userRole"
          label="角色"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="description"
          label="简介"
          width="180"
        ></el-table-column>
        <el-table-column prop="avatar" label="头像" width="180">
          <template slot-scope="scope">
            <el-avatar shape="square" :size="large" :src="scope.row.avatar"></el-avatar>
          </template>
        </el-table-column>

        <el-table-column  label="操作" width="250">
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
import { PersonJsonDataType } from "./PersonType";
import { getPersonList } from "./PersonApi";
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
      jsonData: null as PersonJsonDataType,
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
    handleRowClick() {},
    handleCheckModalOperationVisible() {},
    handleEditModalOperationVisible() {},
    handleDeleteModalOperationVisible() {},
    async getData() {
      const data = (await getPersonList(this.filterData)).data;
      console.log("data:", data);
      console.log("dataddddddddddddddddddd");
      if (data.code === 200) {
        console.log("ddsdlakjflk");
        // this.jsonData.data.users = data.data.users;
        this.jsonData = data;
        console.log("this.jsonData.users:", this.jsonData);
      }
    },
  },
  mounted() {
    // this.onFilterDataChange();
    // const data = getPersonList(this.filterData);
    // console.log(data);
    this.getData();
  },
});
</script>

<style scoped></style>
