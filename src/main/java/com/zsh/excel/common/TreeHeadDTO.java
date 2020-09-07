package com.zsh.excel.common;

import lombok.Data;

import java.util.List;

/**
 * @author 小白i
 * @date 2020/9/7
 */
@Data
public class TreeHeadDTO {

    private String headCode;

    private String headDesc;

    private List<TreeHeadDTO> children;
}
