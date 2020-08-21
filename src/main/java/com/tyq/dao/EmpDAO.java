package com.tyq.dao;

import com.tyq.entity.Emp;
import com.tyq.entity.Emp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpDAO {

    List<Emp> findAll();

    void save(Emp emp);

    void delete(String id);

    Emp findOne(String id);

    void update(Emp emp);
}