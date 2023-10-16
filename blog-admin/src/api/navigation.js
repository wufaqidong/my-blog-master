import request from '@/utils/request'

export function getNavigationList(params) {
    return request({
        url: '/system/navigation/list',
        method: 'get',
        params: params
    })
}
export function save(data) {
    return request({
        url: '/system/navigation/insert',
        method: 'post',
        data
    })
}
export function update(data) {
    return request({
        url: '/system/navigation/update',
        method: 'put',
        data
    })
}
export function deleteBatch(data) {
    return request({
        url: '/system/navigation/deleteBatch',
        method: 'delete',
        data
    })
}