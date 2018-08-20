import request from '@/utils/request'

export function getTeacherCourseList(params) {
  return request({
     url: '/course/selectTeacherCourse',
     method: 'post',
     data: params
  });
}

export function saveCourse(params) {
  return request({
    url: '/course/saveCourse',
    method: 'post',
    data: params
  });
}

export function getStuCourseList(params) {
  return request({
       url: '/course/selectStuCourse',
       method: 'post',
       data: params
    });
}

