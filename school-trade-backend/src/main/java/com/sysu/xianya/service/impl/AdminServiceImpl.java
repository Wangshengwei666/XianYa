package com.sysu.xianya.service.impl;

import com.sysu.xianya.entity.Admin;
import com.sysu.xianya.mapper.AdminMapper;
import com.sysu.xianya.service.AdminService;
import com.sysu.xianya.vo.PageVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    public Admin login(String accountNumber, String adminPassword) {
        return adminMapper.login(accountNumber, adminPassword);
    }

    public PageVo<Admin> getAdminList(int page, int nums) {
        List<Admin> list = adminMapper.getList((page - 1) * nums, nums);
        int count = adminMapper.getCount();
        return new PageVo<>(list, count);
    }

    public boolean addAdmin(Admin admin) {
        return adminMapper.insert(admin) == 1;
    }
}
