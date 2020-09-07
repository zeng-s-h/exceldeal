package com.zsh.excel.download;

import com.zsh.excel.common.CommonDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ForkJoinPool;

/**
 * @author 小白i
 * @date 2020/9/7
 */
@Service
public class ExportService {


    public void export(CommonDTO commonDTO){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletResponse response = requestAttributes.getResponse();
        ForkJoinPool pool = ForkJoinPool.commonPool();
        ExportThread task = new ExportThread();
        pool.execute(task);
    }

}
