package com.example.demo.service;

import com.example.demo.dto.PayDto;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * @Author:caoxiang
 * @Description:
 * @Date: Create in 17:07 2018/4/14
 * @Modified By;
 */
public interface PayService {

    /**
     * 获取所有 PayDto
     */
    List<PayDto> findAll();

    /**
     * 新增 PayDto
     *
     * @param PayDto {@link PayDto}
     */
    JsonObject pay(PayDto payDto);

    /**
     * 查询
     *
     */
    JsonObject query(String billNo);

    /**
     * 更新 PayDto
     *
     * @param book {@link PayDto}
     */
    PayDto update(PayDto book);

    /**
     * 删除 PayDto
     *
     * @param id 编号
     */
    PayDto delete(String billNo);

    /**
     * 获取 PayDto
     *
     * @param id 编号
     */
    PayDto findById(String billNo);
}