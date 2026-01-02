<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { adminStudents } from "@/api/admin";
import type { Student } from "@/types/student";
import { ElMessage, ElMessageBox } from "element-plus";

const list = ref<Student[]>([]);
const loading = ref(false);
const dialogVisible = ref(false);
const editingId = ref<number | null>(null);
const form = reactive<Partial<Student>>({
  studentNo: "",
  name: "",
  className: "",
  phone: "",
  email: "",
});

const load = async () => {
  loading.value = true;
  try {
    list.value = await adminStudents.list();
  } finally {
    loading.value = false;
  }
};

onMounted(load);

const openCreate = () => {
  editingId.value = null;
  Object.assign(form, { studentNo: "", name: "", className: "", phone: "", email: "" });
  dialogVisible.value = true;
};

const openEdit = (row: Student) => {
  editingId.value = row.id;
  Object.assign(form, row);
  dialogVisible.value = true;
};

const save = async () => {
  if (editingId.value) {
    await adminStudents.update(editingId.value, form);
  } else {
    await adminStudents.create(form);
  }
  ElMessage.success("操作成功");
  dialogVisible.value = false;
  await load();
};

const remove = async (row: Student) => {
  await ElMessageBox.confirm("确认删除该记录？", "提示");
  await adminStudents.remove(row.id);
  ElMessage.success("操作成功");
  await load();
};

const resetPwd = async (row: Student) => {
  await ElMessageBox.confirm("确认重置密码？", "提示");
  await adminStudents.resetPassword(row.id);
  ElMessage.success("操作成功");
};
</script>

<template>
  <div class="card">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px">
      <h2 class="page-title">学生管理</h2>
      <el-button type="primary" @click="openCreate">新增</el-button>
    </div>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="studentNo" label="学号" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="className" label="班级" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column label="操作" width="220">
        <template #default="{ row }">
          <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="remove(row)">删除</el-button>
          <el-button type="warning" link @click="resetPwd(row)">重置密码</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑学生' : '新增学生'" width="520px">
      <el-form label-width="120px">
        <el-form-item label="学号">
          <el-input v-model="form.studentNo" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="班级">
          <el-input v-model="form.className" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
