import request from '@/utils/request'

/**
 * 根据角色获取不同角色下的所有用户
 * @param {*} params 
 */
export function getUserByRole(params) {
    return request({
        url: '/userMgr/getUserByRole',
        method: 'post',
        data: params
    });
}

/**
 * 添加用户
 * @param {*} params 
 */
export function addUser(params) {
    return request({
        url: '/userMgr/addUser',
        method: 'post',
        data: params
    });
}

/**
 * 编辑用户
 * @param {*} params 
 */
export function updateUser(params) {
    return request({
        url: '/userMgr/updateUser',
        method: 'post',
        data: params
    });
}

/**
 * 获取用户关联的课程
 * @param {*} params 
 */
export function getCourseByUser(params) {
    return request({
        url: '/course/getCourseByUser',
        method: 'post',
        data: params
    });
}

/**
 * 重置密码
 * @param {*} params 
 */
export function resetPwd(params) {
    return request({
        url: '/userMgr/resetPwd',
        method: 'get',
        params: params
    });
}

/**
 * 删除用户信息
 * @param {*} params 
 */
export function deleteUser(params) {
    return request({
        url: '/userMgr/deleteUser',
        method: 'post',
        data: params
    });
}