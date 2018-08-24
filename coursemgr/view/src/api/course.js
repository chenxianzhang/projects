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

// 通过课程id获取当前课程下所有任务信息courseId
/**
 * 通过课程id获取当前课程下所有任务信息
 * params courseId
 * return List<CourseTasks>
 * */
export function getCourseTasksByCourseId(params) {
  return request({
    url: '/taskMgr/getCourseTasksByCourseId',
    method: 'post',
    data: params
  });
}

export function updateCourse(params) {
  return request({
    url: '/course/updateCourse',
    method: 'post',
    data: params
  });
}



