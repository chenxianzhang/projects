
import request from '@/utils/request'

/**
 * 获取任务信息， 包含任务下试题内容
 * params {CourseTasks task, List<TaskQuestions> questionList}
 * */
export function saveTask(params) {
  return request({
    url: '/taskMgr/saveTask',
    method: 'post',
    data: params
  });
}

/**
 * 获取任务信息， 包含任务下试题内容
 * params {taskId:''}
 * */
export function getTaskDetailByTaskId(params) {
  return request({
    url: '/taskMgr/getTaskDetailByTaskId',
    method: 'post',
    data: params
  });
}

/**
 * getMyTaskSituation 获取我的任务信息， 包含任务下试题内容
 * params {courseId:'', studentNo:''}
 * */
export function getMyTaskSituation(params) {
  return request({
    url: '/taskMgr/getMyTaskSituation',
    method: 'post',
    data: params
  });
}

/**
 * getCourseTaskSituation 通过课程id获取课程下所有任务的完成情况
 * params courseId
 * */
export function getCourseTaskSituation(params) {
  return request({
    url: '/taskMgr/getCourseTaskSituation',
    method: 'post',
    data: params
  });
}

/**
 * submitTaskPaper 学生答题
 * params StudentPaperAnswer
 * */
export function submitTaskPaper(params) {
  return request({
    url: '/taskMgr/submitTaskPaper',
    method: 'post',
    data: params
  });
}

/**
 * getStuTaskDetail 学生答题
 * params {taskId:''， studentNo:''}
 * */
export function getStuTaskDetail(params) {
  return request({
    url: '/taskMgr/getStuTaskDetail',
    method: 'post',
    data: params
  });
}

/**
 * getTaskSituationList 获取指定任务下所有学生的状态及评分人信息
 * params {taskId:''， courseId:'', pageSize:, currPage:}
 * */
export function getTaskSituationList(params) {
  return request({
    url: '/taskMgr/getTaskSituationList',
    method: 'post',
    data: params
  });
}
