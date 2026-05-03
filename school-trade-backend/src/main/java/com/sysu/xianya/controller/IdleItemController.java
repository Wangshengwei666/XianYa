package com.sysu.xianya.controller;

import com.sysu.xianya.entity.IdleItem;
import com.sysu.xianya.enums.ErrorMsg;
import com.sysu.xianya.service.IdleItemService;
import com.sysu.xianya.vo.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 闲置物品模块 - 控制层 (Controller)
 *
 * 负责接收前端关于闲置物品的HTTP请求，例如：发布、查询、更新等。
 * 调用 Service 层处理业务逻辑，并将结果返回给前端。
 */
@CrossOrigin // 允许跨域请求，方便前后端分离部署
@RestController // 声明这是一个RESTful风格的Controller，所有方法默认返回JSON格式
@RequestMapping("/idle") // 为该Controller下的所有接口统一添加"/idle"前缀路径
public class IdleItemController {

    // 使用@Resource注解，自动从Spring容器中注入IdleItemService的实例
    @Resource
    private IdleItemService idleItemService;

    /**
     * 接口功能：发布新的闲置物品
     * 请求路径：POST /idle/add
     *
     * @param shUserId 从Cookie中获取的当前登录用户的ID，用于标识物品发布者。
     * @param idleItem 从请求体(RequestBody)中获取的闲置物品信息JSON对象。
     * @return 返回统一的响应对象。成功时包含新发布的物品信息，失败时包含错误信息。
     */
    @PostMapping("/add")
    public R addIdleItem(
            @CookieValue("shUserId") String shUserId, // @CookieValue用于从浏览器Cookie中直接获取值
            @RequestBody IdleItem idleItem) { // @RequestBody表示接收JSON格式的数据并封装成IdleItem对象

        // 1. 参数校验：检查用户ID是否存在
        if (shUserId == null || shUserId.isEmpty()) {
            return R.fail(ErrorMsg.LOGIN_ERROR); // 如果用户未登录，返回登录异常
        }

        // 2. 补全物品信息
        idleItem.setUserId(Long.valueOf(shUserId)); // 设置物品所属的用户ID
        idleItem.setIdleStatus((byte) 1);           // 设置物品状态为 1 (1:在售, 0:下架)
        idleItem.setReleaseTime(new Date());        // 设置发布时间为当前服务器时间

        // 3. 调用Service层处理业务逻辑
        if (idleItemService.addIdleItem(idleItem)) {
            // 如果Service层返回true，表示添加成功
            return R.success("发布成功", idleItem);
        }

        // 4. 如果失败，返回系统错误
        return R.fail(ErrorMsg.SYSTEM_ERROR);
    }

    /**
     * 接口功能：根据ID获取单个闲置物品的详细信息
     * 请求路径：GET /idle/info?id=xxx
     *
     * @param id 要查询的闲置物品ID
     * @return 返回包含该物品详细信息的响应对象。
     */
    @GetMapping("/info")
    public R getIdleItem(@RequestParam("id") Long id) {
        IdleItem item = idleItemService.getIdleItem(id);
        return R.success(item);
    }

    /**
     * 接口功能：获取当前登录用户发布的所有闲置物品
     * 请求路径：GET /idle/all
     *
     * @param shUserId 从Cookie中获取的当前登录用户的ID。
     * @return 返回包含该用户所有物品列表的响应对象。
     */
    @GetMapping("/all")
    public R getAllIdleItem(@CookieValue("shUserId") String shUserId) {
        if (shUserId == null || shUserId.isEmpty()) {
            return R.fail(ErrorMsg.LOGIN_ERROR);
        }
        return R.success(idleItemService.getAllIdelItem(Long.valueOf(shUserId)));
    }

    /**
     * 接口功能：根据关键词模糊搜索闲置物品（分页查询）
     * 请求路径：GET /idle/find?findValue=手机&page=1&nums=10
     *
     * @param findValue 搜索关键词 (非必需)
     * @param page      页码，即第几页 (非必需, 默认1)
     * @param nums      每页数量 (非必需, 默认8)
     * @return 返回分页查询结果的响应对象。
     */
    @GetMapping("/find")
    public R findIdleItem(
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "nums", required = false, defaultValue = "8") Integer nums
    ) {
        // Service层应负责处理分页逻辑，这里保持Controller简洁
        return R.success(idleItemService.findIdleItem(findValue, page, nums));
    }

    /**
     * 接口功能：根据分类标签筛选闲置物品（分页查询）
     * 请求路径：GET /idle/label?idleLabel=1&page=1&nums=10
     * 注意：原代码中的"lable"为拼写错误，已修正为"label"
     *
     * @param idleLabel 物品分类标签的ID (必需)
     * @param page      页码 (非必需, 默认1)
     * @param nums      每页数量 (非必需, 默认8)
     * @return 返回分页查询结果的响应对象。
     */
    @GetMapping("/label") // 修正了原"lable"的拼写错误
    public R findIdleItemByLabel( // 修正了原"lable"的拼写错误
                                  @RequestParam("idleLabel") Integer idleLabel,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                  @RequestParam(value = "nums", required = false, defaultValue = "8") Integer nums
    ) {
        return R.success(idleItemService.findIdleItemByLabel(idleLabel, page, nums));
    }

    /**
     * 接口功能：更新已发布的闲置物品信息
     * 请求路径：POST /idle/update
     *
     * @param shUserId 从Cookie中获取的当前登录用户的ID，用于权限验证。
     * @param idleItem 从请求体中获取的、需要更新的物品信息。
     * @return 返回更新结果的响应对象。
     */
    @PostMapping("/update")
    public R updateIdleItem(
            @CookieValue("shUserId") String shUserId,
            @RequestBody IdleItem idleItem
    ) {
        if (shUserId == null || shUserId.isEmpty()) {
            return R.fail(ErrorMsg.LOGIN_ERROR);
        }
        // 将当前登录用户的ID设置到要更新的对象中，防止恶意用户更新不属于自己的物品
        idleItem.setUserId(Long.valueOf(shUserId));

        if (idleItemService.updateIdleItem(idleItem)) {
            return R.success("更新成功");
        }

        return R.fail(ErrorMsg.SYSTEM_ERROR);
    }
}