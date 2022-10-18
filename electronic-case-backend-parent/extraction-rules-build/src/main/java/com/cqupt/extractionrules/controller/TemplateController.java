package com.cqupt.extractionrules.controller;

import com.cqupt.extractionrules.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther DyingZhang
 * @Create 2022-10-18 下午 9:01
 * @Discriptioon
 */
@RestController
@RequestMapping("/template")
public class TemplateController {
    @Autowired
    TemplateService templateService;
}
