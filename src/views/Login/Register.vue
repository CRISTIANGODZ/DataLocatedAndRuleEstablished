<template>
  <div class="login clearfix">
    <div class="login-wrap">
      <el-row type="flex" justify="center">
        <el-form
          ref="loginForm"
          :model="user"
          status-icon
          :rules="rules"
          label-width="80px"
        >
          <el-form-item type="radio" label="用户角色" prop="role">
            <el-radio v-model="user.role" label="0">超级管理员</el-radio>
            <el-radio v-model="user.role" label="1">普通管理员</el-radio>
            <el-radio v-model="user.role" label="2">普通用户</el-radio>
          </el-form-item>
          <el-form-item prop="username" label="用户名">
            <el-input
              v-model="user.ucount"
              placeholder="请输入登录的用户名"
            ></el-input>
          </el-form-item>
          <el-form-item prop="ucount" label="用户昵称">
            <el-input
              v-model="user.username"
              placeholder="请输入用户昵称"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password" label="设置密码">
            <el-input
              v-model="user.password"
              show-password
              placeholder="请输入密码"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              icon
              @click="doRegister()"
              style="min-width: 150px"
              >注册账号</el-button
            >
          </el-form-item>
        </el-form>
      </el-row>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
export default Vue.extend({
  data() {
    return {
      d_register: true,
      user: {
        username: "",
        ucount: "",
        password: "",
        role: "0",
      },
      rules: {
        password: [
          {
            required: true,
            message: "密码至少八个字符，至少一个字母和一个数字",
            trigger: "blur",
          },
        ],
        username: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" },
        ],
        ucount: [
          {
            required: true,
            message: "登录用户名不能为空且只能为字母",
            trigger: "blur",
          },
        ],
        role: [
          {
            required: true,
            message: "用户角色不能为空",
            trigger: "blur",
          },
        ],
      },
    };
  },
  props: {
    hanldeRegisterClick: {
      type: Function,
    },
  },
  methods: {
    doRegister() {
      if (this.user.ucount.length < 1) {
        this.$message.error("用户名不能为空");
        return;
      }
      if (this.user.username.length < 1) {
        this.$message.error("用户昵称不能为空");
        return;
      }
      if (this.user.password.length < 1) {
        this.$message.error("密码不能为空");
        return;
      }
      const reg = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8, }$/;
      if (!reg.test(this.user.password)) {
        this.$message.error("密码至少八个字符，至少一个字母和一个数字");
        return;
      }
      this.$http
        .post("/user/register/", {
          ucount: this.user.ucount,
          password: this.user.password,
          role: this.user.role,
          username: this.user.username,
        })
        .then(({ data }) => {
          console.log("register data:", data);
          if (data.code === 200) {
            this.$message.success("注册成功");
            localStorage.setItem("ucount", this.user.ucount);
            localStorage.setItem("password", this.user.password);
            localStorage.setItem("username", this.user.username);
            console.log(localStorage);
            this.hanldeRegisterClick();
            console.log("ddd");
          }
        });
    },
  },
});
</script>
>

<style scoped>
@import "~prismjs/themes/prism-dark.css";

.container {
  max-height: calc(100vh - 64px);
  overflow: hidden;
}
.login-wrap {
  min-width: 400px;
}
</style>
