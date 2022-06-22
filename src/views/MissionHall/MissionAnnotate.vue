<template>
  <div>
    <v-row>
      <el-page-header @back="goBack" content="NLP 标注"> </el-page-header>
    </v-row>
    <v-row v-if="!(jsonData.task.doneState === 1)" style="margin-top: 40px">
      <v-btn @click="predictLabel" v-if="!!!jsonData.task.preLabel" color="info ma-1" id="predictLabelBtn">
        预打标
      </v-btn>
      <v-btn @click="reset" color="primary ma-1"> 重置所有标注 </v-btn>
      <v-btn class="ma-1" :loading="complete_loading" color="success" @click="set_complete">
        标记为已完成
        <template v-slot:loader>
          <span>标记中...</span>
        </template>
      </v-btn>
    </v-row>

    <v-row no-gutters style="margin-top: 40px">
      <v-col>
        <v-row class="pa-2">
          <v-card color="ma-2 pa-6 w-50">
            <h3>标题: {{ jsonData.text.title }}</h3>
            <h4>标注人: {{ jsonData.user.username }}</h4>
            <h4>任务类型: {{ jsonData.taskCategory.title }}</h4>
            <h4>预打标: {{ jsonData.task.preLabel == 1 ? "是" : "否" }}</h4>
            <h4>置信度: {{ taskWeight }}</h4>
            <h4>最后更新时间 {{ jsonData.task.updateTime }}</h4>
          </v-card>
        </v-row>
        <v-row>
          <div class="annotator-container" id="annotator-container" ref="container"></div>
        </v-row>
      </v-col>
      <v-col class="pa-2" v-if="true || annotator !== null">
        <v-card>
          <v-chip class="ma-4 font-weight-bold" text-color="black">
            模板:
            <!-- hell -->
            {{ jsonData.template.title }}
          </v-chip>
        </v-card>
        <v-divider style="margin-top: 10px; margin-bottom: 10px"></v-divider>
        <v-card>
          <v-subheader>标签</v-subheader>

          <v-chip v-for="label in jsonData.annotatorData.labelCategories || []" :key="'label' + label['id']"
            class="ma-2" :color="label['color']" text-color="white">
            {{ label.text }}
          </v-chip>
        </v-card>

        <v-divider style="margin-top: 10px; margin-bottom: 10px"></v-divider>
        <v-card>
          <v-subheader>关联</v-subheader>

          <v-chip v-for="cc in jsonData.annotatorData.connectionCategories || []" :key="'cc' + cc['id']" class="ma-2">
            {{ cc.text }}
          </v-chip>
        </v-card>
      </v-col>
      <v-dialog max-width="290" persistent v-model="showLabelCategoriesDialog">
        <v-card>
          <v-card-title>
            <span class="headline">请选择分类</span>
          </v-card-title>
          <v-card-text>
            <v-radio-group v-model="selectedLabelCategory">
              <v-radio :key="category.id" :label="category.text" :value="category.id"
                v-for="category in jsonData.annotatorData.labelCategories"></v-radio>
            </v-radio-group>
          </v-card-text>
          <v-card-actions>
            <v-btn @click="showLabelCategoriesDialog = false" color="primary">
              取消
            </v-btn>
            <v-btn @click="addLabel" color="primary"> 确定 </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <v-dialog max-width="290" persistent v-model="showConnectionCategoriesDialog">
        <v-card>
          <v-card-title>
            <span class="headline">请选择分类</span>
          </v-card-title>
          <v-card-text>
            <v-radio-group v-model="selectedConnectionCategory">
              <v-radio :key="category.id" :label="category.text" :value="category.id"
                v-for="category in jsonData.annotatorData.connectionCategories"></v-radio>
            </v-radio-group>
          </v-card-text>
          <v-card-actions>
            <v-btn @click="showConnectionCategoriesDialog = false" color="primary">
              取消
            </v-btn>
            <v-btn @click="addConnection" color="primary"> 确定 </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import Prism from "prismjs";
import { Action, Annotator } from "poplar-annotation";
import { LabelCategory } from "poplar-annotation/dist/Store/LabelCategory";
import { ConnectionCategory } from "poplar-annotation/dist/Store/ConnectionCategory";
// import { Label } from "poplar-annotation/dist/Store/Label";
import { toPercent } from "../../util/math";

enum CategorySelectMode {
  Create,
  Update,
}

