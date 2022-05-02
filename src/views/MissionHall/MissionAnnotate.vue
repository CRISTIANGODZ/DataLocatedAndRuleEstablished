<template></template>

<script lang="ts">
import Vue from "vue";
import Prism from "prismjs";
import { Action, Annotator } from "poplar-annotation";
import { LabelCategory } from "poplar-annotation/dist/Store/LabelCategory";
import { ConnectionCategory } from "poplar-annotation/dist/Store/ConnectionCategory";
// import { Label } from "poplar-annotation/dist/Store/Label";

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
      jsonData: {
        taskInfo: {
          taskId: 0,
          templateId: 0,
          taskCategoryId: 0,
          taskCategoryTitle: "大病",
          taskTitle: "hello world",
          taskContent: "hello world",
          doneState: 0,
          userId: 0,
          updateTime: "2022-01-01 00:00:00",
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
      if (!this.doneState) {
        if (this.categorySelectMode === CategorySelectMode.Update) {
          this.annotator.applyAction(
            Action.Label.Update(this.selectedId, this.selectedLabelCategory)
          );
        } else {
          console.log("add label:", {
            textId: this.text_id,
            startIndex: this.startIndex,
            endIndex: this.endIndex,
            categoryId: this.selectedLabelCategory,
            selectedId: this.selectedId,
          });
          this.$http
            .post("/text/insertTaskLabel/", {
              textId: this.text_id,
              startIndex: this.startIndex,
              endIndex: this.endIndex,
              categoryId: this.selectedLabelCategory,
              templateId: this.templateId,
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
      if (!this.doneState) {
        if (this.categorySelectMode === CategorySelectMode.Update) {
          this.annotator.applyAction(
            Action.Connection.Update(
              this.selectedId,
              this.selectedConnectionCategory
            )
          );
        } else {
          console.log("add connection:", {
            textId: this.text_id,
            fromId: this.from,
            toId: this.to,
            categoryId: this.selectedConnectionCategory,
          });
          this.$http
            .post("/text/insertTaskRelation/", {
              textId: this.text_id,
              fromId: this.from,
              toId: this.to,
              categoryId: this.selectedConnectionCategory,
              templateId: this.templateId,
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
        JSON.stringify(this.jsonData),
        this.$refs.container
      );
      if (!this.doneState) {
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
            if (event.ctrlKey) {
              this.categorySelectMode = CategorySelectMode.Update;
              this.selectedId = labelId;
              this.showLabelCategoriesDialog = true;
            } else {
              annotator.applyAction(Action.Label.Delete(labelId));
            }
            this.$http
              .delete("/text/deleteTaskLabel/" + labelId + "/")
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
          }
        });

        annotator.on(
          "connectionRightClicked",
          (connectionId, event: MouseEvent) => {
            if (!this.doneState) {
              if (event.ctrlKey) {
                this.categorySelectMode = CategorySelectMode.Update;
                this.selectedId = connectionId;
                this.showConnectionCategoriesDialog = true;
              } else {
                annotator.applyAction(Action.Connection.Delete(connectionId));
              }
              this.$http
                .delete("/text/deleteTaskRelation/" + connectionId + "/")
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
      this.$http
        .delete("/text/reSet/" + this.text_id + "/" + this.template_id)
        .then(({ data }) => {
          if (data.code === 200) {
            this.complete_loading = false;
            this.$success("重置成功");

            this.jsonData.labels = [];
            this.annotator.remove();
            this.annotator = this.createAnnotator();
            this.updateJSON();
          } else {
            this.$warning(data.msg);
            this.complete_loading = false;
          }
        });
    },
    set_complete: function () {
      this.$http
        .get("/text/saveTaskById/" + this.text_id + "/")
        .then(({ data }) => {
          if (data.code === 200) {
            this.complete_loading = false;
            this.$success("文本标注完成");
            this.$router.push("/").catch((_) => {});
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
          this.$router.push("/").catch((_) => {});
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
      this.text_id = this.$route.query.text_id;
      this.template_id = this.$route.query.template_id;
      this.done_state = this.$route.query.done_state;
      this.doneState = this.$route.query.done_state;
    },
    getTask: function () {
      this.$http
        .get("/text/getTaskById/" + this.text_id + "/" + this.template_id + "/")
        .then(({ data }) => {
          if (data.code === 200) {
            this.jsonData = {
              labels: data.data.labels,

              connections: data.data.connections,

              labelCategories: data.data.labelCategories,
              connectionCategories: data.data.connectionCategories,
              content: data.data.text.content.replaceAll("\n", "\n\n"),
              update_time: data.data.text.updateTime,
              title: data.data.text.title,
            };
            this.doneState = data.data.text.doneState;
            this.templateId = data.data.text.templateId;
            this.templateTitle = data.data.templateTitle;

            this.labelCategories = data.data.labelCategories;
            this.connectionCategories = data.data.connectionCategories;
            if (this.jsonData !== null && this.jsonData.content) {
              this.annotator = this.createAnnotator();
              this.updateJSON();
            }
          } else {
            this.$warning(data.msg);
          }
        });
    },
  },
  computed: {},
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

  mounted(): void {},
};
</script>

<style>
.code-container-wrapper,
.container-wrapper {
  min-height: calc(100vh - 64px);
  max-height: calc(100vh - 64px);
  overflow: hidden;
  padding: 0 !important;
}

.container-wrapper {
  border-right: solid 2px black;
}

.code-container-wrapper {
  border-left: solid 2px black;
}

.container,
.code-container {
  padding-top: 10px;
  overflow: scroll;
  height: calc(100vh - 64px);
}

code {
  max-width: calc(45vw - 16px);
  background: rgb(32, 32, 32) !important;
  box-shadow: none !important;
  padding: 8px !important;
  padding-right: 40px !important;
  margin-bottom: 20px;
}
</style>
<style>
.container > svg {
  width: 45vw;
}

.poplar-annotation-label {
  font-size: 14px;
}

.poplar-annotation-connection {
  font-family: "PingFang SC", serif;
  font-size: 12px;
}
</style>
