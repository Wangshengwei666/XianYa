package com.sysu.xianya.service.impl;

import com.sysu.xianya.entity.IdleItem;
import com.sysu.xianya.entity.User;
import com.sysu.xianya.mapper.IdleItemMapper;
import com.sysu.xianya.mapper.UserMapper;
import com.sysu.xianya.service.IdleItemService;
import com.sysu.xianya.vo.PageVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 闲置物品模块 - 服务层实现类 (Service Implementation)
 *
 * 实现了IdleItemService接口中定义的所有业务方法。
 * 通过调用Mapper层与数据库进行交互，完成具体的业务逻辑处理。
 */
@Service // 声明这是一个Spring的服务类，将其交由Spring容器管理
public class IdleItemServiceImpl implements IdleItemService {

    @Resource // 自动注入IdleItemMapper，用于操作idle_item表
    private IdleItemMapper idleItemMapper;

    @Resource // 自动注入UserMapper，用于操作user表
    private UserMapper userMapper;

    @Override
    public boolean addIdleItem(IdleItem idleItem) {
        // 调用Mapper的insert方法将物品信息插入数据库
        // insert方法会返回受影响的行数，如果为1，则表示插入成功
        return idleItemMapper.insert(idleItem) == 1;
    }

    @Override
    public IdleItem getIdleItem(Long id) {
        // 1. 根据物品ID从数据库查询物品基本信息
        IdleItem idleItem = idleItemMapper.selectByPrimaryKey(id);

        // 2. 如果物品存在，则查询其发布者的信息并进行组装
        if (idleItem != null) {
            // 根据物品中的userId，查询对应的User对象
            User user = userMapper.selectByPrimaryKey(idleItem.getUserId());
            // 将查询到的User对象设置到IdleItem中
            idleItem.setUser(user);
        }

        // 3. 返回组装好的完整物品信息
        return idleItem;
    }

    @Override
    public List<IdleItem> getAllIdelItem(Long userId) {
        // 直接调用Mapper方法获取指定用户的所有物品
        return idleItemMapper.getAllIdleItem(userId);
    }

    @Override
    public PageVo<IdleItem> findIdleItem(String findValue, int page, int nums) {
        // 1. 调用Mapper进行分页查询，获取物品列表
        List<IdleItem> list = idleItemMapper.findIdleItem(findValue, (page - 1) * nums, nums);

        // 2. 填充每个物品的发布者信息
        fillUserForIdleList(list);

        // 3. 查询符合条件的总记录数，用于分页
        int count = idleItemMapper.countIdleItem(findValue);

        // 4. 构建并返回分页结果对象
        return new PageVo<>(list, count);
    }

    @Override
    public PageVo<IdleItem> findIdleItemByLabel(int idleLabel, int page, int nums) {
        // 1. 根据标签分页查询物品列表
        List<IdleItem> list = idleItemMapper.findIdleItemByLabel(idleLabel, (page - 1) * nums, nums);

        // 2. 填充发布者信息
        fillUserForIdleList(list);

        // 3. 根据标签查询总数
        // 注意：这里也需要修正Mapper中的方法名
        int count = idleItemMapper.countIdleItemByLabel(idleLabel);

        // 4. 返回分页结果
        return new PageVo<>(list, count);
    }

    @Override
    public boolean updateIdleItem(IdleItem idleItem) {
        // 调用Mapper的updateByPrimaryKeySelective方法
        // "Selective"表示只更新idleItem对象中不为null的字段
        return idleItemMapper.updateByPrimaryKeySelective(idleItem) == 1;
    }

    @Override
    public PageVo<IdleItem> adminGetIdleList(int status, int page, int nums) {
        // 1. 根据状态分页查询物品列表
        List<IdleItem> list = idleItemMapper.getIdleItemByStatus(status, (page - 1) * nums, nums);

        // 2. 填充发布者信息
        fillUserForIdleList(list);

        // 3. 根据状态查询总数
        int count = idleItemMapper.countIdleItemByStatus(status);

        // 4. 返回分页结果
        return new PageVo<>(list, count);
    }

    /**
     * 【重构】提取出的公共方法：为闲置物品列表填充发布者用户信息
     *
     * @param idleList 待填充的物品列表
     */
    private void fillUserForIdleList(List<IdleItem> idleList) {
        // 如果列表为空，直接返回，避免后续操作
        if (CollectionUtils.isEmpty(idleList)) {
            return;
        }

        // 1. 从物品列表中提取出所有不重复的 userId
        List<Long> userIdList = idleList.stream()
                .map(IdleItem::getUserId)
                .distinct()
                .collect(Collectors.toList());

        // 2. 根据 userId列表，一次性从数据库查询出所有相关的用户信息
        List<User> userList = userMapper.findUserByList(userIdList);

        // 3. 将用户信息列表转换成 "userId -> User对象" 的Map，方便快速查找
        Map<Long, User> userMap = userList.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // 4. 遍历物品列表，从Map中找到对应的User对象并设置进去
        idleList.forEach(item -> item.setUser(userMap.get(item.getUserId())));
    }
}