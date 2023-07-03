// 权限问题后期增加
import { get, post } from '/@/utils/http/axios';
import { UserState } from '/@/store/modules/user/types';
// import axios from 'axios';
enum URL {
    list = '/api/borrow/list',
    userList = '/api/borrow/userBorrowList',
    returnBorrow = '/api/borrow/returnBorrow',
    returnUserBorrow = '/api/borrow/returnUserBorrow',
    delayBorrow = '/api/borrow/delayBorrow',
    delayUserBorrow = '/api/borrow/delayUserBorrow',
    create = '/api/borrow/create',
}

const listApi = async (params: any) => get<any>({ url: URL.list, params: params, data: {}, headers: {} });
const userBorrowListApi = async (params: any) => get<any>({ url: URL.userList, params: params, data: {}, headers: {} });
const createApi = async (data: any) =>
    post<any>({ url: URL.create, params: {}, data: data, headers: { 'Content-Type': 'multipart/form-data;charset=utf-8' } });
const returnBorrowApi = async (params: any) =>
    post<any>({ url: URL.returnBorrow, params: params, headers: { 'Content-Type': 'multipart/form-data;charset=utf-8' } });
const returnUserBorrowApi = async (params: any) =>
    post<any>({ url: URL.returnUserBorrow, params: params, headers: { 'Content-Type': 'multipart/form-data;charset=utf-8' } });
const delayBorrowApi = async (params: any) =>
    post<any>({ url: URL.delayBorrow, params: params, headers: { 'Content-Type': 'multipart/form-data;charset=utf-8' } });
const delayUserBorrowApi = async (params: any) =>
    post<any>({ url: URL.delayUserBorrow, params: params, headers: { 'Content-Type': 'multipart/form-data;charset=utf-8' } });


export { listApi, userBorrowListApi, createApi, returnBorrowApi, returnUserBorrowApi, delayBorrowApi, delayUserBorrowApi };
