import axios from 'axios'
import global from '../../static/global.config'

// create an axios instance
const service = axios.create({
  baseURL: global.BASE_API, // api的base_url
  timeout: 600000, // request timeout
  contentType: 'application/json',
  withCredentials:true,      // 解决跨域问题，使用cors后，需添加此配置
})

// request interceptor
service.interceptors.request.use(config => {
  // Do something before request is sent
  return config
}, error => {
  // Do something with request error
  console.log(error) // for debug
  Promise.reject(error)
})

// respone interceptor
service.interceptors.response.use(
  response =>{
    // 拦截返回，去掉其它response信息，只提取用户需要的data数据
    return response.data
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
})

export default service
