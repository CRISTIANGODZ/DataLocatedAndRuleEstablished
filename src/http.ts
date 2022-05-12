import Vue from "vue";
import axios from "axios";
import router from "./router";

var DEBUG = true;
// var BASEURL = "http://127.0.0.1:3004";
// var BASEURL = "http://10.16.109.50:8081";
// var BASEURL = "http://10.16.29.172:8081";
// var BASEURL = "http://localhost:8081";
// var BASEURL = "http://10.16.79.168:8081";
var BASEURL = "http://10.16.107.2:18081";

var http = axios.create({
  baseURL: BASEURL,
  timeout: 1000,
});

http.interceptors.request.use(
  function (request) {
    request.headers["token"] = localStorage.getItem("token");
    return request;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

http.interceptors.response.use(
  function (response) {
    const request = response.config;
    if (DEBUG) {
      console.log(
        ">>>",
        request.method.toUpperCase(),
        request.url,
        request.params,
        "\n   ",
        response.status,
        response.data
      );
    }
    if (response.data.status === -500) {
      router.push("login").catch((_) => {
        console.log("router fail", _);
      });
      return Promise.reject({ msg: "权限异常" });
    }
    return response;
  },
  function (error) {
    if (DEBUG) {
      let { response, config: request } = error;
      if (request) {
        console.log(
          ">>>",
          request.method.toUpperCase(),
          request.url,
          request.params,
          "\n   ",
          response.status,
          response.data
        );
      }
    }
    // Do something with response error
    return Promise.reject(error);
  }
);

Vue.prototype.$http = http;
