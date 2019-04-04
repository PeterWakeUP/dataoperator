package com.example.dataoperator.dao;

import com.example.dataoperator.dto.StudentVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentDao {

    void save(StudentVo vo);
}
