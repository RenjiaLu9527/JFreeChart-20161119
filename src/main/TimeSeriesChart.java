package main;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;

public class TimeSeriesChart {
	ChartPanel frame1;
	private List<double[]> listTimeseries;

	public TimeSeriesChart(List<double[]> t) {
		this.listTimeseries = t;
		init();
	}

	TimeSeriesChart() {
		init();
	}

	public void init() {
		XYDataset xydataset = createDataset();
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("DwyaneWade - NBA球星德维恩·韦德 微博动态推测情感变化趋势", "时间",
				"情感波动(包含此项情感的百分比 0.0-1.0)", xydataset, true, true, true);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		dateaxis.setDateFormatOverride(new SimpleDateFormat("yyyy"));
		frame1 = new ChartPanel(jfreechart, true);
		dateaxis.setLabelFont(new Font("黑体", Font.BOLD, 18)); // 水平底部标题
		dateaxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12)); // 垂直标题
		ValueAxis rangeAxis = xyplot.getRangeAxis();// 获取柱状
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));// 设置标题字体

	}

	private XYDataset createDataset() {

		Second sc = new Second(0, 1, 2, 3, 4, 2014);
		Year yr = new Year(2014);
		String kindoftoneStr[] = { "anger", "disgust", "fear", "joy", "sadness" };

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		TimeSeries[] timeseriesArr = new TimeSeries[5];// 五条折线
		int tmpi = 4;
		while (tmpi-- >= 0) {
			timeseriesArr[tmpi + 1] = new TimeSeries(kindoftoneStr[tmpi + 1], org.jfree.data.time.Year.class);
		}
		int len = this.listTimeseries.size();// 对少条数据 每条数据对应一个点
		int nmbrofdata = 0;// 第几条数据

		while (nmbrofdata < len) {
			for (int kndoftone = 0; kndoftone < 5; kndoftone++) {
				// 第一个类别的情感对应 第一个timeseriesArr[]数组 对应一条折线
				timeseriesArr[kndoftone].add(new Year(2000 + nmbrofdata), listTimeseries.get(nmbrofdata)[kndoftone]);
			}
			nmbrofdata++;
		}
		// 数据配置完毕 准备显示
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		int tmpi2 = 4;
		while (tmpi2-- >= 0) {
			timeseriescollection.addSeries(timeseriesArr[tmpi2 + 1]);
		}
		return timeseriescollection;

		// timeseries.add(new Minute(0, hour22), 181.80000000000001D);
		// timeseries.add(new Month(3, 2001), 167.30000000000001D);
		// timeseries.add(new Month(4, 2001), 153.80000000000001D);
		// timeseries.add(new Month(5, 2001), 167.59999999999999D);
		// timeseries.add(new Month(6, 2001), 158.80000000000001D);
		// timeseries.add(new Month(7, 2001), 148.30000000000001D);
		// timeseries.add(new Month(8, 2001), 153.90000000000001D);
		// timeseries.add(new Month(9, 2001), 142.69999999999999D);
		// timeseries.add(new Month(10, 2001), 123.2D);
		// timeseries.add(new Month(11, 2001), 131.80000000000001D);
		// timeseries.add(new Month(12, 2001), 139.59999999999999D);
		// timeseries.add(new Month(1, 2002), 142.90000000000001D);
		// timeseries.add(new Month(2, 2002), 138.69999999999999D);
		// timeseries.add(new Month(3, 2002), 137.30000000000001D);
		// timeseries.add(new Month(4, 2002), 143.90000000000001D);
		// timeseries.add(new Month(5, 2002), 139.80000000000001D);
		// timeseries.add(new Month(6, 2002), 137D);
		// timeseries.add(new Month(7, 2002), 132.80000000000001D);

		// TimeSeries timeseries1 = new TimeSeries("legal & general英国指数信任1",
		// org.jfree.data.time.Second.class);
		// timeseries1.add(sc, 131.80000000000001D);
		// timeseries1.add(new Month(2, 2001), 129.59999999999999D);
		// timeseries1.add(new Month(3, 2001), 123.2D);
		// timeseries1.add(new Month(4, 2001), 117.2D);
		// timeseries1.add(new Month(5, 2001), 124.09999999999999D);
		// timeseries1.add(new Month(6, 2001), 122.59999999999999D);
		// timeseries1.add(new Month(7, 2001), 119.2D);
		// timeseries1.add(new Month(8, 2001), 116.5D);
		// timeseries1.add(new Month(9, 2001), 112.7D);
		// timeseries1.add(new Month(10, 2001), 101.5D);
		// timeseries1.add(new Month(11, 2001), 106.09999999999999D);
		// timeseries1.add(new Month(12, 2001), 110.3D);
		// timeseries1.add(new Month(1, 2002), 111.7D);
		// timeseries1.add(new Month(2, 2002), 111D);
		// timeseries1.add(new Month(3, 2002), 109.59999999999999D);
		// timeseries1.add(new Month(4, 2002), 113.2D);
		// timeseries1.add(new Month(5, 2002), 111.59999999999999D);
		// timeseries1.add(new Month(6, 2002), 108.8D);
		// timeseries1.add(new Month(7, 2002), 101.59999999999999D);

		// TimeSeries timeseries11 = new TimeSeries("test",
		// org.jfree.data.time.Month.class);
		// timeseries11.add(new Month(2, 2001), 129.009D);
		// timeseries11.add(new Month(3, 2001), 103.2D);
		// timeseries11.add(new Month(4, 2001), 107.2D);
		// timeseries11.add(new Month(5, 2001), 120.09999999999999D);
		// timeseries11.add(new Month(6, 2001), 120.59999999999999D);
		// timeseries11.add(new Month(7, 2001), 169.2D);
		// timeseries11.add(new Month(8, 2001), 160.5D);
		// timeseries11.add(new Month(9, 2001), 116.7D);
		// timeseries11.add(new Month(10, 2001), 101.5D);
		// timeseries11.add(new Month(11, 2001), 166.09999999999999D);
		// timeseries11.add(new Month(12, 2001), 100.3D);
		// timeseries11.add(new Month(1, 2002), 111.7D);
		// timeseries11.add(new Month(2, 2002), 111D);
		// timeseries11.add(new Month(3, 2002), 100.59999999999999D);
		// timeseries11.add(new Month(4, 2002), 113.2D);
		// timeseries11.add(new Month(5, 2002), 101.59999999999999D);
		// timeseries11.add(new Month(6, 2002), 168.8D);
		// timeseries11.add(new Month(7, 2002), 101.59999999999999D);

	}

	public ChartPanel getChartPanel() {
		return frame1;

	}
}