<script setup lang="ts">
import { reactive, ref } from "vue";
import { useAuthStore } from "@/stores/auth";
import type { LoginRequest } from "@/types/auth";
import { ElMessage } from "element-plus";
import { passwordResetRequest } from "@/api/admin";

const authStore = useAuthStore();
const loading = ref(false);
const form = reactive<LoginRequest>({
  role: "STUDENT",
  username: "",
  password: "",
});

const rules = {
  username: [{ required: true, message: "请输入账号", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
};

const onSubmit = async (formEl: any) => {
  if (!formEl) return;
  await formEl.validate(async (valid: boolean) => {
    if (!valid) return;
    loading.value = true;
    try {
      await authStore.login(form);
    } finally {
      loading.value = false;
    }
  });
};

const onForgot = async () => {
  if (!form.username) {
    ElMessage.warning("请先填写账号再提交申请");
    return;
  }
  await passwordResetRequest({
    role: form.role,
    username: form.username,
    name: form.username,
  });
  ElMessage.success("已提交申请，请联系管理员重置");
};
</script>

<template>
  <div class="login-wrapper">
    <div class="login-card">
      <h2 class="page-title">登录</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="身份">
          <el-radio-group v-model="form.role">
            <el-radio-button label="STUDENT">学生</el-radio-button>
            <el-radio-button label="TEACHER">教师</el-radio-button>
            <el-radio-button label="ADMIN">管理员</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" placeholder="学号/工号/管理员账号" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="onSubmit($refs.formRef)">登录</el-button>
          <el-button type="default" link @click="onForgot">忘记密码</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login-wrapper {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
}
.login-card {
  width: 420px;
  background: #fff;
  padding: 32px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}
</style>
