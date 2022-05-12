import Vue from "vue";
import Router from "vue-router";
import Home from "./views/Home.vue";
import Annotate from "@/views/Annotate.vue";
import Labels from "@/views/Labels.vue";
import Login from "@/views/Login.vue";
import TaskAssign from "@/views/TaskAssign/TaskAssign.vue";

Vue.use(Router);

export default new Router({
  // mode: "history",
  // base: process.env.BASE_URL,
  routes: [
    // {
    //   path: "/",
    //   name: "home",
    //   component: Home,
    // },
    {
      path: "/",
      name: "missionHall",
      component: () => import("@/views/MissionHall/MissionHall.vue"),
    },
    {
      path: "/annotate",
      name: "annotate",
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: Annotate,
    },
    {
      path: "/labels",
      name: "labels",
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: Labels,
    },
    {
      path: "/taskAssign",
      name: "taskAssign",
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: TaskAssign,
    },
    {
      path: "/login",
      name: "login",
      component: Login,
    },
    {
      path: "/missionHall",
      name: "missionHall",
      component: () => import("@/views/MissionHall/MissionHall.vue"),
    },
    {
      path: "/missionAnnotate",
      name: "missionAnnotate",
      component: () => import("@/views/MissionHall/MissionAnnotate.vue"),
    },
    {
      path: "/register",
      name: "register",
      component: () => import("@/views/Register/Register.vue"),
    }
  ],
});
