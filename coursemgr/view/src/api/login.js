import request from '@/utils/request'


export function login (params) {
  return request({
    url: '/login/login',
    method: 'post',
    data: params
  });
}
