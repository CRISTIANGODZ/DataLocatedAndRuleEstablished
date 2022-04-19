<template>
  <div>
    <v-row>
      <v-col cols="12">
        <v-card>
          <v-subheader>未完成 总共: {{ wait_list.length }}</v-subheader>

          <v-list two-line>
            <template v-for="(n, index) in wait_list">
              <v-list-item :key="'inlabel:' + index">
                <v-list-item-avatar color="green darken-1">
                </v-list-item-avatar>

                <v-list-item-content>
                  <v-list-item-title
                    ><a
                      @click="
                        get_file_data(n['id'], n['templateId'], n['doneState'])
                      "
                      >{{ n["title"] }}</a
                    >
                  </v-list-item-title>

                  <v-list-item-subtitle>
                    最近标注时间：{{ n["updateTime"] }}
                  </v-list-item-subtitle>
                </v-list-item-content>

                <v-list-item-action>
                  <div>
                    <TemplateSelector
                      :selectTemplateId="n['templateId']"
                      :selectTemplateIdOperation="selectTemplateIdOperation"
                      :selectTemplateInfo="{
                        textId: n['id'],
                        title: n['templateTitle'],
                      }"
                      :template_list="template_list"
                      :editable="!n['doneState']"
                    ></TemplateSelector>
                  </div>
                </v-list-item-action>
              </v-list-item>

              <v-divider
                v-if="n !== wait_list.length"
                :key="`inLabeling-divider-${index}`"
                inset
              ></v-divider>
            </template>
          </v-list>
        </v-card>
      </v-col>

      <v-col cols="12">
        <v-card>
          <v-subheader>已完成 总共: {{ done_list.length }}</v-subheader>

          <v-list two-line>
            <template v-for="(n, index) in done_list">
              <v-list-item :key="'labeled:' + index">
                <v-list-item-avatar color="grey darken-1"> </v-list-item-avatar>

                <v-list-item-content>
                  <v-list-item-title
                    ><a
                      @click="
                        get_file_data(n['id'], n['templateId'], n['doneState'])
                      "
                      >{{ n["title"] }}</a
                    >
                  </v-list-item-title>

                  <v-list-item-subtitle>
                    最近标注时间：{{ n["updateTime"] }}
                  </v-list-item-subtitle>
                </v-list-item-content>

                <v-chip dark class="mr-20">
                  <!-- <TemplateSelector
                    :selectTemplateId="n['template_id']"
                    :template_list="template_list"
                    :editable="!n['done_state']"
                  ></TemplateSelector> -->
                  {{ n["templateTitle"] }}
                </v-chip>
              </v-list-item>

              <v-divider
                v-if="n !== done_list.length"
                :key="`alreadyLabeled-divider-${index}`"
                inset
              ></v-divider>
            </template>
          </v-list>
        </v-card>
      </v-col>
    </v-row>
    <v-row justify="center">
      <v-dialog
        v-model="select_label"
        persistent
        max-width="1200px"
        overlay-opacity="0.5"
        overlay-color="#00BFFF"
      >
        <v-card>
          <v-card-title>
            <span class="headline">初始化标签模版</span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <template v-for="(n, p_idx) in labels_data">
                <v-list-item :key="p_idx">
                  <v-list-item-content>
                    <v-list-item-title>{{ n["title"] }}</v-list-item-title>
                    <div>
                      标签：
                      <v-chip
                        v-for="(l, idx) in n.labelCategories"
                        :key="idx"
                        class="ma-2"
                        :color="l.color"
                        text-color="white"
                      >
                        {{ l.text }}
                      </v-chip>
                      关联：
                      <v-chip
                        v-for="(cc, idx) in n.connectionCategories"
                        :key="idx"
                        class="ma-2"
                      >
                        {{ cc.text }}
                      </v-chip>
                    </div>
                  </v-list-item-content>

                  <v-list-item-action>
                    <div>
                      <v-btn @click="select_now_label(p_idx)"> 选中 </v-btn>
                    </div>
                  </v-list-item-action>
                </v-list-item>

                <v-divider
                  v-if="n !== 6"
                  :key="`divider-${n}`"
                  inset
                ></v-divider>
              </template>
            </v-container>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="select_label = false">
              返回
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
    <!-- <TemplateSelector
      :selectTemplateId="12"
      :template_list="template_list"
      :editable="true"
    ></TemplateSelector> -->
    <!-- <TemplateSelector :selectTemplateId="12"></TemplateSelector> -->
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import TemplateSelector from "../components/TemplateSelector.vue";

export default Vue.extend({
  data: () => ({
    wait_list: [],
    done_list: [],
    select_label: false,
    labels_data: [],
    temp_filedata: null,
    template_list: [],
    select_template_id: 0,
  }),
  components: { TemplateSelector },
  methods: {
    selectTemplateIdOperation(id) {
      this.select_template_id = id;
      console.log("父组件 selectTemplateIdOperation: ", id);

    },

    get_file_data(id, templateId, doneState) {
      console.log("id:", id);
      console.log("templateId:", templateId);
      console.log("doneState:", doneState);
      // this.$router
      //   .push({
      //     name: "annotate",
      //     params: {
      //       text_id: id,
      //       template_id: templateId,
      //       done_state: doneState,
      //     },
      //   })
      //   .catch((_) => {});
      this.$router
        .push({
          path: "/annotate",
          query: {
            text_id: id,
            template_id:
              this.select_template_id === 0
                ? templateId
                : this.select_template_id,
            done_state: doneState,
          },
        })
        .catch((_) => {});
      // let Base64 = require("js-base64").Base64;
      // var self = this;
      // this.$http
      //   .post("/getTask", {
      //     action: "get_file",
      //     file_name: Base64.encode(filename),
      //   })
      //   .then((data) => {
      //     if (data.status === 200) {
      //       if (
      //         !data.data.labelCategories.length ||
      //         !data.data.connectionCategories.length
      //       ) {
      //         self.get_label_list();
      //         self.temp_filedata = data.data;
      //         self.select_label = true;
      //       } else {
      //         window.setTimeout(() => {
      //           this.$eventbus.$emit("fileUploaded", data.data);
      //           this.$forceUpdate();
      //         }, 10);
      //         this.$router.push("annotate").catch((_) => {});
      //       }
      //     } else {
      //       alert(data.msg);
      //     }
      //   });
    },
    select_now_label(idx) {
      var data = this.temp_filedata;
      data["labelCategories"] = this.labels_data[idx]["labelCategories"];
      data["connectionCategories"] =
        this.labels_data[idx]["connectionCategories"];
      window.setTimeout(() => {
        this.$eventbus.$emit("fileUploaded", data);
        this.$forceUpdate();
      }, 10);
      this.$router.push("annotate").catch((_) => {});
    },
    get_task_list() {
      this.$http.get("/text/getTask").then(({ data }) => {
        if (data.code === 200) {
          this.template_list = data.data.templateList;
          this.wait_list = data.data.waitTextVo;
          this.done_list = data.data.doneTextVo;
          console.log("this wait list: ", this.wait_list);
          // this.get_template_list();
        } else {
          this.$waning(data.msg);
        }
      });
    },
    get_label_list() {
      this.$http.get("/label").then(({ data }) => {
        if (data.status === 200) {
          this.labels_data = data.data;
        } else {
          this.$warning(data.msg);
        }
      });
    },
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
  },

  mounted(): void {
    this.get_task_list();
  },
});
</script>

<style scoped></style>
