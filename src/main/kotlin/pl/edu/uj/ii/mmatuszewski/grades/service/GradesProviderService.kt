package pl.edu.uj.ii.mmatuszewski.grades.service

import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.grades.model.Course
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.round

@Service
class GradesProviderService : CourseDataProvider {

    val random: ThreadLocalRandom = ThreadLocalRandom.current()

    override fun provide(course: Course) {
        course.grade = fetchGrade(course)
    }

    private fun fetchGrade(course: Course): Double {
        return random.nextDouble(2.0, 5.0).roundToHalf()
    }

    private fun Double.roundToHalf(): Double {
        return round(this * 2) / 2.0
    }
}
