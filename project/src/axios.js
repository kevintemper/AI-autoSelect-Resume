import axios from 'axios';
import router from './router'; // 如果需要在拦截器中进行路由跳转

// 创建 Axios 实例
const apiClient = axios.create({
  baseURL: 'http://localhost:5185/api', // 替换为你的后端 API 基础 URL
  headers: {
    'Content-Type': 'application/json',
  },
});
// 请求拦截器：自动添加 Token（如果存在）
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token'); // 从 localStorage 获取 Token
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 响应拦截器：处理全局错误，如 401 未授权
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Token 失效或未授权，自动登出并跳转到登录页面
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      router.push({ name: 'Login' });
    }
    return Promise.reject(error);
  }
);

export default apiClient;