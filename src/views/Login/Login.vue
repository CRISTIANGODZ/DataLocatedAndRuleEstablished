<template>
  <div class="login_container">
    <el-row type="flex" justify="center">
      <el-form
        :label-position="labelPosition"
        label-width="80px"
        :model="formLabelAlign"
      >
        <el-form-item label="用户名称">
          <el-input
            v-model="userData.ucount"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>

        <el-form-item label="密码">
          <el-input
            v-model="userData.password"
            type="password"
            placeholder="请输入密码"
            v-on:keyup.enter="onLogin"
          ></el-input>
        </el-form-item>
        <el-form-item label="记住密码">
          <el-checkbox
            v-model="userData.rememberPassword"
            @change="onRememberPasswordChange"
          ></el-checkbox>
        </el-form-item>
        <!-- <el-button type="primary" @click="onLogin" style="min-width: 150px"
          >忘记密码</el-button
        > -->
        <el-button type="primary" @click="onLogin" style="min-width: 150px"
          >登录</el-button
        >
      </el-form>
    </el-row>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
export default Vue.extend({
  data() {
    return {
      labelPosition: "left",
      userData: {
        username: "",
        password: "",
        ucount: "",
        rememberPassword: localStorage.getItem("rememberPassword") == "true",
      },
      formLabelAlign: {
        name: "",
        region: "",
        type: "",
      },
    };
  },
  methods: {
    onRememberPasswordChange() {
      // this.userData.rememberPassword = !this.userData.rememberPassword;
      localStorage.setItem("rememberPassword", this.userData.rememberPassword);
    },
    onLogin() {
      this.$http
        .post("/user/login/", {
          ucount: this.userData.ucount,
          password: this.userData.password,
        })
        .then(({ data }) => {
          if (data.code === 200) {
            console.log("login data:", data);
            var token = data.data.token;
            // localStorage.setItem("token", this.username + "_" + token);
            localStorage.setItem("token", token);
            localStorage.setItem("role", data.data.user.role);
            localStorage.setItem("username", data.data.user.username);
            localStorage.setItem("ucount", data.data.user.ucount);
            localStorage.setItem("password", this.userData.password);
            localStorage.setItem("headIconUrl", data.data.user.headIconUrl);
            // localStorage.setItem("role", "admin");
            this.username = data.data.user.username;
            this.ucount = data.data.user.ucount;
            this.$router.push("/main").catch((_) => {
              console.log("errrereoreor");
            });
            // console.log("after push /main");
            // location.reload();
          } else {
            this.$message({
              message: "登录失败，密码错误或账号不存在 !!!",
              type: "warning",
            });
          }
        });
    },
  },
  mounted() {
    // this.userData.rememberPassword = localStorage.getItem("rememberPassword");
    if (localStorage.getItem("rememberPassword") == "true") {
      let ucount = localStorage.getItem("ucount");
      let password = localStorage.getItem("password");
      if (!!ucount || !!password) {
        console.log(this.userData.rememberPassword);
        this.userData.ucount = ucount;
        this.userData.password = password;
      }
    }
  },
  updated() {
    console.log("updated");
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
.login_container {
  min-width: 400px;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
}
</style>
