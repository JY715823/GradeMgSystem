<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { fetchTimetable } from "@/api/student";
import { fetchTerms } from "@/api/common";
import type { TimetableItem } from "@/types/offering";

const items = ref<TimetableItem[]>([]);
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
  items.value = await fetchTimetable(filters.term);
};

onMounted(async () => {
  await loadTerms();
  await load();
});

const weekdays = ["周一", "周二", "周三", "周四", "周五", "周六", "周日"];
</script>

<template>
  <div class="card">
    <h2 class="page-title">课表</h2>
    <div style="margin-bottom: 12px">
      <el-select v-model="filters.term" placeholder="选择学期" style="width: 240px; margin-right: 12px" clearable>
        <el-option v-for="term in termOptions" :key="term" :label="term" :value="term" />
      </el-select>
      <el-button type="primary" @click="load">查询</el-button>
    </div>
    <el-table :data="items" stripe>
      <el-table-column prop="courseName" label="课程" />
      <el-table-column prop="teacherName" label="教师" />
      <el-table-column label="星期">
        <template #default="{ row }">{{ weekdays[row.weekday - 1] }}</template>
      </el-table-column>
      <el-table-column label="节次">
        <template #default="{ row }">{{ row.startPeriod }}-{{ row.endPeriod }}</template>
      </el-table-column>
      <el-table-column label="周次">
        <template #default="{ row }">{{ row.startWeek }}-{{ row.endWeek }}</template>
      </el-table-column>
      <el-table-column prop="location" label="地点" />
    </el-table>
  </div>
</template>
