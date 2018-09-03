import request from '@/utils/request'

export function getMySchedule(params) {
  return request({
    url: '/gradeMgr/getMySchedule',
    method: 'post',
    data: params
  });
}
