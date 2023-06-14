package com.cqupt.extraction_rules_build.service.impl;


import com.cqupt.extraction_rules_build.mapper.EntityLabelCategoryMapper;
import com.cqupt.extraction_rules_build.mapper.LabelRulesMapper;
import com.cqupt.extraction_rules_build.mapper.TemplateMapper;
import com.cqupt.extraction_rules_build.mapper.TemplateRulesMapper;
import com.cqupt.extraction_rules_build.pojo.entity.EntityLabelCategory;
import com.cqupt.extraction_rules_build.pojo.entity.LabelRules;
import com.cqupt.extraction_rules_build.pojo.entity.Template;
import com.cqupt.extraction_rules_build.pojo.entity.TemplateRules;
import com.cqupt.extraction_rules_build.service.TemplateService;
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
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public Long addTemplateService(Template template) {
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
        return templateId;
    }

    /**
     * 获取所有的标签模板
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
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
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public Template getTemplateByTemplateId(Long templateId) {
        Template template = templateMapper.getTemplateByTemplateId(templateId);
        if (template != null){

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
        } else {
            return null;
        }
    }

    /**
     * 根据templateId删除模板
     * @param templateId
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void deleteTemplateByTemplateId(Long templateId) {
        //1.删除Template
        templateMapper.deleteTemplateByTemplateId(templateId);
        //2.删除TemplateRules
        templateRulesMapper.deleteTemplateRulesByTemplateId(templateId);
        //3.根据templateId找出对应的List<EntityLabelCategory>
        List<EntityLabelCategory> entityLabelCategoryList = entityLabelCategoryMapper.getEntityLabelCategoryByTemplateId(templateId);
        for (EntityLabelCategory entityLabelCategory : entityLabelCategoryList){
            //3.1获取EntityLabelCategory的labelCategoryId
            Long labelCategoryId = entityLabelCategory.getLabelCategoryId();
            //3.2删除EntityLabelCategory
            entityLabelCategoryMapper.deleteEntityLabelCategoryByTemplateId(templateId);
            //4.根据labelCategoryId删除labelRules
            labelRulesMapper.deleteLabelRulesByLabelCategoryId(labelCategoryId);
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public boolean isTemplateExists(Long templateId) {
        Template template = templateMapper.getTemplateByTemplateId(templateId);
        if (template != null){
            return true;
        } else {
            return false;
        }
    }

}
