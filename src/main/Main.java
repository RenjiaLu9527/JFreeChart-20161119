package main;

import java.awt.GridLayout;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import bean.Root;

public class Main {
	private static String url_str_Base = "https://watson-api-explorer.mybluemix.net/tone-analyzer/api/v3/tone?version=2016-05-19&text=";
	// JDBC driver name and database URL
	final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final static String DB_URL = "jdbc:mysql://localhost/weibodata";

	// Database credentials
	final static String USER = "root";
	final static String PASS = "";

	public static void main(String args[]) {
		// 先取值
		List<double[]> listTimeseries = jdbc();

		JFrame frame = new JFrame("爬虫获取数据本地化-远程调用Watson ToneAnalyzer API分析本地数据-解析Json并显示");
		frame.setLayout(new GridLayout(1, 1, 10, 10));

		frame.add(new TimeSeriesChart(listTimeseries).getChartPanel()); // 添加折线图
		frame.setBounds(50, 50, 1000, 800);
		frame.setVisible(true);
		System.out.println("启动视图窗口");

	}

	static List<double[]> jdbc() {
		int IDNUM = 100;// 最大值
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			// System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("连接数据库-成功");

			// STEP 4: Execute a query
			// System.out.println("Inserting records into the table...");
			stmt = conn.createStatement();

			// 查询
			String sql = "SELECT * FROM cntnttext where id < " + IDNUM;
			rs = stmt.executeQuery(sql);
			int cnt = 0;
			List<double[]> listdouble = new ArrayList<double[]>();// 折线图需要的数组
			while (rs.next()) {
				cnt++;
				// rs.get+数据库中对应的类型+(数据库中对应的列别名)
				int id = rs.getInt("id");
				String text = rs.getString("text");
				text = text.equals("") ? "-" : text;
				text = tidyString(text);// 字符串太长导致解析返回Json格式不一样

				System.out.print("第" + cnt + "条数据id=" + id);
				System.out.println(" text=" + text);

				// 转码 并 调用GET方式发送至 watson分析
				try {
					text = url_str_Base + URLEncoder.encode(text, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				System.out.println("text=" + text);
				String jsonData = Httputils.httpRequest(text);// 请求数据
				Root rt = new ParseJson(jsonData).parse();// 解析Json
				double[] dataArr = new double[5];
				dataArr[0] = rt.getDocument_tone().getTone_categories().get(0).getTones().get(0).getScore();
				dataArr[1] = rt.getDocument_tone().getTone_categories().get(0).getTones().get(1).getScore();
				dataArr[2] = rt.getDocument_tone().getTone_categories().get(0).getTones().get(2).getScore();
				dataArr[3] = rt.getDocument_tone().getTone_categories().get(0).getTones().get(3).getScore();
				dataArr[4] = rt.getDocument_tone().getTone_categories().get(0).getTones().get(4).getScore();

				System.out.println("反馈数组:" + dataArr[0] + " " + dataArr[1] + " " + dataArr[2] + " " + dataArr[3] + " "
						+ dataArr[4]);

				listdouble.add(dataArr);// 加入一条微博的情感分析数据
			} // while_end
			System.out.println("listdouble.size()=" + listdouble.size());
			// 数据收集完成 转化为折线图显示
			return listdouble;

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			} // do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		return null;

	}// jdbc_end

	/**
	 * 
	 * @param str
	 *            待处理字符串
	 * @return
	 */
	static String tidyString(String str) {
		// 在用单引号表示一个字符串的时候，如果字符串中出现单引号，要用两个单引号才能表示一个单引号；如 wang'xiaowei,用单引号表示
		// 'wang''xiaowei'。在用双引号表示一个字符串的时候，如果字符串中出现双引号，要用两个双引号才能表示一个双引号；如
		// wang"xiaowei,用双引号表示"wang""xiaowei"

		// mysql 存入数据时 实际数据的 '单引号需转换成java字符串 \'\'【也就是'单引号准换成两个单引号''】 存入才正确
		str = str.length() > 199 ? str.substring(0, 199) : str;
		str = str.replace(".", ",").replace("\'\'", "\'");

		return str;
	}
}