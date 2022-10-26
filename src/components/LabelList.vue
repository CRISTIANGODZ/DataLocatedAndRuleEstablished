<template>
  <div>
    <h2 style="margin-bottom: 20px">模板管理</h2>
    <v-row>
      <v-btn color="primary ma-1" @click="add_new_info"> 新增模版 </v-btn>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-list two-line>
          <template v-for="(n, p_idx) in temp_data">
            <v-list-item :key="'template' + p_idx">
              <v-list-item-content>
                <v-list-item-title>{{ n["title"] }}</v-list-item-title>
                <div>
                  标签：
                  <v-chip
                    v-for="(l, idx) in n['labelCategories']"
                    :key="'label:' + p_idx + '' + idx"
                    class="ma-2"
                    :color="l.color"
                    text-color="white"
                  >
                    {{ l.text }}
                  </v-chip>
                  关联：
                  <v-chip
                    v-for="(cc, idx) in n['connectionCategories']"
                    :key="'relation:' + p_idx + '' + idx"
                    class="ma-2"
                  >
                    {{ cc.text }}
                  </v-chip>
                </div>
              </v-list-item-content>

              <v-list-item-action>
                <div>
                  <v-btn @click="edit_info(p_idx, n['id'])"> 编辑 </v-btn>
                  <v-btn
                    color="error"
                    style="margin-left: 5px"
                    @click="del_templete(n['id'])"
                  >
                    删除
                  </v-btn>
                </div>
              </v-list-item-action>
            </v-list-item>

            <v-divider
              v-if="n !== 6"
              :key="`divider-${p_idx}`"
              inset
            ></v-divider>
          </template>
        </v-list>
      </v-col>
    </v-row>

    <v-dialog
      v-model="dialog_edit"
      fullscreen
      hide-overlay
      transition="dialog-bottom-transition"
      scrollable
    >
      <v-card tile>
        <v-toolbar flat dark color="primary">
          <v-btn icon dark @click="dialog_edit = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>
            <v-text-field
              v-model="temp_label_info.title"
              label="请输入模版名称"
            ></v-text-field>
          </v-toolbar-title>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <v-btn dark text @click="saveTemp"> 保存 </v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <v-list three-line subheader>
          <v-subheader>标签</v-subheader>
          <v-list-item
            v-for="(l, idx) in temp_label_info.labelCategories"
            :key="idx"
          >
            <v-list-item-content>
              <v-text-field v-model="l.text" :value="l.text"> </v-text-field>
            </v-list-item-content>
            <v-list-item-action>
              <div>
                颜色:
                <colorPicker
                  v-model="l.color"
                  style="margin-right: 200px"
                  show-swatches
                  value="#00eeff"
                />
                <v-btn
                  color="error"
                  style="margin-left: 5px"
                  @click="delLabel(idx, l.id)"
                >
                  删除
                </v-btn>
              </div>
            </v-list-item-action>
          </v-list-item>
          <v-list-item>
            <v-list-item-content>
              <v-text-field
                v-model="temp_label"
                label="请输入标签名称"
              ></v-text-field>
            </v-list-item-content>
            <v-list-item-action>
              <div>
                颜色:
                <colorPicker v-model="temp_color" style="margin-right: 200px" />
                <v-btn class="ma-2" color="secondary" @click="addLabel">
                  添加
                </v-btn>
              </div>
            </v-list-item-action>
          </v-list-item>
        </v-list>
        <v-divider></v-divider>
        <v-list three-line subheader>
          <v-subheader>关联</v-subheader>
          <v-list-item
            v-for="(l, idx) in temp_label_info.connectionCategories"
            :key="idx"
          >
            <v-list-item-content>
              <v-text-field v-model="l.text" :value="l.title"> </v-text-field>
            </v-list-item-content>
            <v-list-item-action>
              <div>
                <v-btn
                  color="error"
                  style="margin-left: 5px"
                  @click="delCon(idx, l['id'])"
                >
                  删除
                </v-btn>
              </div>
            </v-list-item-action>
          </v-list-item>
          <v-list-item>
            <v-list-item-content>
              <v-text-field
                v-model="temp_connectionCategories"
                label="请输入关联名称"
              ></v-text-field>
            </v-list-item-content>
            <v-list-item-action>
              <div>
                <v-btn class="ma-2" color="secondary" @click="addCon">
                  添加
                </v-btn>
              </div>
            </v-list-item-action>
          </v-list-item>
        </v-list>

        <div style="flex: 1 1 auto"></div>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  data: () => ({
    dialog_edit: false,
    temp_label_info: {
      labelCategories: [],
      connectionCategories: [],
      title: "模版名称",
    }, // 待编辑的模板名称
    temp_label: "",
    temp_color: "#00eeff",
    temp_connectionCategories: "",
    temp_data: [],
    // labels_data: [],
    edit_template_id: 0,
  }),
  mounted(): void {
    // this.get_label_list()
    this.get_temp_list();
  },
  methods: {
    get_temp_list() {
      this.$http.get("/template/get/").then(({ data }) => {
        if (data.code === 200) {
          this.temp_data = data.data;
          console.log("this.temp_data:", data.data);
          // this.labels_data = this.temp_data[0]["labelCategories"]
          // console.log("this.labels_data:", this.labels_data);
        } else {
          alert(data.msg);
        }
      });
    },
    // get_label_list() {
    //   this.$http.get("/label").then(({ data }) => {
    //     if (data.status === 200) {
    //       this.labels_data = data.data;
    //     } else {
    //       alert(data.msg);
    //     }
    //   });
    // },
    addLabel() {
      var label = this.temp_label;
      var color = this.temp_color;
      if (!label || !color) {
        // alert("标签和颜色不能为空");
        // this.$swal({
        //   toast: true,
        //   position: "top",
        //   showConfirmButton: false,
        //   timer: 3000,
        //   icon: "warning",
        //   title: "Warning",
        //   text: "标签和颜色不能为空!!!",
        // });
        this.$warning("标签和颜色不能为空");
        return;
      }
      for (let i = 0; i < this.temp_label_info.labelCategories.length; i++) {
        if (label == this.temp_label_info.labelCategories[i].text) {
          // alert("标签不能重复");
          this.$warning("标签不能重复");
          return;
        }
      }
      if (this.edit_template_id !== 0) {
        this.$http
          .post("/label/insert/", {
            borderColor: "#000000",
            color: color,
            templateId: this.edit_template_id,
            text: label,
          })
          .then(({ data }) => {
            if (data.code === 200) {
              this.$success("添加成功");
            } else {
              this.$warning(data.msg);
            }
          });
      }
      this.temp_label = "";
      this.temp_color = "#33ffee";
      this.temp_label_info.labelCategories.push({
        text: label,
        color: color,
        borderColor: "#000000",
      });
    },
    delLabel(idx, id) {
      // 编辑状态删除标签 直接删除 templateId, labelId
      if (this.edit_template_id !== 0) {
        this.temp_label_info.labelCategories.splice(idx, 1);
        this.$http
          .delete("/label/delete/" + this.edit_template_id + "/" + id)
          .then(({ data }) => {
            if (data.code === 200) {
              this.$success("删除成功");
            } else {
              this.$warning(data.msg);
            }
          });
      }
      // 新建状态删除标签 直接删除 labelId
      else {
        this.temp_label_info.labelCategories.splice(idx, 1);
      }
    },
    addCon() {
      var label = this.temp_connectionCategories;
      if (!label) {
        this.$warning("关联不能为空");
        return;
      }
      for (
        let i = 0;
        i < this.temp_label_info.connectionCategories.length;
        i++
      ) {
        if (label == this.temp_label_info.connectionCategories[i].text) {
          this.$warning("关联不能重复");
          return;
        }
      }
      if (this.edit_template_id !== 0) {
        this.$http
          .post("/relation/insert/", {
            templateId: this.edit_template_id,
            text: label,
          })
          .then(({ data }) => {
            if (data.code === 200) {
              this.$success("添加成功");
            } else {
              this.$warning(data.msg);
            }
          });
      }
      this.temp_connectionCategories = "";
      this.temp_label_info.connectionCategories.push({ text: label });
    },
    delCon(idx, id) {
      // 编辑状态 直接添加 templateId, relationId 后端请求
      if (this.edit_template_id !== 0) {
        this.temp_label_info.connectionCategories.splice(idx, 1);
        // this.temp_label_info.connectionCategories.push({ text: label });
        this.$http
          .delete("/relation/delete/" + this.edit_template_id + "/" + id)
          .then(({ data }) => {
            if (data.code === 200) {
              this.$success("删除成功");
            } else {
              this.$warning(data.msg);
            }
          });
      }
      // 新增状态 先放在数组中
      else {
        this.temp_label_info.connectionCategories.splice(idx, 1);
      }
    },
    saveTemp() {
      var res = {};
      res["labelCategories"] = this.temp_label_info.labelCategories;
      res["connectionCategories"] = this.temp_label_info.connectionCategories;
      res["title"] = this.temp_label_info.title;

      if (!res["title"]) {
        this.$warning("模版名称不能为空");
        return;
      }
      if (!res["labelCategories"]) {
        this.$warning("标签不能为空");
        return;
      }

      var self = this;
      // 更新模板
      if (this.edit_template_id !== 0) {
        for (let i = 0; i < this.temp_data.length; i++) {
          if (
            this.edit_template_id !== this.temp_data[i].id &&
            this.temp_label_info.title == this.temp_data[i].title
          ) {
            this.$warning("模板名字不能重复");
            return;
          }
        }
        res["id"] = this.edit_template_id;
        res["method"] = "update";
        console.log("更新模板:", res);
        this.$http.post("/template/update/", res).then(({ data }) => {
          if (data.code === 200) {
            self.get_temp_list();
            self.$success("更新模板成功");
            this.dialog_edit = false;
          } else {
            this.$warning(data.msg);
            this.dialog_edit = false;
          }
          this.edit_template_id = 0;
        });
      }
      // 新增模板
      else {
        for (let i = 0; i < this.temp_data.length; i++) {
          if (this.temp_label_info.title == this.temp_data[i].title) {
            this.$warning("模板名字不能重复");
            return;
          }
        }
        res["method"] = "insert";
        this.$http.post("/template/insert/", res).then(({ data }) => {
          if (data.code === 200) {
            self.get_temp_list();
            this.dialog_edit = false;
            self.$success("添加模板成功");
          } else {
            this.$warning(data.msg);
            this.dialog_edit = false;
          }
        });
        console.log("新增模板:", res);
      }
    },
    del_templete(idx) {
      // var data = this.labels_data[idx];
      console.log("删除 template id:", idx);
      this.$http.delete("/template/delete/" + idx + "/").then(({ data }) => {
        if (data.code === 200) {
          this.get_temp_list();
          this.$success("删除成功");
        } else {
          this.$warning(data.msg);
        }
      });
      // data["method"] = "del";
      // var self = this;
      // this.$http.post("/template/delete", data).then(({ data }) => {
      //   if (data.code === 200) {
      //     self.get_label_list();
      //     this.dialog_edit = false;
      //   } else {
      //     alert(data.msg);
      //     this.dialog_edit = false;
      //   }
      // });
    },
    edit_info(idx, id) {
      this.temp_label_info = this.temp_data[idx];
      this.edit_template_id = id;
      this.dialog_edit = true;
    },
    add_new_info() {
      this.temp_label_info = {
        labelCategories: [],
        connectionCategories: [],
        title: "",
      };
      (this.temp_label = ""),
        (this.temp_color = "#33ffee"),
        (this.temp_connectionCategories = ""),
        (this.dialog_edit = true);
    },
    initial_label_connection() {
      (this.temp_label = ""),
        (this.temp_color = "#33ffee"),
        (this.temp_connectionCategories = "");
    },
  },
});
</script>

<style scoped></style>
