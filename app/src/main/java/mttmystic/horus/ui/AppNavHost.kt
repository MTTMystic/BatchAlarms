package mttmystic.horus.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import mttmystic.horus.ui.elements.AlarmsListScreen
import androidx.hilt.navigation.compose.hiltViewModel
import mttmystic.horus.ui.elements.CreateAlarmsScreen
import mttmystic.horus.ui.viewmodels.AlarmListViewModel
import mttmystic.horus.ui.viewmodels.CreateAlarmsViewModel

@Serializable
object AlarmList
@Serializable
object CreateAlarms

@Composable
fun AppNavHost () {
    val navController  = rememberNavController()

    NavHost(navController = navController, startDestination = AlarmList) {
        composable<AlarmList> {
            val viewModel : AlarmListViewModel = hiltViewModel()
            AlarmsListScreen(viewModel, onClickFAB = {navController.navigate(route = CreateAlarms)})
        }
        composable<CreateAlarms> {
            val viewModel : CreateAlarmsViewModel = hiltViewModel()
            CreateAlarmsScreen(
                viewModel,
                onConfirm = {navController.navigate(route = AlarmList)},
                onDismiss = {navController.navigate(route = AlarmList)} )
        }
    }
}