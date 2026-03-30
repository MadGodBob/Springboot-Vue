<template>
  <el-container class="layout-container-demo" style="height: 100vh">
    <Aside
      :isCollapse="isCollapse"
      :asideWidth="asideWidth"
      :activeMenu="activeMenu"
      @menuChange="handleMenuChange"
    />

    <el-container>
      <el-header style="text-align: right; font-size: 12px; border-bottom: darkgray 1px solid">
        <Header @doCollapse="doCollapse" />
      </el-header>

      <el-main class="page-main">
        <div v-if="activeMenu === 'home'" class="home-page">
          <div class="page-title">首页</div>
          <el-card shadow="hover">
            <template #header>
              <span>用户信息</span>
            </template>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="ID">{{ userInfo.id || "-" }}</el-descriptions-item>
              <el-descriptions-item label="账号">{{ userInfo.no || "-" }}</el-descriptions-item>
              <el-descriptions-item label="姓名">{{ userInfo.name || "-" }}</el-descriptions-item>
              <el-descriptions-item label="性别">{{ sexText }}</el-descriptions-item>
              <el-descriptions-item label="手机号">{{ userInfo.phone || "-" }}</el-descriptions-item>
              <el-descriptions-item label="角色ID">{{ userInfo.roleId ?? "-" }}</el-descriptions-item>
              <el-descriptions-item label="状态">{{ userInfo.isValid || "-" }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </div>

        <div v-else-if="activeMenu === 'user'">
          <div class="page-title">用户</div>
          <Main />
        </div>

        <el-card v-else shadow="hover" class="placeholder-card">
          <div class="page-title">{{ currentMenuTitle }}</div>
          <div class="placeholder-text">{{ currentMenuTitle }}页面开发中</div>
        </el-card>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Aside from "./Aside.vue";
import Header from "./Header.vue";
import Main from "./Main.vue";

export default {
  name: "Index",
  components: {
    Aside,
    Header,
    Main,
  },
  data() {
    return {
      isCollapse: false,
      asideWidth: "150px",
      activeMenu: "home",
    };
  },
  computed: {
    userInfo() {
      const loginInfo = JSON.parse(localStorage.getItem("userToken") || "{}");
      return loginInfo.user || {};
    },
    sexText() {
      if (Number(this.userInfo.sex) === 1) {
        return "男";
      }
      if (Number(this.userInfo.sex) === 2) {
        return "女";
      }
      return "-";
    },
    currentMenuTitle() {
      const titleMap = {
        goods: "商品",
        setting: "设置",
      };
      return titleMap[this.activeMenu] || "页面";
    },
  },
  methods: {
    doCollapse() {
      this.isCollapse = !this.isCollapse;
      this.asideWidth = this.isCollapse ? "60px" : "150px";
    },
    handleMenuChange(menu) {
      this.activeMenu = menu;
    },
  },
};
</script>

<style scoped>
.page-main {
  background: #f5f7fa;
}

.page-title {
  margin-bottom: 16px;
  font-size: 22px;
  font-weight: 600;
  color: #1f2937;
}

.home-page,
.placeholder-card {
  max-width: 1000px;
}

.placeholder-card {
  min-height: 220px;
}

.placeholder-text {
  color: #4b5563;
  font-size: 16px;
}
</style>
