package com.gk.study.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.gk.study.common.APIResponse;
import com.gk.study.common.ResponeCode;
import com.gk.study.entity.Borrow;
import com.gk.study.entity.Thing;
import com.gk.study.permission.Access;
import com.gk.study.permission.AccessLevel;
import com.gk.study.service.BorrowService;
import com.gk.study.service.ThingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowController {

    private final static Logger logger = LoggerFactory.getLogger(BorrowController.class);

    @Autowired
    BorrowService service;

    @Autowired
    ThingService thingService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public APIResponse list(){
        List<Borrow> list =  service.getBorrowList();

        return new APIResponse(ResponeCode.SUCCESS, "查询成功", list);
    }

    // 用户list
    @RequestMapping(value = "/userBorrowList", method = RequestMethod.GET)
    public APIResponse userBorrowList(String userId, String status){
        List<Borrow> list =  service.getUserBorrowList(userId, status);
        return new APIResponse(ResponeCode.SUCCESS, "查询成功", list);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Transactional
    public APIResponse create(Borrow borrow) throws IOException {


        long expectTime = System.currentTimeMillis() + (long) 30 * 24 * 60 * 60 * 1000;
        borrow.setExpectTime(String.valueOf(expectTime));
        borrow.setStatus("1"); // 借入
        borrow.setHasDelayed("0"); // 没延期
        if(StringUtils.isNotBlank(borrow.getThingId()) && StringUtils.isNotBlank(borrow.getUserId())){
            service.createBorrow(borrow);
            // 库存-1
            Thing thing = thingService.getThingById(borrow.getThingId());
            if(Integer.parseInt(thing.getRepertory()) <= 0){
                return new APIResponse(ResponeCode.FAIL, "库存不足");
            }
            thing.setRepertory(String.valueOf(Integer.parseInt(thing.getRepertory()) - 1));
            thingService.updateThing(thing);
            return new APIResponse(ResponeCode.SUCCESS, "创建成功");
        }else {
            return new APIResponse(ResponeCode.FAIL, "参数错误");
        }
    }

    @Access(level = AccessLevel.ADMIN)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public APIResponse delete(String ids){
        System.out.println("ids===" + ids);
        // 批量删除
        String[] arr = ids.split(",");
        for (String id : arr) {
            service.deleteBorrow(id);
        }
        return new APIResponse(ResponeCode.SUCCESS, "删除成功");
    }


    @Access(level = AccessLevel.ADMIN)
    @RequestMapping(value = "/returnBorrow", method = RequestMethod.POST)
    @Transactional
    public APIResponse returnBorrow(Long id) throws IOException {
        Borrow dbBorrow = service.detail(id);
        if(dbBorrow.getStatus().equals("2")){
            return new APIResponse(ResponeCode.FAIL, "已经还过了");
        } else {
            dbBorrow.setStatus("2");
            dbBorrow.setReturnTime(String.valueOf(System.currentTimeMillis()));
            service.updateBorrow(dbBorrow);
            // 库存+1
            Thing thing = thingService.getThingById(dbBorrow.getThingId());
            thing.setRepertory(String.valueOf(Integer.parseInt(thing.getRepertory()) + 1));
            thingService.updateThing(thing);
            return new APIResponse(ResponeCode.SUCCESS, "操作成功");
        }
    }

    @Access(level = AccessLevel.LOGIN)
    @RequestMapping(value = "/returnUserBorrow", method = RequestMethod.POST)
    @Transactional
    public APIResponse returnUserBorrow(Long id) throws IOException {
        Borrow dbBorrow = service.detail(id);
        if(dbBorrow.getStatus().equals("2")){
            return new APIResponse(ResponeCode.FAIL, "已经还过了");
        } else {
            dbBorrow.setStatus("2");
            dbBorrow.setReturnTime(String.valueOf(System.currentTimeMillis()));
            service.updateBorrow(dbBorrow);
            // 库存+1
            Thing thing = thingService.getThingById(dbBorrow.getThingId());
            thing.setRepertory(String.valueOf(Integer.parseInt(thing.getRepertory()) + 1));
            thingService.updateThing(thing);
            return new APIResponse(ResponeCode.SUCCESS, "操作成功");
        }
    }

    @Access(level = AccessLevel.ADMIN)
    @RequestMapping(value = "/delayBorrow", method = RequestMethod.POST)
    @Transactional
    public APIResponse delayBorrow(Long id) throws IOException {
        Borrow dbBorrow = service.detail(id);
        if(dbBorrow.getHasDelayed().equals("1")){
            return new APIResponse(ResponeCode.FAIL, "已经延期过了");
        }
        dbBorrow.setHasDelayed("1");
        long expectTime = Long.parseLong(dbBorrow.getExpectTime()) + (long) 30 * 24 * 60 * 60 * 1000;
        dbBorrow.setExpectTime(String.valueOf(expectTime));
        service.updateBorrow(dbBorrow);
        return new APIResponse(ResponeCode.SUCCESS, "操作成功");
    }

    @Access(level = AccessLevel.LOGIN)
    @RequestMapping(value = "/delayUserBorrow", method = RequestMethod.POST)
    @Transactional
    public APIResponse delayUserBorrow(Long id) throws IOException {
        Borrow dbBorrow = service.detail(id);
        if(dbBorrow.getHasDelayed().equals("1")){
            return new APIResponse(ResponeCode.FAIL, "已经延期过了");
        }
        dbBorrow.setHasDelayed("1");
        long expectTime = Long.valueOf(dbBorrow.getExpectTime()) + (long) 30 * 24 * 60 * 60 * 1000;
        dbBorrow.setExpectTime(String.valueOf(expectTime));
        service.updateBorrow(dbBorrow);
        return new APIResponse(ResponeCode.SUCCESS, "操作成功");
    }

}
