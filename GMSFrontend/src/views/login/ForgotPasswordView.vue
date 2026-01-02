<script setup lang="ts">
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { passwordResetRequest } from "@/api/admin";
import type { UserRole } from "@/types/auth";

const form = reactive({
  role: "STUDENT" as UserRole,
  username: "",
  name: "",
  remark: "",
});
const formRef = ref();

const rules = {
  username: [{ required: true, message: "请输入账号", trigger: "blur" }],
  name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
};

const onSubmit = () => {
  if (!formRef.value) return;
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) return;
    await passwordResetRequest(form);
    ElMessage.success("已提交申请，请联系管理员重置");
  });
};
</script>

<template>
  <div class="login-wrapper">
    <div class="login-card">
      <h2 class="page-title">忘记密码</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="身份">
          <el-radio-group v-model="form.role">
            <el-radio-button label="STUDENT">学生</el-radio-button>
            <el-radio-button label="TEACHER">教师</el-radio-button>
            <el-radio-button label="ADMIN">管理员</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" placeholder="学号/工号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交</el-button>
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
