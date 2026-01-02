<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { fetchGrades } from "@/api/student";
import { fetchTerms } from "@/api/common";
import type { StudentGrade } from "@/types/grade";

const grades = ref<StudentGrade[]>([]);
const termOptions = ref<string[]>([]);
const filters = reactive({
  term: "",
});

const loadTerms = async () => {
  termOptions.value = await fetchTerms();
  if (!filters.term && termOptions.value.length > 0) {
    filters.term = termOptions.value[0] || "";
  }
};

const load = async () => {
  if (!filters.term) return;
  grades.value = await fetchGrades(filters.term);
};

onMounted(async () => {
  await loadTerms();
  await load();
});
</script>

<template>
  <div class="card">
    <h2 class="page-title">成绩查询</h2>
    <div style="margin-bottom: 12px">
      <el-select v-model="filters.term" placeholder="选择学期" style="width: 240px; margin-right: 12px" clearable>
        <el-option v-for="term in termOptions" :key="term" :label="term" :value="term" />
      </el-select>
      <el-button type="primary" @click="load">查询</el-button>
    </div>
    <el-table :data="grades" stripe>
      <el-table-column prop="courseName" label="课程" />
      <el-table-column prop="teacherName" label="教师" />
      <el-table-column prop="status" label="状态" />
      <el-table-column label="分数">
        <template #default="{ row }">{{ row.score ?? "待发布" }}</template>
      </el-table-column>
    </el-table>
  </div>
</template>
