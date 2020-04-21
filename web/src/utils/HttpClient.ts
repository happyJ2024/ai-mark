import axios from 'axios'
import { loadProgressBar } from 'axios-progress-bar'
import 'axios-progress-bar/dist/nprogress.css'
import { message } from 'antd'

let MOCK_HTTP_REQUEST: boolean = true
// 自定义判断元素类型JS
function toType(obj) {
	return ({}).toString.call(obj).match(/\s([a-zA-Z]+)/)[1].toLowerCase()
}
// 参数过滤函数
function filterNull(o) {
	Object.keys(o).map(key => {
		if (o[key] === null) {
			delete o[key]
		}
		if (toType(o[key]) === 'string') {
			o[key] = o[key].trim()
		} else if (toType(o[key]) === 'object') {
			o[key] = filterNull(o[key])
		} else if (toType(o[key]) === 'array') {
			o[key] = filterNull(o[key])
		}
	})
	return o
}
/**
 *
 *
 * @param {String} method Ajax请求类型 'POST'|'PUT'|'GET'|'DELETE'
 * @param {String} url 请求地址
 * @param {Object} params  参数
 * @param {Object} config 请求头的设置参数
 * @returns Promise<T>
 */
function apiAxios(method, url, params, config) {
	if (params) {
		params = filterNull(params)
	}
	if (config == null || config === undefined) config = {};

	return new Promise((resolve, reject) => {

		loadProgressBar()
		axios.defaults.headers.common.Authorization = localStorage.getItem('accessToken');


		axios({
			method,
			url,
			data: method === 'POST' || method === 'PUT' ? params : null,
			params: method === 'GET' || method === 'DELETE' || method === 'PATCH' ? params : null,
			withCredentials: false,
			...config
		})
			.then((res) => {
				if (res.data.msg === 'Token已过期') {
					message.error('Token已过期')
					window.location.href = '/#/login'
				}
				if (res.status === 200) {
					resolve(res)
				} else {
					reject('Axios返回状态不对，查看请求处理过程．．．．')
				}
			}, err => {

				const errCode = err.response.status
				const msg = err.response.message
				if (msg === 'Token已过期') {
					message.error('身份过期，请重新登录')
					window.location.href = '#/login'
				}
				switch (errCode) {
					case 400:
						console.log('错误请求')
						break
					case 401:
						// 权限处理 重新登录 清空token
						message.error('请检查用户名和密码')
						window.location.href = '#/login'
						break
					case 403:
						message.error('身份过期请重新登录', 3)
						window.location.href = '#/login'
						break
					case 404:
						message.error('请求错误,未找到该资源')
						console.log('请求错误,未找到该资源')
						break
					case 405:
						console.log('请求方法未允许')
						break
					case 408:
						console.log('请求超时')
						break
					case 500:
						message.error('服务器端出错')
						console.log('服务器端出错')
						break
					case 501:
						console.log('网络未实现')
						break
					case 502:
						console.log('网络错误')
						break
					case 503:
						console.log('服务不可用')
						break
					case 504:
						console.log('网络超时')
						break
					default:
						message.error('未知错误')
				}
				if (MOCK_HTTP_REQUEST) {
					resolve("MOCK_HTTP_REQUEST");

				} else { reject(err) }
			})
			.catch((err) => {


				const errInfo = { 'err': err.response }
				reject(errInfo)

			})
	})
}
export default {
	get: (url, params) => {
		return apiAxios('GET', url, params, null)
	},
	post: (url, params) => {
		return apiAxios('POST', url, params, null)
	},
	uploadFile: (url, params, ) => {
		// return apiAxios('POST', url, params,
		// 	{
		// 		headers: { 'Content-Type': 'multipart/form-data' }
		// 	})
		return apiAxios('POST', 'https://www.mocky.io/v2/5cc8019d300000980a055e7', params,
			{
				headers: { 'Content-Type': 'multipart/form-data' }
			})

	},
	put: (url, params) => {
		return apiAxios('PUT', url, params, null)
	},
	delete: (url, params) => {
		return apiAxios('DELETE', url, params, null)
	},
	patch: (url, params) => {
		return apiAxios('PATCH', url, params, null)
	}
} 