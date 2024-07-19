package mk.ukim.finki.wp.sep2022.model;

import org.springframework.security.core.GrantedAuthority;

public enum MatchType implements GrantedAuthority {
    FRIENDLY,
    COMPETITIVE,
    CHARITY,
    UNASSIGNED;

    @Override
    public String getAuthority() {
        return name();
    }
}
