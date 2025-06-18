import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:5185/api', // 后端 API 的基础 URL
  timeout: 10000, // 请求超时时间
});

// 定义前端字段到后端字段的映射
const attributeMapping = {
  name: "username", // 将前端的 "name" 映射为后端的 "username"
  email: "email",   // 保持一致
  password: "password" // 保持一致
};

// 请求拦截器
axiosInstance.interceptors.request.use(
  config => {
    if (config.method === 'post' || config.method === 'put') {
      const modifiedData = {};
      // 遍历请求数据并进行映射
      Object.keys(config.data).forEach(key => {
        const mappedKey = attributeMapping[key] || key; // 使用映射，找不到则保持原字段
        modifiedData[mappedKey] = config.data[key];
      });
      config.data = modifiedData; // 替换为映射后的数据
    }

    return config;
  },
  error => Promise.reject(error)
);

// 响应拦截器（如果需要将后端字段映射回前端字段）
axiosInstance.interceptors.response.use(
  response => {
    if (response.data && typeof response.data === 'object') {
      const modifiedResponse = {};
      // 反向映射后端字段为前端字段
      Object.keys(response.data).forEach(key => {
        const originalKey = Object.keys(attributeMapping).find(k => attributeMapping[k] === key) || key;
        modifiedResponse[originalKey] = response.data[key];
      });
      response.data = modifiedResponse; // 替换为映射后的数据
    }
    return response;
  },
  error => {
    return Promise.reject(error);
  }
);

export default axiosInstance;
