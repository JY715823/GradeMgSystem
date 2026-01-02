<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { adminOfferings, adminCourses, adminTeachers } from "@/api/admin";
import type { Offering } from "@/types/offering";
import type { Course } from "@/types/course";
import type { Teacher } from "@/types/teacher";
import { ElMessage, ElMessageBox } from "element-plus";

const list = ref<Offering[]>([]);
const courses = ref<Course[]>([]);
const teachers = ref<Teacher[]>([]);
const loading = ref(false);
const dialogVisible = ref(false);
const editingId = ref<number | null>(null);
const form = reactive<any>({
  term: "",
  courseId: undefined,
  teacherId: undefined,
  classLabel: "",
  weekday: 1,
  startPeriod: 1,
  endPeriod: 2,
  startWeek: 1,
  endWeek: 16,
  location: "",
  capacity: 60,
});

const load = async () => {
  loading.value = true;
  try {
    [list.value, courses.value, teachers.value] = await Promise.all([
      adminOfferings.list(),
      adminCourses.list(),
      adminTeachers.list(),
    ]);
  } finally {
    loading.value = false;
  }
};

onMounted(load);

const openCreate = () => {
  editingId.value = null;
  Object.assign(form, {
    term: "",
    courseId: undefined,
    teacherId: undefined,
    classLabel: "",
    weekday: 1,
    startPeriod: 1,
    endPeriod: 2,
    startWeek: 1,
    endWeek: 16,
    location: "",
    capacity: 60,
  });
  dialogVisible.value = true;
};

const openEdit = (row: any) => {
  editingId.value = row.id;
  Object.assign(form, {
    term: row.term,
    courseId: row.course?.id,
    teacherId: row.teacher?.id,
    classLabel: row.classLabel,
    weekday: row.weekday,
    startPeriod: row.startPeriod,
    endPeriod: row.endPeriod,
    startWeek: row.startWeek,
    endWeek: row.endWeek,
    location: row.location,
    capacity: row.capacity,
  });
  dialogVisible.value = true;
};

const save = async () => {
  const payload = { ...form };
  if (editingId.value) {
    await adminOfferings.update(editingId.value, payload);
  } else {
    await adminOfferings.create(payload);
  }
  ElMessage.success("操作成功");
  dialogVisible.value = false;
  await load();
};

const remove = async (row: Offering) => {
  await ElMessageBox.confirm("确认删除该记录？", "提示");
  await adminOfferings.remove(row.id);
  ElMessage.success("操作成功");
  await load();
};
</script>

<template>
  <div class="card">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px">
      <h2 class="page-title">开课管理</h2>
      <el-button type="primary" @click="openCreate">新增</el-button>
    </div>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column label="课程">
        <template #default="{ row }">{{ row.course?.name }}</template>
      </el-table-column>
      <el-table-column label="教师">
        <template #default="{ row }">{{ row.teacher?.name }}</template>
      </el-table-column>
      <el-table-column prop="term" label="学期" />
      <el-table-column label="时间">
        <template #default="{ row }">周{{ row.weekday }} {{ row.startPeriod }}-{{ row.endPeriod }}</template>
      </el-table-column>
      <el-table-column label="周次">
        <template #default="{ row }">{{ row.startWeek }}-{{ row.endWeek }}</template>
      </el-table-column>
      <el-table-column prop="location" label="地点" />
      <el-table-column prop="capacity" label="容量" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑开课' : '新增开课'" width="680px">
      <el-form label-width="120px">
        <el-form-item label="学期">
          <el-input v-model="form.term" />
        </el-form-item>
        <el-form-item label="课程">
          <el-select v-model="form.courseId" placeholder="选择课程" style="width: 100%">
            <el-option v-for="c in courses" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="教师">
          <el-select v-model="form.teacherId" placeholder="选择教师" style="width: 100%">
            <el-option v-for="t in teachers" :key="t.id" :label="t.name" :value="t.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="教学班">
          <el-input v-model="form.classLabel" />
        </el-form-item>
        <el-form-item label="星期">
          <el-input-number v-model="form.weekday" :min="1" :max="7" />
        </el-form-item>
        <el-form-item label="节次范围">
          <el-input-number v-model="form.startPeriod" :min="1" :max="12" />
          <span style="margin: 0 8px">-</span>
          <el-input-number v-model="form.endPeriod" :min="1" :max="12" />
        </el-form-item>
        <el-form-item label="周次范围">
          <el-input-number v-model="form.startWeek" :min="1" :max="20" />
          <span style="margin: 0 8px">-</span>
          <el-input-number v-model="form.endWeek" :min="1" :max="20" />
        </el-form-item>
        <el-form-item label="地点">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="容量">
          <el-input-number v-model="form.capacity" :min="1" :max="500" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
