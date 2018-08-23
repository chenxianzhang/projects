import request from '@/utils/request'

export function getCourseById(params) {
  return request({
    url: '/course/getCourseById',
    method: 'post',
    data: params
  });
}

export function getStudentsByCourseId(params) {
  return request({
    url: '/userMgr/getStudentsByCourseId',
    method: 'post',
    data: params
  });
}

