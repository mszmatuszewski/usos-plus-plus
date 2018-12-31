package pl.edu.uj.ii.mmatuszewski.auth.model

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

const val ROLE_USER = "ROLE_USER"

@Entity
@Table(name = "users")
class User(@Id @GeneratedValue var id: Long = -1,
           private var name: String = "",
           private var pass: String = "{noop}",
           var calendarUrl: String? = null
) : UserDetails {

    override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority(ROLE_USER))

    override fun getUsername() = name

    override fun getPassword() = pass

    override fun isEnabled() = true

    override fun isCredentialsNonExpired() = true

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true
}
