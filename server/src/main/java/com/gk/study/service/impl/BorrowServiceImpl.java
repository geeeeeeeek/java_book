package com.gk.study.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.study.entity.Borrow;
import com.gk.study.mapper.BorrowMapper;
import com.gk.study.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements BorrowService {
    @Autowired
    BorrowMapper mapper;

    @Override
    public List<Borrow> getBorrowList() {
        return mapper.getList();
    }

    @Override
    public void createBorrow(Borrow borrow) {
        long ct = System.currentTimeMillis();
        borrow.setBorrowTime(String.valueOf(ct));
        borrow.setStatus("1");
        mapper.insert(borrow);
    }

    @Override
    public void deleteBorrow(String id) {
        mapper.deleteById(id);
    }

    @Override
    public void updateBorrow(Borrow borrow) {
        mapper.updateById(borrow);
    }

    @Override
    public List<Borrow> getUserBorrowList(String userId, String status) {
        return mapper.getUserBorrowList(userId, status);
    }

    @Override
    public Borrow detail(Long id) {
        return mapper.selectById(id);
    }
}
