
import request from '@/utils/request'

export function saveTask(params) {
  return request({
    url: '/taskMgr/saveTask',
    method: 'post',
    data: params
  });
}
