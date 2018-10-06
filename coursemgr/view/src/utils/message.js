import { Message } from 'element-ui'

/**
 * 统一封装element-ui Message提示框操作
 */
const Msg = {}

Msg.install = function (Vue, options) {

    Vue.prototype.$msg = {
        success: function (msg, showClose = true) {
            Message({
                message: msg,
                type: 'success',
                showClose: showClose
            })
        },
        warning: function (msg, showClose = true) {
            Message({
                message: msg,
                type: 'warning',
                showClose: showClose
            })
        },
        info: function (msg, showClose = true) {
            Message({
                message: msg,
                type: 'info',
                showClose: showClose
            })
        },
        error: function (msg, showClose = true) {
            Message({
                message: msg,
                type: 'error',
                showClose: showClose
            })
        }
    }
}

export default Msg