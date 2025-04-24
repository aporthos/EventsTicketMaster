package com.globant.ticketmaster.feature.onboarding

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.globant.ticketmaster.core.models.ui.ScreenItem
import kotlinx.coroutines.launch

@Composable
fun OnboardingRoute(viewModel: OnboardingViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    OnboardingScreen(
        uiState = uiState,
        onStart = viewModel::onStart,
        onSkip = viewModel::onSkip,
    )
}

@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    onStart: () -> Unit,
    onSkip: () -> Unit,
) {
    when (uiState) {
        OnboardingUiState.Loading -> Unit
        is OnboardingUiState.Items ->
            OnboardingContent(
                items = uiState.items,
                onStart = onStart,
                onSkip = onSkip,
            )
    }
}

@Composable
fun OnboardingContent(
    items: List<ScreenItem>,
    onStart: () -> Unit,
    onSkip: () -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        val pagerState = rememberPagerState { items.size }
        val scope = rememberCoroutineScope()
        Column(modifier = Modifier.padding(padding)) {
            TextButton(
                modifier =
                    Modifier
                        .align(Alignment.End)
                        .padding(end = 16.dp),
                onClick = {
                    onSkip()
                },
            ) {
                Text(text = stringResource(R.string.button_skip))
            }
            HorizontalPager(modifier = Modifier.weight(1f), state = pagerState) { index ->
                OnboardingItem(screen = items[index])
            }

            BottomSection(
                size = items.size,
                index = pagerState.currentPage,
                onStart = onStart,
                onNextScreen = {
                    scope.launch {
                        if (pagerState.currentPage < items.size - 1) {
                            pagerState.scrollToPage(pagerState.currentPage + 1)
                        } else {
                            onStart()
                        }
                    }
                },
            )
        }
    }
}

@Composable
fun BottomSection(
    size: Int,
    index: Int,
    onStart: () -> Unit,
    onNextScreen: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Indicators(size = size, index = index)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp),
            onClick = {
                scope.launch {
                    if (index < size - 1) {
                        onNextScreen()
                    } else {
                        onStart()
                    }
                }
            },
        ) {
            val text =
                if (index < size - 1) R.string.button_next else R.string.button_start
            Text(text = stringResource(text))
        }
    }
}

@Composable
fun Indicators(
    size: Int,
    index: Int,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        repeat(size) {
            Indicator(isSelected = it == index)
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    val width =
        animateDpAsState(
            targetValue = if (isSelected) 25.dp else 10.dp,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            label = "",
        )

    Box(
        modifier =
            Modifier
                .height(10.dp)
                .width(width.value)
                .clip(CircleShape)
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0XFFF8E2E7),
                ),
    ) {
    }
}
