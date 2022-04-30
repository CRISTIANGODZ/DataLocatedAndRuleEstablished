<template>
  <div>
    <v-sheet color="grey lighten-4" class="pa-4">
      <!-- <v-avatar class="mb-4" color="grey darken-1" size="64"></v-avatar> -->
      <el-avatar
        src="https://w7.pngwing.com/pngs/306/70/png-transparent-computer-icons-management-admin-silhouette-black-and-white-neck.png"
      ></el-avatar>
      <div>管理员 {{username}}</div>
    </v-sheet>

    <v-divider></v-divider>

    <v-list>
      <v-list-item-group v-model="currentRouteAdd" color="primary">
        <v-list-item
          v-for="[icon, text, link] in links"
          :key="link"
          link
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

export default Vue.extend({
  data: () => ({
    admin_links: [
      ["mdi-inbox-arrow-down", "任务大厅", "/"],
      ["mdi-oil-lamp", "Mission Hall", "missionHall"],
      ["mdi-send", "标签管理", "labels"],
      ["mdi-alert", "任务分配", "taskAssign"],
      ["mdi-delete", "个人信息", "profile"],
      ["mdi-alert-octagon", "权限管理", "permission"],
    ],
    member_links: [
      ["mdi-inbox-arrow-down", "任务大厅", "/"],
      ["mdi-send", "标签管理", "labels"],
      ["mdi-send", "任务分配", "taskAssign"],
      ["mdi-delete", "个人信息", "null"],
    ],
    links: [],
    role: "",
    username: "",
    currentRouteAdd: "missoinHall",
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

      this.role =
        localStorage.getItem("role") === "0" ? "管理员" : "标注员";
      this.links =
        localStorage.getItem("role") === "0"
          ? this.admin_links
          : this.member_links;
      this.username = localStorage.getItem("username");
      console.log("links:", this.links);
    },
    log_out() {
      localStorage.setItem("token", "");
      this.$router.push("login").catch((_) => {});
    },
    go_page(name) {
      if (name == "null") {
        alert("未开放功能");
        return;
      }
      this.$router.push(name).catch((_) => {});
    },
  },
  mounted(): void {
    this.get_user_role();
  },
});
</script>

<style scoped></style>
