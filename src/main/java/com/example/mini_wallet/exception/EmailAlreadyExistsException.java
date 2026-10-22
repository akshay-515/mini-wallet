
package com.example.mini_wallet.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("A wallet already exists for email: " + email);
    }
}
