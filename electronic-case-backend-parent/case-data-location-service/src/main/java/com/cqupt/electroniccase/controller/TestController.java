package com.cqupt.electroniccase.controller;

import com.cqupt.electroniccase.domain.entity.Diseases;
import com.cqupt.electroniccase.service.TestService;
import com.cqupt.electroniccase.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * @auther DyingZhang
 * @Create 2023-03-29 下午 10:20
 * @Discriptioon
 */
@RestController
@RequestMapping("/test/es")
public class TestController {

    @Autowired
    TestService testService;

    @PostMapping("/add/index")
    public R addIndexController(@RequestParam("name") String name) {
        testService.addIndex(name);
        return R.ok().message("添加成功");
    }

    @PostMapping("/insert/doc")
    public R insertDocController(Diseases diseases) {
        testService.insertDocService(diseases);
        return R.ok().message("添加成功");
    }

    @GetMapping("/get/doc")
    public R getDocByTerm(Diseases diseases) {
        testService.getDocByTerm(diseases);
        return R.ok().message("添加成功");
    }

}
