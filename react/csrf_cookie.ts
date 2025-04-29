import axiosInstance from "./axiosInstance";

// Unified Request Function
const request = async <T>(
  method: "get" | "post" | "put",
  endpoint: string,
  data?: any,
  headers: Record<string, string> = {}
): Promise<T> => {
  try {
    const response = await axiosInstance.request<T>({
      method,
      url: endpoint,
      data,
      headers,
      withCredentials: true,
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Public API Methods
export const fetchGet = <T>(endpoint: string, headers = {}) =>
  request<T>("get", endpoint, undefined, headers);

export const fetchPost = <T>(endpoint: string, data = {}, headers = {}) =>
  request<T>("post", endpoint, data, headers);

export const fetchPut = <T>(endpoint: string, data = {}, headers = {}) =>
  request<T>("put", endpoint, data, headers);

// Axios Request Interceptor for CSRF Token
axiosInstance.interceptors.request.use(
  (config) => {
    const xsrfToken: string | undefined = getCookie("XSRF-TOKEN");
    if (xsrfToken) {
      if (!config.headers) {
        config.headers = {};
      }
      config.headers["X-XSRF-TOKEN"] = xsrfToken;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Cookie Reader
function getCookie(name: string): string | undefined {
  const match = document.cookie.match(new RegExp(`(^| )${name}=([^;]+)`));
  return match ? decodeURIComponent(match[2]) : undefined;
}