package com.globant.ticketmaster.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.ticketmaster.core.domain.usecases.CompleteOnboardingUseCase
import com.globant.ticketmaster.core.models.ui.ScreenItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.globant.ticketmaster.core.designsystem.R as Designsystem

@HiltViewModel
class OnboardingViewModel
    @Inject
    constructor(
        private val completeOnboardingUseCase: CompleteOnboardingUseCase,
        private val resources: OnboardingResourcesManager,
    ) : ViewModel() {
        val uiState: StateFlow<OnboardingUiState> =
            flow {
                emit(
                    OnboardingUiState.Items(
                        listOf(
                            ScreenItem(
                                title = resources.titleEvents,
                                message = resources.messageEvents,
                                image = Designsystem.raw.events,
                            ),
                            ScreenItem(
                                title = resources.titleFavorites,
                                message = resources.messageFavorites,
                                image = Designsystem.raw.favorites,
                            ),
                            ScreenItem(
                                title = resources.titleLastVisited,
                                message = resources.messageLastVisited,
                                image = Designsystem.raw.saved,
                            ),
                        ),
                    ),
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = OnboardingUiState.Loading,
            )

        fun onStart() =
            viewModelScope.launch {
                completeOnboardingUseCase(Unit)
            }

        fun onSkip() =
            viewModelScope.launch {
                completeOnboardingUseCase(Unit)
            }
    }
