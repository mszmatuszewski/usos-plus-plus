package pl.edu.uj.ii.mmatuszewski.grades.services

import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.grades.Course
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.round

@Service
class GradesProviderService : CourseDataProvider {

    val random: ThreadLocalRandom = ThreadLocalRandom.current()

    override fun provide(course: Course) {
        course.grade = fetchGrade(course)
    }

    private fun fetchGrade(course: Course): Double {
        return roundToHalf(random.nextDouble(2.0, 5.0))
    }

    private fun roundToHalf(d: Double): Double {
        return round(d * 2) / 2.0
    }
}
