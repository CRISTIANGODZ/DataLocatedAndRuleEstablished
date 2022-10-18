package com.cqupt.extractionrules.service.impl;

import com.cqupt.extractionrules.mapper.EntityLabelCategoryMapper;
import com.cqupt.extractionrules.mapper.LabelRulesMapper;
import com.cqupt.extractionrules.mapper.TemplateMapper;
import com.cqupt.extractionrules.mapper.TemplateRulesMapper;
import com.cqupt.extractionrules.service.TemplateService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auther DyingZhang
 * @Create 2022-10-18 下午 9:04
 * @Discriptioon
 */
@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    EntityLabelCategoryMapper entityLabelCategoryMapper;

    @Autowired
    LabelRulesMapper labelRulesMapper;

    @Autowired
    TemplateMapper templateMapper;

    @Autowired
    TemplateRulesMapper templateRulesMapper;

}
