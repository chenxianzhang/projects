
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
