import http from "./http";
import type { LoginRequest, LoginResult } from "@/types/auth";

export const login = (payload: LoginRequest) => {
  return http.post<LoginResult>("/auth/login", payload);
};
