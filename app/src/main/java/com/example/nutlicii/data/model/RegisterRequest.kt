package data.model

data class RegisterRequest(
    val name:String,
    val email:String,
    val username: String,
    val password: String,
    val repeatPassword:String
)
