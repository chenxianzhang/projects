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