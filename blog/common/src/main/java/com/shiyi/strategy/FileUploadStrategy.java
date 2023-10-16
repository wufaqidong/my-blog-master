package com.shiyi.strategy;

import com.shiyi.common.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传策略
 */
public interface FileUploadStrategy {

    String fileUpload(MultipartFile file,String suffix);

    ResponseResult fileList(int pageNo, int pageSize);

    Boolean deleteFile(String ...fileName);
}
