<template>
  <v-app>
    <v-navigation-drawer permanent app>
      <side-sheet></side-sheet>
    </v-navigation-drawer>
    <v-main>
      <v-container class="py-8 px-6 shadow p-3 mb-5 bg-body rounded" fluid>
        <router-view></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>
<!-- <template>
  <el-container>
    <el-aside><side-sheet></side-sheet></el-aside>
    <el-main><router-view></router-view></el-main>
  </el-container>
</template> -->

<script lang="ts">
import Vue from "vue";
import SideSheet from "../components/SideSheet.vue";
import AppBar from "../components/AppBar.vue";

export default Vue.extend({
  components: {
    SideSheet,
    AppBar,
  },
  mounted() {
    console.log("this is main component");
  },
});
</script>

<style scoped>
@import "~prismjs/themes/prism-dark.css";

/* .container {
  max-height: calc(100vh - 64px);
  overflow: hidden;
} */
</style>
