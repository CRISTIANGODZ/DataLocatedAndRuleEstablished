import Vue from "vue";
import axios from "axios";
import router from "./router";

var DEBUG = true;
var LOCAL_URL = "http://10.16.111.242:3004";
// var LOCAL_URL = "http://10.16.22.0:3004";
// var LOCAL_URL = "http://10.16.79.168:3004";
// var BASEURL = "http://10.16.109.50:8081";
// var BASEURL = "http://10.16.22.0:8081";
// var BASEURL = "http://localhost:8081";
// var BASEURL = "http://10.16.79.168:8081";
// var BASEURL = "http://10.16.107.2:18081";
var BASEURL = "http://10.19.0.26:50050";
// var BASEURL = "http://10.16.111.242:8081";


var http = axios.create({
  baseURL: BASEURL,
  timeout: 100000,
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
    console.log(response);
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

var httpl = axios.create({
  baseURL: LOCAL_URL,
  timeout: 1000,
});

httpl.interceptors.request.use(
  function (request) {
    request.headers["token"] = localStorage.getItem("token");
    return request;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

httpl.interceptors.response.use(
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

Vue.prototype.$httpl = httpl;

export { httpl as requestl };
export { http as request };
