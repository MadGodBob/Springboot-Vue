<template>
  <div>
    <el-scrollbar>
        <el-table :data="tableData">
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="no" label="账号" width="100" />
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="sex" label="性别" width="100" />
          <el-table-column prop="phone" label="手机号" width="100" />
          <el-table-column prop="operate" label="操作">
            <el-button size="small" type="success">编辑</el-button>
            <el-button size="small" type="danger">删除</el-button>
          </el-table-column>>
        </el-table>
    </el-scrollbar>
    <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[10, 30, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import resquest from "@/utils/resquest";
import {ElMessage} from "element-plus";

export default {
  name: "Main",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods:{
    handleSizeChange(val) {
      this.pageNum = 1
      this.pageSize = val
      this.loadPost()
    },
    handleCurrentChange(val) {
      this.pageNum = val
      this.loadPost()
    },
    loadPost(){
      resquest.post('/listPage', {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      }).then(res => {
        console.log(res)
        if(res.code == 200){
          this.tableData = res.data
          this.total = res.total
        }
        else {
          ElMessage(res.message)
        }
      })
    }
  },
  beforeMount() {
    // 初始化
    this.loadPost()
  }
}
</script>

<script setup>

</script>

<style scoped>

</style>