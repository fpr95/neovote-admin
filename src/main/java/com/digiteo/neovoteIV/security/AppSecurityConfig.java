package com.digiteo.neovoteIV.security;

import com.digiteo.neovoteIV.security.core.userdetail.CustomUserDetailsService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    //@Resource
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                //.cors().and()
                //.csrf().disable()
                .authorizeHttpRequests()
                //.requestMatchers("/home", "/login").permitAll()
                .requestMatchers("/resources/**").permitAll()
                .requestMatchers("/account/**")
                //.authenticated()
                .hasAuthority("USER")
                .anyRequest().permitAll()
                .and()
                //.authenticationManager(authManager(userDetailsService))
                .formLogin(form -> form
                        .defaultSuccessUrl("/account/homeuser")
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?error=true")
                ).logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                )
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**", "/templates/**", "/static/**");
    }

    //@Bean
    //public DaoAuthenticationProvider authProvider(){
    //    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    //    authenticationProvider.setPasswordEncoder(passwordEncoder());
    //    authenticationProvider.setUserDetailsService(userDetailsService);
    //    return authenticationProvider;
    //}

    //@Bean
    //public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
    //    return configuration.getAuthenticationManager();
    //}

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setPasswordEncoder(passwordEncoder());
        auth.setUserDetailsService(userDetailsService);
        return new ProviderManager(auth);
    }
}
