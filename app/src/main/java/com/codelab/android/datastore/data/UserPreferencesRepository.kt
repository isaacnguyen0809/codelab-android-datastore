/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelab.android.datastore.data

import androidx.datastore.core.DataStore
import com.codelab.android.datastore.UserPreferences
import com.codelab.android.datastore.UserPreferences.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

/**
 * Class that handles saving and retrieving user preferences
 */
class UserPreferencesRepository(
    private val userPreferDataStore: DataStore<UserPreferences>
) {

    val userPreferencesFlow: Flow<UserPreferences> = userPreferDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun updateShowCompleted(showCompleted: Boolean) {
        userPreferDataStore.updateData { prefs ->
            prefs.toBuilder().setShowCompleted(showCompleted).build()
        }
    }


    suspend fun enableSortByDeadline(enable: Boolean) {
        userPreferDataStore.updateData { prefs ->
            val currentOrder = prefs.sortOrder
            val newSortOrder = if (enable) {
                if (currentOrder == SortOrder.BY_PRIORITY) {
                    SortOrder.BY_DEADLINE_AND_PRIORITY
                } else {
                    SortOrder.BY_DEADLINE
                }
            } else {
                if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
                    SortOrder.BY_PRIORITY
                } else {
                    SortOrder.NONE
                }
            }
            prefs.toBuilder().setSortOrder(newSortOrder).build()
        }
    }

    suspend fun enableSortByPriority(enable: Boolean) {
        userPreferDataStore.updateData { prefs ->
            val currentOrder = prefs.sortOrder
            val newSortOrder =
                if (enable) {
                    if (currentOrder == SortOrder.BY_DEADLINE) {
                        SortOrder.BY_DEADLINE_AND_PRIORITY
                    } else {
                        SortOrder.BY_PRIORITY
                    }
                } else {
                    if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
                        SortOrder.BY_DEADLINE
                    } else {
                        SortOrder.NONE
                    }
                }
            prefs.toBuilder().setSortOrder(newSortOrder).build()
        }

    }
}
