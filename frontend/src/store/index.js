import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate';
import LoginStore from './modules/loginStore';
Vue.use(Vuex)

export default new Vuex.Store({
    state: {},
    mutations: {},
    actions: {},
    modules:{LoginStore},
    plugins: [createPersistedState({
      // 테스트용
      storage: window.localStorage,
      // 
      path:['LoginStore'],
    })]
})
