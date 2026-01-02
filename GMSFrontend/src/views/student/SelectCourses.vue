<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { fetchOfferings, enrollCourse, dropCourse } from "@/api/student";
import { fetchTerms } from "@/api/common";
import type { Offering } from "@/types/offering";
import { ElMessageBox, ElMessage } from "element-plus";

const loading = ref(false);
const offerings = ref<Offering[]>([]);
const termOptions = ref<string[]>([]);
const filters = reactive({
  term: "",
  keyword: "",
});

const loadTerms = async () => {
  termOptions.value = await fetchTerms();
  if (!filters.term && termOptions.value.length > 0) {
    filters.term = termOptions.value[0] || "";
  }
};

const load = async () => {
  loading.value = true;
  try {
    offerings.value = await fetchOfferings(filters.term, filters.keyword);
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  await loadTerms();
  await load();
});

const onEnroll = async (offering: Offering) => {
  await enrollCourse(offering.id);
  ElMessage.success("选课成功");
  await load();
};

const onDrop = async (offering: Offering) => {
  await ElMessageBox.confirm("确认退课？", "提示");
  await dropCourse(offering.id);
  ElMessage.success("操作成功");
  await load();
};
</script>

<template>
  <div class="card">
    <h2 class="page-title">选课</h2>
    <div style="margin-bottom: 12px; display: flex; gap: 12px">
      <el-select v-model="filters.term" placeholder="选择学期" style="width: 240px" clearable>
        <el-option label="全部" value="" />
        <el-option v-for="term in termOptions" :key="term" :label="term" :value="term" />
      </el-select>
      <el-input v-model="filters.keyword" placeholder="课程名/教师" style="width: 240px" />
      <el-button type="primary" @click="load">查询</el-button>
    </div>
    <el-table :data="offerings" v-loading="loading" stripe>
      <el-table-column prop="courseName" label="课程" />
      <el-table-column prop="teacherName" label="教师" />
      <el-table-column prop="term" label="学期" />
      <el-table-column label="时间">
        <template #default="{ row }"> 周{{ row.weekday }} {{ row.startPeriod }}-{{ row.endPeriod }} </template>
      </el-table-column>
      <el-table-column label="周次">
        <template #default="{ row }"> {{ row.startWeek }}-{{ row.endWeek }} </template>
      </el-table-column>
      <el-table-column prop="location" label="地点" />
      <el-table-column label="容量">
        <template #default="{ row }"> {{ row.selectedCount }}/{{ row.capacity }} </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button type="primary" link @click="onEnroll(row)">选课</el-button>
          <el-button type="danger" link @click="onDrop(row)">退课</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
