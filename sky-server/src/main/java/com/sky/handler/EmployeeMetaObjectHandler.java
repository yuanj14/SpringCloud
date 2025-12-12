package com.sky.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.po.Employee;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Component
public class EmployeeMetaObjectHandler implements MetaObjectHandler {

    // 插入操作时填充
    @Override
    public void insertFill(MetaObject metaObject) {
        // 1. 设置默认密码（MD5加密）
        String defaultPwd = DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes());
        this.strictInsertFill(metaObject, "password", String.class, defaultPwd);

        // 2. 设置状态为启用
        this.strictInsertFill(metaObject, "status", Integer.class, StatusConstant.ENABLE);

        // 3. 设置创建时间、更新时间
        LocalDateTime now = LocalDateTime.now();
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
//        静态软编码
//        this.strictInsertFill(metaObject, Employee::getUpdateTime, LocalDateTime.class, now);

        // 4. 设置创建人、更新人（从BaseContext获取当前用户ID）
        Long currentId = BaseContext.getCurrentId();
        this.strictInsertFill(metaObject, "createUser", Long.class, currentId);
        this.strictInsertFill(metaObject, "updateUser", Long.class, currentId);
    }

    // 更新操作时填充
    @Override
    public void updateFill(MetaObject metaObject) {
        // 设置更新时间、更新人
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "updateUser", Long.class, BaseContext.getCurrentId());
    }

}
//@Component
//public class CustomMetaObjectHandler implements MetaObjectHandler {
//
//    // 插入操作时填充（区分实体类型）
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        // 1. 获取当前操作的实体对象
//        Object originalObject = metaObject.getOriginalObject();
//
//        // 2. 区分实体类型，执行不同填充
//        if (originalObject instanceof Employee) {
//            // 员工类：默认密码（比如 "employee123" 加密）
//            String empDefaultPwd = DigestUtils.md5DigestAsHex("employee123".getBytes());
//            this.strictInsertFill(metaObject, "password", String.class, empDefaultPwd);
//            // 员工的其他填充（状态、时间等）
//            this.strictInsertFill(metaObject, "status", Integer.class, StatusConstant.ENABLE);
//        } else if (originalObject instanceof Admin) {
//            // 管理员类：默认密码（比如 "admin@root" 加密）
//            String adminDefaultPwd = DigestUtils.md5DigestAsHex("admin@root".getBytes());
//            this.strictInsertFill(metaObject, "password", String.class, adminDefaultPwd);
//            // 管理员的其他填充（状态、时间等）
//            this.strictInsertFill(metaObject, "status", Integer.class, StatusConstant.ENABLE);
//        }
//
//        // 3. 公共字段填充（时间、操作人，所有实体通用）
//        LocalDateTime now = LocalDateTime.now();
//        Long currentId = BaseContext.getCurrentId();
//        // 判断字段是否存在（避免非公共字段报错）
//        if (metaObject.hasSetter("createTime")) {
//            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
//        }
//        if (metaObject.hasSetter("updateTime")) {
//            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
//        }
//        if (metaObject.hasSetter("createUser") && currentId != null) {
//            this.strictInsertFill(metaObject, "createUser", Long.class, currentId);
//        }
//        if (metaObject.hasSetter("updateUser") && currentId != null) {
//            this.strictInsertFill(metaObject, "updateUser", Long.class, currentId);
//        }
//    }
//
//    // 更新操作时填充（公共逻辑，也可以区分实体）
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        LocalDateTime now = LocalDateTime.now();
//        Long currentId = BaseContext.getCurrentId();
//        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);
//        if (currentId != null) {
//            this.strictUpdateFill(metaObject, "updateUser", Long.class, currentId);
//        }
//    }
//}