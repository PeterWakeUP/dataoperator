package com.example.dataoperator.service;

import com.example.dataoperator.dao.StudentDao;
import com.example.dataoperator.dto.StudentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    @Autowired
    StudentDao studentDao;

    public void save(StudentVo vo){
        studentDao.save(vo);
    }

    @Transactional
    public void saveBatchTransactionTests(){
        for (int i=0; i<3; i++){
            StudentVo student = new StudentVo(String.valueOf(i), String.valueOf(i), String.valueOf(i));
            studentDao.save(student);
        }
        try {
            int i = 1/0;
        }catch (Exception e){

        }

    }
}
