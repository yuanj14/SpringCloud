# SpringCloud 微服务项目技术文档
## 一、项目基础信息
- **项目名称**：智慧校园微服务平台  
- **开发周期**：2024.01 - 2024.06  
- **核心目标**：实现校园教学、考勤、后勤服务的模块化管理，支持 5 万+师生并发访问，保障服务高可用  
- **部署环境**：开发环境（Dev）、测试环境（Test）、生产环境（Prod）

## 二、技术栈详情
| 技术类别       | 具体技术/框架                | 版本要求       | 核心作用                     |
|----------------|------------------------------|----------------|------------------------------|
| 基础框架       | SpringBoot                   | 3.2.4          | 微服务应用快速初始化，简化依赖管理 |
| 微服务治理     | SpringCloud Alibaba          | 2023.0.0.0     | 提供服务注册、配置中心、熔断降级能力 |
| 服务注册与发现 | Nacos                        | 2.3.1          | 服务节点管理、配置动态刷新       |
| API 网关       | SpringCloud Gateway          | 4.0.4          | 统一接口入口、路由转发、权限校验 |
| 数据存储       | MySQL                        | 8.0.36         | 业务数据持久化（用户、订单等） |
| 缓存技术       | Redis                        | 7.2.4          | 热点数据缓存、分布式锁实现     |
| ORM 框架       | MyBatis-Plus                 | 3.5.6          | 简化数据库 CRUD 操作，减少重复代码 |
| 消息队列       | RabbitMQ                     | 3.13.0         | 异步通信（如通知推送、订单状态同步） |
| 容器化部署     | Docker + Kubernetes          | Docker 25.0.3 / K8s 1.28.3 | 服务容器化、集群编排管理     |
| 接口文档       | Knife4j                      | 4.4.0          | 自动生成 API 文档，支持在线调试 |
| 日志框架       | Logback + ELK                | Logback 1.4.8  | 日志收集、分析与可视化         |

## 三、项目目录结构
smart-campus/├── common/ # 通用模块（全局复用组件）│ ├── common-core/ # 核心工具类（加密、日期、校验）│ ├── common-db/ # 数据库通用配置（MyBatis-Plus 配置、分页插件）│ ├── common-security/ # 安全模块（JWT 认证、权限拦截器）│ └── common-swagger/ # 接口文档配置（Knife4j 集成）├── service/ # 业务微服务模块│ ├── service-user/ # 用户服务（师生信息管理、登录认证）│ ├── service-teaching/ # 教学服务（课程管理、成绩录入）│ ├── service-attendance/ # 考勤服务（打卡记录、异常统计）│ └── service-logistics/ # 后勤服务（报修管理、宿舍分配）├── gateway/ # 网关服务（路由配置、限流熔断）├── config-server/ # 配置中心（对接 Nacos，集中管理环境配置）├── registry-server/ # 注册中心（Nacos 单机版部署配置）└── doc/ # 项目文档├── api-docs/ # 接口文档导出文件├── deploy-docs/ # 部署手册（Docker/K8s 配置）└── dev-docs/ # 开发规范（代码风格、提交规范）
## 四、核心功能模块
### 1. 服务治理模块
- **服务注册与发现**：所有微服务启动后自动注册到 Nacos，支持服务健康检查（默认每 5 秒检测一次），下线服务自动剔除  
- **动态配置管理**：通过 Nacos 区分环境配置（如 Dev 环境连接测试库，Prod 环境连接生产库），配置修改后 10 秒内自动刷新，无需重启服务  
- **熔断降级**：集成 Sentinel，对高频接口（如考勤打卡接口）配置 QPS 阈值（默认 1000/秒），超过阈值时返回默认提示，避免服务雪崩  

### 2. 网关服务模块
- **路由转发**：基于路径匹配路由（如 `/api/user/**` 转发至 `service-user`，`/api/teaching/**` 转发至 `service-teaching`），支持权重路由（Prod 环境权重分配：主服务 80%，备用服务 20%）  
- **统一认证**：集成 JWT，客户端请求需在 Header 携带 `Authorization: Bearer {token}`，网关验证 token 有效性，无效则返回 401 状态码  
- **限流控制**：基于 IP 限流（单 IP 每分钟最大请求数 600），基于接口限流（如 `/api/attendance/check` 接口 QPS 限制 500）  

### 3. 业务服务模块
#### （1）用户服务（service-user）
- 核心接口：用户注册（支持手机号/学号注册）、登录（密码/验证码登录）、信息修改（姓名、头像、联系方式）、权限分配（学生/教师/管理员角色区分）  
- 数据安全：密码采用 BCrypt 加密存储，敏感信息（手机号、身份证号）脱敏展示（如手机号显示为 138****5678）  

#### （2）考勤服务（service-attendance）
- 核心功能：打卡记录（支持 GPS 定位校验，偏差超过 100 米则打卡失败）、异常统计（迟到/早退/缺勤统计）、考勤报表导出（支持 Excel 格式）  
- 异步处理：打卡成功后通过 RabbitMQ 发送通知消息，由消息消费者服务推送打卡结果至用户微信公众号  

## 五、快速启动步骤
1. **环境准备**  
   - 安装依赖：MySQL 8.0+、Redis 7.0+、RabbitMQ 3.10+、Nacos 2.3.0+  
   - 初始化数据：执行 `doc/sql/init.sql` 脚本，创建数据库表并插入基础数据（如管理员账号：admin/123456）  
   - 配置 Nacos：登录 Nacos 控制台（默认地址：http://localhost:8848/nacos），创建命名空间（dev/test/prod），导入 `config-server/src/main/resources/nacos-config/` 下的配置文件  

2. **服务启动顺序**  
   1. 启动 `registry-server`（Nacos 服务）  
   2. 启动 `config-server`（配置中心）  
   3. 启动 `gateway`（网关服务）  
   4. 启动 `service-user`、`service-teaching` 等业务服务  

3. **接口测试**  
   - 网关地址：Dev 环境 `http://localhost:8080`，Prod 环境 `http://gateway.smartcampus.com`  
   - 测试接口：  
     - 登录接口：`POST http://localhost:8080/api/user/login`，请求体 `{"username":"admin","password":"123456"}`，返回 JWT token  
     - 考勤打卡接口：`POST http://localhost:8080/api/attendance/check`，Header 携带 token，请求体 `{"userId":"1001","location":"114.31,30.52"}`  

## 六、注意事项
- **开发规范**：代码提交需遵循 Git 提交规范（如 `feat: 新增用户头像上传功能`、`fix: 修复考勤打卡定位偏差问题`），提交前执行 `mvn clean compile` 确保编译通过  
- **生产环境配置**：Prod 环境需关闭 Swagger 接口文档，开启 Redis 持久化（AOF + RDB 混合模式），Nacos 采用集群部署（至少 3 节点）  
- **问题排查**：服务启动失败时，优先查看 `logs/` 目录下的日志文件（如 `service-user.log`），常见问题包括 Nacos 连接失败、数据库账号密码错误、端口被占用（可通过 `netstat -ano | findstr "端口号"` 查看占用进程）