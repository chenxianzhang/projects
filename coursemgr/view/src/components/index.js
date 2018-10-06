import dragDialog from './dragDialog.vue'

const UserGlobalComponents = {
    install: function (Vue) {
        Vue.component('drag-dialog', dragDialog)
    }
}

export default UserGlobalComponents