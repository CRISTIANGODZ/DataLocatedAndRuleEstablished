import Vue from "vue";
import VueSweetalert2 from "vue-sweetalert2";

// If you don't need the styles, do not connect
import "sweetalert2/dist/sweetalert2.min.css";

Vue.use(VueSweetalert2);

const smiles = ["😊", "😃", "😉"];
const warnings = ["🤐", "😅", "😮", "🙃", "🏴‍☠️"];

function getRandomSmile() {
  return " " + smiles[Math.floor(Math.random() * smiles.length)];
}

function getRandomWaring() {
  return " " + warnings[Math.floor(Math.random() * warnings.length)];
}

const warning = function (message) {
  this.$swal({
    toast: true,
    position: "top",
    showConfirmButton: false,
    timer: 3000,
    icon: "warning",
    title: "Warning",
    text: message + getRandomWaring(),
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
    text: message + getRandomSmile(),
  });
};

Vue.prototype.$warning = warning;
Vue.prototype.$success = success;
