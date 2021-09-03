package com.nastyastrel.springbootrest.entity.user;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//
//import javax.persistence.*;
//import java.util.Collection;
//import java.util.List;
//
//@Entity
//@Table(name = "todo_users")
//public class User implements UserDetails {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "serial_number")
//    private int serialNumber;
//
//    @Column(name = "first_name")
//    private String firstName;
//
//    @Column(name = "last_name")
//    private String lastName;
//
//    @Column(name = "login")
//    private String login;
//
//    @Column(name = "password")
//    private String password;
//
//    public User() {
//    }
//
//    public User(int serialNumber, String firstName, String lastName, String login, String password) {
//        this.serialNumber = serialNumber;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.login = login;
//        this.password = password;
//    }
//
//    public int getSerialNumber() {
//        return serialNumber;
//    }
//
//    public void setSerialNumber(int serialNumber) {
//        this.serialNumber = serialNumber;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getLogin() {
//        return login;
//    }
//
//    public void setLogin(String login) {
//        this.login = login;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(Role.USER.toString()));
//    }
//
//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return null;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
