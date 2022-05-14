<template>
  <el-container class="app_main_container">
    <div id="particles">
      <vue-particles
        color="#dedede"
        :particleOpacity="0.7"
        :particlesNumber="100"
        shapeType="circle"
        :particleSize="4"
        linesColor="#dedede"
        :linesWidth="1"
        :lineLinked="true"
        :lineOpacity="0.4"
        :linesDistance="150"
        :moveSpeed="3"
        :hoverEffect="true"
        hoverMode="grab"
        :clickEffect="true"
        clickMode="push"
      >
      </vue-particles>
    </div>
    <el-header>
      <header-vue></header-vue>
    </el-header>
    <el-main class="login_register_main">
      <div class="login_register_container">
        <el-row type="flex" justify="center" class="login_register_row">
          <el-tabs v-model="activeName" @tab-click="handleClick" type="card">
            <el-tab-pane label="登录" name="login">
              <login-vue></login-vue>
            </el-tab-pane>
            <el-tab-pane label="注册" name="register">
              <register-vue
                :hanldeRegisterClick="hanldeRegisterClick"
              ></register-vue>
            </el-tab-pane>
          </el-tabs>
        </el-row>
      </div>
    </el-main>
  </el-container>
</template>

<script lang="ts">
import Vue from "vue";
import LoginVue from "../views/Login/Login.vue";
import RegisterVue from "../views/Login/Register.vue";
import HeaderVue from "../views/Login/Header.vue";
import VueParticles from "vue-particles";
Vue.use(VueParticles);
export default Vue.extend({
  components: {
    LoginVue,
    RegisterVue,
    HeaderVue,
  },
  data() {
    return {
      activeName: "login",
    };
  },
  methods: {
    handleClick(tab, event) {
      // console.log(tab, event);
    },
    hanldeRegisterClick() {
      this.activeName = "login";
      console.log("register");
    },
  },
  mounted() {},
});
</script>
>

<style scoped>
.login_register_container {
  display: flex;
  justify-content: center;
}
.app_main_container {
  padding-top: 100px;
  /* background-image: url("${basepath}/resource/images/loginb1.jpeg");
  background-size: 100%; */
  background-repeat: no-repeat;
}
.login_register_row {
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  padding: 20px;
  width: 500px;
  min-height: 200px;
  border-radius: 7px;
}
.login_register_main {
  /* margin-top: 200px; */
  display: flex;
  flex-direction: column;
  justify-content: center;
}
#particles {
  position: absolute;
  width: 100%;
  height: 100%;
  /* background-color: #b61924;
  background-repeat: no-repeat;
  background-size: cover;
  background-position: 0 0; */
}
</style>
