package pl.edu.uj.ii.mmatuszewski.services.grades.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.core.auth.model.User
import pl.edu.uj.ii.mmatuszewski.core.auth.service.LocalUserService
import pl.edu.uj.ii.mmatuszewski.services.grades.controller.CourseProvider
import pl.edu.uj.ii.mmatuszewski.services.grades.model.Course
import pl.edu.uj.ii.mmatuszewski.services.grades.model.CourseComparator
import pl.edu.uj.ii.mmatuszewski.services.grades.model.CourseLocator
import pl.edu.uj.ii.mmatuszewski.services.grades.model.CoursesUserRS

@Service
class CourseProviderService(private val userCoursesDataProvider: UserCoursesDataProvider,
                            private val courseEditionDataProvider: CourseEditionDataProvider,
                            private val courseDetailsDataProvider: CourseDetailsDataProvider,
                            private val examReportDataProvider: ExamReportDataProvider,
                            private val localUserService: LocalUserService) : CourseProvider {

    override fun provide(owner: String): List<Course> {
        val user = localUserService.loadUserByUsername(owner) as User
        val userCourses = userCoursesDataProvider.provide(owner)
        return extractResponse(user, userCourses).sortedWith(CourseComparator())
    }

    fun extractResponse(user: User, coursesUserRS: CoursesUserRS): List<Course> {
        val locators = coursesUserRS.courseEditions?.values?.flatten() ?: emptyList()

        val deferredEditions = locators
                .map { GlobalScope.async { populateCourses(user, it) } }
        val deferredDetails = locators
                .map { GlobalScope.async { courseDetailsDataProvider.provide(user, it) } }

        return runBlocking {
            val details = deferredDetails.map { it.await() }.map { it.id to it }.toMap()
            deferredEditions.flatMap { it.await() }.onEach { it.ects = details[it.courseId]?.ects ?: 0f }
        }
    }

    private suspend fun populateCourses(user: User, locator: CourseLocator): List<Course> {
        val edition = courseEditionDataProvider.provide(user, locator)
        val exams =
                edition.grades?.grades?.values
                        ?.flatMap { it.values }
                        ?.map { it.examId }
                        ?.map { examReportDataProvider.provide(user, it!!) }
                        ?.map { it.id!! to it }
                        ?.toMap() ?: emptyMap()

        val courses = edition.grades?.grades?.values?.flatMap { it.values }?.map {
            Course(name = "[${edition.termId!!}] ${edition.courseName!!["pl"]} (${exams[it.examId!!]?.description!!["pl"]})",
                    grade = it.valueSymbol ?: "",
                    termId = edition.termId,
                    countsIntoAverage = exams[it.examId]?.countsIntoAverage == "T",
                    courseId = edition.courseId!!)
        }
        return courses ?: emptyList()
    }
}
