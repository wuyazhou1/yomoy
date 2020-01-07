package com.nsc.Amoski.parent;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class queryActivityListParent {
    @NotNull(message = "字典类型id必填!")
    @Size(min= 1,message = "字典类型id必填!")
    private String id;
}
