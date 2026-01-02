<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { fetchTeacherOfferings } from "@/api/teacher";
import type { TeacherOffering } from "@/types/offering";

const router = useRouter();
const list = ref<TeacherOffering[]>([]);
const loading = ref(false);
const filters = reactive({
  term: "",
});

const load = async () => {
  loading.value = true;
  try {
    list.value = await fetchTeacherOfferings(filters.term);
  } finally {
    loading.value = false;
  }
};

onMounted(load);

const goDetail = (id: number) => router.push(`/teacher/offering/${id}`);
</script>

<template>
  <div class="card">
    <h2 class="page-title">我的课程</h2>
    <div style="margin-bottom: 12px">
      <el-input v-model="filters.term" placeholder="学期" style="width: 240px; margin-right: 12px" />
      <el-button type="primary" @click="load">查询</el-button>
    </div>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="courseName" label="课程" />
      <el-table-column prop="classLabel" label="班级" />
      <el-table-column prop="timePlace" label="时间地点" />
      <el-table-column prop="studentCount" label="人数" />
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
          <el-button type="primary" link @click="goDetail(row.id)">进入</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
