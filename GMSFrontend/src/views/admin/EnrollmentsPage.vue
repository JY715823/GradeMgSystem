<script setup lang="ts">
import { onMounted, ref } from "vue";
import { adminOfferings, adminEnrollments } from "@/api/admin";
import type { Offering } from "@/types/offering";
import { ElMessageBox, ElMessage } from "element-plus";

const offerings = ref<Offering[]>([]);
const selectedOffering = ref<number | null>(null);
const enrollments = ref<any[]>([]);
const loading = ref(false);

const loadOfferings = async () => {
  offerings.value = await adminOfferings.list();
};

const loadEnrollments = async () => {
  if (!selectedOffering.value) return;
  loading.value = true;
  try {
    enrollments.value = await adminEnrollments.list(selectedOffering.value);
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  await loadOfferings();
});

const removeEnrollment = async (row: any) => {
  if (!selectedOffering.value) return;
  await ElMessageBox.confirm("确认移除该学生选课？", "提示");
  await adminEnrollments.remove(selectedOffering.value, row.student.id);
  ElMessage.success("操作成功");
  await loadEnrollments();
};
</script>

<template>
  <div class="card">
    <h2 class="page-title">选课信息</h2>
    <div style="margin-bottom: 12px; display: flex; gap: 12px">
      <el-select
        v-model="selectedOffering"
        placeholder="选择开课"
        style="width: 360px"
        @change="loadEnrollments"
      >
        <el-option
          v-for="o in offerings"
          :key="o.id"
          :label="`${o.term} ${o.course?.name || ''}`"
          :value="o.id"
        />
      </el-select>
      <el-button type="primary" @click="loadEnrollments">刷新</el-button>
    </div>
    <el-table :data="enrollments" stripe v-loading="loading">
      <el-table-column label="学号" prop="student.studentNo" />
      <el-table-column label="姓名">
        <template #default="{ row }">{{ row.student?.name }}</template>
      </el-table-column>
      <el-table-column label="班级">
        <template #default="{ row }">{{ row.student?.className }}</template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="danger" link @click="removeEnrollment(row)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
