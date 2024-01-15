package com.codelab.android.datastore.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.codelab.android.datastore.UserPreferences
import java.io.InputStream
import java.io.OutputStream

object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (e: Exception) {
            throw CorruptionException("Cannot read proto", e)
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) = t.writeTo(output)
}