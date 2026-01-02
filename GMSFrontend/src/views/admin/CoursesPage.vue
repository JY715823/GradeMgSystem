<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { adminCourses } from "@/api/admin";
import type { Course } from "@/types/course";
import { ElMessage, ElMessageBox } from "element-plus";

const list = ref<Course[]>([]);
const loading = ref(false);
const dialogVisible = ref(false);
const editingId = ref<number | null>(null);
const form = reactive<Partial<Course>>({
  courseCode: "",
  name: "",
  credit: undefined,
});

const load = async () => {
  loading.value = true;
  try {
    list.value = await adminCourses.list();
  } finally {
    loading.value = false;
  }
};

onMounted(load);

const openCreate = () => {
  editingId.value = null;
  Object.assign(form, { courseCode: "", name: "", credit: undefined });
  dialogVisible.value = true;
};

const openEdit = (row: Course) => {
  editingId.value = row.id;
  Object.assign(form, row);
  dialogVisible.value = true;
};

const save = async () => {
  if (editingId.value) {
    await adminCourses.update(editingId.value, form);
  } else {
    await adminCourses.create(form);
  }
  ElMessage.success("操作成功");
  dialogVisible.value = false;
  await load();
};

const remove = async (row: Course) => {
  await ElMessageBox.confirm("确认删除该记录？", "提示");
  await adminCourses.remove(row.id);
  ElMessage.success("操作成功");
  await load();
};
</script>

<template>
  <div class="card">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px">
      <h2 class="page-title">课程管理</h2>
      <el-button type="primary" @click="openCreate">新增</el-button>
    </div>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="courseCode" label="课程号" />
      <el-table-column prop="name" label="课程名" />
      <el-table-column prop="credit" label="学分" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑课程' : '新增课程'" width="520px">
      <el-form label-width="120px">
        <el-form-item label="课程号">
          <el-input v-model="form.courseCode" />
        </el-form-item>
        <el-form-item label="课程名">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="学分">
          <el-input-number v-model="form.credit" :min="0" :max="10" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
