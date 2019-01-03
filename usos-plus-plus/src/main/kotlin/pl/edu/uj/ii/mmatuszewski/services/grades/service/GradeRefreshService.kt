package pl.edu.uj.ii.mmatuszewski.services.grades.service

import org.springframework.cache.annotation.CacheEvict
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.core.auth.repository.UserRepository

@Service
class GradeRefreshService(
        private val courseProviderService: CourseProviderService,
        private val userRepository: UserRepository) {

    @CacheEvict(allEntries = true, value = ["grades"], beforeInvocation = true)
    @Scheduled(fixedRate = 60 * 60 * 1000)
    fun refresh() {
        userRepository.findAll()
                .filter { it.usosAccessToken != null }
                .map { it.username }
                .forEach { courseProviderService.provide(it) }
    }
}
