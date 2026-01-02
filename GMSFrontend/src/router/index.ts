import { createRouter, createWebHistory, type NavigationGuardNext, type RouteLocationNormalized } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { pinia } from "@/stores";
import MainLayout from "@/layouts/MainLayout.vue";
import HomeView from "@/views/home/HomeView.vue";
import LoginView from "@/views/login/LoginView.vue";
import ForgotPasswordView from "@/views/login/ForgotPasswordView.vue";
import ProfileView from "@/views/profile/ProfileView.vue";
import StudentSelectCourses from "@/views/student/SelectCourses.vue";
import StudentTimetable from "@/views/student/TimetableView.vue";
import StudentGrades from "@/views/student/GradesView.vue";
import TeacherOfferings from "@/views/teacher/TeacherOfferings.vue";
import TeacherOfferingDetail from "@/views/teacher/OfferingDetail.vue";
import AdminStudents from "@/views/admin/StudentsPage.vue";
import AdminTeachers from "@/views/admin/TeachersPage.vue";
import AdminCourses from "@/views/admin/CoursesPage.vue";
import AdminOfferings from "@/views/admin/OfferingsPage.vue";
import AdminEnrollments from "@/views/admin/EnrollmentsPage.vue";
import AdminPasswordResets from "@/views/admin/PasswordResetsPage.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/login", component: LoginView },
    { path: "/forgot-password", component: ForgotPasswordView },
    {
      path: "/",
      component: MainLayout,
      children: [
        { path: "", redirect: "/home" },
        { path: "home", component: HomeView },
        { path: "profile", component: ProfileView },
        { path: "student/select-courses", component: StudentSelectCourses, meta: { roles: ["STUDENT"] } },
        { path: "student/timetable", component: StudentTimetable, meta: { roles: ["STUDENT"] } },
        { path: "student/grades", component: StudentGrades, meta: { roles: ["STUDENT"] } },
        { path: "teacher/my-offerings", component: TeacherOfferings, meta: { roles: ["TEACHER"] } },
        { path: "teacher/offering/:offeringId", component: TeacherOfferingDetail, meta: { roles: ["TEACHER"] } },
        { path: "admin/students", component: AdminStudents, meta: { roles: ["ADMIN"] } },
        { path: "admin/teachers", component: AdminTeachers, meta: { roles: ["ADMIN"] } },
        { path: "admin/courses", component: AdminCourses, meta: { roles: ["ADMIN"] } },
        { path: "admin/offerings", component: AdminOfferings, meta: { roles: ["ADMIN"] } },
        { path: "admin/enrollments", component: AdminEnrollments, meta: { roles: ["ADMIN"] } },
        { path: "admin/password-resets", component: AdminPasswordResets, meta: { roles: ["ADMIN"] } },
      ],
    },
  ],
});

const publicPaths = ["/login", "/forgot-password"];

router.beforeEach((to: RouteLocationNormalized, _from: RouteLocationNormalized, next: NavigationGuardNext) => {
  const auth = useAuthStore(pinia);
  if (publicPaths.includes(to.path)) {
    next();
    return;
  }
  if (!auth.isLoggedIn) {
    next("/login");
    return;
  }
  const roles = to.meta.roles as string[] | undefined;
  if (roles && auth.role && !roles.includes(auth.role)) {
    next("/home");
    return;
  }
  next();
});

export default router;
