import router, { dynamicRoutes, constantRoutes } from '@/router'
import {getRoutes, getWhiteRoutes} from '@/api/system/role'
import Layout from '@/layout/index'
import store from '@/store'
import auth from '@/utils/auth'

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        // ParentView组件未找到，使用默认处理
        route.component = Layout
      } else if (route.component === 'InnerLink') {
        // InnerLink组件未找到，使用默认处理
        route.component = Layout
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach(el => {
    el.path = lastRouter ? lastRouter.path + '/' + el.path : el.path
    if (el.children && el.children.length && el.component === 'ParentView') {
      children = children.concat(filterChildren(el.children, el))
    } else {
      children.push(el)
    }
  })
  return children
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes) {
  const res = []
  routes.forEach(route => {
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        res.push(route)
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        res.push(route)
      }
    }
  })
  return res
}

function generateWhiteUrls(routes, whitelistPaths = []) {
  routes.forEach(route => {
    // 将其路径添加到白名单
    if (route.front && route.path) {
      // 确保路径以/开头
      const fullPath = route.path.startsWith('/') ? route.path : '/' + route.path
      whitelistPaths.push(fullPath)

      store.dispatch('settings/setWhiteList', fullPath)
    }

    // 递归处理子路由
    if (route.children && route.children.length) {
      generateWhiteUrls(route.children, whitelistPaths)
    }
  })

  return whitelistPaths
}

const state = {
  routes: [],
  addRoutes: [],
  defaultRoutes: [],
  topbarRouters: [],
  sidebarRouters: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  },
  SET_DEFAULT_ROUTES: (state, routes) => {
    state.defaultRoutes = constantRoutes.concat(routes)
  },
  SET_TOPBAR_ROUTES: (state, routes) => {
    state.topbarRouters = routes
  },
  SET_SIDEBAR_ROUTERS: (state, routes) => {
    const filterFrontRouters = routes.filter(route => {
      if (route.front) {
        return false
      }
      if (route.children && route.children.length > 0) {
        route.children = route.children.filter(child => child.menType !== 4)
      }
      return true
    })
    console.log(filterFrontRouters)
    state.sidebarRouters = filterFrontRouters;
  }
}

const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      getRoutes().then(res => {
        const sdata = JSON.parse(JSON.stringify(res.data))
        const rdata = JSON.parse(JSON.stringify(res.data))
        const sidebarRoutes = filterAsyncRouter(sdata)
        const rewriteRoutes = filterAsyncRouter(rdata, false, true)
        const asyncRoutes = filterDynamicRoutes(dynamicRoutes);
        rewriteRoutes.push({ path: '*', redirect: '/404', hidden: true })
        router.addRoutes(asyncRoutes);
        commit('SET_ROUTES', rewriteRoutes)
        commit('SET_SIDEBAR_ROUTERS', constantRoutes.concat(sidebarRoutes))
        commit('SET_DEFAULT_ROUTES', sidebarRoutes)
        commit('SET_TOPBAR_ROUTES', sidebarRoutes)
        resolve(rewriteRoutes)
      })
    })
  },
  generateWhiteRoutes({ commit }, baseWhiteList) {
    return new Promise(resolve => {
      getWhiteRoutes().then(res => {
        const rdata = JSON.parse(JSON.stringify(res.data))
        const rewriteRoutes = filterAsyncRouter(rdata, false, true)
        const asyncRoutes = filterDynamicRoutes(dynamicRoutes)
        rewriteRoutes.push({path: '*', redirect: '/404', hidden: true})
        commit('SET_ROUTES', rewriteRoutes)
        router.addRoutes(asyncRoutes)

        baseWhiteList.forEach(item => {
          store.dispatch('settings/setWhiteList', item)
        })
        generateWhiteUrls([...baseWhiteList, ...res.data])
        resolve(rewriteRoutes)
      })
    })
  }
}

export const loadView = (view) => {
  if (process.env.NODE_ENV === 'development') {
    return (resolve) => require([`@/views/${view}`], resolve)
  } else {
    // 使用 import 实现生产环境的路由懒加载
    return () => import(`@/views/${view}`)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
