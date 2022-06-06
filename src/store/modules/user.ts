import user from "@/api/user/user";
import { PersonType } from "@/views/PersonManagement/PersonType";
import { LoginJSONData, UserInfoType } from "@/views/Login/LoginTypes";

const state = () => ({
  userInfo: {} as UserInfoType,
});

// getters
const getters = {
  userInfo: (state: { userInfo: UserInfoType }) => {
    return state.userInfo;
  },
};

// actions
const actions = {
  async login(
    { commit }: { commit: any },
    userInfo: PersonType
  ): Promise<number> {
    console.log("userInfo: ", userInfo);
    const { data } = await user.login(userInfo.ucount, userInfo.password);
    console.log("llllogin: ", data);
    if (data.code === 200) {
      commit("setUserInfo", data.data);
      // localStorage.clear();
      localStorage.setItem("ucount", userInfo.ucount);
      localStorage.setItem("password", userInfo.password);
      localStorage.setItem("userVo", JSON.stringify(data.data.userVo));
      localStorage.setItem("userInfo", JSON.stringify(data.data));
      localStorage.setItem("token", JSON.stringify(data.data.token));
      return data.code;
    } else {
      return data.code;
    }
  },

  async setUserInfoFromLocal({ commit }: { commit: any }): Promise<number> {
    const userInfo = JSON.parse(localStorage.getItem("userInfo"));
    const ucount = localStorage.getItem("ucount");
    if (ucount && userInfo) {
      commit("setUserInfo", userInfo);
      return Promise.resolve(1);
    } else {
      return Promise.reject(0);
    }
  },
  async updateUserInfo(
    { commit }: { commit: any },
    userInfo: PersonType
  ): Promise<number> {
    const { data } = await user.updateUserInfo(userInfo);
    if (data.code === 200) {
      commit("setSpecificUserInfo", userInfo);
      localStorage.setItem("userInfo", JSON.stringify(userInfo));
      return Promise.resolve(1);
    }
    return Promise.reject(0);
  },
};

// mutations
const mutations = {
  setUserInfo(state: { userInfo: UserInfoType }, userInfo: UserInfoType) {
    state.userInfo = userInfo;
  },
  setSpecificUserInfo(state: { userInfo: UserInfoType }, userInfo: PersonType) {
    state.userInfo.userVo = userInfo;
  },
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations,
};
