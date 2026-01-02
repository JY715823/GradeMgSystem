export interface StudentGrade {
  offeringId: number;
  courseName: string;
  teacherName: string;
  score?: number | null;
  status: string;
}

export interface GradeInput {
  studentId: number;
  score: number;
}
