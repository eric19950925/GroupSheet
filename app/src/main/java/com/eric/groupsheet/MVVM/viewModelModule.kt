package com.eric.groupsheet.MVVM

import com.eric.groupsheet.EditSheet.EditRule.EditRuleViewModel
import com.eric.groupsheet.EditSheet.EditRule.TextRecogCamViewModel
import com.eric.groupsheet.EditSheet.EditSheet.EditSheetViewModel
import com.eric.groupsheet.EditSheet.SheetAnalysis.SheetAnalysisViewModel
import com.eric.groupsheet.Life.LifeViewModel
import com.eric.groupsheet.LoginViewModel
import com.eric.groupsheet.MainHome.MainHomeViewModel
import com.eric.groupsheet.MainHome.SharedAccountViewModel
import com.eric.groupsheet.NameList.NameListViewModel
import com.eric.groupsheet.Sheet.SheetViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainHomeViewModel(
            get<Navigator>()::toNameListPage,
            get<Navigator>()::toAboutPage,
            get<Navigator>()::toSettingPage,
            get<Navigator>()::toNewSheetPage,
            get<Navigator>()::toEditSheetPage,
            get<Navigator>()::toAccountPage,
            get<Navigator>()::toLifePage
        )
    }
    viewModel {
        NameListViewModel()
    }
    viewModel {
        EditSheetViewModel(
            get<Navigator>()::toEditRulePage,
            get<Navigator>()::toAnalysisPage
        )
    }
    viewModel {
        SheetViewModel()
    }
    viewModel {
        EditRuleViewModel(
            get<Navigator>()::toSearchPage,
            get<Navigator>()::toCameraPage
        )
    }
    viewModel {
        SharedAccountViewModel()
    }
    viewModel {
        LifeViewModel(
            get<Navigator>()::toKey1Page,
            get<Navigator>()::toLifePage,
            get<Navigator>()::toMainHomePage
        )
    }
    viewModel {
        LoginViewModel(get<Navigator>()::toMainHomePage)
    }
    viewModel {
        SheetAnalysisViewModel()
    }
    viewModel {
        TextRecogCamViewModel()
    }
}