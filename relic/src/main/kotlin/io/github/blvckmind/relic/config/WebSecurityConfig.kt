package io.github.blvckmind.relic.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Configuration
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    private val csrfEnabled = false

    override fun configure(web: WebSecurity?) {
        web?.ignoring()?.antMatchers("/assets/**")
    }

    override fun configure(http: HttpSecurity) {
        super.configure(http)
        if (!csrfEnabled) {
            http.csrf().disable()
        }
        http.headers().frameOptions().disable()

        http.formLogin() // указываем страницу с формой логина
            .loginPage("/password") // указываем action с формы логина
            .loginProcessingUrl("/password") // указываем URL при неудачном логине
            .failureUrl("/password?error") // Указываем параметры логина и пароля с формы логина
            .usernameParameter("hidden_user")
            .passwordParameter("password") // даем доступ к форме логина всем
            .defaultSuccessUrl("/", true)
            .permitAll()

        http
            .logout()
            .logoutRequestMatcher(AntPathRequestMatcher("/lock"))
            .logoutSuccessUrl("/")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
    }
}
