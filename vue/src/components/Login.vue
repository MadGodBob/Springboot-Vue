<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-title">登录</div>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-position="top"
        @keyup.enter="handleLogin"
      >
        <el-form-item label="账号" prop="no">
          <el-input v-model="form.no" placeholder="请输入账号" clearable />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
            clearable
          />
        </el-form-item>

        <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">
          登录
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ElMessage } from "element-plus";
import request from "@/utils/resquest";

export default {
  name: "Login",
  data() {
    return {
      loading: false,
      form: {
        no: "",
        password: "",
      },
      rules: {
        no: [{ required: true, message: "请输入账号", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
    };
  },
  methods: {
    async handleLogin() {
      const formEl = this.$refs.formRef;
      if (!formEl) {
        return;
      }

      const valid = await formEl.validate().catch(() => false);
      if (!valid) {
        return;
      }

      this.loading = true;
      try {
        const res = await request.post("/login", this.form);
        if (res.code !== 200) {
          ElMessage.error(res.msg || "登录失败");
          return;
        }

        localStorage.setItem("userToken", JSON.stringify(res.data));
        ElMessage.success("登录成功");
        this.$router.push("/index");
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #dbeafe 0%, #eff6ff 45%, #f8fafc 100%);
}

.login-card {
  width: 420px;
  padding: 36px 32px;
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 18px 45px rgba(15, 23, 42, 0.12);
}

.login-title {
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
  text-align: center;
}

.login-btn {
  width: 100%;
  margin-top: 10px;
}
</style>
