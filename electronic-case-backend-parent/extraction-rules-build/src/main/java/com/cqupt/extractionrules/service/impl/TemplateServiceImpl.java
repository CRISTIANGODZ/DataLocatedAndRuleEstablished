package com.cqupt.extractionrules.service.impl;

import com.cqupt.extractionrules.mapper.EntityLabelCategoryMapper;
import com.cqupt.extractionrules.mapper.LabelRulesMapper;
import com.cqupt.extractionrules.mapper.TemplateMapper;
import com.cqupt.extractionrules.mapper.TemplateRulesMapper;
import com.cqupt.extractionrules.pojo.EntityLabelCategory;
import com.cqupt.extractionrules.pojo.LabelRules;
import com.cqupt.extractionrules.pojo.Template;
import com.cqupt.extractionrules.pojo.TemplateRules;
import com.cqupt.extractionrules.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @auther DyingZhang
 * @Create 2022-10-18 下午 9:04
 * @Discriptioon
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    EntityLabelCategoryMapper entityLabelCategoryMapper;

    @Autowired
    LabelRulesMapper labelRulesMapper;

    @Autowired
    TemplateMapper templateMapper;

    @Autowired
    TemplateRulesMapper templateRulesMapper;

    /**
     * 添加模板
     * @param template
     */
    @Override
    public void addTemplateService(Template template) {
        //1.添加template记录
        templateMapper.addTemplate(template);
        //2.添加templateRules记录
        Long templateId = template.getTemplateId();
        TemplateRules templateRules = template.getTemplateRules();
        templateRules.setTemplateId(templateId);
            //上面为提取templateRules对象，并注入相关属性
        templateRulesMapper.addTemplateRules(templateRules);
        //3.添加EntityLabelCategory记录
        List<EntityLabelCategory> entityLabelCategoryList = template.getEntityLabelCategoryList();
        for (EntityLabelCategory entityLabelCategory : entityLabelCategoryList){
            entityLabelCategory.setTemplateId(templateId);
                //上面为提取entityLabelCategory对象，并注入templateId
            entityLabelCategoryMapper.addEntityLabelCategory(entityLabelCategory);
            Long labelCategoryId = entityLabelCategory.getLabelCategoryId();
            LabelRules labelRules = entityLabelCategory.getLabelRules();
            labelRules.setLabelCategoryId(labelCategoryId);
                //上面为提取labelRules对象，并注入labelId
            labelRulesMapper.addLabelRules(labelRules);
        }
    }
}
