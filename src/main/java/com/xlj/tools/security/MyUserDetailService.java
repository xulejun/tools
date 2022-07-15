package com.xlj.tools.security;

import org.springframework.security.core.userdetails.User;
import com.xlj.tools.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author legend xu
 * @date 2022/7/15
 */
@Service("userDetailsService")
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据数据库查询用户账号密码
        com.xlj.tools.bean.User user = userDao.queryByUsername(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 给用户设置权限、角色（需要添加前缀 “ROLE_”）
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("user,ROLE_sale");
        return new User(user.getName(), new BCryptPasswordEncoder().encode(user.getPassword()), auths);
    }
}
