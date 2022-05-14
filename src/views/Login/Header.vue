<template>
  <el-row type="flex" justify="center" drection="horizontal ">
    <!-- <el-image
      src="../../assets/images/logo.png"
      fit="fit"
    ></el-image> -->
    <!-- <img src="../../assets/images/logo.png" alt=""> -->
    <h1>NLP 标注平台</h1>
  </el-row>
</template>

<script lang="ts">
import Vue from "vue";
export default Vue.extend({
  data() {
    return {
      url: "../../assets/images/logo.png",
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
