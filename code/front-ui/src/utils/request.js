import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import { Loading } from 'element-ui'
import store from '@/store'
const instance = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 60000
})

instance.interceptors.request.use(
  config => {
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

instance.interceptors.response.use(
  response => {
    const res = response.data

    if (res.type === 'application/octet-stream') {
      return response
    }

    if (res.type === 'application/vnd.ms-excel') {
      return response
    }

    if (res.code !== 0) {
      Message({
        message: res.msg || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      if (res.code === 10010002) {
        MessageBox.confirm('登录超时，请重新登录！', '登录提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.msg || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error)
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export function upload(url, file, data) {
  const formData = new FormData()
  formData.append('file', file)

  if (data) {
    Object.keys(data).forEach((key) => {
      formData.append(key, data[key])
    })
  }

  return new Promise((resolve, reject) => {
    const loading = Loading.service({
      text: '正在上传数据...',
      background: 'rgba(0, 0, 0, 0.7)'
    })

    instance.request({
      url: url,
      method: 'post',
      data: formData,
      timeout: 1200000
    }).then(response => {
      console.log(response)
      loading.close()
      resolve(response)
    }).catch(err => {
      loading.close()
      reject(err)
    })
  })
}
