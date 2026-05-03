package com.sysu.xianya.service;

import com.sysu.xianya.entity.IdleItem;
import com.sysu.xianya.vo.PageVo;

import java.util.List;

/**
 * 闲置物品模块 - 服务层接口 (Service Interface)
 *
 * 定义了闲置物品模块需要对外提供的所有业务能力。
 * Controller层应通过此接口与业务逻辑交互，而不是直接与实现类交互。
 */
public interface IdleItemService {

    /**
     * 发布新的闲置物品
     *
     * @param idleItem 包含物品信息的实体对象
     * @return 如果发布成功，返回true；否则返回false
     */
    boolean addIdleItem(IdleItem idleItem);

    /**
     * 根据ID获取单个闲置物品的详细信息
     * (包含发布者用户信息)
     *
     * @param id 物品ID
     * @return 返回包含完整信息的IdleItem对象；如果不存在则返回null
     */
    IdleItem getIdleItem(Long id);

    /**
     * 获取指定用户发布的所有闲置物品列表
     *
     * @param userId 用户ID
     * @return 该用户的物品列表
     */
    List<IdleItem> getAllIdelItem(Long userId);

    /**
     * 根据关键词模糊搜索闲置物品（分页）
     *
     * @param findValue 搜索关键词
     * @param page      页码
     * @param nums      每页数量
     * @return 分页结果对象 (PageVo)，包含物品列表和总数
     */
    PageVo<IdleItem> findIdleItem(String findValue, int page, int nums);

    /**
     * 根据分类标签筛选闲置物品（分页）
     *
     * @param idleLabel 物品分类标签ID
     * @param page      页码
     * @param nums      每页数量
     * @return 分页结果对象 (PageVo)，包含物品列表和总数
     */
    PageVo<IdleItem> findIdleItemByLabel(int idleLabel, int page, int nums);

    /**
     * 更新闲置物品信息（如：修改价格、下架等）
     *
     * @param idleItem 包含待更新信息的物品对象 (必须包含ID)
     * @return 如果更新成功，返回true；否则返回false
     */
    boolean updateIdleItem(IdleItem idleItem);

    /**
     * (管理员用) 根据状态获取闲置物品列表（分页）
     *
     * @param status 物品状态 (1:在售, 0:下架, -1:违规)
     * @param page   页码
     * @param nums   每页数量
     * @return 分页结果对象 (PageVo)，包含物品列表和总数
     */
    PageVo<IdleItem> adminGetIdleList(int status, int page, int nums);
}