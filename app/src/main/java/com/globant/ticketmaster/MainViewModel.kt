package com.globant.ticketmaster

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.ticketmaster.core.domain.usecases.GetOnboardingUseCase
import com.globant.ticketmaster.navigation.AppSections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val getOnboardingUseCase: GetOnboardingUseCase,
    ) : ViewModel() {
        val showOnboarding: StateFlow<AppSections> =
            getOnboardingUseCase(Unit)
                .map { canShowOnboarding ->
                    if (canShowOnboarding) {
                        AppSections.Onboarding
                    } else {
                        AppSections.Home
                    }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    initialValue = AppSections.Home,
                )
    }
