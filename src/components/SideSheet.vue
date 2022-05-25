<template>
  <div>
    <v-sheet
      color="grey lighten-4"
      class="pa-4 d-flex flex-column align-center justify-center"
    >
      <!-- <v-avatar class="mb-4" color="grey darken-1" size="64"></v-avatar> -->
      <el-avatar :src="userInfo.avatar"></el-avatar>
      <div>{{ userInfo.username }}</div>
      <div>{{ userInfo.userRole }}</div>
    </v-sheet>

    <v-divider></v-divider>

    <v-list>
      <v-list-item-group v-model="currentRouteAdd" color="primary">
        <v-list-item
          v-for="[
            index,
            { icon, title: text, fpath: link },
          ] of permissions.entries()"
          :key="index"
          link
          color="primary"
          @click="go_page(link)"
        >
          <v-list-item-icon>
            <v-icon>{{ icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ text }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list-item-group>
    </v-list>

    <v-divider></v-divider>

    <v-list>
      <v-list-item @click="log_out">
        <v-list-item-icon>
          <v-icon>mdi-delete</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title>退出登陆</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { mapActions, mapState } from "vuex";
import { permission_links, permission_roles } from "../constants";
import { PersonType } from "@/views/PersonManagement/PersonType";
import { PermissionType, UserVo } from "@/views/Login/LoginTypes";

export default Vue.extend({
  data: () => ({
    // permission_links: [
    //   // super_admin_links:
    //   [
    //     ["mdi-inbox-arrow-down", "任务大厅", "main"],
    //     ["mdi-alert", "任务分配", "taskAssign"],
    //     ["mdi-send", "标签管理", "labels"],
    //     ["mdi-delete", "个人信息", "profile"],
    //     ["mdi-alert-octagon", "权限管理", "personManagement"],
    //   ],
    //   // regular_admin_links:
    //   [
    //     ["mdi-inbox-arrow-down", "任务大厅", "main"],
    //     ["mdi-send", "标签管理", "labels"],
    //     ["mdi-send", "任务分配", "taskAssign"],
    //     ["mdi-delete", "个人信息", "profile"],
    //   ],
    //   // regular_person_link:
    //   [],
    // ],
    links: [],
    // permission_roles: ["超级管理员", "管理员", "普通用户"],
    role: "0",
    username: "",
    currentRouteAdd: "1",
    activePath: "",
    permissions: [] as PermissionType[],
    userInfo: {} as UserVo,

    headIconUrl:
      "https://w7.pngwing.com/pngs/306/70/png-transparent-computer-icons-management-admin-silhouette-black-and-white-neck.png",
  }),
  methods: {
    get_user_role() {
      // this.$http.get("/task?action=user_info").then((data) => {
      //   if (data.status === 200) {
      //     var user_info = data.data;
      //     this.role = user_info.role === "admin" ? "管理员" : "标注员";
      //     this.links =
      //       user_info.role === "admin" ? this.admin_links : this.member_links;

      //     console.log("user_info:", user_info);
      //   } else {
      //     alert(data.msg);
      //   }
      // });
      if (localStorage.length === 0) {
        this.links = [];
        return;
      }
      this.role = permission_roles[Number(localStorage.getItem("role")) - 1];
      this.links = permission_links[Number(localStorage.getItem("role")) - 1];

      this.username = localStorage.getItem("username");
      console.log("links:", this.links);
    },
    log_out() {
      localStorage.setItem("token", "");
      // localStorage.clear();
      this.$router.push("login").catch((_) => {});
    },
    go_page(name) {
      if (name == "null") {
        alert("未开放功能");
        return;
      }
      this.$router.push(name).catch((_) => {});
    },

    ...mapActions("user", ["setUserInfoFromLocal"]),
  },
  mounted(): void {
    // this.currentRouteAdd = this.$route.path;
    // console.log("this.currentRouteAdd:", this.currentRouteAdd);
    // this.get_user_role();
    this.$store.dispatch("user/setUserInfoFromLocal").then((value) => {
      console.log("value", value);
      console.log('this.$store', this.$store);
      //  permissions: (state) => state.user.userInfo.loginRoleVo.permissions || [] as Array<PermissionType>,
      this.permissions =
        this.$store.getters["user/userInfo"].loginRoleVo.permissions;
      this.userInfo = this.$store.getters["user/userInfo"].userVo;
      console.log("this.userInfo:", this.userInfo);
      // userInfo: (state) => state.user.userInfo.userVo || {} as PersonType,
    });
  },

  watch: {
    $route(to, from) {
      // this.currentRouteAdd = this.$route.path;
      console.log("this.currentRouteAdd:", this.currentRouteAdd);
      console.log(this.$route.path);
    },
  },
  computed: mapState({
    // permissions: (state) => state.user.userInfo.loginRoleVo.permissions || [] as Array<PermissionType>,
    // userInfo: (state) => state.user.userInfo.userVo || {} as PersonType,
  }),
});
</script>

<style scoped></style>
