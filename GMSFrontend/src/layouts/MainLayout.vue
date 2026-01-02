<script setup lang="ts">
import { computed } from "vue";
import { useRoute, useRouter, RouterView } from "vue-router";
import { ElMenuItem } from "element-plus";
import { ArrowDown } from "@element-plus/icons-vue";
import { useAuthStore } from "@/stores/auth";

const router = useRouter();
const route = useRoute();
const auth = useAuthStore();

const menus = computed(() => {
  const base = [
    { path: "/home", label: "首页" },
    { path: "/profile", label: "个人中心" },
  ];
  if (auth.role === "STUDENT") {
    return [
      ...base,
      { path: "/student/select-courses", label: "选课" },
      { path: "/student/timetable", label: "课表" },
      { path: "/student/grades", label: "成绩查询" },
    ];
  }
  if (auth.role === "TEACHER") {
    return [...base, { path: "/teacher/my-offerings", label: "我的课程" }];
  }
  if (auth.role === "ADMIN") {
    return [
      ...base,
      { path: "/admin/students", label: "学生管理" },
      { path: "/admin/teachers", label: "教师管理" },
      { path: "/admin/courses", label: "课程管理" },
      { path: "/admin/offerings", label: "开课管理" },
      { path: "/admin/enrollments", label: "选课信息" },
      { path: "/admin/password-resets", label: "重置申请" },
    ];
  }
  return base;
});

const handleSelect = (index: string) => {
  router.push(index);
};

const activeMenu = computed(() => route.path);

const avatarName = computed(() => auth.username || "用户");
</script>

<template>
  <el-container style="min-height: 100vh">
    <el-aside width="220px" style="background: #fff; border-right: 1px solid #ebeef5">
      <div style="padding: 16px; font-weight: 600">成绩管理</div>
      <el-menu :default-active="activeMenu" class="el-menu-vertical-demo" @select="handleSelect" router>
        <template v-for="item in menus" :key="item.path">
          <ElMenuItem :index="item.path">{{ item.label }}</ElMenuItem>
        </template>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header
        style="display: flex; justify-content: space-between; align-items: center; background: #fff; border-bottom: 1px solid #ebeef5"
      >
        <div style="font-weight: 600">简易成绩管理系统</div>
        <el-dropdown>
          <span class="el-dropdown-link">
            {{ avatarName }}
            <el-icon class="el-icon--right">
              <ArrowDown />
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/profile')">个人中心</el-dropdown-item>
              <el-dropdown-item divided @click="auth.logout()">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main class="page-container">
        <RouterView />
      </el-main>
    </el-container>
  </el-container>
</template>
