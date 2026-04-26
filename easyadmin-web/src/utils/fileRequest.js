import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 50000, // 下载可能需要更长时间，建议延长超时时间
  responseType: 'blob', // 重要：设置为 blob 用于文件下载
})

// request interceptor
service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      config.headers['Authorization'] = "Bearer " + getToken()
    }
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    // 如果是文件下载响应
    if (response.config.responseType === 'blob') {
      return handleBlobResponse(response)
    }
    return response.data
  },
  error => {
    console.log('err' + error)

    // 处理 blob 类型的错误响应
    if (error.response && error.response.config.responseType === 'blob') {
      return handleBlobError(error)
    }

    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

/**
 * 处理 blob 响应（文件下载）
 */
function handleBlobResponse(response) {
  const blob = response.data
  const contentDisposition = response.headers['content-disposition']

  // 从响应头中获取文件名
  let fileName = 'download'
  if (contentDisposition) {
    // 优先解析 RFC 5987 格式: filename*=UTF-8''
    const rfc5987Match = contentDisposition.match(/filename\*=(?:UTF-8|utf-8)''([^;]+)/i)
    if (rfc5987Match && rfc5987Match[1]) {
      // RFC 5987 格式，直接解码
      fileName = decodeURIComponent(rfc5987Match[1])
    } else {
      // 回退到传统格式: filename="xxx"
      const fileNameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)
      if (fileNameMatch && fileNameMatch[1]) {
        fileName = fileNameMatch[1].replace(/['"]/g, '')
        // 尝试解码（传统格式可能已经编码）
        try {
          fileName = decodeURIComponent(fileName)
        } catch (e) {
          // 如果解码失败，保持原样
          console.warn('文件名解码失败:', fileName)
        }
      }
    }
  }

  // 检查是否是错误响应（JSON格式的错误信息被包装成了blob）
  if (blob.type === 'application/json') {
    return new Promise((resolve, reject) => {
      const reader = new FileReader()
      reader.onload = () => {
        try {
          const errorData = JSON.parse(reader.result)
          Message({
            message: errorData.message || errorData.msg || '下载失败',
            type: 'error',
            duration: 5 * 1000
          })
          reject(new Error(errorData.message || '下载失败'))
        } catch (e) {
          Message({
            message: '下载失败',
            type: 'error',
            duration: 5 * 1000
          })
          reject(new Error('下载失败'))
        }
      }
      reader.readAsText(blob)
    })
  }

  // 创建下载链接并触发下载
  const url = window.URL.createObjectURL(new Blob([blob]))
  const link = document.createElement('a')
  link.href = url
  link.setAttribute('download', fileName)
  document.body.appendChild(link)
  link.click()

  // 清理
  document.body.removeChild(link)
  window.URL.revokeObjectURL(url)

  // 返回成功信息
  return {
    success: true,
    message: '下载成功',
    fileName: fileName
  }
}

/**
 * 处理 blob 类型的错误响应
 */
function handleBlobError(error) {
  if (error.response && error.response.data instanceof Blob) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader()
      reader.onload = () => {
        try {
          const errorData = JSON.parse(reader.result)
          const errorMessage = errorData.message || errorData.msg || '下载失败'
          Message({
            message: errorMessage,
            type: 'error',
            duration: 5 * 1000
          })
          reject(new Error(errorMessage))
        } catch (e) {
          Message({
            message: error.message || '下载失败',
            type: 'error',
            duration: 5 * 1000
          })
          reject(new Error('下载失败'))
        }
      }
      reader.readAsText(error.response.data)
    })
  }

  Message({
    message: error.message || '下载失败',
    type: 'error',
    duration: 5 * 1000
  })
  return Promise.reject(error)
}

/**
 * 下载文件方法
 * @param {string} url 下载地址
 * @param {Object} data 请求参数
 * @param {string} method 请求方法，默认为 POST
 */
export function downloadFile(url, data = {}, method = 'post') {
  return service({
    url,
    method,
    data,
    responseType: 'blob'
  })
}

/**
 * GET 方式下载文件
 * @param {string} url 下载地址
 * @param {Object} params 请求参数
 */
export function downloadFileGet(url, params = {}) {
  return service({
    url,
    method: 'get',
    params,
    responseType: 'blob'
  })
}

export default service
