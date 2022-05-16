<template>
  <el-dialog title="角色详细权限信息" :visible.sync="visible" width="60%">
    <el-alert
      :title="'确定要删除角色' + roleInfo.title + '吗 ？'"
      type="warning"
      v-if="isDelete"
      show-icon
      :closable="false"
      effect="dark"
    >
    </el-alert>
    <el-row type="flex" justify="left">
      <el-form
        :label-position="labelPosition"
        label-width="100px"
        :model="formLabelAlign"
        :disabled="isNotCheck"
      >
        <el-form-item label="角色英文名称">
          <el-input
            v-model="roleInfo.name"
            placeholder="请输入角色名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="角色名称">
          <el-input
            v-model="roleInfo.title"
            placeholder="请输入角色名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="角色详情">
          <el-input
            v-model="roleInfo.description"
            type="textarea"
            placeholder="请输入角色详情"
          ></el-input>
        </el-form-item>
      </el-form>
    </el-row>
    <el-transfer
      v-model="roleInfo.permissions"
      :data="all_permissions"
      :titles="['未拥有权限', '已拥有权限']"
      :button-texts="['到左边', '到右边']"
    ></el-transfer>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeModal">取 消</el-button>
      <el-button v-if="!isDelete" type="primary" @click="closeModal"
        >保 存</el-button
      >
      <el-button v-else type="danger" @click="confirmDelete"
        >确 定 删 除</el-button
      >
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { PropType } from "vue";
import { Permission, RoleInfo, RoleOperation } from "./PermissionTypes";

export default {
  props: {
    allPermissions: {
      type: Array as PropType<Permission[]>,
      default: () => [],
      required: true,
    },
    roleInfo: {
      type: Object as PropType<RoleInfo>,
      default: () => {},
    },
    visible: {
      type: Boolean,
      default: false,
    },
    state: {
      type: Number,
      default: RoleOperation.CHECK,
    },
    closeModal: {
      type: Function,
      default: () => {},
    },
    confirmDelete: {
      type: Function,
      default: () => {},
    },
  },
  data() {
    return {
      centerDialogVisible: this.visible,
      formLabelAlign: {
        name: "",
        region: "",
        type: "",
      },
      labelPosition: "left",
      // all_permissions: [],
      // selected_permissions: [],
    };
  },
  updated() {
    console.log("this.permissions:", this.allPermissions);
    console.log("this.roleInfo:", this.roleInfo);
    // this.all_permissions = this.allPermissions.map((p) => ({
    //   key: p.id,
    //   label: p.title,
    // }));
    // this.selected_permissions = this.roleInfo.permissions || [];
  },
  computed: {
    all_permissions: {
      get: function () {
        return (
          this.allPermissions.map((p) => ({
            key: p.id,
            label: p.title,
            disabled:
              this.state === RoleOperation.CHECK ||
              this.state === RoleOperation.DELETE,
          })) || []
        );
      },
      set: function (newValue) {
        console.log("newValue:", newValue);
      },
    },
    isNotCheck: {
      get: function () {
        return (
          this.state === RoleOperation.CHECK ||
          this.state === RoleOperation.DELETE
        );
      },
    },
    isDelete: {
      get: function () {
        return this.state === RoleOperation.DELETE;
      },
    },
    // selected_permissions: {
    //   get: function () {
    //     return this.roleInfo.permissions || [];
    //   },
    //   set: function (newValue) {
    //     console.log("newValue:", newValue);
    //     // this.selected_permissions = newValue;
    //   },
    // },
  },
};
</script>

<style scoped>
.el-alert {
  margin-bottom: 20px;
  line-height: 1.5;
}
span.el-alert__title {
  font-size: 20px;
}
</style>
