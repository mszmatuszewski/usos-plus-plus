package pl.edu.uj.ii.mmatuszewski.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pl.edu.uj.ii.mmatuszewski.auth.service.LocalUserService

@Configuration
class HttpConfig(val userService: LocalUserService) : WebSecurityConfigurerAdapter(), WebMvcConfigurer {

    override fun configure(http: HttpSecurity) {
        // @formatter:off
        http
                .authorizeRequests()
                    .antMatchers("/login", "/user/register", "/calendar/retrieve/**", "/webjars/**", "/css/**")
                        .permitAll()
                    .anyRequest()
                        .fullyAuthenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                .and()
                .logout()
                    .logoutRequestMatcher(AntPathRequestMatcher("/logout"))
                .and()
                .sessionManagement()
                    .maximumSessions(1)
        // @formatter:on
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        // @formatter:off
        auth
                .userDetailsService(userService)
                .passwordEncoder(encoder())
        // @formatter:on
    }

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/login")
        registry.addViewController("/").setViewName("index")
    }

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()
}
