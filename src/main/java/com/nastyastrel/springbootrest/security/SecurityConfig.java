package com.nastyastrel.springbootrest.security;


import com.nastyastrel.springbootrest.model.user.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        """
                                SELECT login, password, enabled
                                FROM todo_users
                                WHERE login =?
                                """)
                .authoritiesByUsernameQuery(
                        """
                                SELECT login,
                                       name
                                FROM (SELECT login,
                                             role_id
                                      FROM todo_users
                                               INNER JOIN user_role on todo_users.user_id = user_role.user_id) tab1
                                         INNER JOIN
                                     (SELECT user_role.role_id,
                                             name
                                      FROM user_role
                                               INNER JOIN todo_roles on user_role.role_id = todo_roles.role_id) tab2
                                     ON tab1.role_id = tab2.role_id
                                WHERE login =?
                                """);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/api/todos/**").hasAuthority(RoleName.USER.toString())
                .antMatchers("/api/users/**").hasAuthority(RoleName.ADMIN.toString())
                .antMatchers("/**").permitAll()
                .and().
                httpBasic().
                and().
                sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
