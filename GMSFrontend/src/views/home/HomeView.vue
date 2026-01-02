<script setup lang="ts">
import { ref, onMounted } from "vue";
import { fetchProfile } from "@/api/profile";
import type { Profile } from "@/types/profile";

const profile = ref<Profile | null>(null);

onMounted(async () => {
  profile.value = await fetchProfile();
});

const statCards = [
  { title: "欢迎", value: "角色首页" },
  { title: "快捷入口", value: "按左侧菜单进入" },
];
</script>

<template>
  <div class="card">
    <h2 class="page-title">首页</h2>
    <p v-if="profile">欢迎，{{ profile.name }}（{{ profile.role }}）</p>
    <el-row :gutter="16">
      <el-col v-for="item in statCards" :key="item.title" :span="8">
        <div class="card">
          <div style="font-size: 14px; color: #909399">{{ item.title }}</div>
          <div style="font-size: 20px; margin-top: 8px">{{ item.value }}</div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
