package com.example.noteshare.notes.navigation

import com.example.noteshare.notes.list.model.NoteListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

enum class NoteRoute {
    NotesList,
    AddNote
}

enum class NavigationDirection(val value: Int) {
    Forward(1),
    Backward(-1)
}

class NoteRouterViewModel {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _backstack = mutableListOf<NoteRoute>(NoteRoute.NotesList)

    private val _currentScreen = MutableStateFlow(NoteRoute.NotesList)
    val currentScreen: StateFlow<NoteRoute> = _currentScreen.asStateFlow()

    private val _navigationDirection = MutableStateFlow(NavigationDirection.Forward)
    val navigationDirection: StateFlow<NavigationDirection> = _navigationDirection.asStateFlow()

    fun navigateTo(
        screen: NoteRoute,
        direction: NavigationDirection = NavigationDirection.Forward
    ) {
        _navigationDirection.value = direction
        _backstack.add(screen)
        _currentScreen.value = screen
    }

    fun popBack() {
        if (_backstack.size > 1) {
            _navigationDirection.value = NavigationDirection.Backward
            _backstack.removeLast()
            _currentScreen.value = _backstack.last()
        }
    }

    fun resetToRoot() {
        _navigationDirection.value = NavigationDirection.Backward
        _backstack.clear()
        _backstack.add(NoteRoute.NotesList)
        _currentScreen.value = NoteRoute.NotesList
    }

    // NOTE: Only used for iOS
    fun collectState(collector: (Pair<NoteRoute, NavigationDirection>) -> Unit) {
        scope.launch(Dispatchers.Main) {
            currentScreen.combine(navigationDirection) { route, direction ->
                Pair(route, direction)
            }.collect {
                collector(it)
            }
        }
    }
}
