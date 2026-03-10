<template>
  <div>
    <!--模糊查询-->
    <div style="display: flex;margin-bottom: 10px">
      <el-input v-model="name" clearable @change="loadPost" placeholder="请输入名字" style="width: 200px;"></el-input>
      <el-select v-model="sex" placeholder="请选择性别" style="width: 150px;margin-left: 10px">
        <el-option
            v-for="item in sex_option"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
      <el-button type="danger" @click="resetInput">重置</el-button>
      <el-button type="success" @click="addNew_FormVisible = true">新增</el-button>
    </div>

    <!--表单-->
    <el-dialog v-model="addNew_FormVisible" title="新增账号" width="500">
      <el-form :model="form">
        <el-form-item label="账号" :label-width="formLabelWidth">
          <el-input v-model="form.no" :style="{width: formValueWidth}" autocomplete="off" />
        </el-form-item>
        <el-form-item label="姓名" :label-width="formLabelWidth">
          <el-input v-model="form.name" :style="{width: formValueWidth}" autocomplete="off" />
        </el-form-item>
        <el-form-item label="性别" :label-width="formLabelWidth">
          <el-select v-model="form.sex" :style="{width: formValueWidth}" placeholder="请选择性别">
            <el-option label="男" value="1" />
            <el-option label="女" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" :label-width="formLabelWidth">
          <el-input v-model="form.phone" :style="{width: formValueWidth}" autocomplete="off" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="addNew_FormVisible = false">取消</el-button>
          <el-button type="primary" @click="addNew" :loading="loadFlag">提交</el-button>
        </div>
      </template>
    </el-dialog>

    <el-table :data="tableData" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="no" label="账号" width="100" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="sex" label="性别" width="100" >
          <template v-slot="slot">
              {{ slot.row.sex === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="100" />
        <el-table-column prop="operate" label="操作">
            <el-button size="small" type="success">编辑</el-button>
            <el-button size="small" type="danger">删除</el-button>
        </el-table-column>>
    </el-table>
    <el-pagination
        style="margin-top: 5px"
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
      total: 0,
      name: "",
      sex: '',
      loadFlag: false,
      sex_option: [
        {
          value: '1',
          label: '男',
        },
        {
          value: '0',
          label: '女',
        }
      ],
      form: {
        no: '',
        name: '',
        sex: '',
        phone: '',
      },
      addNew_FormVisible: false,
      formLabelWidth: '140px',  // 表单内容宽度
      formValueWidth: '250px',  // 表单输入宽度
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
    resetInput(){
      this.name = ""
      this.sex = ""
      this.loadPost()
    },
    formValidCheck(){
      if(this.form.no == '') return false
      if(this.form.name == '') return false
      if(this.form.sex == '') return false
      if(this.form.phone == '') return false
      return true
    },
    async addNew(){ // 表单新增数据回调
      if(!this.formValidCheck()){
        ElMessage('信息不完整!')
        return
      }
      this.loadFlag = true
      const ret = await this.save()
      if(ret.code == 200){
        this.addNew_FormVisible = false
        this.loadFlag = false
        this.loadPost()
      }
    },
    save(){ // User新增数据
      return resquest.post('/save', this.form)
    },
    loadPost(){
      resquest.post('/listPage', {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        data:{
          name: this.name,
          sex: this.sex
        }
      }).then(res => {
        // console.log(res)
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