package com.example.dataoperator.service;

import com.example.dataoperator.dao.StudentDao;
import com.example.dataoperator.dto.StudentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    StudentDao studentDao;

    public void save(StudentVo vo){
        studentDao.save(vo);
    }
}
