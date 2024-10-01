package com.cbm.android.cbmcalculator.ui.calculcation

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.cbm.android.cbmcalculator.ASelf
import com.cbm.android.cbmcalculator.MainActivity
import com.cbm.android.cbmcalculator.R
import com.cbm.android.cbmcalculator.anim.MediumAnims
import com.cbm.android.cbmcalculator.custom.AnswerListItem
import com.cbm.android.cbmcalculator.databinding.LayoutMainBinding
import com.cbm.android.cbmcalculator.extended.CBMOptions
import com.cbm.android.cbmcalculator.settings.SettingsActivity
import com.cbm.android.cbmcalculator.utility.Ut
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import java.math.BigDecimal

class CalculationFragment : Fragment() {

    private lateinit var aSelf: ASelf
    private lateinit var ad: AlertDialog
    lateinit var tvCurrent: TextView
    lateinit var btnPoint:android.widget.Button
    lateinit var btnMASMD:android.widget.Button
    lateinit var btnPercentage:android.widget.Button
    lateinit var btnEqual:Button
    lateinit var btnHistory:Button
    lateinit var btnSettings:Button
    
    lateinit var binding:LayoutMainBinding
    lateinit var cvm: CalculationViewModel
    companion object {
        fun newInstance() = CalculationFragment()
    }

    private val viewModel: CalculationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cvm = ViewModelProvider(requireActivity()).get(CalculationViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutMainBinding.inflate(getLayoutInflater())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        try {
            aSelf = ASelf.get(getActivity())
            ad = AlertDialog.Builder(getActivity()).create()

//            binding.llNC.getViewTreeObserver().addOnGlobalLayoutListener(OnGlobalLayoutListener {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    val screenH: Int =
//                        requireActivity().windowManager.getMaximumWindowMetrics().getBounds().height()
//                    val appWH: Int = requireActivity().window.getDecorView().getHeight()
//
//                    if (appWH <= ((screenH.toString() + "").toFloat() / 1.6f)) {
//                        binding.tvDoneCalculation.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
//                        Toast.makeText(
//                            getActivity(),
//                            "CBMCalc Height less than half of screen",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else {
//                        binding.tvDoneCalculation.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_NONE)
//                    }
//                }
//            })
            binding.llScifra.setFocusable(true)
            binding.llScifra.setClickable(true)
            aSelf.setStandBy(true)
            MediumAnims.scifraMenu(binding.svScifra, false)
            initEntry()
            applyOnClick()
            glBtns()
            cvm.hc = 0
            cvm.hca = false
            clearC()
            registerForContextMenu(binding.btnMInverse)
            applySettings()
            val ctx =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            binding.etExpression.addTextChangedListener(object:TextWatcher {
                override fun beforeTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(text: Editable?) {
                    ctx.hideSoftInputFromWindow(binding.etExpression.getWindowToken(), 0)
//                    if(text.toString().startsWith("0")) {
//                        binding.etExpression.
//                    }

                    val c = binding.etExpression.getText().toString()
//                    if(c.startsWith("0")&&binding.etExpression.selectionStart==1&&text.toString().equals("0", false)){return}
                    /*if (c.isEmpty()) {
                        text!!.append("0")
                    } else*/
//                    if(!text!!.toString().endsWith("."))text!!.replace(0, text.length, BigDecimal(text.toString()).toString())
                    if(c.startsWith("0")&&!c.equals("0", false)) {
                        var i = 0;
                        c.forEach {ch->if(ch.toString().equals("0", false)){
                            if(!text.toString().equals("0", false)){text!!.delete(0, 1)}}else{return@forEach}  }
                        text!!.delete(0, i);
                    }
                    if (aSelf.arr.size < 1) {
                        aSelf.arr.add(text.toString())
                    }
                    if (cvm.currIndex > -1) {
                        if (ASelf.isNumber(text.toString())) {
                            tvCurrent.setText(text)
                            if (aSelf.arr.size > 0) {
                                aSelf.arr.set(cvm.currIndex, text.toString())
                            }
                        }
                    }
                }
            });

            binding.etExpression.viewTreeObserver.addOnGlobalLayoutListener {
                ctx.hideSoftInputFromWindow(binding.etExpression.getWindowToken(), 0)

            }
//            addCurrentTV()
            val dHistory = resources.getDrawable(R.drawable.svg_history)
            //Drawable dOptions = getResources().getDrawable(R.drawable.svg_options);
            val dSettings = resources.getDrawable(R.drawable.svg_settings)

            dHistory.setBounds(0, 0, 100, 100)
            //dOptions.setBounds(0,0,100,100);
            dSettings.setBounds(0, 0, 100, 100)

            binding.btnMSettings.setCompoundDrawables(dSettings, null, null, null)
            //binding.btnSci.setCompoundDrawables(dOptions,null,null,null);
            binding.btnMHistory.setCompoundDrawables(dHistory, null, null, null)

        } catch (ex: Exception) {
//            Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace()
        }

        tvCurrent.addTextChangedListener{
            text: Editable? ->
                try {
                    if(cvm.currIndex>-1){if(ASelf.isNumber(text.toString())){aSelf.arr.set(cvm.currIndex, text.toString())}}

                } catch (ex:Exception) {
                    ex.printStackTrace();
                }
        }

    }

