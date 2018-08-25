import request from '@/utils/request'

export function addStudent(params) {
  return request({
    url: '/userMgr/addStudent',
    method: 'post',
    data: params
  });
}
