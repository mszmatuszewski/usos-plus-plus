package pl.edu.uj.ii.mmatuszewski.auth.controller

import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import pl.edu.uj.ii.mmatuszewski.auth.model.RegistrationForm
import pl.edu.uj.ii.mmatuszewski.auth.model.User
import pl.edu.uj.ii.mmatuszewski.auth.repository.UserRepository
import javax.annotation.PostConstruct

@Controller
@RequestMapping("/user")
class RegistrationController(val userRepository: UserRepository, val passwordEncoder: PasswordEncoder) {

    private val LOGGER = LoggerFactory.getLogger(RegistrationController::class.java)

    @GetMapping("/register")
    fun registrationForm(): String = "registration"

    @PostMapping("/register")
    fun processRegistration(form: RegistrationForm): String {
        userRepository.save(User(name = form.username, pass = passwordEncoder.encode(form.password)))
        LOGGER.debug("Registered ${form.username} with password ${form.password}")
        return "redirect:/login"
    }

    @PostConstruct
    fun registerDummy() {
        //TODO: remove before deploy
        userRepository.save(User(name = "user",
                pass = passwordEncoder.encode("user"),
                calendarUrl = "webcal://apps.usos.uj.edu.pl/services/tt/upcoming_ical?lang=pl&user_id=208972&key=EkCXnYhMcwNT9g8zwqyD"))
    }
}
