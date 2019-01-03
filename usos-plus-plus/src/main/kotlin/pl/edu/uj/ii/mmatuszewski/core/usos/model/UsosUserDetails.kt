package pl.edu.uj.ii.mmatuszewski.core.usos.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UsosUserDetails(@JsonProperty("id") var id: String? = "",
                           @JsonProperty("first_name") var firstName: String? = "",
                           @JsonProperty("last_name") var lastName: String? = "",
                           @JsonProperty("email") var email: String? = "")
