import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import permission from './modules/permission'
import tagsView from './modules/tagsView'

import getters from './getters'

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        user,
        permission,
        tagsView
    },
    getters
})

export default store
