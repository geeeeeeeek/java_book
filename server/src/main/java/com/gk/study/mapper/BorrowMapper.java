package com.gk.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk.study.entity.Borrow;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BorrowMapper extends BaseMapper<Borrow> {

    List<Borrow> getList();

    List<Borrow> getUserBorrowList(String userId, String status);
}
