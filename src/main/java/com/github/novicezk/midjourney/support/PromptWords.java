package com.github.novicezk.midjourney.support;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("关键词")
public class PromptWords implements Serializable {

    @ApiModelProperty("关键词ID")
    private String id;

    @ApiModelProperty("关键词")
    private String text;

    @ApiModelProperty("中文翻译")
    private String langZh;

    @ApiModelProperty("目录")
    private String dir;

    @ApiModelProperty("描述内容")
    private String desc;

    @ApiModelProperty("分类")
    private String subType;
}
