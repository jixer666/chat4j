import request from '@/utils/request'

export function getDictPage(data) {
  return request({
    url: '/system/dict/page',
    method: 'get',
    params: data
  })
}

export function updateDict(data) {
  return request({
    url: '/system/dict',
    method: 'put',
    data: data
  })
}

export function addDict(data) {
  return request({
    url: '/system/dict',
    method: 'post',
    data: data
  })
}


export function deleteDict(data) {
  return request({
    url: '/system/dict',
    method: 'delete',
    data: data
  })
}
