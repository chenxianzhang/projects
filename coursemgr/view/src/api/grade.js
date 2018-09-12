import request from '@/utils/request'
import global from '../../static/global.config'

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

/**
 * updateSubjectScore 更新学生主观题分数
 * params SubjectGradeModel
 * */
export function updateSubjectScore(params) {
  return request({
    url: '/gradeMgr/updateSubjectScore',
    method: 'post',
    data: params
  });
}

export function download(courseId, studentNo, isStudent) {
    if (!isStudent) {
      window.location.href = global.BASE_API + "/course/exportCourseGrade?courseId=" + courseId;
    } else {
      window.location.href = global.BASE_API + "/course/exportStuGrade?courseId="
       + courseId + "&studentNo=" + studentNo;
    }

}

export function exportZip(courseId, studentNo, isStudent) {
    if (!isStudent) {
      window.location.href = global.BASE_API + "/course/exportCourseProcess?courseId=" + courseId;
    } else {
//      window.location.href = global.BASE_API + "/course/exportStuGrade?courseId="
//       + courseId + "&studentNo=" + studentNo;
    }

}
