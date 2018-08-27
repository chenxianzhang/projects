import request from '@/utils/request'

/**
 * 随机分组
 * params {courseId:'',memberCnt:''}
 * */
export function randomGroup(params) {
  return request({
    url: '/groupMgr/randGroup',
    method: 'post',
    data: params
  });
}

/**
 * 添加到已有分组
 * params {groupId:'',studentNo:''}
 * */
export function appendStudent2Group(params) {
  return request({
    url: '/groupMgr/appendStudent2Group',
    method: 'post',
    data: params
  });
}

/**
 * 指定分组
 * params AssignGroupModel
 * */
export function assignGroup(params) {
  return request({
    url: '/groupMgr/assignGroup',
    method: 'post',
    data: params
  });
}

/**
 * 更换组长
 * params { groupId:'', studentNo:''}
 * */
export function changeGroupLeader(params) {
  return request({
    url: '/groupMgr/changeGroupLeader',
    method: 'post',
    data: params
  });
}

/**
 * 解散组
 * params { groupId:'', groupNo:''}
 * */
export function relieveGroup(params) {
  return request({
    url: '/groupMgr/relieveGroup',
    method: 'post',
    data: params
  });
}

/**
 * 获取当前课程下的分组信息
 * params courseId
 * */
export function getGroupDetail(params) {
  return request({
    url: '/groupMgr/getGroupDetail',
    method: 'post',
    data: params
  });
}

/**
 * 获取当前课程下我的分组信息
 * params {courseId:'', studentNo:''}
 * */
export function getGroupDetailByStudent(params) {
  return request({
    url: '/groupMgr/getGroupDetailByStudent',
    method: 'post',
    data: params
  });
}

/**
 * 获取当前课程的所有成绩
 * params courseId
 * */
export function getAllGradeInfo(params) {
  return request({
    url: '/groupMgr/getAllGradeInfo',
    method: 'post',
    data: params
  });
}
