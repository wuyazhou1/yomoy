package com.nsc.Amoski.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.regex.Pattern;

public class NotNullValidator implements ConstraintValidator<MyValidator,String> {

    Logger logger = LoggerFactory.getLogger(NotNullValidator.class);

    @Autowired
    Validator globalValidator;
    private MyValidator MyValidator;
    private Map<String,Object> memberValues;


    @Override
    public void initialize(MyValidator MyValidator) {
        this.MyValidator=MyValidator;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String value = request.getParameter(this.MyValidator.name());
        //为requestBody赋值
        if(value!=null && !value.equals("")){
            s=value;
        }
        //不能为空
        if(NotNull(context,s)){
            return false;
        }
        //最大值
        if(max(context,s)){
            return false;
        }
        //最小值
        if(min(context,s)){
            return false;
        }
        //最小长度
        if(lengthMin(context,s)){
            return false;
        }
        //最大长度
        if(lengthMax(context,s)){
            return false;
        }
        return true;
    }

    private boolean lengthMax(ConstraintValidatorContext context,String value){
        if(this.MyValidator.lengthMax()!=-1){
            if(value.length()>this.MyValidator.lengthMax()){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(this.MyValidator.ColumnName() + "值长度不能大于"+this.MyValidator.lengthMax())
                        .addConstraintViolation();
                return true;
            }
        }
        return false;
    }

    private boolean lengthMin(ConstraintValidatorContext context,String value){
        if(this.MyValidator.lengthMin()!=-1){
            if(value.length()<this.MyValidator.lengthMin()){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(this.MyValidator.ColumnName() + "值长度不能小于"+this.MyValidator.lengthMin())
                        .addConstraintViolation();
                return true;
            }
        }
        return false;
    }

    private boolean max(ConstraintValidatorContext context,String value){
        if(this.MyValidator.max()!=-1){
            if (Pattern.compile("^-?\\d+(\\.\\d+)?$").matcher(value).matches()) {
                if(Double.parseDouble(value)<this.MyValidator.max()){
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(this.MyValidator.ColumnName() + "值不能大于"+this.MyValidator.max())
                            .addConstraintViolation();
                    return true;
                }
            }else{
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(this.MyValidator.ColumnName() + "值必须是数字")
                        .addConstraintViolation();
                return true;
            }
        }
        return false;
    }

    private boolean min(ConstraintValidatorContext context,String value){
        if(this.MyValidator.min()!=-1){
            if (Pattern.compile("^-?\\d+(\\.\\d+)?$").matcher(value).matches()) {
                if(Double.parseDouble(value)<this.MyValidator.min()){
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(this.MyValidator.ColumnName() + "值不能小于"+this.MyValidator.min())
                            .addConstraintViolation();
                    return true;
                }
            }else{
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(this.MyValidator.ColumnName() + "值必须是数字")
                        .addConstraintViolation();
                return true;
            }
        }
        return false;
    }

    private boolean NotNull(ConstraintValidatorContext context,String value){
        if(this.MyValidator.NotNull()){
            if(value==null || value.equals("")){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(this.MyValidator.ColumnName() + "为必填项，不能为空")
                        .addConstraintViolation();
                return true;
            }
        }
        return false;
    }

}
