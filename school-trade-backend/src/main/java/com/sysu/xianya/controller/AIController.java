package com.sysu.xianya.controller;

import com.sysu.xianya.service.AIService;
import com.sysu.xianya.vo.R;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin
public class AIController {

    @Resource
    private AIService aiService;

    @PostMapping("/generate-description")
    public R generateDescription(@RequestBody Map<String, String> payload) {
        String title = payload.get("title");
        if (title == null || title.trim().isEmpty()) {
            return R.fail(null, "商品标题不能为空");
        }
        try {
            String description = aiService.generateDescriptionByTitle(title);
            return R.success(description);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(null, "AI服务暂时不可用，请稍后再试");
        }
    }
}