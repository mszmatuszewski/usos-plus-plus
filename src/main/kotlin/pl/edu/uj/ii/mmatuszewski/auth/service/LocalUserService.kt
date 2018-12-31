package pl.edu.uj.ii.mmatuszewski.auth.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.auth.repository.UserRepository

@Service
class LocalUserService(val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
            userRepository.findByName(username) ?: throw UsernameNotFoundException("User $username not found!")
}
