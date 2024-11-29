package data.model
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
data class Userdata(
    @PrimaryKey val id: Int? = null,
    val username: String,
    val name: String? = null,
    val token: String? = null
) : Parcelable
