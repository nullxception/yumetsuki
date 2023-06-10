package io.chaldeaprjkt.yumetsuki.data.checkin

import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInNote
import io.chaldeaprjkt.yumetsuki.data.checkin.source.CheckInNetworkSource
import io.chaldeaprjkt.yumetsuki.data.checkin.source.CheckInNoteDao
import io.chaldeaprjkt.yumetsuki.data.common.HoYoApiCode
import io.chaldeaprjkt.yumetsuki.data.common.HoYoData
import io.chaldeaprjkt.yumetsuki.data.common.HoYoError
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.domain.repository.CheckInRepo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.onEach

@Singleton
class CheckInRepoImpl
@Inject
constructor(
    private val checkInNetworkSource: CheckInNetworkSource,
    private val checkInNoteDao: CheckInNoteDao,
) : CheckInRepo {

    override val data = checkInNoteDao.all()
    private val dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override suspend fun sync(user: User, gameAccount: GameAccount) =
        checkInNetworkSource.status(cookie = user.cookie, game = gameAccount.game).onEach {
            if (it is HoYoData) {
                val yesterday = LocalDate.now().minusDays(1).format(dateFmt)
                val date = if (it.data.signed) it.data.today else yesterday
                CheckInNote(gameAccount.uid, user.uid, gameAccount.game, date).let { note ->
                    checkInNoteDao.insert(note)
                }
            }
        }

    override suspend fun checkIn(user: User, gameAccount: GameAccount) =
        checkInNetworkSource.checkIn(cookie = user.cookie, game = gameAccount.game).onEach {
            val signed =
                it is HoYoError.Api &&
                    (it.code == HoYoApiCode.CheckedIntoHoyolab ||
                        it.code == HoYoApiCode.ClaimedDailyReward)

            if (it is HoYoData || signed) {
                val today = LocalDate.now().format(dateFmt)
                CheckInNote(gameAccount.uid, user.uid, gameAccount.game, today).let { note ->
                    checkInNoteDao.insert(note)
                }
            }
        }
}
