package com.sysu.xianya.controller;

import com.sysu.xianya.enums.ErrorMsg;
import com.sysu.xianya.service.FileService;
import com.sysu.xianya.utils.IdFactoryUtil;
import com.sysu.xianya.vo.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 文件上传与访问 - 控制层
 * (基于原始代码的最终稳定版)
 */
@CrossOrigin
@RestController
public class FileController {

    @Value("${userFilePath}")
    private String userFilePath;

    @Value("${baseUrl}")
    private String baseUrl; // 从 application.yml 注入，值为: http://localhost:8080

    @Resource
    private FileService fileService;

    /**
     * 文件上传接口
     * 访问路径: POST http://localhost:8080/file
     */
    @PostMapping("/file")
    public R uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        String fileName = "file" + IdFactoryUtil.getFileId() + multipartFile.getOriginalFilename();
        try {
            if (fileService.uploadFile(multipartFile, fileName)) {
                // 【核心】返回拼接好的、完整的、可直接访问的图片URL
                String fileUrl = baseUrl + "/image?imageName=" + fileName;
                return R.success(fileUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return R.fail(ErrorMsg.SYSTEM_ERROR);
        }
        return R.fail(ErrorMsg.FILE_UPLOAD_ERROR);
    }

    /**
     * 图片获取接口
     * 访问路径: GET http://localhost:8080/image?imageName=...
     */
    @GetMapping("/image")
    public void getImage(@RequestParam("imageName") String imageName, HttpServletResponse response) {
        File imageFile = new File(userFilePath, imageName);

        if (imageFile.exists() && imageFile.canRead()) {
            try (FileInputStream fis = new FileInputStream(imageFile);
                 OutputStream os = response.getOutputStream()) {

                // 动态设置Content-Type，让浏览器正确识别图片类型
                String extension = imageName.substring(imageName.lastIndexOf(".") + 1).toLowerCase();
                if ("jpg".equals(extension) || "jpeg".equals(extension)) {
                    response.setContentType("image/jpeg");
                } else if ("png".equals(extension)) {
                    response.setContentType("image/png");
                } else if ("gif".equals(extension)) {
                    response.setContentType("image/gif");
                }

                // 使用缓冲区进行分块读写，更安全高效
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
            } catch (IOException e) {
                System.err.println("Error writing file to output stream. Filename: " + imageName);
                e.printStackTrace();
            }
        }
    }
}