import user from "@/api/user/user";
import { PersonType } from "@/views/PersonManagement/PersonType";

const state = () => ({
  userInfo: {} as PersonType,
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
    console.log('userInfo: ', userInfo);
    const { data } = await user.login(userInfo.ucount, userInfo.password);
    commit("setUserInfo", data.data.userVo);
  },
};

// mutations
const mutations = {
  setUserInfo(state: { userInfo: PersonType }, userInfo: PersonType) {
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
