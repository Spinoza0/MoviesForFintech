package com.spinoza.moviesforfintech.presentation.activity

import com.spinoza.moviesforfintech.domain.repository.SourceType

interface OnFragmentSendDataListener {
    operator fun invoke(sourceType: SourceType)
}