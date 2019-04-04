package com.example.dataoperator.controller;

import com.example.dataoperator.dto.R;
import com.example.dataoperator.dto.StudentVo;
import com.example.dataoperator.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("data")
public class DataController {

    @Autowired
    StudentService studentService;


    @RequestMapping("save")
    public R save(StudentVo vo){
        if(vo.getType().equals("mysql")){
            studentService.save(vo);
        }else{

        }
        return R.ok();
    }


}
