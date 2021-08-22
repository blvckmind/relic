package io.github.blvckmind.relic.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import javax.annotation.PostConstruct

@EnableWebSecurity
@Configuration
class WebSecurityConfig(
    val applicationProperties: ApplicationProperties,
    val auth: AuthenticationManagerBuilder
) : WebSecurityConfigurerAdapter() {

    private val csrfEnabled = false

    override fun configure(web: WebSecurity?) {
        web?.ignoring()?.antMatchers("/assets/**")
    }

    override fun configure(http: HttpSecurity) {
        if (!csrfEnabled) {
            http.csrf().disable()
        }

        http.headers().frameOptions().disable()

        if (applicationProperties.authorization.outsideAccess) {
            super.configure(http)
            http
                .formLogin() // указываем страницу с формой логина
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
        } else {
            http.authorizeRequests().antMatchers("/**").permitAll()
        }
    }

    @PostConstruct
    fun addDefaultUser() {
        if (applicationProperties.authorization.outsideAccess) {
            auth
                .inMemoryAuthentication()
                .withUser("blvckmind")
                .password("{noop}${applicationProperties.authorization.password}")
                .roles("USER")
        }
    }

}
