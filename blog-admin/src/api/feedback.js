import request from '@/utils/request'

export function fetchFeedback(params) {
  return request({
    url: '/system/feedback/list',
    method: 'get',
    params: params
  })
}
export function deleteBatchFeedback(data) {
  return request({
    url: '/system/feedback/deleteBatch',
    method: 'delete',
    data
  })
}

export function updateFeedBack(data) {
  return request({
    url: '/system/feedback/update',
    method: 'put',
    data
  })
}

