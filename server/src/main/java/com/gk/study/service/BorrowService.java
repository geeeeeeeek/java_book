package com.gk.study.service;


import com.gk.study.entity.Borrow;

import java.util.List;

public interface BorrowService {
    List<Borrow> getBorrowList();
    void createBorrow(Borrow borrow);
    void deleteBorrow(String id);

    void updateBorrow(Borrow borrow);

    List<Borrow> getUserBorrowList(String userId, String status);

    Borrow detail(Long id);
}