    override fun onPause() {
        super.onPause()
        try {
            val sped = ASelf.getCalcSet(getActivity()).edit()
            if (aSelf.arr.size > 0 && !ASelf.phOn) {
                sped.putString("RecentExp", Ut.toJSON(aSelf.arr))
            } else {
                sped.putString("RecentExp", "")
            }
            sped.apply()

            //aSelf.onSave(MainActivity.getActivity(), -1);
            clearC()
        } catch (ex: Exception) {
//            +(), Toast.LENGTH_LONG).show();
            ex.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            applySettings()
            clearC()
            val sp = ASelf.getCalcSet(getActivity())
            if (!sp.getString("RecentExp", "")!!.isEmpty()) {
                aSelf.setStandBy(false)
                //aSelf.phOn = false;
                val l = Ut.toArray(sp.getString("RecentExp", "")) as List<String>
                for (x in l.indices) {
                    val str = l[x].toString()
                    if (!str.isEmpty()) {
                        entry(str)
                    }
                }
                sp.edit().putString("RecentExp", "").apply()
                //btnEqual.callOnClick();
            } else if (sp.getString(
                    resources.getString(R.string.stand_by_example_switch),
                    "On"
                ) == "On"
            ) {
                if (aSelf.getSBEg(getActivity()) == 1) placeholders()
            } else {
                clearC()

                //aSelf.onSaved(MainActivity.getActivity(), 1, binding.llCalculation, binding.tvDoneCalculation);
            }
        } catch (ex: Exception) {
//            Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace()
        }
    }

//    override fun onBackPressed() {
//        if (binding.svScifra.getVisibility() == View.VISIBLE) {
//            MediumAnims.scifraMenu(binding.svScifra, false)
//            return
//        }
//
//        super.onBackPressed()
//    }
    override fun onCreateContextMenu(cm: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        for (x in ASelf.strSigns().indices) {
            (cm as Menu).add(
                41 + x, 0 + x, Menu.NONE,
                ASelf.strSigns()[x]
            )
        }
    }

    override fun onContextItemSelected(mi: MenuItem): Boolean {
        when (mi.groupId) {
            41, 42, 43, 44, 45, 46 -> {
                aSelf.arr.set(aSelf.tvCInd, ASelf.strSigns()[mi.itemId])
                tvCurrent.setText(aSelf.arr.get(aSelf.tvCInd))
                aSelf.arrAct(getActivity())
                return true
            }
        }
        return false
    }

    /*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //aSelf = ASelf.get(getActivity());
        aSelf.setStandBy(false);
        clearC();
        String exp = savedInstanceState.getString("exp", "");
        for(int s=0; s<exp.length(); s++) {
            aSelf.entry(getActivity(),exp.charAt(s)+"");
        }

        Toast.makeText(getActivity(), "exp: "+exp+" arr:"+aSelf.arr.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String exp ="";
        for(int s=0; s<aSelf.arr.size(); s++) {
            exp+=aSelf.arr.get(s);
        }
        outState.putString("exp", exp);
        super.onSaveInstanceState(outState);

        Toast.makeText(getActivity(), aSelf.arr.toString(), Toast.LENGTH_LONG).show();
    }*/
    fun addCurrentTV() {
        addCurrentTV("")
    }

    fun addCurrentTV(t:String) {
        var txt = t
        val tv = TextView(activity);

        tv.setPadding(resources.getDimensionPixelSize(R.dimen.dim8dp),resources.getDimensionPixelSize(R.dimen.dim8dp),resources.getDimensionPixelSize(R.dimen.dim8dp),resources.getDimensionPixelSize(R.dimen.dim8dp))
        tv.tag=binding.llNC.getChildCount().toString();
        cvm.currIndex=binding.llNC.getChildCount()
        tv.setBackgroundResource(R.drawable.bg_black_s)
        tv.setOnClickListener {
            if(ASelf.isNumber((it as TextView).text.toString())) {
                tvCurrent = it
                cvm.currIndex=it.tag.toString().toInt()
                binding.etExpression.setText(it.text)
            }
        }
        aSelf.arr.add(txt)
        if(txt.isEmpty()){txt="_"}
        tv.setText(txt);
        tvCurrent = tv;
        binding.llNC.addView(tv);
    }

