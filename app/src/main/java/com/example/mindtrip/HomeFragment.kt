package com.example.mindtrip

import android.graphics.Color
import android.os.Bundle
import android.util.EventLogTags
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mindtrip.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.io.FileNotFoundException
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    val actdata: ArrayList<ActData> = ArrayList()
    var stats: ArrayList<Stats> = ArrayList()
    lateinit var chartWeek:BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        initData()
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root

    }

    private fun initData(){
        stats.add(Stats("카페",0))
        stats.add(Stats("공원",0))
        stats.add(Stats("테마파크",0))
        stats.add(Stats("관광지",0))

        try {
            val scan1 = Scanner(activity?.openFileInput("statistics.txt"))
            var inta = readFileScan(scan1, actdata,stats)
            for(i in 0..3){
                stats[i].num = inta[i]
            }
        }catch (e: FileNotFoundException){
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChartView(view)
    }
    private fun setChartView(view: View) {
        chartWeek = view.findViewById<BarChart>(R.id.chart_week)
        setWeek(chartWeek)
    }

    private fun initBarDataSet(barDataSet: BarDataSet) {
        //Changing the color of the bar
        barDataSet.color = Color.parseColor("#304567")
        //Setting the size of the form in the legend
        barDataSet.formSize = 15f
        //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false)
        //setting the text size of the value of the bar
        barDataSet.valueTextSize = 12f
    }

    private fun setWeek(barChart: BarChart) {
        initBarChart(barChart)

        barChart.setScaleEnabled(false) //Zoom In/Out

        val valueList = ArrayList<Double>()
        val entries: ArrayList<BarEntry> = ArrayList()
        val title = "활동 통계"

        //input data
        for (i in 0..3) {
            valueList.add(stats[i].num.toDouble())
        }

        //fit the data into a bar
        for (i in 0 until valueList.size) {
            val barEntry = BarEntry(i.toFloat(), valueList[i].toFloat())
            entries.add(barEntry)
        }
        val barDataSet = BarDataSet(entries, title)
        val data = BarData(barDataSet)
        barChart.data = data
        barChart.invalidate()
    }

    private fun initBarChart(barChart: BarChart) {
        //hiding the grey background of the chart, default false if not set
        barChart.setDrawGridBackground(false)
        //remove the bar shadow, default false if not set
        barChart.setDrawBarShadow(false)
        //remove border of the chart, default false if not set
        barChart.setDrawBorders(false)

        //remove the description label text located at the lower right corner
        val description = Description()
        description.setEnabled(false)
        barChart.setDescription(description)

        //X, Y 바의 애니메이션 효과
        barChart.animateY(1000)
        barChart.animateX(1000)


        //바텀 좌표 값
        val xAxis: XAxis = barChart.getXAxis()
        val xAxisLabels = listOf("카페","공원","테마파크","관광지")
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        //change the position of x-axis to the bottom
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //set the horizontal distance of the grid line
        xAxis.granularity = 1f
        xAxis.textColor = Color.RED
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false)
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false)


        //좌측 값 hiding the left y-axis line, default true if not set
//        val leftAxis: YAxis = barChart.getAxisLeft()
//        leftAxis.setDrawAxisLine(false)
//        leftAxis.textColor = Color.RED


        //우측 값 hiding the right y-axis line, default true if not set
        val rightAxis: YAxis = barChart.getAxisRight()
        rightAxis.setDrawAxisLine(false)
        rightAxis.textColor = Color.RED


        //바차트의 타이틀
        val legend: Legend = barChart.getLegend()
        //setting the shape of the legend form to line, default square shape
        legend.form = Legend.LegendForm.LINE
        //setting the text size of the legend
        legend.textSize = 11f
        legend.textColor = Color.BLACK
        //setting the alignment of legend toward the chart
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        //setting the stacking direction of legend
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(false)
    }
//
//    private fun init() {
//
//        with(binding) {
//            clearbtn.setOnClickListener {
//                val builder = android.app.AlertDialog.Builder(this@HomeFragment.context)
//                builder!!.setMessage("활동 데이터 채울까요?")
//                    .setTitle("채우기")
//
//                builder.apply {
//                    setPositiveButton("예") { dialog, id ->
//                        val selectedId = id
//                        actdata.clear()
//                        stats.clear()
//                        clearbtn.visibility = android.view.View.GONE
//                        refreshbtn.visibility = android.view.View.GONE
//                        refreshbtn2.visibility = android.view.View.VISIBLE
//                        HomeFragment()
//
//                    }
//                    setNegativeButton("아니요") { dialog, id ->
//                        val selectedId = id
//
//                    }
//                }
//                val dialog: AlertDialog? = builder.create()
//
//                dialog!!.show()
//
//
//            }
//            refreshbtn2.setOnClickListener {
//                setChartView()
//            }
//            refreshbtn.setOnClickListener {
//            }
//        }
//    }

    fun readFileScan(scan: Scanner, data: java.util.ArrayList<ActData>,data2: java.util.ArrayList<Stats>):ArrayList<Int> {
        var num = arrayListOf<Int>(0, 0, 0, 0)
        val test = arrayListOf<String>("카페", "공원", "테마파크", "관광지")
        while (scan.hasNextLine()) {
            val name = scan.nextLine()
            val type = scan.nextLine()
            for (i in 0..3) {
                if (type.equals(test[i]))
                    num[i]++
            }

            data.add(ActData(name, type))

        }
        return num
    }
}