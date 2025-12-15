package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.po.SetmealDish;
import com.sky.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper  extends BaseMapper<User> {
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(@Param("openid") String openid);
}
