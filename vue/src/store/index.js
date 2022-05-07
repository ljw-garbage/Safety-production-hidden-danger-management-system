import { createStore } from 'vuex'
import request from "../api/axios.config";

export default createStore({
  state: {
    // DetailData:''
    // id:''
    History: '',
  },
  mutations: {
    getHistory(state, newValue){
      state.History = newValue
    },
  },
  actions: {
    getHistory(context) {
        // 请求
        request.get('/report').then(res=>{
          // 修改getAData的值
          context.commit('getHistory',res)
        })
    }
  },
  modules: {
  }
})
