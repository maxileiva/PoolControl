package com.example.poolcontrol.navigation

sealed class NavigationEvent{
    object  NavigateToHome : NavigationEvent()
    object  NavigateToLogin : NavigationEvent()
    object  NavigateToRegister : NavigationEvent()
    object  NavigateToLose : NavigationEvent()

}
