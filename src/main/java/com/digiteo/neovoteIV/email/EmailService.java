package com.digiteo.neovoteIV.email;

import com.digiteo.neovoteIV.email.context.AbstractEmailContext;
import jakarta.mail.MessagingException;

public interface EmailService {

    void sendEmail(final AbstractEmailContext email) throws MessagingException;
}
