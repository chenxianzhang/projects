import request from '@/utils/request'

/**
 * 统计课程所有成绩
 * params  { courseId:'', sort:'asc'/'desc'}
 * */
export function statAllGrade(params) {
  return request({
    url: '/stat/statAllGrade',
    method: 'post',
    data: params
  });
}

/**
 * 任务提交/评阅统计
 * params  { courseId:''}
 * */
export function statTaskSubmitReview(params) {
  return request({
    url: '/stat/statTaskSubmitReview',
    method: 'post',
    data: params
  });
}

/**
 * 题型数目统计
 * params  { courseId:''}
 * */
export function statQuestionTypeCnt(params) {
  return request({
    url: '/stat/statQuestionTypeCnt',
    method: 'post',
    data: params
  });
}

/**
 * 学生任务得分统计图
 * params  { courseId:'', studentNo:''}
 * */
export function statStudentTaskScore(params) {
  return request({
    url: '/stat/statStudentTaskScore',
    method: 'post',
    data: params
  });
}

/**
 * 学生综合统计（综合得分/排名/得分率统计）
 * params  { courseId:'', studentNo:''}
 * */
export function statStuSynthesizeInfo(params) {
  return request({
    url: '/stat/statStuSynthesizeInfo',
    method: 'post',
    data: params
  });
}
