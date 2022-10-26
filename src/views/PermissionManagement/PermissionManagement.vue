<template>
  <el-container direction="vertical">
    <h2>权限管理</h2>
    <!-- <el-header></el-header> -->
    <el-row el-row type="flex" justify="between" :gutter="20">
      <el-col>
        <el-input
          v-model="filterData.roleName"
          placeholder="请输入角色名称"
        ></el-input>
      </el-col>

      <el-col>
        <el-button type="primary" @click="handleAddModalOperationVisible"
          >添加角色</el-button
        >
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
    <role-permission-modal-vue
      :allPermissions="jsonData.permissions"
      :closeModal="handleCloseModalOperation"
      :handleOperation="handleOperation"
      :updateUserInfo="updateUserInfo"
      :visible="operation.modalVisible"
      :roleInfo="selectRole"
      :confirmDelete="handleConfirmDelete"
      :state="operation.operationState"
    ></role-permission-modal-vue>
  </el-container>
</template>

<script lang="ts">
import Vue from "vue";
import RolePermissionModalVue from "./RolePermissionModal.vue";
import { RoleInfo, Permission, RoleOperation } from "./PermissionTypes";
import {
  getRoleList,
  getPermissionList,
  updateRole,
  addRole,
} from "./PermissionApi";
export default Vue.extend({
  data() {
    return {
      filterData: {
        roleName: "",
        pagination: {
          currentIndex: 1,
          pageSize: 10,
          total: 3,
        },
      },
      jsonData: {
        permissions: [] as Permission[],
        roles: [] as RoleInfo[],
      },
      operation: {
        modalVisible: false,
        operationState: RoleOperation.CHECK,
      },
      selectRole: {} as RoleInfo,
    };
  },
  components: {
    RolePermissionModalVue,
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
    onFilterDataChange() {
      this.getRoles();
    },
    handleLabelStateClick(row) {
      console.log("handleLabelStateClick");
      console.log(row);
    },
    customColorMethod(percentage) {
      if (percentage < 30) {
        return "#909399";
      } else if (percentage < 70) {
        return "#e6a23c";
      } else {
        return "#67c23a";
      }
    },
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },
    handleLabelStateMouseover(row) {
      console.log("handleLabelStateMouseover");
      console.log(row);
    },
    async getRoles() {
      const roles_json_data = (await getRoleList(this.filterData.pagination))
        .data;
      if (roles_json_data.code === 200) {
        this.jsonData.roles = roles_json_data.data.roles;
        this.filterData.pagination.total = roles_json_data.data.total;
      }
    },
    handleModalOperationVisible() {
      console.log("dddd");
      this.operation.modalVisible = true;
    },
    handleRowClick(row, column, event) {
      this.selectRole = row;
      console.log(row);
      console.log(column);
    },
    handleEditModalOperationVisible() {
      this.handleModalOperationVisible();
      this.operation.operationState = RoleOperation.EDIT;
    },
    handleCheckModalOperationVisible() {
      this.handleModalOperationVisible();
      this.operation.operationState = RoleOperation.CHECK;
    },
    handleAddModalOperationVisible() {
      this.operation.operationState = RoleOperation.ADD;
      this.handleModalOperationVisible();
      this.selectRole = {
        id: 0,
        name: "",
        description: "",
        permissions: [],
      };
    },
    handleDeleteModalOperationVisible() {
      this.handleModalOperationVisible();
      this.operation.operationState = RoleOperation.DELETE;
    },
    handleConfirmDelete() {
      this.$success("删除成功");
      this.handleCloseModalOperation();
    },
    handleCloseModalOperation() {
      this.operation.modalVisible = false;
    },
    handleOperation() {
      if (this.operation.operationState === RoleOperation.ADD) {
        this.onAddRole();
      }
      if (this.operation.operationState === RoleOperation.EDIT) {
        this.onUpdateRole();
      }
    },
    async getPermisions() {
      const permissions_json_data = (await getPermissionList()).data;
      if (permissions_json_data.code === 200) {
        this.jsonData.permissions = permissions_json_data.data;
      }
    },
    async onAddRole() {
      const add_role_json_data = (await addRole(this.selectRole)).data;
      if (add_role_json_data.code === 200) {
        this.$success("添加成功");
        this.operation.modalVisible = false;
        this.getRoles();
      }
    },

    async onUpdateRole() {
      console.log("selectRole", this.selectRole);
      const update_role_json_data = (await updateRole(this.selectRole)).data;
      if (update_role_json_data.code === 200) {
        this.$success("更新成功");
        this.operation.modalVisible = false;
        this.getRoles();
      }
    },
    async getData() {
      this.getRoles();
      this.getPermisions();
    },
    updateUserInfo() {
      this.getData();
    },
  },
  mounted() {
    // this.getRoles();
    this.getData();
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
