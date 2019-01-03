package pl.edu.uj.ii.mmatuszewski.core.auth.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.core.auth.model.User
import pl.edu.uj.ii.mmatuszewski.core.auth.repository.UserRepository

@Service
class LocalUserService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
            userRepository.findByName(username) ?: throw UsernameNotFoundException("User $username not found!")

    fun saveAccessToken(user: User, token: String, secret: String) {
        user.usosAccessToken = token
        user.usosAccessSecret = secret
        userRepository.save(user)
    }

    fun saveUsosId(user: User, id: String) {
        user.usosUserId = id
        userRepository.save(user)
    }

    fun invalidateAccessToken(user: User) {
        user.usosAccessToken = null
        user.usosAccessSecret = null
        user.usosUserId = null
        userRepository.save(user)
    }
}
