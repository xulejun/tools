package com.xlj.tools.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @author legend xu
 * @date 2022/7/15
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyUserDetailService userDetailService;

    /**
     * 注入数据源，为自动登录提供
     */
    @Autowired
    private DataSource dataSource;

    /**
     * 配置自动登录对象
     *
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 开启后自动建表 persistent_logins
        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }


    /**
     * 密码加密对象
     *
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 从自定义类扫描用户密码
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 自定义编写登录页面
        http.formLogin()
                // 登录页面设置
                .loginPage("/security/login")
//                .usernameParameter("")
//                .passwordParameter("")
                // 登录访问路径，与表单提交路径一致
                .loginProcessingUrl("/user/login")
                // 登录成功之后跳转路径
                .defaultSuccessUrl("/security/success", true).permitAll()
                .and().authorizeRequests()
                // 哪些路径可以直接访问
                .antMatchers("/security/hello", "/security/login").permitAll()
                // 登录用户，具有 admin 权限才可以访问（只针对一个权限）
                .antMatchers("/security/admin").hasAuthority("admin")
                // 登录用户，具有 admin/manager 权限才可以访问（针对多个权限）
                .antMatchers("/security/admin").hasAnyAuthority("admin,manager")
                // 登录用户，为 sale 角色才可以访问（只针对单个角色）
                .antMatchers("/security/sale").hasRole("sale")
                // 登录用户，为 sale/boss 角色才可以访问（针对多个角色）
                .antMatchers("/security/sale").hasAnyRole("sale,boss")
                .anyRequest().authenticated()
                // 开启自动登录（关闭浏览器后免登录）
                // 引入对象
                .and().rememberMe().tokenRepository(persistentTokenRepository())
                // token 有效时长
                .tokenValiditySeconds(60)
                .userDetailsService(userDetailService)
                // 关闭 csrf 防护
                .and().csrf().disable();
        // 403 无权限访问跳转自定义页面
        http.exceptionHandling().accessDeniedPage("/security/unauth");
        // 退出注销
        http.logout().logoutUrl("/security/logout")
                .logoutSuccessUrl("/security/hello").permitAll();
    }
}
