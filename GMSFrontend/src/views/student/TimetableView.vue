<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { fetchTimetable } from "@/api/student";
import type { TimetableItem } from "@/types/offering";

const items = ref<TimetableItem[]>([]);
const filters = reactive({
  term: "",
});

const load = async () => {
  if (!filters.term) return;
  items.value = await fetchTimetable(filters.term);
};

onMounted(load);

const weekdays = ["周一", "周二", "周三", "周四", "周五", "周六", "周日"];
</script>

<template>
  <div class="card">
    <h2 class="page-title">课表</h2>
    <div style="margin-bottom: 12px">
      <el-input v-model="filters.term" placeholder="学期" style="width: 240px; margin-right: 12px" />
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
