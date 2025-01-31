package edu.mod7.authenticationservice.repositories;

import edu.mod7.authenticationservice.PostgreSQLTestContainerConfiguration;
import edu.mod7.authenticationservice.models.VerifyEmailCode;
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
class VerifyEmailCodeRepositoryTest {
    @Autowired
    VerifyEmailCodeRepository repository;

    static final VerifyEmailCode singleVerifyCode = new VerifyEmailCode("romario22@gmail.com", "192837");



    @Test
    @Sql("/insert_1_verify-code.sql")
    void findByEmail() {
        String email = singleVerifyCode.getEmail();
        VerifyEmailCode verifyEmailCode = repository.findByEmail(email).get();

        Assertions.assertEquals(singleVerifyCode, verifyEmailCode);
    }

    @Test
    @Sql("/insert_1_verify-code.sql")
    void deleteByEmail() {
        String email = singleVerifyCode.getEmail();
        repository.deleteByEmail(email);

        Assertions.assertTrue(
                repository.findByEmail(email).isEmpty()
        );
    }

}