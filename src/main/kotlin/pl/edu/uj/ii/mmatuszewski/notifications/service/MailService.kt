package pl.edu.uj.ii.mmatuszewski.notifications.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MailService(val mailSender: JavaMailSender) {

    private val LOGGER = LoggerFactory.getLogger(MailService::class.java)

    @Value("\${spring.application.name}")
    private lateinit var appName: String

    fun sendMessage(to: String, subject: String, text: String) {
        LOGGER.info("Sending email to $to with subject $subject and body $text")
        val message = SimpleMailMessage()
        message.setFrom(appName)
        message.setTo(to)
        message.setSubject(subject)
        message.setText(text)
        try {
            mailSender.send(message)
        } catch (e: Exception) {
            LOGGER.error("Exception while sending email", e)
        }
    }
}
