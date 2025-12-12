package com.sky.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据库操作类型
 */
@Getter
//@AllArgsConstructor
public enum OperationType {
    UPDATE,
    INSERT
    ;
//    @EnumValue
//    private final int value;
//    @JsonValue
//    private final String status;

}