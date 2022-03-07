import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const loginStore = {
    namespaced: true,
    state: {
        user: null,
        toPath: ''
    },
    mutations: {
        Login(state, payload) {
            state.user = payload;
        },
        Logout(state) {
            state.user = null;
        },
        setPath(state, payload) {
            state.toPath = payload;
        }
    },
    actions: {
        Login(context, payload) {
            return context.commit('Login', payload);
        }
    },
    getters: {
        getLogin: function (state) {
            return state.user;
        },
        getPath(state) {
            return state.toPath;
        }
    }
}

export default loginStore;
