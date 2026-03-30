<template>
  <div>
    <div class="toolbar-row">
      <el-input
        v-model="name"
        clearable
        placeholder="请输入姓名"
        style="width: 200px"
        @change="loadPost"
      />
      <el-select v-model="sex" placeholder="请选择性别" style="width: 150px; margin-left: 10px">
        <el-option
          v-for="item in sexOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
      <el-button type="danger" @click="resetInput">重置</el-button>
      <el-button type="success" @click="openAddDialog">新增</el-button>
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500">
      <el-form ref="formRef" :model="form" :rules="formRules">
        <el-form-item prop="no" label="账号" :label-width="formLabelWidth">
          <el-input v-model="form.no" :style="{ width: formValueWidth }" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="name" label="姓名" :label-width="formLabelWidth">
          <el-input v-model="form.name" :style="{ width: formValueWidth }" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="sex" label="性别" :label-width="formLabelWidth">
          <el-select
            v-model="form.sex"
            :style="{ width: formValueWidth }"
            placeholder="请选择性别"
          >
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item prop="phone" label="手机号" :label-width="formLabelWidth">
          <el-input v-model="form.phone" :style="{ width: formValueWidth }" autocomplete="off" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeDialog">取消</el-button>
          <el-button type="primary" :loading="loadFlag" @click="submitForm">提交</el-button>
        </div>
      </template>
    </el-dialog>

    <el-table :data="tableData" v-loading="tableLoadingFlag" border>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="no" label="账号" width="100" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="sex" label="性别" width="100">
        <template #default="slot">
          {{ Number(slot.row.sex) === 1 ? "男" : "女" }}
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="140" />
      <el-table-column prop="operate" label="操作">
        <template #default="slot">
          <el-button size="small" type="success" @click="openEditDialog(slot.row)">编辑</el-button>
          <el-popconfirm title="确认删除吗？" placement="bottom" @confirm="deleteById(slot.row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pageNum"
      v-model:page-size="pageSize"
      style="margin-top: 5px"
      :page-sizes="[10, 30, 50]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import request from "@/utils/resquest";
import { ElMessage } from "element-plus";

const createEmptyForm = () => ({
  id: null,
  no: "",
  name: "",
  sex: "",
  phone: "",
});

export default {
  name: "Main",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: "",
      sex: "",
      loadFlag: false,
      tableLoadingFlag: false,
      dialogVisible: false,
      isEdit: false,
      form: createEmptyForm(),
      formLabelWidth: "140px",
      formValueWidth: "250px",
      sexOptions: [
        { value: "1", label: "男" },
        { value: "2", label: "女" },
      ],
    };
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? "编辑用户" : "新增用户";
    },
    formRules() {
      return {
        no: [{ required: true, message: "请输入账号", trigger: "blur" }],
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        sex: [{ required: true, message: "请选择性别", trigger: "change" }],
        phone: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          { validator: this.checkFormPhone, trigger: "blur" },
        ],
      };
    },
  },
  methods: {
    handleSizeChange(val) {
      this.pageNum = 1;
      this.pageSize = val;
      this.loadPost();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.loadPost();
    },
    checkFormPhone(rule, value, callback) {
      if (!value) {
        callback(new Error("请输入手机号"));
        return;
      }
      if (!/^1\d{10}$/.test(String(value))) {
        callback(new Error("手机号格式不正确"));
        return;
      }
      callback();
    },
    resetInput() {
      this.name = "";
      this.sex = "";
      this.loadPost();
    },
    resetForm() {
      this.form = createEmptyForm();
      this.$nextTick(() => {
        this.$refs.formRef?.clearValidate();
      });
    },
    openAddDialog() {
      this.isEdit = false;
      this.dialogVisible = true;
      this.resetForm();
    },
    openEditDialog(row) {
      this.isEdit = true;
      this.dialogVisible = true;
      this.form = {
        id: row.id,
        no: row.no,
        name: row.name,
        sex: Number(row.sex),
        phone: row.phone,
      };
      this.$nextTick(() => {
        this.$refs.formRef?.clearValidate();
      });
    },
    closeDialog() {
      this.dialogVisible = false;
      this.resetForm();
    },
    async submitForm() {
      const formEl = this.$refs.formRef;
      if (!formEl) {
        return;
      }

      const valid = await formEl.validate().catch(() => false);
      if (!valid) {
        return;
      }

      this.loadFlag = true;
      try {
        const ret = this.isEdit ? await this.updateById() : await this.save();
        if (ret === true || ret.code === 200) {
          ElMessage.success(this.isEdit ? "修改成功" : "新增成功");
          this.dialogVisible = false;
          this.resetForm();
          this.loadPost();
          return;
        }
        ElMessage.error(ret.msg || ret.message || (this.isEdit ? "修改失败" : "新增失败"));
      } finally {
        this.loadFlag = false;
      }
    },
    async deleteById(id) {
      await request.get(`/delete?id=${id}`);
      this.loadPost();
    },
    save() {
      return request.post("/save", this.form);
    },
    updateById() {
      return request.post("/updateById", this.form);
    },
    loadPost() {
      this.tableLoadingFlag = true;
      request
        .post("/listPage", {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          data: {
            name: this.name,
            sex: this.sex,
          },
        })
        .then((res) => {
          if (res.code === 200) {
            this.tableData = res.data;
            this.total = res.total;
            return;
          }
          ElMessage.error(res.msg || res.message || "加载失败");
        })
        .finally(() => {
          this.tableLoadingFlag = false;
        });
    },
  },
  beforeMount() {
    this.loadPost();
  },
};
</script>

<style scoped>
.toolbar-row {
  display: flex;
  margin-bottom: 10px;
  gap: 10px;
  align-items: center;
}
</style>
