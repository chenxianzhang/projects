import request from '@/utils/request'
//import qs from 'qs'


export function login(data) {
  return request({
    url: '/login/login',
    method: 'post',
    data: data
  });
}

export function getUserInfo(serialNo) {
  return request({
    url: '/login/getUserInfo',
    method: 'post',
    data: { serialNo: serialNo }
  });
}

export function getVerCodeImageUrl() {
  var rad = Math.floor(Math.random() * Math.pow(10, 8));
  return window.global.BASE_API + "/verificationCode/generate?random=" + rad;
}

export function getRoleList() {
  return request({
      url: '/login/getRoleList',
      method: 'get'
    });
}

export function update(data) {
  return request({
      url: '/login/update',
      method: 'post',
      data: data
    });
}

export function findUser(serialNo) {
  return request({
      url: '/login/findUser',
      method: 'post',
      data: { serialNo: serialNo }
    });
}

export function register(params) {
  return request({
      url: '/login/register',
      method: 'post',
      data: params
    });
}
