package pl.edu.uj.ii.mmatuszewski.core.auth.controller

import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import pl.edu.uj.ii.mmatuszewski.core.auth.model.RegistrationForm
import pl.edu.uj.ii.mmatuszewski.core.auth.model.User
import pl.edu.uj.ii.mmatuszewski.core.auth.repository.UserRepository

@Controller
@RequestMapping("/user")
class RegistrationController(private val userRepository: UserRepository,
                             private val passwordEncoder: PasswordEncoder) {

    private val LOGGER = LoggerFactory.getLogger(RegistrationController::class.java)

    @GetMapping("/register")
    fun registrationForm(): String = "registration"

    @PostMapping("/register")
    fun processRegistration(form: RegistrationForm): String {
        val userInDatabase = userRepository.findByName(form.username)
        return when (userInDatabase) {
            null -> {
                userRepository.save(User(name = form.username, pass = passwordEncoder.encode(form.password)))
                LOGGER.debug("Registered ${form.username} with password ${form.password}")
                "redirect:/login"
            }
            else -> "redirect:/user/register?error"
        }
    }
}
