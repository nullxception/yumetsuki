package io.chaldeaprjkt.yumetsuki.data.store

import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

inline fun <reified T> yumeDataStore(moshi: Moshi, key: String, default: T) =
    dataStore("$key.json", MoshiDataStoreSerializer(moshi, T::class.java, default))

class MoshiDataStoreSerializer<T>(
    private val moshi: Moshi,
    private val type: Class<T>,
    private val default: T
) : Serializer<T> {

    override val defaultValue get() = default

    override suspend fun readFrom(input: InputStream): T = try {
        val json = input.readBytes().decodeToString()
        moshi.adapter(type).fromJson(json) ?: default
    } catch (e: JsonDataException) {
        e.printStackTrace()
        default
    }

    override suspend fun writeTo(t: T, output: OutputStream) = withContext(Dispatchers.IO) {
        val json = moshi.adapter(type).toJson(t)
        output.write(json.encodeToByteArray())
    }
}
