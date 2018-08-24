const course = {
  state: {
    cId: '',
  },
  mutations: {
    SET_COURSE_ID: (state, cid) => {
      state.cId = cid
    }
  },
  actions: {
    changeCourseId({commit}, cid){
      commit('SET_COURSE_ID', cid);
    }
  },
};

export default course
