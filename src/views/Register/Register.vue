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
          <h3>注册</h3>
          <hr />
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
            <el-button type="primary" icon @click="doRegister()"
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
            message: "密码长度不能小于8位且必须包含字母、数字、符号中的两种",
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
  methods: {
    doRegister() {
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
            console.log(localStorage);
            this.$router.push("/login");
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
.login {
  width: 100%;
  height: 740px;
  /* background: rgb(131, 58, 180);
  background: linear-gradient(
    90deg,
    rgba(131, 58, 180, 1) 0%,
    rgba(156, 29, 253, 1) 50%,
    rgba(252, 176, 69, 1) 100%
  ); */
  /* background: url("../assets/images/bg1.png") no-repeat; */
  /* background-size: cover; */
  overflow: hidden;
}
.login-wrap {
  /* background: url("../assets/images/login_bg.png") no-repeat; */
  /* background-size: cover; */
  width: 500px;
  height: 400px;
  margin: 215px auto;
  overflow: hidden;
  padding-top: 10px;
  line-height: 20px;
}

h3 {
  color: #0babeab8;
  font-size: 24px;
}
hr {
  background-color: #444;
  margin: 20px auto;
}

.el-button {
  width: 80%;
  margin-left: -50px;
}
</style>
