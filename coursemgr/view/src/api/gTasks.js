import request from '@/utils/request'

export function getMySchedule(params) {
  return request({
    url: '/gradeMgr/getMySchedule',
    method: 'post',
    data: params
  });
}

/**
 * 获取指定学生在指定课程中的所有待办信息
 * params{studentNo:'', courseId:''}
 * return { status:'', data:Schedule[], msg:'' }
 * */
export function getScheduleByStudent(params) {
  return request({
    url: '/gradeMgr/getScheduleByStudent',
    method: 'post',
    data: params
  });
}

/**
 * 移交待办事项
 * params{ courseId:, originStudentNo:, dstStudentNo: }
 * return { status:'', data:'success', msg:'' }
 * */
export function handOverSchedule(params) {
  return request({
    url: '/gradeMgr/handOverSchedule',
    method: 'post',
    data: params
  });
}

export function checkStuTaskFinished(params) {
  return request({
    url: '/taskMgr/checkStuTaskFinished',
    method: 'get',
    params: params
  });
}