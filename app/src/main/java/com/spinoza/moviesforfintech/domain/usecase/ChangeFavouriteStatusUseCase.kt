package com.spinoza.moviesforfintech.domain.usecase

import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.repository.FilmsRepository
import javax.inject.Inject

class ChangeFavouriteStatusUseCase @Inject constructor(
    private val filmsRepository: FilmsRepository,
) {
    suspend operator fun invoke(film: Film) = filmsRepository.changeFavouriteStatus(film)
}