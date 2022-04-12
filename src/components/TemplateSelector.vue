<template>
  <v-container fluid>
    <v-row align="center">
      <v-col class="d-flex" cols="1" sm="6">
        <v-select
          v-model="select"
          :items="template_list"
          item-text="title"
          item-value="id"
          label="选择模板"
          @change="changeTemplate"
          :readonly="!editable"
        ></v-select>
      </v-col>
      <v-btn
        color="primary"
        depressed
        elevation="1"
        @click="selectTemplate"
        :disabled="!editable && !selected"
        >确定</v-btn
      >
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";

// interface TemplateArray{
//   [index:number]:{
//     id : number;
//     title : string;
//   }
// }

export default Vue.extend({
  data: () => ({
    // template_list: [],
    select:0,
    selected: true,
  }),
  props: {
    selectTemplateId: Number,
    editable: Boolean,
    template_list: Array,
    selectTemplateInfo: Object,
  },
  methods: {
    // get_template_list() {
    //   this.$http.get("/getTemplateLists").then(({ data }) => {
    //     if (data.code === 200) {
    //       this.template_list = data.data;
    //       console.log("template_list:", this.template_list);
    //     } else {
    //       alert(data.msg);
    //     }
    //   });
    // },
    selectTemplate() {
      console.log("select:", this.select);
    },
    initialTemplate() {
      if (this.editable) {
        if (this.selectTemplateId !== 0) {
          if (this.selectTemplate) {
            this.select = this.selectTemplate;
          } else {
            this.select = { title: "", id: 0 };
          }
          this.selected = true;
          this.select = this.selectTemplateId;
        } else {
          this.selected = false;
          // this.select.id = 0;
        }
      }
    },
    changeTemplate(id) {
      if (this.selectTemplateId === 0) {
        this.selected = false;
        this.select= id;
        console.log("this.select:", this.select);
      }
      // this.selected = false;
      // this.select.id = id;
      // this.select.id = id;
      console.log("this.select:", this.select);
    },
  },
  mounted(): void {
    // this.get_template_list();
    this.initialTemplate();
    console.log("this.selectTemplateId:", this.selectTemplateId);
    console.log("this.template_list:", this.template_list);
    console.log("this.select:", this.select);
  },
});
</script>

<style></style>
