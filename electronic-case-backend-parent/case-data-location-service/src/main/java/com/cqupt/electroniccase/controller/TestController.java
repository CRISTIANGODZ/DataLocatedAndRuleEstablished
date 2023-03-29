package com.cqupt.electroniccase.controller;

import com.cqupt.electroniccase.domain.entity.Diseases;
import com.cqupt.electroniccase.service.TestService;
import com.cqupt.electroniccase.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther DyingZhang
 * @Create 2023-03-29 下午 10:20
 * @Discriptioon
 */
@Controller
@RequestMapping("/test/es")
public class TestController {

    @Autowired
    TestService testService;

    @PostMapping("/add")
    public R addIndexController(@RequestParam("name") String name) {
        testService.addIndex(name);
        return R.ok();
    }

}
