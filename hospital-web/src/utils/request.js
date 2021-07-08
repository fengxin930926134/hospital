import axios from 'axios'
import router from '@/router/index.js'
import Vue from 'vue'
import {Button, Message} from 'element-ui'

const request = axios.create({
  baseURL: process.env.API,
  timeout: 100000,
  headers: {
    'Content-Type': "application/json;charset=utf-8"
  }
})


// 添加请求拦截器
request.interceptors.request.use(function (config) {
    // 每次发送请求之前判断是否存在token，如果存在，则统一在http请求的header都加上token
    // 即使本地存在token，也有可能token是过期的，所以在响应拦截器中要对返回状态进行判断
    const token = sessionStorage.getItem('token')
    token && (config.headers.Authorization = token)
    return config
  }, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
})

// 添加响应拦截器
request.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    return response
  }, function (error) {
    // 对响应错误做点什么
    switch (error.response.status) {
        // 401: 未登录
        case 401:
            Message({ message: '未登录', type: 'warning' })
            router.replace({
                path: '/login',
                query: { redirect: router.currentRoute.fullPath }
            });
            break
        // 403 没权限
        // 登录过期对用户进行提示
        // 清除本地token和清空vuex中token对象
        // 跳转登录页面
        case 403:
            Message({ message: '登录过期', type: 'warning' })
            // 清除token
            sessionStorage.removeItem('token')
            // 跳转登录页面，并将要浏览的页面fullPath传过去，登录成功后跳转需要访问的页面
            setTimeout(() => {
                router.replace({
                    path: '/login',
                    query: {
                        redirect: router.currentRoute.fullPath
                    }
                })
            }, 1000);
            break
        // 404请求不存在
        case 404:
            Message({ message: '找不到页面', type: 'warning' })
            router.replace({
                path: '/404',
                query: { redirect: router.currentRoute.fullPath }
            })
            break
        // 其他错误，直接抛出错误提示
        default:
    }
    return Promise.reject(error)
})

//提供
export default request
