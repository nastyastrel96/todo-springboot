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
                                FROM users
                                WHERE login =?
                                """)
                .authoritiesByUsernameQuery(
                        """
                                SELECT login, role
                                FROM users t1 INNER JOIN user_role t2 ON t1.id = t2.users_id
                                               INNER JOIN roles t3 ON t2.roles_id=t3.id
                                WHERE login =?
                                """);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/api/todos/**").hasAuthority(RoleName.USER.toString())
                .antMatchers("/api/tags/**").hasAuthority(RoleName.USER.toString())
                .antMatchers("/api/users/**").hasAuthority(RoleName.ADMIN.toString())
                .antMatchers("/**").permitAll()
                .and().
                httpBasic().
                and().
                sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
