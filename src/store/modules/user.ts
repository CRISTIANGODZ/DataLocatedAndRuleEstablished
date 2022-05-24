import user from "@/api/user/user";
import { PersonType } from "@/views/PersonManagement/PersonType";
import { LoginJSONData } from "@/views/Login/LoginTypes";

const state = () => ({
  userInfo: {} as LoginJSONData,
});

// getters
const getters = {
  userInfo: (state: { userInfo: PersonType }) => {
    return state.userInfo;
  },
};

// actions
const actions = {
  async login({ commit }: { commit: any }, userInfo: PersonType): Promise<any> {
    console.log("userInfo: ", userInfo);
    const { data } = await user.login(userInfo.ucount, userInfo.password);
    commit("setUserInfo", data.data);
    // localStorage.clear();
    localStorage.setItem("ucount", userInfo.ucount);
    localStorage.setItem("password", userInfo.password);
    localStorage.setItem("userVo", JSON.stringify(data.data.userVo));
    localStorage.setItem("userInfo", JSON.stringify(data.data));
    localStorage.setItem("token", JSON.stringify(data.data.token));
  },

  setUserInfoFromLocal({ commit }: { commit: any }): void {
    const userInfo = JSON.parse(localStorage.getItem("userInfo"));
    const ucount = localStorage.getItem("ucount");
    if (ucount && userInfo) {
      commit("setUserInfo", userInfo);
    }
  },
};

// mutations
const mutations = {
  setUserInfo(state: { userInfo: LoginJSONData }, userInfo: LoginJSONData) {
    state.userInfo = userInfo;
  },
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations,
};
