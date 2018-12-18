package vpnk8.vpnk8platformapi.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import vpnk8.vpnk8platformapi.users.User;
import vpnk8.vpnk8platformapi.users.UsersService;

import java.util.Collections;
import java.util.Optional;

@Component
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    @Autowired
    private UsersService usersService;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        String email    = auth.getName();
        String password = auth.getCredentials().toString();

        Optional<User> user = usersService.findByEmailAndPassword(email, password);

        if (user.isPresent()) {

            return new UsernamePasswordAuthenticationToken(email, password, Collections.emptyList());

        } else {

            throw new BadCredentialsException("External system authentication failed");

        }

    }

    @Override
    public boolean supports(Class<?> auth) {

        return auth.equals(UsernamePasswordAuthenticationToken.class);

    }

}
