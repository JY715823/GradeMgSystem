import http from "./http";
import type { Student } from "@/types/student";
import type { Teacher } from "@/types/teacher";
import type { Course } from "@/types/course";
import type { Offering } from "@/types/offering";

export const adminStudents = {
  list: () => http.get<Student[]>("/admin/students"),
  create: (payload: Partial<Student>) => http.post<Student>("/admin/students", payload),
  update: (id: number, payload: Partial<Student>) => http.put<Student>(`/admin/students/${id}`, payload),
  remove: (id: number) => http.delete<void>(`/admin/students/${id}`),
  resetPassword: (id: number) => http.post<void>(`/admin/students/${id}/reset-password`),
};

export const adminTeachers = {
  list: () => http.get<Teacher[]>("/admin/teachers"),
  create: (payload: Partial<Teacher>) => http.post<Teacher>("/admin/teachers", payload),
  update: (id: number, payload: Partial<Teacher>) => http.put<Teacher>(`/admin/teachers/${id}`, payload),
  remove: (id: number) => http.delete<void>(`/admin/teachers/${id}`),
  resetPassword: (id: number) => http.post<void>(`/admin/teachers/${id}/reset-password`),
};

export const adminCourses = {
  list: () => http.get<Course[]>("/admin/courses"),
  create: (payload: Partial<Course>) => http.post<Course>("/admin/courses", payload),
  update: (id: number, payload: Partial<Course>) => http.put<Course>(`/admin/courses/${id}`, payload),
  remove: (id: number) => http.delete<void>(`/admin/courses/${id}`),
};

export const adminOfferings = {
  list: () => http.get<Offering[]>("/admin/offerings"),
  create: (payload: Partial<Offering> & { courseId: number; teacherId: number; term: string }) =>
    http.post<Offering>("/admin/offerings", payload),
  update: (id: number, payload: Partial<Offering> & { courseId: number; teacherId: number; term: string }) =>
    http.put<Offering>(`/admin/offerings/${id}`, payload),
  remove: (id: number) => http.delete<void>(`/admin/offerings/${id}`),
};

export const adminEnrollments = {
  list: (offeringId: number) => http.get<any[]>(`/admin/offerings/${offeringId}/enrollments`),
  remove: (offeringId: number, studentId: number) =>
    http.delete<void>(`/admin/offerings/${offeringId}/enrollments/${studentId}`),
};

export interface PasswordResetItem {
  id: number;
  role: string;
  username: string;
  name: string;
  remark?: string;
  status: string;
  createdAt: string;
}

export const passwordResets = {
  list: (status = "PENDING") => http.get<PasswordResetItem[]>("/admin/password-resets", { params: { status } }),
  handle: (id: number) => http.post<void>(`/admin/password-resets/${id}/handle`),
};

export const passwordResetRequest = (payload: { role: string; username: string; name: string; remark?: string }) =>
  http.post<void>("/password-resets/request", payload);
