package vpnk8.vpnk8platformapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vpnk8.vpnk8platformapi.authentication.AuthenticationProvider;
import vpnk8.vpnk8platformapi.authentication.JWTAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final AuthenticationProvider customAuthenticationProvider;

    @Autowired
    public SecurityConfig(final AuthenticationProvider customAuthenticationProvider) {

        this.customAuthenticationProvider = customAuthenticationProvider;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http    //
                // Enable CORS
                //
                .cors().and()
                //
                // Disable CSRF
                //
                .csrf().disable()
                //
                // Allow these urls to bypass JWT token requirements
                //
                .authorizeRequests().antMatchers("/users/login",
                                                 "/upload/**/status/**",
                                                 "/is_alive").permitAll()
                //
                // Now require authentication for other urls
                //
                .anyRequest().authenticated().and()
                //
                // Finally, require JWT token for all other requests
                //
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {

        auth.authenticationProvider(customAuthenticationProvider);

    }

}
