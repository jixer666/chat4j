import variables from '@/styles/element-variables.scss'
import defaultSettings from '@/settings'
import router from "@/router";

const { showSettings, tagsView, fixedHeader, sidebarLogo } = defaultSettings

const state = {
  theme: variables.theme,
  showSettings: showSettings,
  tagsView: tagsView,
  fixedHeader: fixedHeader,
  sidebarLogo: sidebarLogo,
  whiteList: [],
  baseWhiteList: ['/admin/login', '/admin/auth-redirect'],
  isWhiteListLoaded: false
}

const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    // eslint-disable-next-line no-prototype-builtins
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  },
  SET_WHITE_LIST: (state, path) => {
    if (!state.whiteList.includes(path)) {
      state.whiteList.push(path)
    }
  },
  SET_WHITE_LIST_LOADED: (state, status) => {
    state.isWhiteListLoaded = status
  }
}

const actions = {
  changeSetting({ commit }, data) {
    commit('CHANGE_SETTING', data)
  },
  setWhiteList({ commit }, path) {
    commit('SET_WHITE_LIST', path)
  },
  getWhiteList: async function ({commit, dispatch, state}) {
    return new Promise(async (resolve) => {
      if (state.isWhiteListLoaded) {
        resolve(state.whiteList)
        return
      }

      try {
        const whiteRoute = await dispatch('permission/generateWhiteRoutes', state.baseWhiteList, { root: true})
        router.addRoutes(whiteRoute)
        commit('SET_WHITE_LIST_LOADED', true)

        resolve(state.whiteList)
      } catch (error) {
        commit('SET_WHITE_LIST_LOADED', true)
        resolve(state.whiteList)
      }
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

