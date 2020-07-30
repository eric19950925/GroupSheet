package com.eric.groupsheet.MVVM

import org.koin.dsl.module

val coordinator = module {
    single { Navigator() }
}