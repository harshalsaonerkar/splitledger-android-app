package com.splitledger.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.splitledger.app.data.preferences.TokenManager
import com.splitledger.app.ui.screens.balance.BalancesScreen
import com.splitledger.app.ui.screens.balance.BalancesViewModel
import com.splitledger.app.ui.screens.expense.AddExpenseScreen
import com.splitledger.app.ui.screens.expense.AddExpenseViewModel
import com.splitledger.app.ui.screens.group.CreateGroupScreen
import com.splitledger.app.ui.screens.group.CreateGroupViewModel
import com.splitledger.app.ui.screens.group.GroupDetailScreen
import com.splitledger.app.ui.screens.group.GroupDetailViewModel
import com.splitledger.app.ui.screens.home.HomeScreen
import com.splitledger.app.ui.screens.home.HomeViewModel
import com.splitledger.app.ui.screens.login.LoginScreen
import com.splitledger.app.ui.screens.login.LoginViewModel
import com.splitledger.app.ui.screens.register.RegisterScreen
import com.splitledger.app.ui.screens.register.RegisterViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val CREATE_GROUP = "create_group"
    const val GROUP_DETAIL = "group/{groupId}"
    const val ADD_EXPENSE = "add_expense/{groupId}"
    const val BALANCES = "balances/{groupId}"

    fun groupDetail(groupId: String) = "group/$groupId"
    fun addExpense(groupId: String) = "add_expense/$groupId"
    fun balances(groupId: String) = "balances/$groupId"
}

@Composable
fun AppNavigation(tokenManager: TokenManager) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = LoginViewModel(tokenManager),
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = RegisterViewModel(tokenManager),
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                viewModel = HomeViewModel(tokenManager),
                tokenManager = tokenManager,
                onGroupClick = { groupId ->
                    navController.navigate(Routes.groupDetail(groupId))
                },
                onCreateGroup = {
                    navController.navigate(Routes.CREATE_GROUP)
                },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.CREATE_GROUP) {
            CreateGroupScreen(
                viewModel = CreateGroupViewModel(tokenManager),
                onGroupCreated = { groupId ->
                    navController.navigate(Routes.groupDetail(groupId)) {
                        popUpTo(Routes.CREATE_GROUP) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.GROUP_DETAIL) { backStackEntry ->
            val groupId = backStackEntry.arguments
                ?.getString("groupId") ?: return@composable
            GroupDetailScreen(
                groupId = groupId,
                viewModel = GroupDetailViewModel(tokenManager),
                onAddExpense = { id ->
                    navController.navigate(Routes.addExpense(id))
                },
                onViewBalances = { id ->
                    navController.navigate(Routes.balances(id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ADD_EXPENSE) { backStackEntry ->
            val groupId = backStackEntry.arguments
                ?.getString("groupId") ?: return@composable
            AddExpenseScreen(
                groupId = groupId,
                viewModel = AddExpenseViewModel(tokenManager),
                onExpenseAdded = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.BALANCES) { backStackEntry ->
            val groupId = backStackEntry.arguments
                ?.getString("groupId") ?: return@composable
            BalancesScreen(
                groupId = groupId,
                viewModel = BalancesViewModel(tokenManager),
                onBack = { navController.popBackStack() }
            )
        }
    }
}