import { login, getUserInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
    state: {
        name: '',
        token: getToken(),
        roles: []
    },
    mutations: {
        SET_TOKEN: (state, token) => {
            state.token = token
        },
        SET_ROLES: (state, roles) => {
            state.roles = roles;
        },
        SET_NAME: (state, name) => {
            state.name = name;
        }
    },
    actions: {
        loginProcess({ commit }, userInfo) {
            return new Promise((resolve, reject) => {
                login(userInfo).then(response => {
                    if (response.status === 1) {
                        commit('SET_TOKEN', response.data.token)
                        setToken(response.data.token)
                        resolve()
                    } else {
                        reject(response.msg);
                    }
                }).catch(err => {
                    reject(err);
                })
            });
        },
        getUser({ commit, state }) {
            return new Promise((resolve, reject) => {
                getUserInfo(state.token).then(response => {
                    if (response.status === 0) {
                        reject(response.msg);
                        return;
                    }
                    const data = response.data
                    if (data.roles && data.roles.length > 0) {
                        commit('SET_ROLES', data.roles)
                    } else {
                        reject('roles must be a non-null array !');
                        return;
                    }
                    commit('SET_NAME', data.name);
                    resolve(response);
                }).catch(err => {
                  reject(err);
                });
            });
        },
        // 前端页面登出
        PageLogOut({ commit }) {
            return new Promise(resolve => {
                commit('SET_TOKEN', '');
                removeToken();
                resolve();
            })
        }
    }
}

export default user
