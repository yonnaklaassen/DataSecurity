package datasecurity.group10;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class ClientConfiguration extends WebSecurityConfigurerAdapter {



    @Override
    protected void configure(HttpSecurity http) throws Exception {
    http.requiresChannel().anyRequest().requiresSecure();
        http.csrf().disable();
        http
                // Configure your HTTP security rules
                .authorizeRequests()
                .antMatchers("/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/login").defaultSuccessUrl("/home")
                .permitAll()
                .and()
                .logout()
                .permitAll()
        ;


}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        System.out.println("AuthProvider reach");

        return new ResourceServerAuthentication();
    }

}

