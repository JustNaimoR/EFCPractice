package edu.mod7.authenticationservice.repositories;

import edu.mod7.authenticationservice.PostgreSQLTestContainerConfiguration;
import edu.mod7.authenticationservice.models.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import({
        PostgreSQLTestContainerConfiguration.class,
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientRepositoryTest {
    @Autowired
    ClientRepository repository;

    static final Client singleClient = new Client(0, "romario", "romario22@gmail.com");



    @Test
    @Sql("/insert_1_client.sql")
    void findByUsername() {
        String username = singleClient.getUsername();
        Client client = repository.findByUsername(username).get();

        Assertions.assertEquals(singleClient, client);
    }
}