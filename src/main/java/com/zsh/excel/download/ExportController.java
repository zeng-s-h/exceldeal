package com.zsh.excel.download;

import com.zsh.excel.common.CommonDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 小白i
 * @date 2020/9/7
 */
@RestController
@RequestMapping("export")
public class ExportController {

    @Resource
    private ExportService exportService;

    @PostMapping("exportData")
    public void export(@RequestBody CommonDTO commonDTO) {
        exportService.export(commonDTO);
    }

}
