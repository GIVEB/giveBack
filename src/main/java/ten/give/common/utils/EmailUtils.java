package ten.give.common.utils;

import ten.give.web.form.EmailResultForm;
import ten.give.web.form.MailSendForm;

/**
 * @version 0.0
 * @Author shinywoon
 * Email Utils
 */

public interface EmailUtils {

    /**
     * @author shinywoon
     * @version 0.0
     * @param toAddress Input Address where you want to send
     * @param subject our Utils make subject as userName + basic form
     * @param body our Utils make body as userName + token + basic form
     * @param result final sms form for send
     * @return look up EmailResultForm
     * @see EmailResultForm
     */
    EmailResultForm sendEmail(String toAddress, String subject, String body,EmailResultForm result);

    /**
     * @author shinywoon
     * @version 0.0
     * @param name target name for send email [ inputted join form given name ]
     * @param token random token for Authentication
     * @return look up MailSendForm
     * @see MailSendForm
     */
    MailSendForm setMailSubjectAndBody(String name, String token);
}
