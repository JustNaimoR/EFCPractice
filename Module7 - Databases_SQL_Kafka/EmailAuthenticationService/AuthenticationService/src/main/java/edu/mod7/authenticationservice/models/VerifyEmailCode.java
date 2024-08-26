package edu.mod7.authenticationservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VerifyEmailCode {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "verify_code")
    private String verifyCode;

}