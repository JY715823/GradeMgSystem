import { createRouter, createWebHistory } from "vue-router";

import HomeView from "@/views/home/HomeView.vue";
import LoginView from "@/views/login/LoginView.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/login", component: LoginView },
    { path: "/", redirect: "/home" },
    { path: "/home", component: HomeView },
  ],
});

export default router;
