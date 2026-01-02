export interface Offering {
  id: number;
  term: string;
  courseName: string;
  teacherName: string;
  classLabel?: string | null;
  weekday: number;
  startPeriod: number;
  endPeriod: number;
  startWeek: number;
  endWeek: number;
  location?: string | null;
  capacity: number;
  selectedCount?: number;
  course?: any;
  teacher?: any;
}

export interface TeacherOffering {
  id: number;
  term: string;
  courseName: string;
  classLabel?: string | null;
  timePlace: string;
  studentCount: number;
}

export interface TimetableItem {
  offeringId: number;
  courseName: string;
  teacherName: string;
  weekday: number;
  startPeriod: number;
  endPeriod: number;
  startWeek: number;
  endWeek: number;
  location?: string | null;
}
