# sky-mp:使用Mybatis-plus完成重写

## 项目架构
基于 Maven 聚合的单体架构

## 最小前置技术栈
- Spring Boot 
- MyBatis-Plus 
- MySQL 
- Redis 
- Vue2/3
## 管理工具
后端
- maven 
- git 
- docker
- Swagger

前端
- Vite
- npm/yarn/pnpm
## 项目背景
苍穹外卖是一款面向餐饮行业的后端管理系统，原技术栈基于Spring Boot + MyBatis实现数据交互。本次使用MyBatis-Plus（简称MP）对数据层进行重写，旨在简化CRUD代码、提升开发效率，并利用MP的高级特性增强功能。


## 改写部分
1. **基础CRUD替换**
   原MyBatis的Mapper接口及XML映射文件全部替换为MP的BaseMapper接口，例如：
    - 原UserMapper接口+UserMapper.xml（包含selectById、insert等方法）
    - 重写后：`public interface UserMapper extends BaseMapper<User> {}`（无需XML，直接调用MP内置方法）
后：`userMapper.insertBatchSomeColumn(userList);`（MP自带批量插入，需配置插件）

