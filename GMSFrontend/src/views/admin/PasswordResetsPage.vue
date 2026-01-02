<script setup lang="ts">
import { onMounted, ref } from "vue";
import { passwordResets } from "@/api/admin";
import type { PasswordResetItem } from "@/api/admin";
import { ElMessageBox, ElMessage } from "element-plus";

const list = ref<PasswordResetItem[]>([]);
const loading = ref(false);

const load = async () => {
  loading.value = true;
  try {
    list.value = await passwordResets.list("PENDING");
  } finally {
    loading.value = false;
  }
};

onMounted(load);

const handle = async (row: PasswordResetItem) => {
  await ElMessageBox.prompt("输入新密码", "重置密码", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
  })
    .then(async ({ value }) => {
      await passwordResets.handle(row.id, value);
      ElMessage.success("操作成功");
      await load();
    })
    .catch(() => {});
};
</script>

<template>
  <div class="card">
    <h2 class="page-title">重置申请管理</h2>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="role" label="角色" />
      <el-table-column prop="username" label="账号" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="remark" label="备注" />
      <el-table-column prop="status" label="状态" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="primary" link @click="handle(row)">重置密码</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
