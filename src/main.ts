import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import "./http";
import vuetify from "./plugins/vuetify";
import "roboto-fontface/css/roboto/roboto-fontface.css";
import "@mdi/font/css/materialdesignicons.css";
import i18n from "./i18n";
import vcolorpicker from "vcolorpicker";
Vue.use(vcolorpicker);
import "./alert";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

Vue.use(ElementUI);

router.beforeEach((to, from, next) => {
  var token = localStorage.getItem("token");
  //如果没登录,都导向登录页
  if (!token && to.path !== "/register") {
    if (to.path !== "/login") {
      next({ path: "/login" });
    } else {
      next();
    }
  } else {
    next();
  }
});

Vue.config.productionTip = false;
Vue.prototype.$eventbus = new Vue();
new Vue({
  router,

  // @ts-ignore
  vuetify,

  i18n,
  render: (h) => h(App),
}).$mount("#app");
