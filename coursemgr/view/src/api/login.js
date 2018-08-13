import request from '@/utils/request'
import global from '../../static/global.config'
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
  return global.BASE_API + "/verificationCode/generate?random=" + rad;
}
