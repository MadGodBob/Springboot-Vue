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
      <el-form :model="form" ref="formRef" :rules="formRules">
        <el-form-item prop="no" label="账号" :label-width="formLabelWidth">
          <el-input v-model="form.no" :style="{width: formValueWidth}" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="name" label="姓名" :label-width="formLabelWidth">
          <el-input v-model="form.name" :style="{width: formValueWidth}" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="sex" label="性别" :label-width="formLabelWidth">
          <el-select v-model="form.sex" :style="{width: formValueWidth}" placeholder="请选择性别">
            <el-option label="男" value="1" />
            <el-option label="女" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item prop="phone" label="手机号" :label-width="formLabelWidth">
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

    <el-table :data="tableData" v-loading="tableLoadingFlag" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="no" label="账号" width="100" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="sex" label="性别" width="100" >
          <template v-slot="slot">
              {{ slot.row.sex === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column prop="operate" label="操作">
          <template v-slot="slot">
            <el-button size="small" type="success">编辑</el-button>
            <el-popconfirm title="确定删除吗" placement="bottom" @confirm="deleteById(slot.row.id)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
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
import {ref} from "vue";

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
      tableLoadingFlag: false,
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
      formRules: {
        no: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        name: { required: true, message: '请输入姓名', trigger: 'blur' },
        sex: { required: true, message: '请选择性别', trigger: 'blur' },
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }, { validator: this.checkFormPhone, trigger: 'blur' }],
      },
      formRef: ref(),
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
    checkFormPhone: (rule, value, callback) => {
      if (!value) return callback(new Error('请输入手机号'))
      const s = String(value)
      if (!/^1\d{10}$/.test(s)) return callback(new Error('手机号格式不正确'))
      callback()
    },
    resetInput(){
      this.name = ""
      this.sex = ""
      this.loadPost()
    },
    async addNew(){ // 表单新增数据回调
      const formEl = this.$refs.formRef
      if (!formEl) return
      const valid = await formEl.validate().catch(() => false)
      if(!valid){
        return
      }
      this.loadFlag = true
      const ret = await this.save()
      if(ret.code == 200){
        this.addNew_FormVisible = false
        this.loadFlag = false
        this.loadPost()
      }
      else{
        ElMessage.error(ret.msg)
        this.loadFlag = false
      }
    },
    async deleteById(id){
      await resquest.get(`/delete?id=${id}`)
      this.loadPost()
    },
    save(){ // User新增数据
      return resquest.post('/save', this.form)
    },
    loadPost(){
      this.tableLoadingFlag = true
      resquest.post('/listPage', {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        data:{
          name: this.name,
          sex: this.sex
        }
      }).then(res => {
        if(res.code == 200){
          this.tableData = res.data
          this.total = res.total
          this.tableLoadingFlag = false
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