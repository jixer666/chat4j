import request from '@/utils/request'
import fileRequest from '@/utils/fileRequest'

export function getGenerateTablePage(data) {
  return request({
    url: '/system/generateTable/page',
    method: 'get',
    params: data
  })
}

export function updateGenerateTable(data) {
  return request({
    url: '/system/generateTable',
    method: 'put',
    data: data
  })
}

export function addGenerateTable(data) {
  return request({
    url: '/system/generateTable',
    method: 'post',
    data: data
  })
}


export function deleteGenerateTable(data) {
  return request({
    url: '/system/generateTable',
    method: 'delete',
    data: data
  })
}

export function getGenerateTableDbPage(data) {
  return request({
    url: '/system/generateTable/db/page',
    method: 'get',
    params: data
  })
}

export function importGenerateTable(data) {
  return request({
    url: '/system/generateTable/importTable',
    method: 'post',
    data: data
  })
}

export function previewGenerateTable(genTableId) {
  return request({
    url: '/system/generateTable/preview/' + genTableId,
    method: 'post',
  })
}

export function downloadGenerateTableCode(data) {
  return fileRequest({
    url: '/system/generateTable/download',
    method: 'post',
    data
  })
}

export function getGenerateTableInfo(genTableId) {
  return request({
    url: '/system/generateTable/info/' + genTableId,
    method: 'get',
  })
}
