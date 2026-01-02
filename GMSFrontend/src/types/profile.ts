import type { UserRole } from "./auth";

export interface Profile {
  userId: number;
  role: UserRole;
  username: string;
  name: string;
  classOrDept?: string | null;
  phone?: string | null;
  email?: string | null;
}

export interface UpdateProfileRequest {
  name: string;
  classOrDept?: string;
  phone?: string;
  email?: string;
}

export interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
}
