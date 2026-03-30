import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import axios from 'axios';
import 'element-plus/dist/index.css'
import App from './App.vue'
import './assets/global.css';
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import router from './route'

const app = createApp(App)

app.use(ElementPlus)
app.use(router)
app.config.globalProperties.$axios = axios
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.mount('#app')