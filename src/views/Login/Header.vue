<template>
  <el-row
    type="flex"
    justify="center"
    direction="horizontal"
    class="login_register_header"
  >
    <!-- <img src="../../assets/images/logo.png" alt=""> -->
    <!-- <el-image :src="url" fit="cover"></el-image> -->
    <h1>NLP 标注平台</h1>
  </el-row>
</template>

<script lang="ts">
import Vue from "vue";
export default Vue.extend({
  data() {
    return {
      url: "https://s2.loli.net/2022/05/14/FnMwDUxXHsoi4e8.png",
    };
  },
});
</script>

<style scoped>
h1 {
  color: blue;
  font-size: 2em;
}
</style>
