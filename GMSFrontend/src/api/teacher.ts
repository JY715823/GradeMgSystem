import http from "./http";
import type { TeacherOffering } from "@/types/offering";
import type { GradeInput } from "@/types/grade";

export interface OfferingStudent {
  studentId: number;
  studentNo: string;
  name: string;
  className: string;
  score?: string | null;
}

export const fetchTeacherOfferings = (term?: string) =>
  http.get<TeacherOffering[]>("/teacher/offerings", { params: { term } });

export const fetchOfferingStudents = (offeringId: number) =>
  http.get<OfferingStudent[]>(`/teacher/offerings/${offeringId}/students`);

export const saveDraftGrades = (offeringId: number, grades: GradeInput[]) =>
  http.put<void>(`/teacher/offerings/${offeringId}/grades/draft`, grades);

export const publishGrades = (offeringId: number) =>
  http.post<void>(`/teacher/offerings/${offeringId}/grades/publish`);
