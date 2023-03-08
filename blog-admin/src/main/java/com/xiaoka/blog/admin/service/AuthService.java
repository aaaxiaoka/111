package com.xiaoka.blog.admin.service;

import com.xiaoka.blog.admin.model.params.Permission;
import com.xiaoka.blog.admin.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private AdminService adminService;

    public boolean auth(HttpServletRequest request, Authentication authentication){
        //权限认证
        String requestURI = request.getRequestURI();
        Object principal = authentication.getPrincipal();
        if (principal == null || "anonymousUser".equals(principal)){
            //未登录
            return false;
        }

        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Admin admin = adminService.findAdminByUserName(username);
        if (admin == null){
            return false;
        }
        List<Permission> permissions = adminService.findPermissionsByAdminId(admin.getId());
        if (admin.getId() == 1){
            //认为是超级管理员
            return true;
        }
        for (Permission permission : permissions) {
            if (requestURI.equals(permission.getPath())){
                return true;
            }
        }
        return false;

    }
}
