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
        <el-button
          type="primary"
          @click="vlogin(userData.ucount, userData.password)"
          style="min-width: 150px"
          >Vuex登录</el-button
        >
      </el-form>
    </el-row>
  </div>
</template>

<script lang="ts">
import { mapState, mapActions } from "vuex";
import { PersonType } from "@/views/PersonManagement/PersonType";
import Vue from "vue";
import { LoginJSONData } from "@/views/Login/LoginTypes";
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
    vlogin(ucount: string, password: string) {
      this.$store
        .dispatch("user/login", { ucount, password } as PersonType)
        .then(() => {
          this.$router.push(this.main_url);
        });
      console.log("this.$store.state.user", this.$store.state.user);
    },
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
            localStorage.setItem("role", data.data.userVo.userRoleId);
            localStorage.setItem("username", data.data.userVo.username);
            localStorage.setItem("ucount", data.data.userVo.ucount);
            localStorage.setItem("password", this.userData.password);
            localStorage.setItem("headIconUrl", data.data.userVo.avatar);
            this.username = data.data.userVo.username;
            this.ucount = data.data.userVo.ucount;
            // localStorage.setItem("role", data.data.user.role);
            // localStorage.setItem("username", data.data.user.username);
            // localStorage.setItem("ucount", data.data.user.ucount);
            // localStorage.setItem("password", this.userData.password);
            // this.username = data.data.user.username;
            // this.ucount = data.data.user.ucount;
            // localStorage.setItem("role", "admin");
            const first_url = data.data.loginRoleVo.permissions[0]["fpath"];
            this.$router.push(first_url).catch((_) => {
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
  computed: mapState({
    main_url: (state) =>
      state.user.userInfo.loginRoleVo.permissions[0]["fpath"],
  }),
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
