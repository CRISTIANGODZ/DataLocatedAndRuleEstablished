import Vue from "vue";
import VueSweetalert2 from "vue-sweetalert2";

// If you don't need the styles, do not connect
import "sweetalert2/dist/sweetalert2.min.css";

Vue.use(VueSweetalert2);

const warning = function (message) {
  this.$swal({
    toast: true,
    position: "top",
    showConfirmButton: false,
    timer: 3000,
    icon: "warning",
    title: "Warning",
    text: message,
  });
};

const success = function (message) {
  this.$swal({
    toast: true,
    position: "top",
    showConfirmButton: false,
    timer: 3000,
    icon: "success",
    title: "成功",
    text: message,
  });
};

Vue.prototype.$warning = warning;
Vue.prototype.$success = success;
