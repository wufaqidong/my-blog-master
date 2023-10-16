import request from "@/utils/request";

export function getSayList(params) {
  return request({
    url: '/system/say/list',
    method: 'get',
    params: params
  })
}

export function getSayInfo(id) {
  return request({
    url: '/system/say/info',
    method: 'get',
    params: {
      id: id
    }
  })
}
export function updateSay(data) {
  return request({
    url: '/system/say/update',
    method: 'put',
    data
  })
}
export function insertSay(data) {
  return request({
    url: '/system/say/',
    method: 'post',
    data
  })
}
export function deleteSay(data) {
  return request({
    url: '/system/say/delete',
    method: 'delete',
    data
  })
}
