import axios from 'axios'
import {ElMessage} from "element-plus";

const request = axios.create({
    baseURL: 'http://localhost:8090',
    timeout: 30000
})

// request 拦截器
// 这个拦截器可以在请求发送前对请求进行处理
request.interceptors.request.use(config => {
    // 设置请求头，指定请求内容类型为JSON
    config.headers['Content-Type'] = 'application/json;charset=utf-8';

    // 从localStorage中获取用户token，如果不存在则默认为空对象
    let user = JSON.parse(localStorage.getItem("userToken") || '{}')
    // 将token添加到请求头中，以便后端进行验证
    config.headers['token'] = user.token;

    // 返回处理后的config对象，以便继续请求
    return config;
}, error => {
    // 如果请求发生错误，打印错误信息以便调试
    console.error('request error: ' + error);
    // 返回Promise.reject以通知请求发生错误
    return Promise.reject(error);
});

// response 拦截器
// 这个拦截器可以在接口响应后对结果进行统一处理
request.interceptors.response.use(
    response => {
        // 获取响应数据
        let res = response.data;
        // 兼容处理服务端返回的字符串数据，将其解析为JSON对象
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }
        return res;
    },
    error => {
        ElMessage('response error: ' + error);
        return Promise.reject(error);
    }
)

// 导出配置好的axios实例，以便在其他模块中使用
export default request;