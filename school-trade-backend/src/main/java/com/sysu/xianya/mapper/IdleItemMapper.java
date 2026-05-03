package com.sysu.xianya.mapper;

import com.sysu.xianya.entity.IdleItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 闲置物品模块 - 数据访问层接口 (Mapper/DAO)
 *
 * 定义了所有与'sh_idle_item'表相关的数据库操作。
 * MyBatis框架会根据此接口，自动与对应的XML文件中的SQL语句进行绑定。
 */
@Mapper // 声明这是一个MyBatis的Mapper接口，Spring会自动扫描并为其创建代理对象
public interface IdleItemMapper {

    // --- 删 ---
    int deleteByPrimaryKey(Long id);

    // --- 增 ---
    int insert(IdleItem record);
    int insertSelective(IdleItem record); // "Selective"表示只插入record中不为null的字段

    // --- 改 ---
    int updateByPrimaryKeySelective(IdleItem record); // "Selective"表示只更新record中不为null的字段
    int updateByPrimaryKey(IdleItem record);

    // --- 查 ---
    IdleItem selectByPrimaryKey(Long id);

    List<IdleItem> getAllIdleItem(Long userId);

    /**
     * 根据关键词模糊查询总数
     * @param findValue 搜索关键词
     * @return 符合条件的总记录数
     */
    int countIdleItem(@Param("findValue") String findValue);

    /**
     * 根据分类标签查询总数
     * @param idleLabel 分类标签ID
     * @return 符合条件的总记录数
     */
    int countIdleItemByLabel(@Param("idleLabel") int idleLabel);

    /**
     * 根据状态查询总数
     * @param status 物品状态
     * @return 符合条件的总记录数
     */
    int countIdleItemByStatus(@Param("status") int status);

    /**
     * 根据关键词分页查询
     * @param findValue 搜索关键词
     * @param begin     分页查询的起始位置 ( (page-1)*nums )
     * @param nums      每页数量
     * @return 物品列表
     */
    List<IdleItem> findIdleItem(@Param("findValue") String findValue, @Param("begin") int begin, @Param("nums") int nums);

    /**
     * 根据分类标签分页查询
     * @param idleLabel 分类标签ID
     * @param begin     分页查询的起始位置
     * @param nums      每页数量
     * @return 物品列表
     */
    List<IdleItem> findIdleItemByLabel(@Param("idleLabel") int idleLabel, @Param("begin") int begin, @Param("nums") int nums);

    /**
     * 根据状态分页查询
     * @param status 物品状态
     * @param begin  分页查询的起始位置
     * @param nums   每页数量
     * @return 物品列表
     */
    List<IdleItem> getIdleItemByStatus(@Param("status") int status, @Param("begin") int begin, @Param("nums") int nums);

    /**
     * 根据ID列表批量查询物品
     * @param idList 物品ID列表
     * @return 物品列表
     */
    List<IdleItem> findIdleByList(@Param("idList") List<Long> idList);
}