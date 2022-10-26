<template>
  <div>
    <LabelList></LabelList>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import LabelList from "../components/LabelList.vue";

export default Vue.extend({
  components: { LabelList },
});
</script>

<style scoped>
@import "~prismjs/themes/prism-dark.css";

.container {
  max-height: calc(100vh - 64px);
  overflow: hidden;
}
</style>
