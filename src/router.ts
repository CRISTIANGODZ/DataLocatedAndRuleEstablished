import Vue from "vue";
import Router from "vue-router";
// import Home from "./views/Home.vue";
// import Annotate from "@/views/Annotate.vue";
// import Labels from "@/views/Labels.vue";
// import Login from "@/views/Login.vue";
// import TaskAssign from "@/views/TaskAssign/TaskAssign.vue";
// import Register from "@/views/Register/Register.vue";

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
    // {
    //   path: "/",
    //   name: "missionHall",
    //   component: () => import("@/views/MissionHall/MissionHall.vue"),
    // },
    // {
    //   path: "/",
    //   name: "home",
    //   component: () => import("@/App.vue"),
    // },
    {
      path: "/",
      name: "home",
      component: () => import("@/views/LoginAndRegister.vue"),
    },
    // {
    //   path: "/annotate",
    //   name: "annotate",
    //   // route level code-splitting
    //   // this generates a separate chunk (about.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: Annotate,
    // },
    // {
    //   path: "/labels",
    //   name: "labels",
    //   // route level code-splitting
    //   // this generates a separate chunk (about.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: Labels,
    // },
    // {
    //   path: "/taskAssign",
    //   name: "taskAssign",
    //   // route level code-splitting
    //   // this generates a separate chunk (about.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: TaskAssign,
    // },
    {
      path: "/login",
      name: "login",
      component: () => import("@/views/LoginAndRegister.vue"),
    },
    {
      path: "/main",
      name: "main",
      component: () => import("@/views/Main.vue"),
      children: [
        {
          path: "/",
          name: "missionHall",
          component: () => import("@/views/MissionHall/MissionHall.vue"),
        },
        {
          path: "/missionHall",
          name: "missionHallr",
          component: () => import("@/views/MissionHall/MissionHall.vue"),
        },
        {
          path: "/missionAnnotate",
          name: "missionAnnotate",
          component: () => import("@/views/MissionHall/MissionAnnotate.vue"),
        },
        {
          path: "/taskAssign",
          name: "taskAssign",
          component: () => import("@/views/TaskAssign/TaskAssign.vue"),
        },
        {
          path: "/labels",
          name: "labels",
          component: () => import("@/views/Labels.vue"),
        },
        {
          path: "/profile",
          name: "profile",
          component: () => import("@/views/Profile/Profile.vue"),
        },
        {
          path: "/personManagement",
          name: "personManagement",
          component: () =>
            import("@/views/PersonManagement/PersonManagement.vue"),
        },
        {
          path: "/permissionManagement",
          name: "permissionManagement",
          component: () =>
            import("@/views/PermissionManagement/PermissionManagement.vue"),
        },
        {
          path: "/modelManagement",
          name: "modelManagement",
          component: () =>
            import("@/views/ModelManagement/ModelManagement.vue"),
        },
      ],
    },
    // {
    //   path: "/register",
    //   name: "register",
    //   component: () => import("@/views/Login/Register.vue"),
    //   // component: Register,
    // },
    // {
    //   path: "/missionHall",
    //   name: "missionHall",
    //   component: () => import("@/views/MissionHall/MissionHall.vue"),
    // },
    // {
    //   path: "/missionAnnotate",
    //   name: "missionAnnotate",
    //   component: () => import("@/views/MissionHall/MissionAnnotate.vue"),
    // },
  ],
});
