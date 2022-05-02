<!--
 * @Author: Zhou Xianghui
 * @Date: 2022-04-11 14:28:15
 * @LastEditors: Zhou Xianghui
 * @LastEditTime: 2022-04-30 14:03:48
 * @FilePath: \ai_ann_front\src\views\Login.vue
 * @Description:
 * after a long, long, long time
 * Copyright (c) 2022 by Zhou Xianghui/Qianjiang Tech, All Rights Reserved.
-->
<template>
  <v-row justify="center">
    <v-dialog
      v-model="d_login"
      persistent
      max-width="600px"
      overlay-opacity="1"
      overlay-color="#00BFFF"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn color="primary" dark v-bind="attrs" v-on="on"> ac </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="headline">NLP标注平台</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field
                  label="账号"
                  required
                  v-model="ucount"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  label="密码"
                  type="password"
                  v-model="password"
                  required
                  v-on:keyup.enter="login_res"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="login_res"> 登陆 </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  components: {},
  data() {
    return {
      d_login: true,
      username: null,
      ucount: null,
      password: null,
    };
  },
  methods: {
    login_res() {
      var self = this;
      this.$http
        .post("/user/login/", {
          ucount: this.ucount,
          password: this.password,
        })
        .then(({ data }) => {
          if (data.code === 200) {
            console.log("login data:", data);
            var token = data.data.token;
            // localStorage.setItem("token", this.username + "_" + token);
            localStorage.setItem("token", token);
            localStorage.setItem("role", data.data.user.role);
            // localStorage.setItem("role", "admin");
            this.username = data.data.user.username;
            this.ucount = data.data.user.ucount;
            localStorage.setItem("username", this.username);
            this.$router.push("/").catch((_) => {});
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
});
</script>

<style scoped>
@import "~prismjs/themes/prism-dark.css";

.container {
  max-height: calc(100vh - 64px);
  overflow: hidden;
}
</style>
