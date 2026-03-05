<template>
  <el-container class="layout-container-demo" style="height: 100vh;">
    <Aside :isCollapse="isCollapse" :asideWidth="asideWidth"></Aside>

    <el-container>
      <el-header style="text-align: right; font-size: 12px;border-bottom: darkgray 1px solid;">
        <Header @doCollapse="doCollapse"></Header>
      </el-header>

      <el-main>
        <Main></Main>
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
  data(){
    return {
      isCollapse: false,
      asideWidth: '150px'
    }
  },
  components:{
    Aside, Header, Main
  },
  methods:{
    doCollapse(){
      this.isCollapse = !this.isCollapse
      this.asideWidth = this.isCollapse ? '60px' : '150px'
    }
  }
}
</script>

<script setup>
</script>

<style scoped>
.layout-container-demo .el-header {
  position: relative;
  background-color: var(--el-color-primary-light-7);
  color: var(--el-text-color-primary);
}
.layout-container-demo .el-aside {
  color: var(--el-text-color-primary);
  background: var(--el-color-primary-light-8);
}
.layout-container-demo .el-menu {
  border-right: none;
}
.layout-container-demo .el-main {
  padding: 0px;
}
.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
</style>