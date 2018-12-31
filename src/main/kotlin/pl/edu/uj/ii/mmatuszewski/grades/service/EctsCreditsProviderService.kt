package pl.edu.uj.ii.mmatuszewski.grades.service

import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.grades.model.Course

@Service
class EctsCreditsProviderService : CourseDataProvider {

    override fun provide(course: Course) {
        course.ects = 1
    }
}
