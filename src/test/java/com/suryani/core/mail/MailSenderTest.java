package com.zyd.core.mail;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;

/**
 * @author neo
 */
public class MailSenderTest extends EasyMockSupport {
    MailSender mailSender;

    @Before
    public void createMailSender() {
        mailSender = new MailSender();
    }

    @Test
    public void useFromAddressAsReplyTo() throws MessagingException {
        MimeMessageHelper message = createMock(MimeMessageHelper.class);
        message.setReplyTo("from@mail.com");

        replayAll();

        Mail mail = new Mail();
        mail.setFrom("from@mail.com");
        mailSender.setReplyTo(mail, message);

        verifyAll();
    }

    @Test
    public void useReplyToAddress() throws MessagingException {
        MimeMessageHelper message = createMock(MimeMessageHelper.class);
        message.setReplyTo("replyTo@mail.com");

        replayAll();

        Mail mail = new Mail();
        mail.setFrom("from@mail.com");
        mail.setReplyTo("replyTo@mail.com");
        mailSender.setReplyTo(mail, message);

        verifyAll();
    }
}
