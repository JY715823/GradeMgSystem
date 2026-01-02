import http from "./http";
import type { Profile, UpdateProfileRequest, ChangePasswordRequest } from "@/types/profile";

export const fetchProfile = () => http.get<Profile>("/profile/me");

export const updateProfile = (payload: UpdateProfileRequest) => http.put<Profile>("/profile/me", payload);

export const changePassword = (payload: ChangePasswordRequest) =>
  http.post<void>("/profile/change-password", payload);
