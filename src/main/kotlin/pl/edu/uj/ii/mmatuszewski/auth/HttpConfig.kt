package pl.edu.uj.ii.mmatuszewski.auth

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class HttpConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        // @formatter:off
        http
                .authorizeRequests()
                    .antMatchers("/", "/login", "/css/**").permitAll()
                .anyRequest()
                    .fullyAuthenticated()
                    .and()
                .formLogin()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .deleteCookies("JSESSIONID")
                    .and()
                .sessionManagement()
                    .maximumSessions(1)
        // @formatter:on
    }
}
