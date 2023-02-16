package com.spinoza.moviesforfintech.domain.usecase

import com.spinoza.moviesforfintech.domain.repository.FilmsRepository
import javax.inject.Inject

class GetStateUseCase @Inject constructor(private val filmsRepository: FilmsRepository) {
    operator fun invoke() = filmsRepository.getState()
}