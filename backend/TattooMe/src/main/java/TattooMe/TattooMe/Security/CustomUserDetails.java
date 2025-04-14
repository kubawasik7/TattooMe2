package TattooMe.TattooMe.Security;

import TattooMe.TattooMe.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private User user;
    public CustomUserDetails(User user) {
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //TE ROLE DO POPRAWY TEZ
        String role;
        if(user.getUserRole().isClient()){
            role = "ROLE_CLIENT";
        }else if( user.getUserRole().isTattooArtist()){
            role = "ROLE_TATTOO_ARTIST";
        }else{
            role = "ROLE_TRAINEE";
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role));

    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getNickname();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
