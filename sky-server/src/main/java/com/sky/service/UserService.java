package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.UserLoginDTO;
import com.sky.po.Dish;
import com.sky.po.SetmealDish;
import com.sky.po.User;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import com.sky.vo.UserLoginVO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService extends IService<User> {

    UserLoginVO wxLogin(@RequestBody UserLoginDTO userLoginDTO);

}
