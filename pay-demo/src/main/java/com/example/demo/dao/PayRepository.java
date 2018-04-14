package com.example.demo.dao;

import com.example.demo.dto.PayDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Book 数据持久层操作接口
 *
 * Created by bysocket on 09/10/2017.
 */
@Repository
public interface PayRepository extends JpaRepository<PayDto, Long> {

    public PayDto findByBillNo(String billNo);
}
