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

    /**
     * 获取所有的标签模板
     * @return
     */
    @Override
    public List<Template> getAllTemplates() {
        //1.获取所有的模板的列表
        List<Template> templateList = templateMapper.getAllTemplates();
        for (Template template : templateList){
            Long templateId = template.getTemplateId();
            //2.根据templateId获取TemplateRules相关信息
            TemplateRules templateRules = templateRulesMapper.getTemplateRulesByTemplateId(templateId);
            //2.1将templateRules注入template中
            template.setTemplateRules(templateRules);
            //3.根据templateId获取EntityLabelCategory信息
            List<EntityLabelCategory> entityLabelCategoryList = entityLabelCategoryMapper.getEntityLabelCategoryByTemplateId(templateId);
            for (EntityLabelCategory entityLabelCategory : entityLabelCategoryList){
                //4.根据labelCategoryId获取LabelRules信息
                Long labelCategoryId = entityLabelCategory.getLabelCategoryId();
                LabelRules labelRules = labelRulesMapper.getLabelRules(labelCategoryId);
                //4.1将labelRules注入到entityLabelCategory中
                entityLabelCategory.setLabelRules(labelRules);
            }
            //3.1将list<EntityLabelCategory>注入到template中
            template.setEntityLabelCategoryList(entityLabelCategoryList);
        }
        return templateList;
    }

    /**
     * 根据templateId获取模板
     * @param templateId
     * @return
     */
    @Override
    public Template getTemplateByTemplateId(Long templateId) {
        Template template = templateMapper.getTemplateByTemplateId(templateId);
        //1.根据templateId获取TemplateRules相关信息
        TemplateRules templateRules = templateRulesMapper.getTemplateRulesByTemplateId(templateId);
        //1.1将templateRules注入template中
        template.setTemplateRules(templateRules);
        //2.根据templateId获取EntityLabelCategory信息
        List<EntityLabelCategory> entityLabelCategoryList = entityLabelCategoryMapper.getEntityLabelCategoryByTemplateId(templateId);
        for (EntityLabelCategory entityLabelCategory : entityLabelCategoryList){
            //3.根据labelCategoryId获取LabelRules信息
            Long labelCategoryId = entityLabelCategory.getLabelCategoryId();
            LabelRules labelRules = labelRulesMapper.getLabelRules(labelCategoryId);
            //3.1将labelRules注入到entityLabelCategory中
            entityLabelCategory.setLabelRules(labelRules);
        }
        //2.1将list<EntityLabelCategory>注入到template中
        template.setEntityLabelCategoryList(entityLabelCategoryList);
        return template;
    }

}
