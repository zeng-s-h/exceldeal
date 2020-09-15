package com.zsh.excel.upload;

import com.zsh.excel.common.CommonDTO;
import com.zsh.excel.download.ExportService;
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
@RequestMapping("upload")
public class UploadController {

    @Resource
    private UploadService uploadService;

    @PostMapping("importData")
    public void export(@RequestBody UploadDTO uploadDTO) {
        uploadService.upload(uploadDTO);
    }

}
