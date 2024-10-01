package com.cbm.android.cbmcalculator.ui.calculcation

import androidx.lifecycle.ViewModel
import java.math.BigDecimal

class CalculationViewModel : ViewModel() {
    var currIndex = 0
    var cSI:kotlin.Int = 0
    var hc:kotlin.Int = 0
    var bracketOpen:kotlin.Int = 0
    var bracketIndex:kotlin.Int = 0 //current Selected Index
    val bracketAnsw = BigDecimal(0)
    val nowValue = ""
    var isSFMShowing: Boolean = false
    var isDotd: Boolean = false
    var hca:kotlin.Boolean = false
    var placeholders:kotlin.Boolean = false
    
    
}