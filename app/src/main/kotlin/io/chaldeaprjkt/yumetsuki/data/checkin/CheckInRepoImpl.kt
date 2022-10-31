package io.chaldeaprjkt.yumetsuki.data.checkin

import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInNote
import io.chaldeaprjkt.yumetsuki.data.checkin.source.CheckInNetworkSource
import io.chaldeaprjkt.yumetsuki.data.checkin.source.CheckInNoteDao
import io.chaldeaprjkt.yumetsuki.data.common.HoYoData
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.domain.repository.CheckInRepo
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckInRepoImpl @Inject constructor(
    private val checkInNetworkSource: CheckInNetworkSource,
    private val checkInNoteDao: CheckInNoteDao,
) : CheckInRepo {

    override val data = checkInNoteDao.all()

    override suspend fun sync(user: User, gameAccount: GameAccount) =
        checkInNetworkSource.status(cookie = user.cookie, game = gameAccount.game)
            .onEach {
                if (it is HoYoData && it.data.signed) {
                    CheckInNote(gameAccount.uid, user.uid, gameAccount.game, it.data.today)
                        .let { note -> checkInNoteDao.insert(note) }
                }
            }

    override suspend fun checkIn(user: User, gameAccount: GameAccount) =
        checkInNetworkSource.checkIn(cookie = user.cookie, game = gameAccount.game)
            .onEach {
                if (it is HoYoData) {
                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val today = formatter.format(Calendar.getInstance().time)
                    CheckInNote(gameAccount.uid, user.uid, gameAccount.game, today)
                        .let { note -> checkInNoteDao.insert(note) }
                }
            }
}
