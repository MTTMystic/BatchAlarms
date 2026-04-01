package mttmystic.batchAlarms.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import mttmystic.batchAlarms.ui.elements.listscreen.oldAlarmsListScreen
import androidx.hilt.navigation.compose.hiltViewModel
import mttmystic.batchAlarms.ui.elements.createscreens.CreateAlarmsScreen
//import mttmystic.batchAlarms.ui.elements.oldCreateAlarmsScreen
import mttmystic.batchAlarms.ui.elements.SettingsScreen
import mttmystic.batchAlarms.ui.viewmodels.AlarmListViewModel
import mttmystic.batchAlarms.ui.viewmodels.CreateAlarmBatchViewModel
import mttmystic.batchAlarms.ui.viewmodels.CreateAlarmViewModel
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
            oldAlarmsListScreen(
                viewModel,
                onClickFAB = {navController.navigate(route = CreateAlarms)},
                onClickSettings = {navController.navigate(route = SettingsRoute)})
        }
        //TODO refactor this
        composable<CreateAlarms> {
            val multiAlarmsViewModel : CreateAlarmBatchViewModel = hiltViewModel()
            val singleAlarmViewModel : CreateAlarmViewModel = hiltViewModel()
            CreateAlarmsScreen(
                multiAlarmsViewModel,
                singleAlarmViewModel,
                onDone = {navController.navigate(route = AlarmList)},
            )
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