export default {
  data() {
    return {
      annotator: null as Annotator | null,
      selectedLabelCategory: null as LabelCategory.Entity | null,
      selectedConnectionCategory: null as ConnectionCategory.Entity | null,
      showLabelCategoriesDialog: false as boolean,
      showConnectionCategoriesDialog: false as boolean,
      json: "",
      startIndex: -1,
      endIndex: -1,
      first: -1,
      second: -1,
      categorySelectMode: CategorySelectMode.Create,
      selectedId: -1,
      complete_loading: false,
      save_loading: false,
      textId: 0,
      templateId: 0,
      doneState: false,
      labelCategories: [],
      connectionCategories: [],
      templateTitle: "",
      taskId: 0,
      // modeld: 0,
      jsonData: {
        template: {
          id: 0,
          title: "",
          createTime: "",
        },
        task: {
          id: 18,
          textId: 1,
          userId: 2,
          templateId: 1,
          taskCategoryId: 1,
          weight: 0,
          preLabel: 0,
          doneState: 0,
          createTime: "2022-05-02T09:08:39.000+0000",
          updateTime: "2022-05-02T09:08:39.000+0000",
        },
        taskCategory: {
          id: 0,
          title: "",
        },
        text: {
          id: 1,
          title: "病例测试",
          content: "aaaaaaaaaaaaaaaa",
          uploadTime: "2022-04-29T20:14:49.000+0000",
        },
        user: {
          id: 2,
          ucount: "rosdfsdot",
          username: "roodddt",
          password: "123456",
          role: 2,
        },
        annotatorData: {
          labelCategories: [
            { id: 0, text: "赫连o", color: "#ffeedd", borderColor: "#eeaaa1" },
          ],
          connectionCategories: [{ id: 0, text: "病毒关系" }],
          labels: [
            { id: 0, categoryId: 0, startIndex: 10, endIndex: 21 },
            { id: 1, categoryId: 0, startIndex: 50, endIndex: 61 },
          ],
          connections: [{ id: 0, categoryId: 0, fromId: 0, toId: 1 }],
          content: "",
        },
      },
    };
  },
  methods: {
    updateJSON(): void {
      this.json = this.highlight(
        JSON.stringify(this.annotator.store.json, null, 2)
      );
    },
    addLabel(): void {
      if (!this.jsonData.task.doneState) {
        if (this.categorySelectMode === CategorySelectMode.Update) {
          this.annotator.applyAction(
            Action.Label.Update(this.selectedId, this.selectedLabelCategory)
          );
        } else {
          console.log("add label:", {
            taskId: this.taskId,
            startIndex: this.startIndex,
            endIndex: this.endIndex,
            categoryId: this.selectedLabelCategory,
            templateId: this.jsonData.template.id,
          });
          this.$http
            .post("/task/insertTaskLabel/", {
              taskId: this.taskId,
              startIndex: this.startIndex,
              endIndex: this.endIndex,
              categoryId: this.selectedLabelCategory,
              templateId: this.jsonData.template.id,
            })
            .then(({ data }) => {
              if (data.code === 200) {
                this.complete_loading = false;
                this.$success("添加成功");
                console.log("add label: ", data.data);
                this.annotator.store.labelRepo["nextId"] = data.data;
                this.annotator.applyAction(
                  Action.Label.Create(
                    this.selectedLabelCategory,
                    this.startIndex,
                    this.endIndex
                  )
                );
                this.showLabelCategoriesDialog = false;
                this.updateJSON();
              } else {
                this.$warning(data.msg);
              }
            });
        }
      }
    },
    addConnection(): void {
      if (!this.jsonData.task.doneState) {
        if (this.categorySelectMode === CategorySelectMode.Update) {
          this.annotator.applyAction(
            Action.Connection.Update(
              this.selectedId,
              this.selectedConnectionCategory
            )
          );
        } else {
          console.log("add connection:", {
            taskId: this.taskId,
            fromId: this.from,
            toId: this.to,
            categoryId: this.selectedConnectionCategory,
          });
          this.$http
            .post("/task/insertTaskRelation/", {
              taskId: this.taskId,
              fromId: this.from,
              toId: this.to,
              categoryId: this.selectedConnectionCategory,
              templateId: this.jsonData.template.id,
            })
            .then(({ data }) => {
              if (data.code === 200) {
                this.complete_loading = false;
                this.annotator.store.connectionRepo["nextId"] = data.data;
                this.annotator.applyAction(
                  Action.Connection.Create(
                    this.selectedConnectionCategory,
                    this.from,
                    this.to
                  )
                );
                this.$success("添加成功");
                console.log("add connection: ", data.data);
                this.showLabelCategoriesDialog = false;
                this.updateJSON();
              } else {
                this.$warning(data.msg);
              }
            });
          this.showConnectionCategoriesDialog = false;
          // this.updateJSON();
        }
      }
    },
    createAnnotator(): Annotator {
      const annotator = new Annotator(
        JSON.stringify(this.jsonData.annotatorData),
        this.$refs.container
      );
      if (!this.jsonData.task.doneState) {
        annotator.on("textSelected", (startIndex, endIndex) => {
          this.startIndex = startIndex;
          this.endIndex = endIndex;
          this.categorySelectMode = CategorySelectMode.Create;
          this.showLabelCategoriesDialog = true;
        });
        annotator.on("twoLabelsClicked", (fromLabelId, toLabelId) => {
          this.from = fromLabelId;
          this.to = toLabelId;
          this.categorySelectMode = CategorySelectMode.Create;
          this.showConnectionCategoriesDialog = true;
        });
        annotator.on("labelRightClicked", (labelId, event: MouseEvent) => {
          if (!this.doneState) {
            this.$confirm("确定要删除标注吗？", "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
            }).then(() => {
              if (event.ctrlKey) {
                this.categorySelectMode = CategorySelectMode.Update;
                this.selectedId = labelId;
                this.showLabelCategoriesDialog = true;
              } else {
                annotator.applyAction(Action.Label.Delete(labelId));
              }
              this.$http
                .delete("/task/deleteTaskLabel/" + labelId + "/")
                .then(({ data }) => {
                  if (data.code === 200) {
                    this.complete_loading = false;
                    this.$success("删除成功");
                    console.log("delete label: ", data.data);
                    this.showLabelCategoriesDialog = false;
                    this.updateJSON();
                    // this.getTask();
                  } else {
                    this.$warning(data.msg);
                  }
                });
              this.showLabelCategoriesDialog = false;
              this.updateJSON();
            });
          }
        });

        annotator.on(
          "connectionRightClicked",
          (connectionId, event: MouseEvent) => {
            if (!this.doneState) {
              this.$confirm("确定要删除吗？", "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
              }).then(() => {
                if (event.ctrlKey) {
                  this.categorySelectMode = CategorySelectMode.Update;
                  this.selectedId = connectionId;
                  this.showConnectionCategoriesDialog = true;
                } else {
                  annotator.applyAction(Action.Connection.Delete(connectionId));
                }
                this.$http
                  .delete("/task/deleteTaskRelation/" + connectionId + "/")
                  .then(({ data }) => {
                    if (data.code === 200) {
                      this.complete_loading = false;
                      this.$success("删除成功");
                      console.log("delete connection: ", data.data);
                      this.showLabelCategoriesDialog = false;
                      this.updateJSON();
                      // this.getTask();
                    } else {
                      this.$warning(data.msg);
                    }
                  });
                this.showLabelCategoriesDialog = false;
                this.updateJSON();
              });
            }
          }
        );
        annotator.on("contentInput", (position, value) => {
          annotator.applyAction(Action.Content.Splice(position, 0, value));
          this.updateJSON();
        });
        annotator.on("contentDelete", (position, length) => {
          annotator.applyAction(Action.Content.Splice(position, length, ""));
          this.updateJSON();
        });
        return annotator;
      }
    },
    highlight(code: string): string {
      return Prism.highlight(code, Prism.languages.javascript, "javascript");
    },
    download: function () {
      const eleLink = document.createElement("a");
      eleLink.download = "data.json";
      eleLink.style.display = "none";
      const blob = new Blob([
        JSON.stringify(this.annotator.store.json, null, 4),
      ]);
      eleLink.href = URL.createObjectURL(blob);
      document.body.appendChild(eleLink);
      eleLink.click();
      document.body.removeChild(eleLink);
    },
    reset: function () {
      this.$confirm("确定要重置所有标注吗？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$http
          .delete(
            "/task/reSet/" + this.taskId + "/" + this.jsonData.template.id
          )
          .then(({ data }) => {
            if (data.code === 200) {
              this.complete_loading = false;
              this.$success("重置成功");

              this.jsonData.annotatorData.labels = [];
              this.annotator.remove();
              this.getTask();
            } else {
              this.$warning(data.msg);
              this.complete_loading = false;
            }
          });
      });
    },
    set_complete: function () {
      this.$http
        // .get("/task/saveTaskById/" + this.taskId + "/")
        .post("/task/saveTaskById/",
          {
            id: this.taskId
          })
        .then(({ data }) => {
          if (data.code === 200) {
            this.complete_loading = false;
            this.$success("文本标注完成");
            this.$router.go("-1");
            this.updateJSON();
          } else {
            this.$warning(data.msg);
            this.complete_loading = false;
          }
        });
    },
    set_complete2: function () {
      this.complete_loading = true;
      var res_data = {
        textId: this.text_id,
        doneState: true,
        textLabelList: [],
        textRelationList: [],
      };
      // var res_data1 = this.jsonData;
      res_data["textLabelList"] = this.annotator.store.json.labels.map((v) => {
        v["textId"] = this.text_id;
        return v;
      });
      res_data["textRelationList"] = this.annotator.store.json.connections.map(
        (v) => {
          v["textId"] = this.text_id;
          return v;
        }
      );
      res_data["done_state"] = true;
      console.log("保存text:", res_data);
      this.$http.post("/text/saveTask", res_data).then(({ data }) => {
        if (data.code === 200) {
          this.complete_loading = false;
          this.$success("保存成功");
          this.$router.push("/").catch((_) => { });
        } else {
          this.$warning(data.msg);
          this.complete_loading = false;
        }
      });
    },
    set_save: function () {
      this.save_loading = true;
      var res_data = this.jsonData;
      res_data["labels"] = this.annotator.store.json.labels;
      res_data["connections"] = this.annotator.store.json.connections;
      this.$http.post("/task", res_data).then(({ data }) => {
        if (data.status === 200) {
          this.save_loading = false;
        } else {
          this.$warning(data.msg);
          this.save_loading = false;
        }
      });
    },
    init_params: function () {
      this.taskId = this.$route.query.taskId;
      // this.text_id = this.$route.query.text_id;
      // this.template_id = this.$route.query.template_id;
      // this.done_state = this.$route.query.done_state;
      // this.doneState = this.$route.query.done_state;
    },
    getTaskInfo() { },
    getLabels() { },
    getConnections() { },
    getLabelCategories() { },
    getConnectionCategories() { },
    getTask: function () {
      this.$http.get("/task/getTask/" + this.taskId + "/").then(({ data }) => {
        if (data.code === 200) {
          this.jsonData.template = data.data.template;
          this.jsonData.task = data.data.task;
          this.jsonData.user = data.data.user;
          this.jsonData.text = data.data.text;
          this.jsonData.taskCategory = data.data.taskCategory;
          this.jsonData.annotatorData = {
            labelCategories: data.data.labelList || [],
            connectionCategories: data.data.relationList || [],
            labels: data.data.taskLabelList || [],
            connections: data.data.taskRelationList || [],
            content: data.data.text.content,
          };
          console.log("this.jsonData: ", this.jsonData);
          if (
            this.jsonData.annotatorData !== null &&
            this.jsonData.annotatorData.content
          ) {
            this.annotator = this.createAnnotator();
            this.updateJSON();
          }
        } else {
          this.$warning(data.msg);
        }
      });
    },
    goBack() {
      this.$router.go(-1);
    },
    predictLabel() {
      const loading = this.$loading({
        lock: true,
        // text: "预打标中...",
        spinner: "el-icon-loading",
        // background: "rgba(0, 0, 0, 0.7)",
        customClass: "annotate-y-loading",
        target: "#predictLabelBtn",
      });
      this.$http
        // .get("/model/preLabel/" +
        //   // this.modeld + "/" +
        //   this.taskId
        // )
        .post("/model/preLabel/",
          { id: this.taskId }
        )
        .then(({ data }) => {
          if (data.code === 200) {
            console.log("model data: ", data);
            loading.close();
            this.$success("预测成功");
            this.annotator.remove();
            this.getTask();
          } else {
            this.$warning("未选择预测模型或模型正在启动中");
            loading.close();
            this.annotator.remove();
          }
        });
    },
  },
  computed: {
    taskWeight: {
      get: function () {
        return toPercent(this.jsonData.task.weight);
      },
    },
  },
  created(): void {
    this.$eventbus.$on("fileUploaded", (jsonData: JSON) => {
      this.jsonData = jsonData;
      if (this.annotator !== null) {
        this.annotator.remove();
      }
      if (this.jsonData !== null && this.jsonData.content) {
        this.annotator = this.createAnnotator();
        this.updateJSON();
      }
    });
    this.$eventbus.$on("downloadRequest", () => {
      this.download();
    });
    this.$eventbus.$on("downloadSVGRequest", () => {
      this.downloadSVG();
    });
    this.init_params();
    this.getTask();
  },

  mounted(): void { },
};
</script>
<style scoped>
.code-container-wrapper,
.container-wrapper {
  min-height: calc(100vh - 64px);
  max-height: calc(100vh - 64px);
  overflow: hidden;
  /* padding: 0 !important; */
}

.container-wrapper {
  border-right: solid 2px black;
}

.code-container-wrapper {
  border-left: solid 2px black;
}

.annotator-container,
.code-container {
  padding-top: 10px;
  /* overflow: scroll; */
  /* height: calc(100vh - 64px); */
}

code {
  /* max-width: calc(45vw - 16px); */
  background: rgb(32, 32, 32) !important;
  box-shadow: none !important;
  padding: 8px !important;
  /* padding-right: 40px !important; */
  margin-bottom: 20px;
}
</style>
<style>
.annotator-container>svg {
  width: 45vw;
}

.poplar-annotation-label {
  font-size: 14px;
}

.poplar-annotation-connection {
  font-family: "PingFang SC", serif;
  font-size: 12px;
}

#predictLabelBtn {
  display: flex;
  justify-items: center;
  align-content: center;
}
</style>
