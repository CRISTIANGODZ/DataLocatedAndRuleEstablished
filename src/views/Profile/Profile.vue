<template>
  <el-container>
    <el-row>
      <el-form
        :label-position="labelPosition"
        label-width="100px"
        :model="formLabelAlign"
      >
        <el-form-item label="用户 ucount">
          <el-input disabled v-model="userData.ucount"></el-input>
        </el-form-item>
        <el-form-item label="用户昵称">
          <el-input
            v-model="userData.username"
            :disabled="!isEdit"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            :disabled="!isEdit"
            list-type="picture"
            action="https://jsonplaceholder.typicode.com/posts/"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="userData.avatar" :src="userData.avatar" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="用户角色">
          <el-input
            v-model="userData.userRole"
            :disabled="true"
            placeholder="请输入角色"
          ></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input
            v-model="userData.email"
            type="email"
            placeholder="请输入用户名"
            :disabled="!isEdit"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="userData.password"
            :disabled="!isEdit"
            type="password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号码">
          <el-input
            v-model="userData.phone"
            :disabled="!isEdit"
            type="tel"
            placeholder="请输入手机号码"
          ></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input
            v-model="userData.address"
            :disabled="!isEdit"
            type="address"
            placeholder="请输入地址"
          ></el-input>
        </el-form-item>
        <el-form-item label="个人简介">
          <el-input
            v-model="userData.description"
            :disabled="!isEdit"
            type="textarea"
            placeholder="请输入个人简介"
          ></el-input>
        </el-form-item>
        <el-button
          :type="isEdit ? 'primary' : 'success'"
          @click="onUserDataModify"
          style="min-width: 150px"
          >{{ !isEdit ? "修改" : "保存" }}</el-button
        >
      </el-form>
    </el-row>
  </el-container>
</template>

<script lang="ts">
import Vue from "vue";
export default Vue.extend({
  data() {
    return {
      labelPosition: "left",
      isEdit: false,
      formLabelAlign: {
        name: "",
        region: "",
        type: "",
      },
      userData: {
        username: "周翔辉",
        email: "fankaljead@gmail.com",
        password: "123456",
        ucount: "fankaljead",
        phone: "18523771521",
        address: "重庆市南岸区重庆邮电大学",
        userRole: "超级管理员",
        description: "",
        avatar:
          "https://avatars.githubusercontent.com/u/30434925?s=400&u=0a2acdf9f2ea4954f44343079c9804fcd11ec933&v=4",
      },
    };
  },
  methods: {
    onUserDataModify() {
      this.isEdit = !this.isEdit;
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      // const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      // if (!isJPG) {
      //   this.$message.error("上传头像图片只能是 JPG 格式!");
      // }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      // return isJPG && isLt2M;
      return isLt2M;
    },
  },
});
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
  border-radius: 50%;
}
.el-row {
  width: 100%;
}
</style>
