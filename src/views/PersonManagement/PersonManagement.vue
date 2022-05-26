<template>
  <el-container direction="vertical">
    <h2>人员管理</h2>
    <el-row el-row type="flex" justify="between" :gutter="20">
      <el-col>
        <el-input
          v-model="filterData.userName"
          placeholder="请输入人员名称"
          @change="onFilterDataChange"
        ></el-input>
      </el-col>
      <!-- <el-col>
        <el-input
          v-model="filterData.userRole"
          placeholder="请输入人员角色"
        ></el-input>
      </el-col> -->

      <el-col>
        <el-select
          v-model="filterData.userRole"
          filterable
          reserve-keyword
          clearable
          placeholder="选择用户角色"
          @change="onFilterDataChange"
        >
          <el-option
            v-for="item in roles"
            :key="item.id"
            :label="item.title"
            :value="item.id"
          >
          </el-option>
          <el-pagination
            background
            @size-change="handleRoleSizeChange"
            @current-change="handleRoleCurrentChange"
            :current-page="roleFilterData.currentIndex"
            :page-sizes="[5, 10, 15, 20]"
            :page-size="roleFilterData.pageSize"
            layout="prev, pager, next"
            :total="roleFilterData.total"
          >
          </el-pagination>
        </el-select>
      </el-col>

      <el-col>
        <el-button type="primary" @click="handleAddModalOperationVisible"
          >添加人员</el-button
        >
      </el-col>
    </el-row>
    <el-row>
      <el-table
        :data="jsonData.data.userList"
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
        <el-table-column prop="password" label="密码" width="180">
          <template slot-scope="scope">
            <!-- <el-input
            v-model="scope.row.password"
            disabled
            type="password"
          ></el-input> -->
            <el-tag>{{ scope.row.password.replace(/[\s\S]/g, "*") }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="description"
          label="简介"
          width="180"
        ></el-table-column>
        <el-table-column prop="avatar" label="头像" width="180">
          <template slot-scope="scope">
            <el-avatar
              shape="square"
              size="large"
              :src="scope.row.avatar"
            ></el-avatar>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="250">
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
    <person-profile-modal-vue
      :closeModal="handleCloseModalOperation"
      :visible="operation.modalVisible"
      :userData="selectPerson"
      :confirmDelete="handleConfirmDelete"
      :allRoles="roles"
      :state="operation.operationState"
    ></person-profile-modal-vue>
  </el-container>
</template>

<script lang="ts">
import Vue from "vue";
import PersonProfileModalVue from "./PersonProfileModal.vue";
import {
  PersonJsonDataType,
  PersonOperation,
  PersonType,
  UserInfoJsonDataType,
} from "./PersonType";
import { getPersonList, getUserInfoCondition } from "./PersonApi";
import { getRoleList } from "../PermissionManagement/PermissionApi";
import { RoleInfo } from "../PermissionManagement/PermissionTypes";

export default Vue.extend({
  components: {
    PersonProfileModalVue,
  },
  data() {
    return {
      roleSelectLoading: false,
      filterData: {
        userName: "",
        userRole: null,
        pagination: {
          currentIndex: 1,
          pageSize: 10,
          total: 3,
        },
      },
      roleFilterData: {
        roleName: null,
        currentIndex: 1,
        pageSize: 5,
        total: 3,
      },
      jsonData: {
        code: "",
        message: "",
        ok: false,
        data: {
          userList: [] as RoleInfo[],
          current: 1,
          size: 10,
          total: 3,
        },
      } as unknown as UserInfoJsonDataType,
      operation: {
        modalVisible: false,
        operationState: PersonOperation.CHECK,
      },
      roles: [] as RoleInfo[],
      selectPerson: {} as PersonType,
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
    handleRoleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.roleFilterData.pageSize = val;
      this.onRoleFilterDataChange();
    },
    handleRoleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.roleFilterData.currentIndex = val;
      this.onRoleFilterDataChange();
    },
    handleModalOperationVisible() {
      this.operation.modalVisible = true;
    },
    handleRowClick(row, column, event) {
      this.selectPerson = row;
      console.log(row);
      console.log(column);
    },
    handleEditModalOperationVisible() {
      this.handleModalOperationVisible();
      this.operation.operationState = PersonOperation.EDIT;
    },
    handleCheckModalOperationVisible() {
      this.handleModalOperationVisible();
      this.operation.operationState = PersonOperation.CHECK;
    },
    handleAddModalOperationVisible() {
      this.operation.operationState = PersonOperation.ADD;
      this.handleModalOperationVisible();
      this.selectPerson = {} as PersonType;
    },
    handleDeleteModalOperationVisible() {
      this.handleModalOperationVisible();
      this.operation.operationState = PersonOperation.DELETE;
    },
    handleConfirmDelete() {
      this.$success("删除成功");
      this.handleCloseModalOperation();
    },
    handleCloseModalOperation() {
      this.operation.modalVisible = false;
      if (this.operation.operationState !== PersonOperation.EDIT) {
        this.getData();
      }
    },
    onFilterDataChange() {
      this.getData();
    },
    onRoleFilterDataChange() {
      this.getRemoteRoles();
    },
    async getData() {
      const user_data = (await getUserInfoCondition(this.filterData)).data;
      if (user_data.code === 200) {
        this.jsonData = user_data;
        this.filterData.pagination.total = user_data.data.total;
        this.filterData.pagination.currentIndex = user_data.data.current;
      }
      console.log("user_data:", user_data);

      // const data = (await getPersonList(this.filterData)).data;
      // console.log("data:", data);
      // console.log("dataddddddddddddddddddd");
      // if (data.code === 200) {
      //   console.log("ddsdlakjflk");
      //   // this.jsonData.data.users = data.data.users;
      //   this.jsonData = data;
      //   console.log("this.jsonData.users:", this.jsonData);
      //   this.selectPerson = data.data.users[0];
      // }
    },
    async getRemoteRoles() {
      const role_data = (await getRoleList(this.roleFilterData)).data;
      if (role_data.code === 200) {
        this.roles = role_data.data.roles;
        console.log("this.roles:", this.roles);
        this.roleFilterData.total = role_data.data.total;
        this.roleFilterData.currentIndex = role_data.data.currentPage;
      }
    },
  },
  mounted() {
    // this.onFilterDataChange();
    // const data = getPersonList(this.filterData);
    // console.log(data);
    this.getData();
    this.getRemoteRoles();
  },
});
</script>

<style scoped>
.el-row {
  margin-top: 10px;
  margin-bottom: 10px;
}
.el-table {
  margin-top: 10px;
  margin-bottom: 10px;
}
</style>
