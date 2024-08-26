package edu.mod7.authenticationservice.security.services;

import edu.mod7.authenticationservice.models.Client;
import edu.mod7.authenticationservice.repositories.ClientRepository;
import edu.mod7.authenticationservice.security.models.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username wasn't found")
        );

        return new UserDetailsImpl(client);
    }
}