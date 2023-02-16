package com.spinoza.moviesforfintech.domain.usecase

import com.spinoza.moviesforfintech.domain.repository.FilmsRepository
import javax.inject.Inject

class LoadAllFilmsUseCase @Inject constructor(private val filmsRepository: FilmsRepository) {
    suspend operator fun invoke() = filmsRepository.loadAllFilms()
}