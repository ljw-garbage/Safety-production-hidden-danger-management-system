import Vue from 'vue'
import Vuex from 'vuex'
import router from '../router'


Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    currentPathName:''
  },
  mutations: {
    setPath(state){
      state.currentPathName=localStorage.getItem("currentPathName")
    },
    logout(){
      localStorage.clear();
      router.push("/login");
    }
  },
  actions: {
  },
  modules: {
  }
})
