package pl.edu.uj.ii.mmatuszewski.services.grades.service

import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.core.auth.repository.UserRepository

@Service
class GradeRefreshService(
        private val courseProviderService: CourseProviderService,
        private val userRepository: UserRepository) {

    private val LOGGER = LoggerFactory.getLogger(GradeRefreshService::class.java)

    @CacheEvict(allEntries = true, value = ["grades"], beforeInvocation = true)
    @Scheduled(fixedRate = 60 * 60 * 1000)
    fun refresh() {
        userRepository.findAll()
                .filter { it.usosAccessToken != null }
                .map { it.username }
                .onEach { LOGGER.info("Refreshing grades for user: $it") }
                .forEach { courseProviderService.provide(it) }
    }
}
