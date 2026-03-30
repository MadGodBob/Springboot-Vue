import axios from "axios";
import { ElMessage } from "element-plus";

const request = axios.create({
  baseURL: "http://localhost:8090",
  timeout: 30000,
});

request.interceptors.request.use(
  (config) => {
    config.headers["Content-Type"] = "application/json;charset=utf-8";

    const user = JSON.parse(localStorage.getItem("userToken") || "{}");
    config.headers.token = user.token;

    return config;
  },
  (error) => {
    console.error("request error: " + error);
    return Promise.reject(error);
  }
);

request.interceptors.response.use(
  (response) => {
    let res = response.data;
    if (typeof res === "string") {
      res = res ? JSON.parse(res) : res;
    }
    return res;
  },
  (error) => {
    ElMessage.error("response error: " + error);
    return Promise.reject(error);
  }
);

export default request;
