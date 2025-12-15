package com.sky.dto;

import com.sky.po.DishFlavor;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜品ID")
    private Long id;

    //菜品名称
    @ApiModelProperty(value = "菜品名称")
    private String name;

    //菜品分类id
    @ApiModelProperty(value = "菜品分类id")
    private Long categoryId;

    //菜品价格
    @ApiModelProperty(value = "菜品价格")
    private BigDecimal price;

    //图片
    @ApiModelProperty(value = "img公网地址")
    private String image;

    //描述信息
    @ApiModelProperty(value = "描述信息")
    private String description;

    //0 停售 1 起售
    @ApiModelProperty(value = "是否起售")
    private Integer status;

    @ApiModelProperty(value = "口味")
    private List<DishFlavor> flavors = new ArrayList<>();
}
