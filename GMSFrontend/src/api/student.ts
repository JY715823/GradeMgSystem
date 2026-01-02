import http from "./http";
import type { Offering, TimetableItem } from "@/types/offering";
import type { StudentGrade } from "@/types/grade";

export const fetchOfferings = (term?: string, keyword?: string) =>
  http.get<Offering[]>("/student/offerings", { params: { term, keyword } });

export const enrollCourse = (offeringId: number) =>
  http.post<void>("/student/enrollments", { offeringId });

export const dropCourse = (offeringId: number) =>
  http.delete<void>(`/student/enrollments/${offeringId}`);

export const fetchTimetable = (term: string) => http.get<TimetableItem[]>("/student/timetable", { params: { term } });

export const fetchGrades = (term: string) => http.get<StudentGrade[]>("/student/grades", { params: { term } });
