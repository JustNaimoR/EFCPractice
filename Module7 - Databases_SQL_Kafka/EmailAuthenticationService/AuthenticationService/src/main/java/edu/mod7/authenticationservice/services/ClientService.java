package edu.mod7.authenticationservice.services;

import edu.mod7.authenticationservice.exceptions.ClientNotFoundException;
import edu.mod7.authenticationservice.models.Client;
import edu.mod7.authenticationservice.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;



    public Client findByUsername(String username) {
        Optional<Client> opt = clientRepository.findByUsername(username);

        return opt.orElseThrow(ClientNotFoundException::new);
    }
}