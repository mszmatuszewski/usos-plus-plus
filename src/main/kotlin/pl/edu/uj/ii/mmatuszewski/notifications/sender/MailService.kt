package pl.edu.uj.ii.mmatuszewski.notifications.sender

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MailService(val mailSender: JavaMailSender) {

    fun sendMessage(to: String, subject: String, text: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.setSubject(subject)
        message.setText(text)
        mailSender.send(message)
    }
}
