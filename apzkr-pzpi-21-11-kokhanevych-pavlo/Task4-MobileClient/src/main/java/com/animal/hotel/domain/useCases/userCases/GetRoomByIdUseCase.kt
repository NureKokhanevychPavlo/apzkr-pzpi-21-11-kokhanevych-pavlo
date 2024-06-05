package com.animal.hotel.domain.useCases.userCases

import com.animal.hotel.domain.models.user.Room
import com.animal.hotel.domain.sources.UserSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRoomByIdUseCase @Inject constructor(
    private val userSource: UserSource
) {

    suspend operator fun invoke(roomId: Int): List<Room> {
        return withContext(Dispatchers.IO) {
            userSource.getRoomById(roomId)
        }
    }
}