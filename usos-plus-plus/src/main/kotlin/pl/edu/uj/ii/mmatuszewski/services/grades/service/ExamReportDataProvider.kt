package pl.edu.uj.ii.mmatuszewski.services.grades.service

import com.github.scribejava.core.oauth.OAuth10aService
import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.core.auth.model.User
import pl.edu.uj.ii.mmatuszewski.core.auth.service.LocalUserService
import pl.edu.uj.ii.mmatuszewski.core.usos.service.AbstractUsosConnectorService
import pl.edu.uj.ii.mmatuszewski.services.grades.model.ExamReport

@Service
class ExamReportDataProvider(usosConnector: OAuth10aService, localUserService: LocalUserService) :
        AbstractUsosConnectorService<ExamReport>(usosConnector, localUserService) {

    override val serviceUrlPath: String = "services/examrep/exam"

    fun provide(owner: User, examId: String): ExamReport =
            sendAndReceive(owner,
                    mapOf("id" to examId, "fields" to "id|description|type_description|counts_into_average"))

    override fun extractResponse(responseBody: String): ExamReport =
            mapper.readValue(responseBody, ExamReport::class.java)
}
