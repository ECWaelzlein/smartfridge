package de.isemwaf.smartFridge.services;

import de.isemwaf.smartFridge.details.AccountUserDetails;
import de.isemwaf.smartFridge.model.Account;
import de.isemwaf.smartFridge.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new AccountUserDetails().setAccount(account).setAuthorities(AuthorityUtils.createAuthorityList("ROLE_USER"));
    }
}
