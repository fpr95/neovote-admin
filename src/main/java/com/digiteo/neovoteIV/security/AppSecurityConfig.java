package com.digiteo.neovoteIV.security;

import com.digiteo.neovoteIV.security.core.userdetail.CustomUserDetailsService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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

    @Configuration
    public static class VoterConfigurationAdapter {
        @Bean
        public SecurityFilterChain voterSecurityFilterChain(HttpSecurity http) throws Exception{
            return http
                    //.cors().and()
                    //.csrf().disable()
                    //.securityMatcher("/auth-voter/**")
                    .authorizeHttpRequests()
                    //.requestMatchers("/register", "/login").permitAll()
                    .requestMatchers("/resources/**").permitAll()
                    .requestMatchers("/auth-voter/**")
                    //.authenticated()
                    .hasAuthority("USER")
                    .anyRequest().permitAll()
                    .and()
                    //.authenticationManager(authManager(userDetailsService))
                    .formLogin(form -> form
                            .defaultSuccessUrl("/auth-voter/welcome-voter", true)
                            //.successForwardUrl("/auth-voter/welcome-voter")
                            .loginPage("/login")
                            .loginProcessingUrl("/login")
                            .failureUrl("/login?error=true")
                    ).logout(logout -> logout
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                            .permitAll()
                    )
                    .build();
        }
    }

    @Configuration
    @Order(1)
    public static class AdminConfigurationAdapter {
        @Bean
        public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception{
            return http
                    //.cors().and()
                    //.csrf().disable()
                    .securityMatcher("/login-admin", "/auth/**")
                    .authorizeHttpRequests()
                    //.requestMatchers("/register", "/login").permitAll()
                    .requestMatchers("/uploads/**").permitAll()
                    .requestMatchers("/static/**").permitAll()
                    .requestMatchers("/resources/**").permitAll()
                    .requestMatchers("/auth/**")
                    //.authenticated()
                    .hasAuthority("ADMIN")
                    .anyRequest().permitAll()
                    .and()
                    //.authenticationManager(authManager(userDetailsService))
                    .formLogin(form -> form
                            .defaultSuccessUrl("/auth/welcome-admin", true)
                            .loginPage("/login-admin")
                            .loginProcessingUrl("/login-admin")
                            .failureUrl("/login-admin?error=true")
                    ).logout(logout -> logout
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                            .permitAll()
                    )
                    .build();
        }
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**", "/uploads/**", "/templates/**", "/static/**", "/img-usr/**");
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

    //the bcrypt hashing function should be used only in dev and testing enviroments, for production this must be changed
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
