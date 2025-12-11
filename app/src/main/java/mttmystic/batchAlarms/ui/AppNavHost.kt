package mttmystic.batchAlarms.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import mttmystic.batchAlarms.ui.elements.AlarmsListScreen
import androidx.hilt.navigation.compose.hiltViewModel
import mttmystic.batchAlarms.ui.elements.CreateAlarmsScreen
import mttmystic.batchAlarms.ui.elements.SettingsScreen
import mttmystic.batchAlarms.ui.viewmodels.AlarmListViewModel
import mttmystic.batchAlarms.ui.viewmodels.CreateAlarmsViewModel
import mttmystic.batchAlarms.ui.viewmodels.SettingsViewModel

@Serializable
object AlarmList
@Serializable
object CreateAlarms
@Serializable
object SettingsRoute
@Composable
fun AppNavHost () {
    val navController  = rememberNavController()

    NavHost(navController = navController, startDestination = AlarmList) {
        composable<AlarmList> {
            val viewModel : AlarmListViewModel = hiltViewModel()
            AlarmsListScreen(
                viewModel,
                onClickFAB = {navController.navigate(route = CreateAlarms)},
                onClickSettings = {navController.navigate(route = SettingsRoute)})
        }
        composable<CreateAlarms> {
            val viewModel : CreateAlarmsViewModel = hiltViewModel()
            CreateAlarmsScreen(
                viewModel,
                onConfirm = {navController.navigate(route = AlarmList)},
                onDismiss = {navController.navigate(route = AlarmList)} )
        }
        composable<SettingsRoute> {
            val viewModel : SettingsViewModel = hiltViewModel()
            SettingsScreen(
                viewModel,
                onDismiss = {navController.navigate(route = AlarmList)}
            )
        }
    }
}