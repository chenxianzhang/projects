import request from '@/utils/request'

/**
 * 获取所有成绩--教师
 * params {courseId:''}
 * */
export function getAllGradeInfo(params) {
  return request({
    url: '/course/getAllGradeInfo',
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

export function download(params) {
   return request({
    url: '/course/exportCourseGrade',
    method:'post',
    data: params
   })
}
