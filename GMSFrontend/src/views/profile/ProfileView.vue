<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { fetchProfile, updateProfile, changePassword } from "@/api/profile";
import type { Profile } from "@/types/profile";
import { ElMessage } from "element-plus";

const profile = ref<Profile | null>(null);
const form = reactive({
  name: "",
  classOrDept: "",
  phone: "",
  email: "",
});
const pwdForm = reactive({
  oldPassword: "",
  newPassword: "",
});

const loadProfile = async () => {
  profile.value = await fetchProfile();
  if (profile.value) {
    form.name = profile.value.name;
    form.classOrDept = profile.value.classOrDept || "";
    form.phone = profile.value.phone || "";
    form.email = profile.value.email || "";
  }
};

onMounted(loadProfile);

const saveProfile = async () => {
  await updateProfile(form);
  ElMessage.success("操作成功");
  await loadProfile();
};

const savePassword = async () => {
  if (!pwdForm.oldPassword || !pwdForm.newPassword) {
    ElMessage.warning("请填写完整");
    return;
  }
  await changePassword(pwdForm);
  ElMessage.success("操作成功");
  pwdForm.oldPassword = "";
  pwdForm.newPassword = "";
};
</script>

<template>
  <div class="card">
    <h2 class="page-title">个人中心</h2>
    <el-row :gutter="16">
      <el-col :span="12">
        <div class="card">
          <h3 style="margin: 0 0 12px">基本信息</h3>
          <el-form label-width="120px">
            <el-form-item label="姓名">
              <el-input v-model="form.name" />
            </el-form-item>
            <el-form-item label="班级/院系">
              <el-input v-model="form.classOrDept" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="form.phone" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="form.email" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveProfile">保存</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card">
          <h3 style="margin: 0 0 12px">修改密码</h3>
          <el-form label-width="120px">
            <el-form-item label="原密码">
              <el-input v-model="pwdForm.oldPassword" type="password" />
            </el-form-item>
            <el-form-item label="新密码">
              <el-input v-model="pwdForm.newPassword" type="password" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="savePassword">保存</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
