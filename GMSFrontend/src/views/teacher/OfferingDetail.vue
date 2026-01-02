<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { fetchOfferingStudents, saveDraftGrades, publishGrades } from "@/api/teacher";
import type { OfferingStudent } from "@/api/teacher";
import { ElMessage, ElMessageBox } from "element-plus";

const route = useRoute();
const offeringId = Number(route.params.offeringId);

const students = ref<OfferingStudent[]>([]);
const loading = ref(false);

const load = async () => {
  loading.value = true;
  try {
    students.value = await fetchOfferingStudents(offeringId);
  } finally {
    loading.value = false;
  }
};

onMounted(load);

const saveDraft = async () => {
  await saveDraftGrades(
    offeringId,
    students.value.map((s) => ({ studentId: s.studentId, score: Number(s.score || 0) }))
  );
  ElMessage.success("已保存草稿");
};

const publish = async () => {
  await ElMessageBox.confirm("确认发布成绩？发布后学生可查看。", "提示");
  await publishGrades(offeringId);
  ElMessage.success("已发布，学生可查看");
  await load();
};
</script>

<template>
  <div class="card">
    <h2 class="page-title">课程详情</h2>
    <el-tabs>
      <el-tab-pane label="学生名单">
        <el-table :data="students" stripe v-loading="loading">
          <el-table-column prop="studentNo" label="学号" />
          <el-table-column prop="name" label="姓名" />
          <el-table-column prop="className" label="班级" />
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="成绩管理">
        <div style="margin-bottom: 12px; display: flex; gap: 12px">
          <el-button type="primary" @click="saveDraft">保存草稿</el-button>
          <el-button type="danger" @click="publish">发布成绩</el-button>
        </div>
        <el-table :data="students" stripe v-loading="loading">
          <el-table-column prop="studentNo" label="学号" />
          <el-table-column prop="name" label="姓名" />
          <el-table-column label="分数">
            <template #default="{ row }">
              <el-input-number v-model="row.score" :min="0" :max="100" />
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
