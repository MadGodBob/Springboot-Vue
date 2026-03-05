```
git branch -M main
git push -u origin main
```

下载nodejs	http://nodejs.cn/download/

css样式	https://www.cainiaojc.com/css-reference/css-reference.html

## 安装Vue脚手架及其它依赖

```
npm install -g @vue/cli
npm install element-plus --save
npm install axios --save
```

进入工作区创建Vue项目(名称内不能有大写字母)，之后上下方向键选择Vue3按回车。完成后测试

```
vue create vuedemo
cd vuedemo
npm run serve
```

将main.js修改为

```
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import axios from 'axios';
import 'element-plus/dist/index.css'
import App from './App.vue'
import './assets/global.css';
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

app.use(ElementPlus)
app.config.globalProperties.$axios = axios
app.mount('#app')
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
```

修改App.vue进行测试

```
<template>
  <div id="app">
    <el-button>Test</el-button>
  </div>
</template>

<script>

export default {
  name: 'App',
  components: {

  }
}
</script>
```

## 搭建页面布局

Element-ui官方组件库

```
https://element-plus.org/zh-CN/component/overview
```

## 跨域

springboot给controller添加@CrossOrigin

添加resquest.js

```
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
```

举例

```
import resquest from "@/utils/resquest";

resquest.get('/list').then(res => {
  console.log(res)
})
```

