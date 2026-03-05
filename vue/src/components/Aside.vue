<template>
  <el-aside :width="asideWidth">
    <el-scrollbar>
      <el-menu style="height:100vh" default-active="1"  :collapse="isCollapse" :collapse-transition="false">

        <el-menu-item index="1">
          <el-icon><HomeFilled /></el-icon>
          <template  v-slot:title><span>首页</span></template>
        </el-menu-item>

        <el-menu-item index="2">
          <el-icon><Flag /></el-icon>
          <template  v-slot:title><span>导航一</span></template>
        </el-menu-item>

        <el-menu-item index="3">
          <el-icon><Position /></el-icon>
          <template  v-slot:title><span>导航二</span></template>
        </el-menu-item>

      </el-menu>
    </el-scrollbar>
  </el-aside>
</template>

<script>

import {Flag, HomeFilled, Position} from "@element-plus/icons-vue";

export default {
  name: "Aside",
  props:{
    isCollapse: Boolean,
    asideWidth: String
  },
  components: {Position, Flag, HomeFilled}
}
</script>

<style scoped>

</style>