package com.spinoza.moviesforfintech.domain.usecase

import com.spinoza.moviesforfintech.domain.repository.FilmsRepository
import com.spinoza.moviesforfintech.domain.repository.ScreenType
import javax.inject.Inject

class SwitchSourceToUseCase @Inject constructor(
    private val filmsRepository: FilmsRepository,
) {
    suspend operator fun invoke(target: ScreenType) = filmsRepository.switchSourceTo(target)
}
