package pl.edu.uj.ii.mmatuszewski.grades.services

import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.grades.Course

@Service
class EctsCreditsProviderService : CourseDataProvider {

    override fun provide(course: Course) {
        course.ects = 1
    }
}
