<template>
  <el-container>
    <el-row>
      <h2 style="margin-bottom: 10px">个人信息</h2>
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
import { mapActions, mapGetters, mapState } from "vuex";
import { PersonType } from "../PersonManagement/PersonType";
import { updateUserInfo } from "./ProfileApi";

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
      userData: {} as PersonType,
    };
  },
  methods: {
    onUserDataModify() {
      if (this.isEdit) {
        // updateUserInfo(this.userData).then((res) => {
        //   if (res.data.code === 200) {
        //     this.isEdit = false;
        //   }
        //   this.$swal({
        //     title: "修改成功",
        //     type: "success",
        //     showConfirmButton: false,
        //     timer: 1500,
        //   });
        // });
        this.updateUserInfo(this.userData).then((_) => {
          this.$success("修改个人信息成功");
        });
      }
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
    ...mapActions("user", ["updateUserInfo"]),
  },
  mounted() {
    this.userData = {...this.userInfo.userVo};
    console.log("this.userData:", this.userData);
  },
  computed: {
    ...mapGetters({
      userInfo: "user/userInfo",
    }),
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
