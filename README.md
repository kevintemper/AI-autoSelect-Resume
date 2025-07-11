# 智能简历筛选与就业数据可视化平台

> **AI赋能招聘决策 | 前后端分离 | 可视化洞察 | 多岗位多维度匹配**

---

## 项目简介

本项目是一个集**AI简历自动筛选**与**就业数据可视化**为一体的多端应用，包含：

- **前端（Vue3）**：实现可视化大屏与用户操作界面，支持“AI智能简历筛选”一键跳转。
- **后端（.NET Core Web API）**：负责账号管理、数据服务、统计分析等RESTful接口。
- **AI简历智能筛选模块（Keras+Gradio）**：自动识别岗位需求、OCR解析PDF简历，智能打分、排序与筛选候选人。

### 背景与行业痛点

当前，招聘市场面临**简历个人与企业适配度不高**的严重问题，传统的HR筛选方式由于工作量庞大且效率低，往往依赖于人工判断，识别准确率不足60%。

因此，智能简历筛选可以帮助HR提高筛选效率，根据HR需要的简历数量快速从多份简历中筛选出相应数量的较为适合的简历供HR进行二轮筛选，大幅度减少需要筛选的简历数量，减少人为偏差，提升招聘的准确性与公平性。

---

## 目录结构说明

```text
project-root/
├── back/                # 后端服务（Java 项目）
│   ├── src/             # 后端源代码
│   │   └── main/        # 后端主目录
│   │       └── java/    # Java 源代码
│   │           └── com/
│   │               └── example/
│   │                   └── zwtcampuscareerview/
│   │                       ├── config/               # 配置文件
│   │                       ├── controllers/          # 控制器
│   │                       ├── DTO/                  # 数据传输对象
│   │                       ├── models/               # 模型类
│   │                       ├── repositories/         # 数据库访问
│   │                       ├── services/             # 业务逻辑
│   │                       └── utils/                # 工具类
│   ├── resources/        # 配置资源
│   │   ├── mapper/       # SQL 映射
│   │   │   └── JobTrendMapper.xml
│   │   └── application.yml  # 配置文件
│   └── target/           # 编译后的输出文件
├── project/               # 前端服务（Vue 项目）
│   ├── src/               # 前端源代码
│   └── package.json       # 前端依赖配置文件
├── pythonusing/           # AI 模型与Gradio接口
│   ├── Resume.csv         # 岗位描述与简历结构化数据
│   ├── resume_api.py      # Gradio 智能筛选入口
│   └── resume_model_final.keras # 已训练好模型
└── README.md              # 项目说明文档（当前文件）
```

---

## 环境依赖 & 准备

### 1. Node.js & 前端依赖

- Node.js >= 14.x（推荐最新版）

- 依赖安装：

  ```bash
  cd project
  npm install
  ```

- 主要依赖【可参考project文件夹中的`package.json`】：

  - Vue 3.x, Element-Plus, Axios, ECharts, Vue-Router, Vuex 等
  - 可选：`npm install -g @vue/cli` 安装全局 vue-cli

### 2. Python & AI依赖

- Python >= 3.8

- 推荐使用虚拟环境（venv/conda等）

- 依赖安装：

  ```bash
  pip install -r requirements.txt
  ```

### 3. 后端 (.NET Core)

- .NET 6 或以上（或根据实际项目版本调整）

- 数据库依赖请见 `/back` 内相关文档

- 依赖安装：

  ```bash
  cd back
  dotnet restore
  ```

---

## 启动与部署流程

### 1. 启动后端服务

```bash
cd back
dotnet run
```

默认端口可在项目配置文件中调整，具体接口详见[API帮助文档](sandbox:/mnt/data/api帮助文档.pdf)  

---

### 2. 启动AI智能筛选服务（Gradio）

```bash
cd pythonusing
python resume_api.py
```

- 默认将在 `http://localhost:7860` 启动 Gradio Web 服务。
- 前端“智能简历筛选”按钮会自动跳转到该页面。

---

### 3. 启动前端服务

```bash
cd project
npm run serve
```

- 访问 `http://localhost:8080`
- 适合在 Chrome、Edge 或 Firefox 等现代浏览器中使用

---

## 主要功能简介

### AI智能简历筛选

- **一键上传多份简历PDF或图片**（支持批量），可选择岗位类别，自动识别并智能排序出最优 Top-K 人才。
- **Gradio Web界面**，支持实时交互、评分展示。
- **岗位描述与筛选标准**可灵活切换，无需重复训练。

### 数据可视化门户

- **就业趋势分析**：支持按年度、岗位、地区等多维度可视化。
- **岗位对口性分析**：洞察岗位技能需求与人才流动。
- **用户账号体系**：注册/登录/密码找回/权限分级。
- **多数据来源接入**：支持动态增减岗位、年份、数据源。

---

## 数据与模型说明

- **简历数据/岗位描述**：`pythonusing/Resume.csv`
  - 每行对应一份简历及所属岗位类别，模型训练与筛选时使用。
- **模型架构**：`resume_model_final.keras`
  - 基于Keras，采用CNN+LSTM+注意力机制（Attention）混合，兼顾局部特征与全局语义，分类+评分双重能力。
- **模型训练过程**：详见源码及注释，包含文本清洗、Tokenize、标签标准化、交叉验证等步骤。
- **OCR引擎**：采用 EasyOCR，自动识别上传简历图片/PDF文本内容。

---

## 典型使用流程

1. **管理员/用户** 登录前端门户，浏览可视化数据与招聘趋势。
2. 需要筛选简历时，点击“智能简历筛选”按钮，自动跳转到 Gradio Web 服务。
3. 选择岗位类别，上传多份PDF或图片格式简历，设定Top-K，点击开始。
4. 后台自动完成OCR→文本分析→模型评分→智能排序，展示最优人选名单及评分。

---

## 常见问题 FAQ

- **Gradio启动后打不开？**
  - 检查端口占用（默认7860），或有无Python依赖缺失。
- **前端页面接口403/500？**
  - 检查后端服务是否正常运行、接口地址和端口配置是否一致。
- **AI模块批量上传慢？**
  - 受限于本地硬件与EasyOCR性能，建议使用GPU服务器加速。
- **岗位类别列表为空？**
  - 请确保 `Resume.csv` 数据完整，并与前端/Gradio服务读取路径一致。

---

## 作者与致谢

> 本项目由Kaiven Deng开发与维护。  
> 如果对本项目有建议或合作需求，欢迎提问或邮件联系：`dkw2266@smail.seig.edu.cn`  
>
> 感谢所有开源依赖与指导老师的支持！

---

## License

本项目采用 MIT 开源协议。  
详细内容请见 [LICENSE 文件](./LICENSE)。

---

