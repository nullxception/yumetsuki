package io.chaldeaprjkt.yumetsuki.data.user

import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.data.user.source.UserDao
import io.chaldeaprjkt.yumetsuki.data.user.source.UserNetworkSource
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepoImpl
@Inject
constructor(
    private val userDao: UserDao,
    private val networkUserInfoSource: UserNetworkSource,
) : UserRepo {

    override val users
        get() = userDao.all()

    override suspend fun activeUserOf(game: HoYoGame) = userDao.activeUserOf(game)

    override suspend fun fetch(cookie: String) = networkUserInfoSource.fetch(cookie = cookie)

    override suspend fun ofId(uid: Int) = userDao.ofUID(uid)

    override suspend fun add(user: User) {
        userDao.insert(user)
    }

    override suspend fun clear(user: User) {
        userDao.delete(user)
    }

    override suspend fun update(user: User) {
        userDao.update(user)
    }
}