    fun addCalculatedTV(jo: JSONObject): AnswerListItem? {
        try {
            val tv = AnswerListItem(activity)

            //((MainActivity)c).registerForContextMenu(tv);
            val lp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            tv.layoutParams = lp

            //Drawable d = getResources().getDrawable(R.drawable.bg_grey_green_cc, null);
            var d = resources.getDrawable(R.drawable.bg_grey_green_s, null)
            val clr = Color.parseColor("#EEFFFFFF")
            try {
                if (Ut.jsonContainsP(jo, ASelf.Calculation.dType.toString()) &&
                    jo.getInt(ASelf.Calculation.dType.toString()) == 1
                ) {
                    //d=getResources().getDrawable(R.drawable.bg_accent_cc, null);
                    d = resources.getDrawable(R.drawable.bg_accent_s, null)
                    //clr = Color.BLACK;
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                //d = getResources().getDrawable(R.drawable.bg_grey_green_cc, null);
                d = resources.getDrawable(R.drawable.bg_grey_green_s, null)
            }

            tv.alpha = if (cvm.placeholders) 0.5f else 1f
            tv.gravity = Gravity.CENTER
            tv.rlALI.background = d
            tv.tvAns.setTextColor(clr)
            //tv.tvAnsNr.setText("@"+(ASelf.isNumber(jo.getString(ASelf.Calculation.Ix.toString()))?jo.getString(ASelf.Calculation.LmtIx.toString()):jo.getString(ASelf.Calculation.Ix.toString())));
            tv.tvAnsNr.text = "@" + jo.getString(ASelf.Calculation.LmtIx.toString())
            tv.tvAnsCalc.text = jo.getString(ASelf.Calculation.Exp.toString())

            tv.tvAns.text = if (Ut.jsonContainsP(
                    jo,
                    ASelf.Calculation.ExpAns.toString()
                )
            ) jo.getString(ASelf.Calculation.ExpAns.toString()) else jo.getString(ASelf.Calculation.DoneCalc.toString())
            //tv.setTag(((MainActivity)c).llCalculation.getChildCount()+"");
            if (ASelf.isNumber(jo.getString(ASelf.Calculation.Ix.toString()))) {
                tv.setOnClickListener(
                    onAnsCalcClick(
                        jo.getString(ASelf.Calculation.Ix.toString()).toInt()
                    )
                )
            }
//            if (jo.has(activity!!.getResources().getString(R.string.answer_for))) {
//                tv.setOtherInfo(jo[activity!!.getResources().getString(R.string.answer_for)].toString())
//            }
            binding.llCalculation.addView(tv)

            return tv
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

        return null
    }

    fun addCalculatedTV(jo: JSONObject, dType: Int): AnswerListItem? {
        try {
            jo.put(ASelf.Calculation.dType.toString(), dType)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return addCalculatedTV(jo)
    }

    fun onAnsCalcClick(ansIndx: Int): View.OnClickListener {
        return View.OnClickListener { v ->
            try {
                if (binding.llCalculation.childCount > 0) {
                    if (ASelf.get(requireActivity()).hcb) {
                        clearC()
                        ASelf.get(requireActivity()).onSaved(
                            requireActivity(),
                            ansIndx,
                            binding.llCalculation,
                            binding.tvDoneCalculation
                        )
                        ASelf.get(requireActivity()).setHCB(false)
                        //Toast.makeText(c, ansIndx+":@", Toast.LENGTH_SHORT).show();
                    } else {
                        clearCTVS()
                        if (binding.llCalculation.getChildAt(binding.llCalculation.childCount - 1) != v) {
                            binding.llCalculation.getChildAt(ansIndx).isSelected =
                                true
                            for (n in 0 until binding.llNC.childCount) {
                                binding.llNC.getChildAt(n).isSelected =
                                    false
                            }
                            for (x in 1 until binding.llCalculation.childCount) {
                                val ansV =
                                    binding.llCalculation.getChildAt(x) as AnswerListItem
                                if (ansV.isSelected) {
                                    val showIndx = 0
                                    val ja = JSONArray(ansV.getOtherInfo())
                                    if (ja.length() > 0) binding.llNC.getChildAt(
                                        ja.getInt(
                                            0
                                        )
                                    ).isSelected =
                                        true
                                    if (ja.length() > 1) {
                                        binding.llNC.getChildAt(ja.getInt(1)).isSelected =
                                            true

                                        //showIndx = ((MainActivity) c).llNC.getChildAt(ja.getInt(0)).getX()<((MainActivity) c).hsvNC.getScrollX()?ja.getInt(0):ja.getInt(1);
                                    }
                                    if (ja.length() > 2) {
                                        binding.llNC.getChildAt(ja.getInt(2)).isSelected =
                                            true
                                        //showIndx = ((MainActivity) c).llNC.getChildAt(ja.getInt(0)).getX()<0?ja.getInt(0):ja.getInt(2);
                                    }
                                    binding.svCalculation.smoothScrollTo(0, Math.round(ansV.y))
                                    binding.hsvNowCalculation.smoothScrollTo(
                                        Math.round(
                                            binding.llNC.getChildAt(
                                                ja.getInt(0)
                                            ).x
                                        ), 0
                                    )
                                    break
                                }
                            }
                        }
                    }
                }
            } catch (ex: java.lang.Exception) {
                Toast.makeText(requireActivity(), ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun placeholders() {
        cvm.placeholders = true
        aSelf.setStandBy(false)
        var exp = ASelf.getCalcSet(getActivity()).getString(
            resources.getString(R.string.stand_by_example_default_expression_texttcn),
            "TextTCN_Stand_By_+Example_Default_Expression"
        )
        if (exp!!.isEmpty()) {
            exp = "1+1-1×1÷1"
        }
        if(exp.length>2) {
            for (s in 0 until exp.length) {
                entry(exp[s].toString() + "")
            }
        }

        btnEqual.callOnClick()

        cvm.placeholders = false
        ASelf.phOn = true
        aSelf.setStandBy(true)
    }

    fun entry(value: String) {
        if (ASelf.isNumber(value)||value.equals(".", false)) {
            binding.etExpression.text.append(value)
        } else {
//            var strEt: String = binding.etExpression.getText().toString()
//            strEt = if (strEt.isEmpty()) "0" else strEt
//            addCurrentTV(strEt)
            binding.etExpression.setText(binding.etExpression.text.toString().ifEmpty { "0" })
            addCurrentTV(value)
            addCurrentTV()
            binding.etExpression.setText("")
        }
    }

    private fun applySettings() {
        try {
            if (!ASelf.getCalcSet(getActivity())
                    .contains(resources.getString(R.string.stand_by_example_default_expression_texttcn))
            ) {
                SettingsActivity.resetSettings(getActivity())
            }
            val dl = ASelf.getCalcSet(getActivity())
                .getString(resources.getString(R.string.decimal_count_textn), "1")!!
                .toInt()
            try {
                ASelf.mDF.maximumFractionDigits = dl
                ASelf.mDF.minimumFractionDigits = dl
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            ASelf.carryAnswer = ASelf.getCalcSet(getActivity())
                .getString(resources.getString(R.string.history_carry_answer_switch), "Off") == "On"
            /*llDoneCalculation.setBackground(DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(getActivity()).getString(getResources().getString(R.string.answer_color),""))));
            binding.tvDoneCalculation.setTextColor(ASelf.getCalcSet(getActivity()).getInt(getResources().getString(R.string.answer_textc),Color.BLACK));
            ((LinearLayout)binding.llEntry.getChildAt(3)).getChildAt(2).setBackgroundResource(R.drawable.bg_accent_sr);
            ((Button)((LinearLayout)binding.llEntry.getChildAt(3)).getChildAt(2)).setTextColor(Color.WHITE);
            llLM.setBackgroundColor(ASelf.getCalcSet(getActivity()).getInt(getResources().getString(R.string.background_color),Color.WHITE));
            binding.llEntry.setBackground(DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(getActivity()).getString(getResources().getString(R.string.background_entry_color),""))));
            llToolbar.setBackground(DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(getActivity()).getString(getResources().getString(R.string.background_toolbar_color),""))));
            aSelf.setEntryNumberColors(requireActivity(), DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(getActivity()).getString(getResources().getString(R.string.numbers_color),""))), ASelf.getCalcSet(getActivity()).getInt(getResources().getString(R.string.numbers_textc),0));
            aSelf.setSymbolColors(requireActivity(), DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(getActivity()).getString(getResources().getString(R.string.symbols_color),""))), ASelf.getCalcSet(getActivity()).getInt(getResources().getString(R.string.symbols_textc),0));

            hsvNC.setBackground(DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(getActivity()).getString(getResources().getString(R.string.calculation_now_color),""))));
            binding.svCalculation.setBackground(DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(getActivity()).getString(getResources().getString(R.string.calculation_details_color),""))));
            */
        } catch (ex: Exception) {
            //Toast.makeText(getActivity(), ex.getMessage(),Toast.LENGTH_SHORT).show();
            ex.printStackTrace()
        }
    }

    private fun glBtns() {
        try {
            binding.btnMHistory.setOnClickListener(View.OnClickListener {
                CBMOptions.funHistory(
                    getActivity(),
                    arrayOf<Any>(binding.llCalculation, binding.tvDoneCalculation)
                )
            })
            //binding.btnMASMD=((Button)requireActivity().findViewById(R.id.binding.btnMASMD));
            //binding.btnMASMD.setEnabled(false);
            binding.btnMASMD.setOnClickListener(View.OnClickListener { /*if (aSelf.arr.size() > 0&&aSelf.isStandBy())
                            CBMOptions.funMADS(MainActivity.getActivity(), new Object[]{binding.llCalculation, binding.llNC, btnEqual, aSelf.calcNm, ad});
                            */
                try {
                    if (aSelf.arr.size > 0 && aSelf.isStandBy()) {
                        binding.btnMASMD.setSelected(!binding.btnMASMD.isSelected())
                        if (binding.btnMASMD.isSelected()) {
                            binding.tvDoneCalculation.setText(Ut.toBodmas(aSelf.arr)[0])
                        } else {
                            binding.tvDoneCalculation.setText(ASelf.calculation(aSelf.arr)[1])
                        }
                    }
                } catch (ex: Exception) {
                    Toast.makeText(getActivity(), ex.message, Toast.LENGTH_SHORT).show()
                }
            })
            (requireActivity().findViewById<View>(R.id.btnMPie) as Button).setOnClickListener {
                if (aSelf.isStandBy()) {
                    clearC()
                    aSelf.setStandBy(false)
                }
                CBMOptions.funPie(getActivity(), null)
            }
            binding.btnMPercentage.setOnClickListener(View.OnClickListener {
                if (aSelf.isStandBy()) {
                    clearC()
                }
                if (aSelf.arr.size < 1 || (aSelf.arr.get(aSelf.arr.size - 1) != "%")) {
                    aSelf.entry(getActivity(), "%")
                }
            })
            binding.btnMPercentage.setOnClickListener(View.OnClickListener {
                if (aSelf.isStandBy()) {
                    clearC()
                }
                if (aSelf.arr.size < 1 || (aSelf.arr.get(aSelf.arr.size - 1) != "%")) {
                    aSelf.entry(getActivity(), "%")
                }
            })
            (requireActivity().findViewById<View>(R.id.btnMERt) as Button).setOnClickListener {
                if (aSelf.isStandBy()) {
                    clearC()
                    aSelf.setStandBy(false)
                }
                if (aSelf.arr.size < 1) {
                    aSelf.entry(getActivity(), "2")
                }
                aSelf.entry(getActivity(), "√")
            }
            (requireActivity().findViewById<View>(R.id.btnMPowr) as Button).setOnClickListener { //if(!aSelf.arr.get(aSelf.arr.size()-1).equals("√"))
                if (aSelf.isStandBy()) {
                    clearC()
                    aSelf.setStandBy(false)
                }
                if (aSelf.arr.size < 1) {
                    aSelf.entry(getActivity(), "1")
                }
                aSelf.entry(getActivity(), "e")
            }
        } catch (ex: Exception) {
            Toast.makeText(getActivity(), ex.message, Toast.LENGTH_SHORT).show()
            ex.printStackTrace()
        }
    }

    private fun applyOnClick() {
        try {
            /* for(int a = 0; a < binding.llEntry.getChildCount(); a++) {
                LinearLayout ll = ((LinearLayout)binding.llEntry.getChildAt(a));

                for(int b = 0; b < ll.getChildCount(); b++) {
                    final int mb = b;

                    ((Button)ll.getChildAt(b)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                             if(aSelf.mKeypadMode.equals(ASelf.KeypadModeOpt.Numbers)) {
                                 String str = ((Button)v).getText().toString();
                                 if(aSelf.isStandBy()) {
                                     clearC();
                                     aSelf.setStandBy(false);
                                     if(ASelf.carryAnswer&&!ASelf.isNumber(str)){
                                         if(!aSelf.carriedAnswer.isEmpty()&&ASelf.isNumber(aSelf.carriedAnswer)) {
                                             aSelf.entry(MainActivity.getActivity(),aSelf.carriedAnswer);
                                         }
                                     }
                                 }
                                 cvm.hc=1;
                                 cvm.hca=true;
                                 aSelf.entry(MainActivity.getActivity(),str);
                                    //Toast.makeText(requireActivity(), aSelf.arr.toString(), Toast.LENGTH_SHORT).show();
                             } else if(aSelf.mKeypadMode.equals(ASelf.KeypadModeOpt.Selection)) {
                                    switch (mb) {
                                     case 0: //Copy, Paste, Options

                                         break;
                                     case 1: //Move Up

                                         break;
                                     case 2: //Close Mode

                                         break;
                                     case 3: //Move LeftgetStandBy()
                                         //tvCurrent.setSelection()
                                         break;
                                     case 4: //Select, Select All

                                         break;
                                     case 5: //Move Right

                                         break;
                                     case 6: //Plain

                                         break;
                                     case 7: //Move Down

                                         break;
                                     case 8: //Plain

                                         break;
                                 }
                             }
                        aSelf.arrAct(MainActivity.getActivity());
                        } catch(Exception ex) {
                            Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                     }
                });
            }

                tvCurrent.addTextChangedListener(new TextWatcher() {
									@Override
										   public void beforeTextChanged(CharSequence t, int a, int b, int cc) {}

										   @Override
									     public void onTextChanged(CharSequence t, int a, int b, int cc) {
										       try {
                            if(aSelf.arr.size()<binding.llNC.getChildCount()){//(((TextView)((MainActivity)c).binding.llNC.getChildAt(((MainActivity)c).binding.llNC.getChildCount()-1)).getText().toString().equals("=")) {
                                t="=";
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

										   @Override
									     public void afterTextChanged(Editable e) {}
            });
            }*/

            binding.btnScifra.setOnClickListener(View.OnClickListener {
                MediumAnims.scifraMenu(
                    binding.svScifra,
                    if ((binding.svScifra.getAlpha() == 1f)) false else true
                )
            })

            binding.btnMSettings.setOnClickListener(View.OnClickListener { //Toast.makeText(requireActivity(), "Settings", Toast.LENGTH_SHORT).show();
                startActivity(Intent(requireActivity(), SettingsActivity::class.java))
            })

            binding.btnMInverse.setOnClickListener(View.OnClickListener { v ->
                if (ASelf.isNumber(tvCurrent.getText().toString())) {
                    CBMOptions.funSignChange(getActivity(), arrayOf<Any>(tvCurrent))
                    aSelf.arrAct(getActivity())
                } else {
                    v.showContextMenu()
                }
            })

            btnPoint.setOnClickListener(View.OnClickListener { v ->
                binding.etExpression.getText().append(if (binding.etExpression.getText().toString().contains(".")) "" else ".")
                //                    if(tvCurrent==null) {aSelf.entry(v.getContext(), "0.");}
                if (!aSelf.isStandBy()) {
                    val s = (v as Button).text.toString()
                    var tvc: String = tvCurrent.getText().toString()
                    if (v.text.toString() == ".") {
                        if (!cvm.isDotd) {
                            //tvCurrent.setText(aSelf.editNowValue(((Button)v).getText().toString()));
                            cvm.isDotd = binding.etExpression.getText().toString().contains(".")
                            if (tvCurrent.getText().toString().isEmpty() || aSelf.cTxt.isEmpty()) {
                                //tvCurrent.setText(tvCurrent.getText() + "0.");
                                tvc = "0"
                                tvCurrent.setText(tvc)
                                aSelf.editNowValue("0")
                            }
                            tvCurrent.setText(tvc + s)
                            aSelf.editNowValue(v.text.toString())
                        } else {
                            val i = tvc.replace(".", "").toInt()
                            tvCurrent.setText(i.toString() + "")
                            cvm.isDotd = false
                            aSelf.cTxt = i.toString() + ""
                            aSelf.editNowValue(i.toString() + "", true)
                        }
                    }
                } else {
                    clearC()
                    aSelf.setStandBy(false)
                    entry("0.")
                }
                aSelf.arrAct(getActivity())
                if (aSelf.cTxt.contains(".")) {
                    cvm.isDotd = true
                }
            })

            binding.btnRefresh.setOnClickListener(View.OnClickListener {
                clearC()
                if (aSelf.getSBEg(requireActivity()) == 1) placeholders()
                aSelf.carriedAnswer = ""
            })

            binding.btnBackspace.setOnClickListener(View.OnClickListener {
                try {
//                    if (tvCurrent == null) {if(binding.etExpression.selectionStart>0){binding.etExpression.text.delete(binding.etExpression.selectionStart-1, binding.etExpression.selectionEnd)}; return@OnClickListener}

                    if(binding.etExpression.text.isNotEmpty()) {
                        if(!binding.etExpression.hasFocus()){binding.etExpression.requestFocus()}
                        if(binding.etExpression.selectionStart<1){binding.etExpression.setSelection(binding.etExpression.length())}
                        binding.etExpression.text.delete(binding.etExpression.selectionStart-1, binding.etExpression.selectionEnd)
                        tvCurrent.setText(binding.etExpression.text.toString())

                        var text = tvCurrent.getText().toString()
                        if(text.toString().isEmpty()){text="0"}
                        tvCurrent.setText(text)
                    } else {
                        if(binding.etExpression.text.isEmpty()&&binding.llNC.childCount>1) {
                            binding.llNC.removeView(binding.llNC.getChildAt(cvm.currIndex) as TextView)
                            binding.llNC.removeView(binding.llNC.getChildAt(cvm.currIndex - 1) as TextView)
                            if (aSelf.arr.size > cvm.currIndex) {
                                aSelf.arr.removeAt(cvm.currIndex)
                                aSelf.arr.removeAt(cvm.currIndex - 1)
                            }
                            tvCurrent = binding.llNC.getChildAt(cvm.currIndex - 2) as TextView}
                        if(tvCurrent!=null){tvCurrent.callOnClick()}
                    }

//                    val value: Int = tvCurrent.getTag().toString().toInt()
//                    if (!aSelf.isStandBy() && (ASelf.isNumber(aSelf.arr.get(value)) || aSelf.arr.get(
//                            value
//                        ).isEmpty())
//                    ) {
//                        if (binding.etExpression.text.isNotEmpty()) {
//                            if(binding.etExpression.selectionStart>0){binding.etExpression.text.delete(binding.etExpression.selectionStart-1, binding.etExpression.selectionEnd)}
//                            var str1: String = binding.etExpression.text.toString()
////                            aSelf.editNowValue("", true)
////                            str1 = aSelf.editNowValue(str1.substring(0, str1.length - 1))
//                            if (aSelf.arr.size > value) {
//                                aSelf.arr.set(value, str1)
//                                tvCurrent.setText(str1)
////                                binding.etExpression.setText(str1)
//                            }
//                        } else {
//                            if (binding.llNC.getChildCount() > 1 && (ASelf.isNumber(aSelf.arr.get(value)) || aSelf.arr.get(
//                                    value
//                                ).isEmpty()) && !ASelf.isNumber(aSelf.arr.get(value - 1))
//                            ) {
//                                binding.llNC.removeView(binding.llNC.getChildAt(value) as TextView)
//                                binding.llNC.removeView(binding.llNC.getChildAt(value - 1) as TextView)
//                                if (aSelf.arr.size > value) {
////                                    aSelf.arr.remove(value);
//                                    aSelf.arr.removeAt(value)
//                                    aSelf.arr.removeAt(value - 1)
//                                }
//                                tvCurrent = binding.llNC.getChildAt(value - 2) as TextView
//                            }
//                            for (xx in 0 until binding.llNC.getChildCount()) {
//                                binding.llNC.getChildAt(xx).setTag(xx)
//                            }
//                            aSelf.cTxt = tvCurrent.getText().toString()
//                            //tvCurrent.callOnClick();
//                        }
//
//                        if (binding.llNC.getChildCount() <= 0) {
////                            CurrentComponents.addCurrentTV(getActivity(), "")
//                            addCurrentTV()
//                            binding.etExpression.text.append("")
//                        }
//                        if (aSelf.cTxt.contains(".")) {
//                            cvm.isDotd = true
//                        } else {
//                            cvm.isDotd = false
//                        }
//                    }
//                    aSelf.arrAct(getActivity())
//                    if (binding.llNC.getChildCount() == 1) {
//                        if ((binding.llNC.getChildAt(0) as TextView).text.toString().isEmpty()) {
//                            binding.btnRefresh.callOnClick()
//                        }
//                    }
                    //Toast.makeText(requireActivity(), "tvCurrent="+tvCurrent.getText().toString(), Toast.LENGTH_SHORT).show();
                } catch (ex: Exception) {
                    ex.printStackTrace()

                    //                    Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })

            binding.btnBackspace.setOnLongClickListener(OnLongClickListener {
                try {
                    if (tvCurrent == null) return@OnLongClickListener false
                    val value: Int = tvCurrent.getTag().toString().toInt()
                    if (tvCurrent.getText().toString().length > 0) {
                        aSelf.editNowValue("", true)
                        if (aSelf.arr.size > value) {
                            aSelf.arr.set(value, "")
                            tvCurrent.setText("")
                        }
                    }
                    aSelf.arrAct(getActivity())
                    if (binding.llNC.getChildCount() == 1) {
                        if ((binding.llNC.getChildAt(0) as TextView).text.toString().isEmpty()) {
                            binding.btnRefresh.callOnClick()
                        }
                    }
                    //Toast.makeText(requireActivity(), "Backspace Option", Toast.LENGTH_SHORT).show();
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                true
            })
            btnEqual.setOnClickListener(View.OnClickListener {
                try {

                    if (aSelf.arr.size > 0 && !aSelf.isStandBy()) {
                        //equate();
                        binding.etExpression.setText(binding.etExpression.text.toString().ifEmpty { "0" })
                        var txt = binding.etExpression.text.toString()
                        txt = if(txt.isEmpty()){"0"}else{txt}
//                        addCurrentTV(txt)
                        if(txt.isEmpty()){binding.etExpression.setText(txt)}

                        clearCTVS()
                        binding.svCalculation.setVisibility(View.VISIBLE)
                        binding.etLayout.setVisibility(View.GONE)
                        binding.llCalculation.removeAllViews()
                        aSelf.setHCB(false)
                        var ans: BigDecimal = BigDecimal(aSelf.arr.get(0))
                        var ans2 = BigDecimal(0)
                        var anso: BigDecimal? = BigDecimal(0)
                        val openBracket = false
                        val arrBra = ArrayList<String>()
                        var cAns = 1
                        val calcN = 1
                        var answ = ""
                        var answ2 = ""
                        var answ3 = ""
                        var currentSign = "!+"
                        val braSign = ""
                        val conjSign = ""
                        //Toast.makeText(requireActivity(), "= Calculate", Toast.LENGTH_LONG).show();
                        var standByPH = "Empty"
                        var expCont = false

                        if (aSelf.arrS.isEmpty()) {
                            expCont = true
                            aSelf.addListItem(
                                aSelf.arrS, JSONObject().put(
                                    "CalculationName",
                                    "#$calcN Calculation"
                                ).put("Calculation", aSelf.arr)
                            )
                            aSelf.allTypeCalculations() //aSelf.addListItem(aSelf.arrS, new JSONObject().put("CalculationName",aSelf.getCurrentCalculationType().getString("name")+" Calculation").put("Calculation",Ut.toBodmas(aSelf.arr)));
                        }
                        binding.tvCATitle!!.setText(
                            /*if (cvm.placeholders) standByPH else */getString(R.string.calculation)
                        )
                        for (x in aSelf.arr.indices) {
                            ans2 = ans
                            if (!ASelf.isNumber(aSelf.arr.get(x))) {
                                currentSign = aSelf.arr.get(x)
                                //ans = ans + Integer.parseInt(aSelf.arr.get(x+1));
                            } else {
                                if (x > 1) {
                                    anso = BigDecimal(aSelf.arr.get(x))
                                } else {
                                    continue
                                }
                                ans = ASelf.calculate(currentSign, ans, anso)

                                var c = ""
                                if (x >= 0) {
                                    if (cAns > 1) c = "(@" + (cAns - 1) + ")"

                                    answ =
                                        if ((ans2.toString() + "").endsWith(".0")) (ans2.toString() + "").substring(
                                            0,
                                            (ans2.toString() + "").indexOf(".")
                                        ).toInt().toString() + "" else (ans2.toString() + "")
                                    answ2 =
                                        if ((ans.toString() + "").contains(".")) (if ((ans.toString() + "").endsWith(
                                                ".0"
                                            )
                                        ) (ans.toString() + "").replace(".0", "").toInt()
                                            .toString() + "" else (ans.toString() + "")) else (ans.toString() + "")
                                    answ3 = aSelf.arr.get(x)
                                    if (ASelf.getCalcSet(requireActivity()).getString(
                                            resources.getString(R.string.decimal_always_switch),
                                            "Switch_Numbers_+Decimal_Always"
                                        ) == "On"
                                    ) {
                                        try {
                                            answ =
                                                ASelf.mDF.format(BigDecimal(ans2.toString() + ""))
                                            answ2 =
                                                ASelf.mDF.format(BigDecimal(ans.toString() + ""))
                                            answ3 = ASelf.mDF.format(BigDecimal(aSelf.arr.get(x)))
                                        } catch (ex: Exception) {
                                            ex.printStackTrace()
                                        }
                                    }
                                    if (x > 0) {
                                        addCalculatedTV(
                                            JSONObject()
                                                .put(
                                                    ASelf.Calculation.Ix.toString(),
                                                    cAns.toString() + ""
                                                )
                                                .put(
                                                    ASelf.Calculation.LmtIx.toString(),
                                                    cAns.toString() + ""
                                                )
                                                .put(
                                                    ASelf.Calculation.Exp.toString(),
                                                    "$answ$currentSign$answ3="
                                                )
                                                .put(ASelf.Calculation.ExpAns.toString(), answ2)
                                                .put(ASelf.Calculation.DoneCalc.toString(), answ2)
                                                .put(
                                                    resources.getString(R.string.answer_for),
                                                    JSONArray().put(x - 2).put(x - 1).put(x)
                                                        .toString()
                                                )
                                        )
                                        cAns += 1
                                    }
                                }
                            }
                        }

                        //String answ = String.valueOf(ans).endsWith(".0")?String.valueOf(Integer.parseInt(ans+"")+""):String.valueOf(ans);
                        if (ASelf.getCalcSet(requireActivity()).getString(
                                resources.getString(R.string.decimal_always_switch),
                                ""
                            ) == "On"
                        ) {
                            //answ2 = ans+"";
                            try {
                                answ2 = ASelf.mDF.format(ans)
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }
                        if (currentSign == "%") answ =
                            BigDecimal(ans.toString() + "").toString() + "(" + (ans.multiply(
                                BigDecimal(100)
                            )) + "%)"
                        binding.tvDoneCalculation.setAlpha(if (cvm.placeholders) 0.5f else 1f)
                        binding.tvDoneCalculation.setText(answ2)
                        if (ASelf.carryAnswer) aSelf.carriedAnswer = answ2
                        //tvNowCalculation.setText(tvNowCalculation.getText()+"=");
                        if ((binding.llNC.getChildAt(binding.llNC.getChildCount() - 1) as TextView).text.toString() != "=") {
//                            CurrentComponents.addCurrentTV(binding.llNC, "=", 1)
                            addCurrentTV("=")
                        }
                        //aSelf.arr.add("=");
                        binding.btnMInverse.setEnabled(false)
                        //if(Ut.containsMads(aSelf.arr.toString()))binding.btnMASMD.setEnabled(true);
                        if ((cAns - 1) < 1) {
                            cAns = 1
                        } else if (cAns >= binding.llCalculation.getChildCount()) {
                            cAns = cAns - 1
                        }
                        addCalculatedTV(
                            JSONObject().put(ASelf.Calculation.Ix.toString(), "FA")
                                .put(ASelf.Calculation.LmtIx.toString(), cAns.toString() + "")
                                .put(ASelf.Calculation.Exp.toString(), "Final Answer: @$cAns=")
                                .put(ASelf.Calculation.ExpAns.toString(), answ2)
                                .put(ASelf.Calculation.DoneCalc.toString(), answ2)
                                .put(ASelf.Calculation.dType.toString(), 1)
                                .put(
                                    resources.getString(R.string.answer_for),
                                    JSONArray().put(aSelf.arr.size).toString()
                                )
                        )
                        //new String[]{"FA", "Final Answer: @"+cAns+"=", ans+""}), 1);
                        if (binding.llCalculation.getChildCount() > 0) {
                            if (binding.tvDoneCalculation.getAlpha() != 1f) {
                                binding.tvDoneCalculation.setText(
                                    binding.tvDoneCalculation.getText()
                                        .toString() + " | Write an expression"
                                )
                            }
                            standByPH += " +Example"
                        }

                        //tvCTitle.setText(binding.tvDoneCalculation.getAlpha()==1?"Calculation "+calcN:standByPH);
                        binding.tvCATitle!!.setText(if (cvm.placeholders) standByPH else getString(R.string.calculation))
                        binding.btnMPercentage.setEnabled(false)
                        aSelf.setStandBy(true)
                        cvm.isDotd = false
                        //aSelf.aSelf.editNowValue("", true);
                        //aSelf.setHCB(MainActivity.getActivity(), false);
                        //if(expCont){
                        //onSave(getHC()+1);
                        if (!cvm.placeholders) {
                            ASelf.phOn = false
                            aSelf.onSave(getActivity(), aSelf.getHC(getActivity()) + 1)
                        }
                        //}
                    }
                } catch (ex: Exception) {
                    //                    Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    ex.printStackTrace()
                }
            })
        } catch (ex: Exception) {
//            Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace()
        }
    }

    private fun createEntry(title: String, type: Int): Button {
        val btn = Button(getActivity())
        btn.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT)
        val dim4dp = Math.round(resources.getDimension(R.dimen.dim4dp))
        lp.setMargins(dim4dp, dim4dp, dim4dp, dim4dp)
        lp.weight = 1f
        btn.layoutParams = lp
        btn.text = title
        btn.textSize = 22f
        if (type == 0) {
            btn.background = resources.getDrawable(R.drawable.bg_white_green_r)
            btn.setTextColor(Color.BLACK)
        } else if (type == 1) {
            btn.background = resources.getDrawable(R.drawable.bg_black_blue_sr)
            btn.setTextColor(Color.WHITE)
        }

        return btn
    }

    private fun initEntry() {
        val mads = resources.getString(R.string.MADS)
        for (text in mads.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            val btn = createEntry(text, 1)
            btn.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)
            val dim4dp = Math.round(resources.getDimension(R.dimen.dim4dp))
            lp.setMargins(dim4dp, dim4dp, dim4dp, dim4dp)
            lp.weight = 1f
            btn.layoutParams = lp
            btn.setOnClickListener(onEntryClick())
            (binding.llEntry.getChildAt(1) as LinearLayout).addView(btn)
        }
        var eRow = 2
        var eColumn = 3
        for (n in -2..9) {
            if (n < 1) {
                if (n == -2) {
                    btnPoint = createEntry(".", 0)
                    btnPoint.setBackground(resources.getDrawable(R.drawable.bg_black_blue_sr))
                    btnPoint.setTextColor(Color.WHITE)
                    ((binding.llEntry.getChildAt(0) as LinearLayout).getChildAt(3) as LinearLayout).addView(
                        btnPoint
                    )
                }
                if (n == -1) {
                    val btn = createEntry("0", 0)
                    btn.setOnClickListener(onEntryClick())
                    ((binding.llEntry.getChildAt(0) as LinearLayout).getChildAt(3) as LinearLayout).addView(
                        btn
                    )
                }
                if (n == 0) {
                    btnEqual = createEntry("=", 0)
                    btnEqual.setBackground(resources.getDrawable(R.drawable.bg_accent_sr))
                    btnEqual.setTextColor(Color.WHITE)
                    ((binding.llEntry.getChildAt(0) as LinearLayout).getChildAt(3) as LinearLayout).addView(
                        btnEqual
                    )
                }
            } else {
                eColumn -= 1
                val btn = createEntry(n.toString(), 0)
                btn.setOnClickListener(onEntryClick())
                ((binding.llEntry.getChildAt(0) as LinearLayout).getChildAt(eRow) as LinearLayout).addView(
                    btn
                )

                if (eColumn == 0) {
                    eRow -= 1
                    eColumn = 3
                }
            }
        }
    }

    private fun onEntryClick(): View.OnClickListener {
        return View.OnClickListener { v ->
            try {
                val str = (v as Button).text.toString()
                if (aSelf.isStandBy()) {
                    clearC()
                    binding.svCalculation.setVisibility(View.GONE)
                    binding.etExpression.setVisibility(View.VISIBLE)
                    aSelf.setStandBy(false)
                    if (ASelf.carryAnswer && !ASelf.isNumber(str)) {
                        if (!aSelf.carriedAnswer.isEmpty() && ASelf.isNumber(aSelf.carriedAnswer)) {
                            entry(aSelf.carriedAnswer)
                        }
                    }
                }
                cvm.hc = 1
                cvm.hca = true
                if (!ASelf.isNumber(str) && aSelf.arr != null && !aSelf.arr.isEmpty()
                    && ASelf.isNumber(aSelf.arr.get(aSelf.arr.size - 1))
                    && tvCurrent.getText().toString().endsWith(".")
                ) {
                    aSelf.arr.set(aSelf.arr.size - 1, aSelf.editNowValue("0"))
                    tvCurrent.setText(aSelf.arr.get(aSelf.arr.size - 1))
                }
                if (ASelf.isNumber(str)) {
                    binding.etExpression.append(str)
                } else {
//                    var strEt: String = binding.etExpression.getText().toString()
//                    strEt = if (strEt.isEmpty()) "0" else strEt
                    entry(str)
//                    addCurrentTV()
//                    aSelf.arr.add(strEt)
//                    aSelf.arr.add(str)
//                    binding.etExpression.setText("")
//                    aSelf.entry(getActivity(), strEt)
//                    aSelf.entry(getActivity(), str)
                }
                aSelf.arrAct(getActivity())
                if (!ASelf.isNumber(str)) {
                    cvm.isDotd = false
                }
            } catch (ex: Exception) {
//                    Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                ex.printStackTrace()
            }
        }
    }

    fun clearCalculation() {
        binding.llCalculation.removeAllViews()
        binding.tvDoneCalculation.setAlpha(0.5f)
    }

    fun clearC() {
        binding.llNC.removeAllViews()
        binding.llCalculation.removeAllViews()
        binding.hsvNowCalculation.smoothScrollTo(0, 0)
        binding.svCalculation.smoothScrollTo(0, 0)
        binding.etLayout.setVisibility(View.VISIBLE)
        binding.svCalculation.setVisibility(View.GONE)
//        CurrentComponents.addCurrentTV(getActivity(), "")
        cvm.currIndex = -1
        addCurrentTV()
        binding.etExpression.setText("")
        aSelf.arr.clear()
        aSelf.arrS.clear()
        //binding.btnMASMD.setEnabled(false);
        binding.btnMInverse.setEnabled(false)
        clearCTVS()
        aSelf.setStandBy(true)
        aSelf.calcNm = "Expression"
        binding.tvCETitle!!.setText(aSelf.calcNm)
        binding.tvDoneCalculation.setAlpha(if (aSelf.isStandBy()) 0.5f else 1f)
        binding.tvDoneCalculation.setText(if (aSelf.isStandBy()) "Write an expression" else "")
        binding.btnMPercentage.setEnabled(false)
        binding.btnMASMD.setSelected(false)
        cvm.isDotd = false
    }

    fun clearCTVS() {
        for (x in 0 until binding.llNC.getChildCount()) {
            (binding.llNC.getChildAt(x) as TextView).isSelected = false
            (binding.llNC.getChildAt(x) as TextView).setTextColor(Color.WHITE)
        }

        for (x in 1 until binding.llCalculation.getChildCount()) {
            (binding.llCalculation.getChildAt(x) as AnswerListItem).isSelected = false
            //((AnswerListItem)binding.llCalculation.getChildAt(x)).tvAnsCalc.setTextColor(Color.WHITE);
            //((AnswerListItem)binding.llCalculation.getChildAt(x)).tvAnsNr.setTextColor(Color.WHITE);
        }

        val tvD = resources.getDrawable(R.drawable.bg_blk_green_sr, null)
        val tvClr = Color.WHITE
        if (aSelf.arr.size > 0) {
            if ((binding.llNC.getChildAt(binding.llNC.getChildCount() - 1) as TextView).text.toString() == "=") {
                (binding.llNC.getChildAt(binding.llNC.getChildCount() - 1) as TextView).background =
                    resources.getDrawable(R.drawable.bg_accent_c, null)
                (binding.llNC.getChildAt(binding.llNC.getChildCount() - 1) as TextView).setTextColor(Color.WHITE)
            }
        }
    }

}