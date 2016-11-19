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
		// ��ȡֵ
		List<double[]> listTimeseries = jdbc();

		JFrame frame = new JFrame("�����ȡ���ݱ��ػ�-Զ�̵���Watson ToneAnalyzer API������������-����Json����ʾ");
		frame.setLayout(new GridLayout(1, 1, 10, 10));

		frame.add(new TimeSeriesChart(listTimeseries).getChartPanel()); // �������ͼ
		frame.setBounds(50, 50, 1000, 800);
		frame.setVisible(true);
		System.out.println("������ͼ����");

	}

	static List<double[]> jdbc() {
		int IDNUM = 100;// ���ֵ
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			// System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("�������ݿ�-�ɹ�");

			// STEP 4: Execute a query
			// System.out.println("Inserting records into the table...");
			stmt = conn.createStatement();

			// ��ѯ
			String sql = "SELECT * FROM cntnttext where id < " + IDNUM;
			rs = stmt.executeQuery(sql);
			int cnt = 0;
			List<double[]> listdouble = new ArrayList<double[]>();// ����ͼ��Ҫ������
			while (rs.next()) {
				cnt++;
				// rs.get+���ݿ��ж�Ӧ������+(���ݿ��ж�Ӧ���б���)
				int id = rs.getInt("id");
				String text = rs.getString("text");
				text = text.equals("") ? "-" : text;
				text = tidyString(text);// �ַ���̫�����½�������Json��ʽ��һ��

				System.out.print("��" + cnt + "������id=" + id);
				System.out.println(" text=" + text);

				// ת�� �� ����GET��ʽ������ watson����
				try {
					text = url_str_Base + URLEncoder.encode(text, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				System.out.println("text=" + text);
				String jsonData = Httputils.httpRequest(text);// ��������
				Root rt = new ParseJson(jsonData).parse();// ����Json
				double[] dataArr = new double[5];
				dataArr[0] = rt.getDocument_tone().getTone_categories().get(0).getTones().get(0).getScore();
				dataArr[1] = rt.getDocument_tone().getTone_categories().get(0).getTones().get(1).getScore();
				dataArr[2] = rt.getDocument_tone().getTone_categories().get(0).getTones().get(2).getScore();
				dataArr[3] = rt.getDocument_tone().getTone_categories().get(0).getTones().get(3).getScore();
				dataArr[4] = rt.getDocument_tone().getTone_categories().get(0).getTones().get(4).getScore();

				System.out.println("��������:" + dataArr[0] + " " + dataArr[1] + " " + dataArr[2] + " " + dataArr[3] + " "
						+ dataArr[4]);

				listdouble.add(dataArr);// ����һ��΢������з�������
			} // while_end
			System.out.println("listdouble.size()=" + listdouble.size());
			// �����ռ���� ת��Ϊ����ͼ��ʾ
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
	 *            �������ַ���
	 * @return
	 */
	static String tidyString(String str) {
		// ���õ����ű�ʾһ���ַ�����ʱ������ַ����г��ֵ����ţ�Ҫ�����������Ų��ܱ�ʾһ�������ţ��� wang'xiaowei,�õ����ű�ʾ
		// 'wang''xiaowei'������˫���ű�ʾһ���ַ�����ʱ������ַ����г���˫���ţ�Ҫ������˫���Ų��ܱ�ʾһ��˫���ţ���
		// wang"xiaowei,��˫���ű�ʾ"wang""xiaowei"

		// mysql ��������ʱ ʵ�����ݵ� '��������ת����java�ַ��� \'\'��Ҳ����'������׼��������������''�� �������ȷ
		str = str.length() > 199 ? str.substring(0, 199) : str;
		str = str.replace(".", ",").replace("\'\'", "\'");

		return str;
	}
}