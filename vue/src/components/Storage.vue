<template>
  <div>
    <div class="toolbar-row">
      <el-input
        v-model="itemName"
        clearable
        placeholder="请输入物品名称"
        style="width: 200px"
        @change="loadPost"
      />
      <el-input
        v-model="itemId"
        clearable
        placeholder="请输入物品编号"
        style="width: 200px; margin-left: 10px"
        @change="loadPost"
      />
      <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
      <el-button type="danger" @click="resetInput">重置</el-button>
      <el-button type="success" @click="openAddDialog">新增</el-button>
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500">
      <el-form ref="formRef" :model="form" :rules="formRules">
        <el-form-item prop="itemId" label="物品编号" :label-width="formLabelWidth">
          <el-input v-model="form.itemId" :style="{ width: formValueWidth }" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="itemName" label="物品名称" :label-width="formLabelWidth">
          <el-input v-model="form.itemName" :style="{ width: formValueWidth }" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="remark" label="备注" :label-width="formLabelWidth">
          <el-input v-model="form.remark" :style="{ width: formValueWidth }" autocomplete="off" />
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
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="itemId" label="物品编号" width="150" />
      <el-table-column prop="itemName" label="物品名称" width="200" />
      <el-table-column prop="remark" label="备注" />
      <el-table-column prop="operate" label="操作" width="180">
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
  itemId: "",
  itemName: "",
  remark: "",
});

export default {
  name: "Storage",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      itemName: "",
      itemId: "",
      loadFlag: false,
      tableLoadingFlag: false,
      dialogVisible: false,
      isEdit: false,
      form: createEmptyForm(),
      formLabelWidth: "140px",
      formValueWidth: "250px",
    };
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? "编辑物品" : "新增物品";
    },
    formRules() {
      return {
        itemId: [{ required: true, message: "请输入物品编号", trigger: "blur" }],
        itemName: [{ required: true, message: "请输入物品名称", trigger: "blur" }],
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
    resetInput() {
      this.itemName = "";
      this.itemId = "";
      this.pageNum = 1;
      this.loadPost();
    },
    openAddDialog() {
      this.isEdit = false;
      this.dialogVisible = true;
    },
    openEditDialog(row) {
      this.isEdit = true;
      this.form = { ...row };
      this.dialogVisible = true;
    },
    closeDialog() {
      this.dialogVisible = false;
      this.resetForm();
    },
    resetForm() {
      this.form = createEmptyForm();
      this.$refs.formRef?.resetFields();
    },
    async submitForm() {
      const valid = await this.$refs.formRef.validate().catch(() => false);
      if (!valid) return;

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
      await request.get(`/storage/delete?id=${id}`);
      this.loadPost();
    },
    save() {
      return request.post("/storage/save", this.form);
    },
    updateById() {
      return request.post("/storage/updateById", this.form);
    },
    loadPost() {
      this.tableLoadingFlag = true;
      request
        .post("/storage/listPage", {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          data: {
            itemName: this.itemName,
            itemId: this.itemId,
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
