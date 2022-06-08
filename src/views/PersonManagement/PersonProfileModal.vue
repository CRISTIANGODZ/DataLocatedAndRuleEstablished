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
        :rules="rules"
      >
        <el-form-item label="用户 ucount" v-if="!isAdd">
          <el-input disabled v-model="userData.ucount"></el-input>
        </el-form-item>
        <el-form-item label="用户昵称">
          <el-input
            v-model="userData.username"
            :disabled="!isCheckOrDelete || isEdit"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item label="头像" v-if="!isEdit">
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
          <el-select
            v-model="userData.userRoleId"
            filterable
            reserve-keyword
            :disabled="!isCheckOrDelete"
            placeholder="选择用户角色"
          >
            <el-option
              v-for="role in roles"
              :key="role.id"
              :label="role.title"
              :value="role.id"
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
            </el-pagination
          ></el-select>
        </el-form-item>

        <el-form-item label="邮箱" v-if="!isEdit">
          <el-input
            v-model="userData.email"
            type="email"
            placeholder="请输入邮箱"
            :disabled="!isCheckOrDelete"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码" v-if="!isEdit">
          <el-input
            v-model="userData.password"
            :disabled="!isCheckOrDelete"
            type="password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号码" v-if="!isEdit" prop="phone">
          <el-input
            v-model="userData.phone"
            :disabled="!isCheckOrDelete"
            type="tel"
            placeholder="请输入手机号码"

          ></el-input>
        </el-form-item>
        <el-form-item label="地址" v-if="!isEdit">
          <el-input
            v-model="userData.address"
            :disabled="!isCheckOrDelete"
            type="address"
            placeholder="请输入地址"
          ></el-input>
        </el-form-item>
        <el-form-item label="个人简介" v-if="!isEdit">
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
      <el-button v-if="isCheckOrDelete" type="primary" @click="handleSaveAction"
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
import { getRoleList } from "../PermissionManagement/PermissionApi";
import user from "@/api/user/user";

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
    updateUserInfo: {
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
      roleFilterData: {
        roleName: 0,
        currentIndex: 1,
        pageSize: 100,
        total: 3,
      },
      centerDialogVisible: this.visible,
      formLabelAlign: {
        name: "",
        region: "",
        type: "",
      },
      labelPosition: "left",
      roles: [] as Array<RoleInfo>,
      rules: {
        phone: [
          {
            required: true,
            pattern: /^1[34578]\d{9}$/, //可以写正则表达式呦呦呦
            message: "目前只支持中国大陆的手机号码",
            trigger: "blur",
          },
        ],
      },
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
  },
  methods: {
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
    handleAvatarSuccess() {},
    beforeAvatarUpload() {},
    onRoleFilterDataChange() {
      this.getRemoteRoles();
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
    onUpdateUserInfo() {
      user.updateUserInfo(this.userData).then((res) => {
        if (res.data.code === 200) {
          this.$success("修改用户信息成功");
          this.closeModal();
          this.updateUserInfo();
        }
      });
    },
    onAddUserInfo() {
      user.registerUserInfo(this.userData).then((res) => {
        if (res.data.code === 200) {
          this.$success("添加用户成功");
          this.closeModal();
          this.updateUserInfo();
        }
      });
    },
    handleSaveAction() {
      if (this.isEdit) {
        this.onUpdateUserInfo();
      } else if (this.isAdd) {
        this.onAddUserInfo();
      }
    },
  },
  mounted() {
    this.getRemoteRoles();
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
