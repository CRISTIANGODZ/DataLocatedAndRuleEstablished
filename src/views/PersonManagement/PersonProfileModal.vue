<template>
  <el-dialog
    :title="isAdd ? '添加人员' : '人员详细信息'"
    :visible.sync="visible"
    width="60%"
  >
    <el-alert
      :title="'确定要删除吗 ？'"
      type="warning"
      v-if="isDelete"
      show-icon
      :closable="false"
      effect="dark"
    >
    </el-alert>
    <el-row>
      <el-form
        :label-position="labelPosition"
        label-width="100px"
        :model="formLabelAlign"
      >
        <el-form-item label="用户 ucount" v-if="!isAdd">
          <el-input disabled v-model="userData.ucount"></el-input>
        </el-form-item>
        <el-form-item label="用户昵称">
          <el-input
            v-model="userData.username"
            :disabled="!isCheckOrDelete"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            :disabled="!isCheckOrDelete"
            list-type="picture"
            action="https://jsonplaceholder.typicode.com/posts/"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img
              v-if="userData.avatar"
              :src="userData.avatar"
              class="avatar"
              style="width: 100px"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="用户角色">
          <!-- <el-input
            v-model="userData.userRole"
            :disabled="true"
            placeholder="请输入角色"
          ></el-input> -->
          <el-select
            v-model="userData.userRoleId"
            clearable
            :disabled="!isCheckOrDelete"
            placeholder="请选择"
          >
            <el-option
              v-for="role in allRoles"
              :key="role.id"
              :label="role.title"
              :value="role.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input
            v-model="userData.email"
            type="email"
            placeholder="请输入用户名"
            :disabled="!isCheckOrDelete"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="userData.password"
            :disabled="!isCheckOrDelete"
            type="password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号码">
          <el-input
            v-model="userData.phone"
            :disabled="!isCheckOrDelete"
            type="tel"
            placeholder="请输入手机号码"
          ></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input
            v-model="userData.address"
            :disabled="!isCheckOrDelete"
            type="address"
            placeholder="请输入地址"
          ></el-input>
        </el-form-item>
        <el-form-item label="个人简介">
          <el-input
            v-model="userData.description"
            :disabled="!isCheckOrDelete"
            type="textarea"
            placeholder="请输入个人简介"
          ></el-input>
        </el-form-item>
      </el-form>
    </el-row>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeModal">取 消</el-button>
      <el-button v-if="isCheckOrDelete" type="primary" @click="closeModal"
        >保 存</el-button
      >
      <el-button v-else-if="isDelete" type="danger" @click="confirmDelete"
        >确 定 删 除</el-button
      >
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { PropType } from "vue";
import { PersonType, PersonOperation } from "./PersonType";
import { RoleInfo } from "../PermissionManagement/PermissionTypes";

export default {
  props: {
    allRoles: {
      type: Array as PropType<RoleInfo[]>,
      default: () => [],
      required: true,
    },
    userData: {
      type: Object as PropType<PersonType>,
      default: () => {},
    },
    visible: {
      type: Boolean,
      default: false,
    },
    state: {
      type: Number,
      default: PersonOperation.CHECK,
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
    };
  },
  updated() {},
  computed: {
    isCheckOrDelete: {
      get: function () {
        return (
          this.state !== PersonOperation.CHECK &&
          this.state !== PersonOperation.DELETE
        );
      },
    },
    isDelete: {
      get: function () {
        return this.state === PersonOperation.DELETE;
      },
    },
    isAdd: {
      get: function () {
        return this.state === PersonOperation.ADD;
      },
    },
    isEdit: {
      get: function () {
        return this.state === PersonOperation.EDIT;
      },
    },
    handleAvatarSuccess() {},
    beforeAvatarUpload() {},
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
