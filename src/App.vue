<!--
 * @Author: Zhou Xianghui
 * @Date: 2022-05-14 09:08:09
 * @LastEditors: Zhou Xianghui
 * @LastEditTime: 2022-05-14 09:08:11
 * @FilePath: \ai_ann_front\src\App.vue
 * @Description:
 * after a long, long, long time
 * Copyright (c) 2022 by Zhou Xianghui/Qianjiang Tech, All Rights Reserved.
-->
<!-- <template>
  <v-app>
    <v-navigation-drawer app>
      <side-sheet></side-sheet>
    </v-navigation-drawer>
    <v-main>
      <v-container class="py-8 px-6 shadow p-3 mb-5 bg-body rounded" fluid>
        <router-view></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script lang="ts">
import Vue from "vue";
import SideSheet from "./components/SideSheet.vue";
import AppBar from "./components/AppBar.vue";

export default Vue.extend({
  components: {
    SideSheet,
    AppBar,
  },
});
</script> -->

<template>
  <el-container style="width: 100%">
    <el-main class="login_register_container">
      <router-view></router-view>
      <!-- <router-link to="/login">登录</router-link>
      <router-link to="/register">注册</router-link> -->
      <!-- <login-vue></login-vue> -->
    </el-main>
  </el-container>
</template>

<script lang="ts">
import Vue from "vue";
import LoginVue from "./views/Login/Login.vue";
export default Vue.extend({
  components: {
    LoginVue,
  },
});
</script>


<style scoped>
.login_register_container {
  /* display: flex;
  width: 100%;
  justify-content: center; */
}
</style>
