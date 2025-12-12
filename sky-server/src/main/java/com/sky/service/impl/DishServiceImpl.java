package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.*;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.EmployeeMapper;
import com.sky.po.Dish;
import com.sky.po.DishFlavor;
import com.sky.po.Employee;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.service.EmployeeService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.FlashMapManager;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     *
     * @param dishDTO
     */
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        // 添加一条 dish 数据
        Dish dish = BeanUtil.copyProperties(dishDTO, Dish.class);
        dishMapper.insert(dish);

        // 添加 dish 对应的 flavor 集合数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        // 添加 dish 生成 dishId
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dish.getId());
        }

        dishFlavorMapper.insert(flavors);
//        批处理 性能优化 100k+ data
//        this.saveBatch(flavors);
//        &rewriteBatchedStatements=true
//        url: jdbc:mysql://127.0.0.1:port/table?&rewriteBatchedStatements=true
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        Page<DishVO> page = new Page<>(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        // 2. MP Lambda拼接查询条件（类型安全，无硬编码）
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<Dish>()
                .like(StringUtils.isNotBlank(dishPageQueryDTO.getName()),
                        Dish::getName,
                        dishPageQueryDTO.getName()
                )
                .eq(dishPageQueryDTO.getCategoryId() != null,
                        Dish::getCategoryId,
                        dishPageQueryDTO.getCategoryId()
                )
                .eq(dishPageQueryDTO.getStatus() != null,
                        Dish::getStatus,
                        dishPageQueryDTO.getStatus()
                )
                .orderByDesc(Dish::getUpdateTime);

        IPage<DishVO> resultPage = dishMapper.pageQuery(page, dishPageQueryDTO, queryWrapper);
        List<DishVO> dishVOList = resultPage.getRecords();

        return new PageResult(resultPage.getTotal(), dishVOList);
    }

}
