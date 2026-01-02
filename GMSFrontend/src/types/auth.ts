export type UserRole = "STUDENT" | "TEACHER" | "ADMIN";

export interface LoginRequest {
  role: UserRole;
  username: string;
  password: string;
}

export interface LoginResult {
  token: string;
  userId: number;
  role: UserRole;
  username: string;
}
