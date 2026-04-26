import request from '@/utils/request'

export function getRoutes() {
  return request({
    url: '/system/routes',
    method: 'get'
  })
}

export function getWhiteRoutes() {
  return request({
    url: '/system/white/routes',
    method: 'get'
  })
}

export function getRolePage(data) {
  return request({
    url: '/system/role/page',
    method: 'get',
    params: data
  })
}

export function addRole(data) {
  return request({
    url: '/system/role',
    method: 'post',
    data
  })
}

export function updateRole(data) {
  return request({
    url: `/system/role`,
    method: 'put',
    data
  })
}

export function deleteRole(data) {
  return request({
    url: `/system/role`,
    method: 'delete',
    data: data
  })
}

export function getRoleMenuTree(roleId) {
  return request({
    url: `/system/role/getRoleMenuTree/${roleId}`,
    method: 'get'
  })
}

export function addRoleMenu(data) {
  return request({
    url: '/system/role/saveRoleMenu',
    method: 'post',
    data
  })
}
