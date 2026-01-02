import axios from "axios";
import type { AxiosRequestConfig } from "axios";
import { useAuthStore } from "@/stores/auth";
import { ElMessage } from "element-plus";

const instance = axios.create({
  baseURL: "/api",
  timeout: 10000,
});

instance.interceptors.request.use((config) => {
  const auth = useAuthStore();
  if (auth.token) {
    config.headers = {
      ...(config.headers || {}),
      Authorization: `Bearer ${auth.token}`,
    } as any;
  }
  return config;
});

instance.interceptors.response.use(
  (res) => {
    const data = res.data;
    if (data && data.code !== undefined && data.code !== 0) {
      ElMessage.error(data.message || "操作失败");
      return Promise.reject(data);
    }
    return data.data;
  },
  (err) => {
    ElMessage.error(err.response?.data?.message || "网络异常，请稍后重试");
    return Promise.reject(err);
  }
);

const request = <T>(config: AxiosRequestConfig) => instance.request<any, T>(config);

const http = {
  get<T>(url: string, config?: AxiosRequestConfig) {
    return request<T>({ ...config, url, method: "GET" });
  },
  post<T>(url: string, data?: any, config?: AxiosRequestConfig) {
    return request<T>({ ...config, url, method: "POST", data });
  },
  put<T>(url: string, data?: any, config?: AxiosRequestConfig) {
    return request<T>({ ...config, url, method: "PUT", data });
  },
  delete<T>(url: string, config?: AxiosRequestConfig) {
    return request<T>({ ...config, url, method: "DELETE" });
  },
};

export default http;
