<template>
  <div class="toolbar" style="display: flex;line-height: 60px">
    <div style="margin-top: 10px;">
      <el-icon style="font-size: 20px;cursor: pointer" @click="collapse"><Expand /></el-icon>
    </div>
    <div style="flex: 1;text-align: center;font-size: 20px">
      <span>欢迎来到仓库管理系统</span>
    </div>

    <!--下拉框-->
    <el-dropdown @command="handleCommand">
      <span>
        <span class="username">牢大</span>
        <el-icon class="el-icon--right">
          <ArrowDownBold />
        </el-icon>
      </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item command="toUser">个人中心</el-dropdown-item>
          <el-dropdown-item command="loginOut">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script>
import {ArrowDownBold} from "@element-plus/icons-vue";
import { ElMessage } from 'element-plus'

export default {
  name: "Header",
  components: {ArrowDownBold},
  methods:{
    collapse(){
      this.$emit("doCollapse")
    },
    handleCommand(command){
      ElMessage(`${command}`)
}
  }
}
</script>

<script setup>
import {Expand} from "@element-plus/icons-vue";
</script>

<style scoped>

</style>