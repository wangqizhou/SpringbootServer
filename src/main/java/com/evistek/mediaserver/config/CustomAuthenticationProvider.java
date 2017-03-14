package com.evistek.mediaserver.config;

import com.evistek.mediaserver.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.interfaces.RSAPrivateKey;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/19.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private IAuthService rsaService;
    private IAuthService desService;
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public CustomAuthenticationProvider(CustomUserDetailsService customUserDetailsService,
                                        @Qualifier("RSAService") IAuthService rsaService,
                                        @Qualifier("DESService") IAuthService desService) {
        this.customUserDetailsService = customUserDetailsService;
        this.rsaService = rsaService;
        this.desService = desService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails principal = this.customUserDetailsService.loadUserByUsername(name);
        if (principal == null ||
                !principal.isAccountNonLocked() ||
                !principal.isAccountNonExpired() ||
                !principal.isCredentialsNonExpired() ||
                !principal.isEnabled()) {
            return null;
        }

        RSAPrivateKey rsaPrivateKey = this.rsaService.getPrivateKey();
        if (rsaPrivateKey != null) {
            try {
                byte[] clientPasswordBytes = this.rsaService.decryptFromBase64String(password, rsaPrivateKey);
                String clientPasswordString = new String(clientPasswordBytes, "UTF-8");
                byte[] serverPasswordBytes = this.desService.decryptFromBase64String(principal.getPassword(),
                        this.desService.getKeyForPassword());
                String serverPasswordString = new String(serverPasswordBytes, "UTF-8");

                if (serverPasswordString.equals(clientPasswordString)) {
                    return new UsernamePasswordAuthenticationToken(
                                principal,
                                principal.getPassword(),
                                principal.getAuthorities()
                            );
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
