<template>
  <el-aside :width="asideWidth">
    <el-scrollbar>
      <el-menu
        style="height: 100vh"
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        @select="handleSelect"
      >
        <el-menu-item index="home">
          <el-icon><HomeFilled /></el-icon>
          <template #title>
            <span>首页</span>
          </template>
        </el-menu-item>

        <el-menu-item index="user">
          <el-icon><User /></el-icon>
          <template #title>
            <span>用户</span>
          </template>
        </el-menu-item>

        <el-menu-item index="goods">
          <el-icon><Box /></el-icon>
          <template #title>
            <span>商品</span>
          </template>
        </el-menu-item>

        <el-menu-item index="setting">
          <el-icon><Setting /></el-icon>
          <template #title>
            <span>设置</span>
          </template>
        </el-menu-item>
      </el-menu>
    </el-scrollbar>
  </el-aside>
</template>

<script>
import { Box, HomeFilled, Setting, User } from "@element-plus/icons-vue";

export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    asideWidth: String,
    activeMenu: {
      type: String,
      default: "home",
    },
  },
  components: { HomeFilled, User, Box, Setting },
  methods: {
    handleSelect(index) {
      this.$emit("menuChange", index);
    },
  },
};
</script>

<style scoped>
</style>
