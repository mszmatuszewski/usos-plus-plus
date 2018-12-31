package pl.edu.uj.ii.mmatuszewski.notifications.controller

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import pl.edu.uj.ii.mmatuszewski.notifications.service.MailService

@Controller
@RequestMapping("/notif")
class NotificationController(val mailService: MailService) {

    @GetMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    fun send(@RequestParam to: String) {
        mailService.sendMessage(to, "Mail test", "Testing testing testing")
    }
}
