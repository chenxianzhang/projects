import request from '@/utils/request'

/**
 * 获取所有成绩--教师
 * params {courseId:''}
 * */
export function getAllGradeInfo(params) {
  return request({
    url: '/groupMgr/getAllGradeInfo',
    method: 'post',
    data: params
  });
}

/**
 * 获取我的成绩信息
 * params {courseId:'', studentNo:''}
 * */
export function getStuGradeInfo(params) {
  return request({
    url: '/course/getStuGradeInfo',
    method: 'post',
    data: params
  });
}
