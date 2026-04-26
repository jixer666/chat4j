import request from '@/utils/request'

export function sendRegisterEmail(data) {
  return request({
    url: '/system/sendRegisterEmail',
    method: 'post',
    data
  })
}
