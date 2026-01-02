import { defineStore } from "pinia";
import { ElMessage } from "element-plus";
import { login as loginApi } from "@/api/auth";
import type { LoginRequest, LoginResult, UserRole } from "@/types/auth";
import router from "@/router";

interface AuthState extends Partial<LoginResult> {
  name?: string;
}

const TOKEN_KEY = "gms_token";
const ROLE_KEY = "gms_role";
const USERNAME_KEY = "gms_username";

export const useAuthStore = defineStore("auth", {
  state: (): AuthState => ({
    token: localStorage.getItem(TOKEN_KEY) || "",
    role: (localStorage.getItem(ROLE_KEY) as UserRole) || undefined,
    username: localStorage.getItem(USERNAME_KEY) || undefined,
    userId: undefined,
    name: "",
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
  },
  actions: {
    async login(payload: LoginRequest) {
      const res = await loginApi(payload);
      this.token = res.token;
      this.role = res.role;
      this.username = res.username;
      this.userId = res.userId;
      localStorage.setItem(TOKEN_KEY, res.token);
      localStorage.setItem(ROLE_KEY, res.role);
      localStorage.setItem(USERNAME_KEY, res.username);
      ElMessage.success("登录成功");
      await router.push("/home");
    },
    logout() {
      this.token = "";
      this.role = undefined;
      this.username = undefined;
      this.userId = undefined;
      localStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(ROLE_KEY);
      localStorage.removeItem(USERNAME_KEY);
      router.push("/login");
    },
  },
});
