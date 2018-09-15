import request from '@/utils/request'
import global from '../../static/global.config'

export function getCourseById(params) {
  return request({
    url: '/course/getCourseById',
    method: 'post',
    data: params
  });
}
/**
 * 通过课程id获取当前课程下所有学生信息
 * params courseId
 * return List<User>
 * */
export function getStudentsByCourseId(params) {
  return request({
    url: '/userMgr/getStudentsByCourseId',
    method: 'post',
    data: params
  });
}
/**
 * 通过课程id获取当前课程下所有未分组学生信息
 * params courseId
 * return List<User>
 * */
export function getNoGroupStuList(params) {
  return request({
    url: '/userMgr/getNoGroupStuList',
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

export function deleteStudent(params) {
  return request({
    url: '/userMgr/deleteStudent',
    method: 'post',
    data: params
  });
}

export function deleteTask(params) {
  return request({
    url: '/taskMgr/deleteTask',
    method: 'post',
    data: params
  });
}

export function dodwnloadStuTemp() {
  window.location.href = global.BASE_API + "/fileMgr/downloadStuTemplate";
}

export function freedomGroup(params) {
  return request({
    url: '/groupMgr/freedomGroup',
    method: 'get',
    params: params
  });
}

export function getAllStudentsByCourseId(params) {
  return request({
      url: '/userMgr/getAllStudentsByCourseId',
      method: 'get',
      params: params
    });
}

