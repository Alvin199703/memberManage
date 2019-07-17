package manicure;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class nanaNail extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Vector vData = new Vector();
	Vector vRow = new Vector();   //用于构造表格，vName为列名，vRow为行向量，vData为行向量集
	Vector vName = new Vector();
	public String password = "nanameijia";
	public JFrame regis,sele,cons,seleLog,mana,coun;				//分页面
	public JButton b1,b2,b3,b4,b5,b6;  //主页按钮
	String indexString[] = {"姓名查询","尾号查询"};
	String typeString[] = {"美甲","接睫毛",".."};
	String typeConsume[] = {"余额消费","积分消费","其他支付"};
	String modiString[] = {"会员充值","更换微信","更换手机","更改优惠"};
	public JComboBox index = new JComboBox(indexString);
	public JComboBox type = new JComboBox(typeString);
	public JComboBox consType = new JComboBox(typeConsume);
	public JComboBox manaType = new JComboBox(modiString);
	public JButton register,select,consume,cons_select;	
	public JButton modiSelect,modify;
	public JTextField regisName,regisPhone,regisWeChat,regisMoney,regisScore,regisDiscount;//注册所需输入信息的文本框
	public JTextField selectString,consumeString,modifyString;
	public JTextArea selectResult;
	public JTable consumeLog;
	public DefaultTableModel model;
	public static Connection Database;
	nanaNail(){
		super("娜娜美甲会员管理系统");
		Database = database.Connect();
		Container con =getContentPane();
		con.setLayout(new GridLayout(6, 1));
		b1 = new JButton (" 注册会员");
		b2 = new JButton (" 查询会员");
		b3 = new JButton ("查询交易记录 ");
		b4 = new JButton (" 会员消费  ");
		b5 = new JButton (" 会员管理 ");
		b6 = new JButton (" 今日统计  ");
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
		b5.addActionListener(this);
		b6.addActionListener(new countActionListener());
		con.add(b1,-1);
		con.add(b2,-1);
		con.add(b3,-1);
		con.add(b4,-1);
		con.add(b5,-1);
		con.add(b6,-1);
		
		setLocation(100, 100);
		setSize(300, 500);
		setVisible(true);
	}
	public void createAccountFile(){ //创建会员文档
		String firstLine = "姓名" + "\t" + "联  系  方 式 " + "  微信" + "\t" + "余额" + "\t" + "积分" + "\t" + "优惠";
		String name,phone,wechat,money,score,discount,row;
		BufferedWriter bw = null;
		try {
			bw= new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"/会员.txt"));
			bw.write(firstLine);
			bw.newLine();
			Statement sta = Database.createStatement();
			ResultSet re = sta.executeQuery("select * from account");
			while(re.next()) {
				name = re.getString("name");
				phone = re.getString("phone");
				wechat = re.getString("wechat");
				money = re.getString("money");
				score = re.getString("score");
				discount = re.getString("discount");
				row = name  + "\t" + phone + "\t" + wechat + "\t" + money + "\t" + score + "\t" + discount + "\n"  ;
				bw.write(row);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"文件创建失败");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void createLogFile() {
		String firstLine = "姓名" + "\t" + "尾号 " + "  消费种类" + "\t" + "金额" + "\t" + "  日期";
		String name,tail_num,money,type,date,row;
		BufferedWriter bw = null;
		try {
			bw= new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"/消费记录.txt"));
			bw.write(firstLine);
			bw.newLine();
			Statement sta = Database.createStatement();
			ResultSet re = sta.executeQuery("select * from transaction");
			while(re.next()) {
				name = re.getString("name");
				tail_num = re.getString("tail_num");
				type = re.getString("type");
				money = re.getString("amount");
				date = re.getString("date");
				row = name  + "\t" + tail_num + "\t" + type + "\t" + money + "\t" + date +  "\n"  ;
				bw.write(row);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"文件创建失败");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static boolean isNumeric(String str){  //判断是否为整数
	   for (int i = str.length();--i>=0;){  
	       if (!Character.isDigit(str.charAt(i))){
	           return false;
	       }
	   }
	   return true;
	}
	private boolean isDouble(String str) {  //判断是否为浮点数
		if (null == str || "".equals(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static void main(String[] args) {
		nanaNail nail = new nanaNail();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == b1) {
			regis = new JFrame("会员注册");
			Container reg = regis.getContentPane();
			reg.setLayout(new FlowLayout());
			JLabel j1,j2,j3,j4,j5,j6;
			j1 = new JLabel(" 姓     名  :");
			j2 = new JLabel("联系方式:");
			j3 = new JLabel(" 微     信  :");
			j4 = new JLabel(" 余     额  :");
			j5 = new JLabel(" 积     分  :");
			j6 = new JLabel(" 折     扣  :");
			regisName = new JTextField(15);
			regisPhone = new JTextField(15);
			regisWeChat = new JTextField(15);
			regisMoney = new JTextField(15);
			regisScore = new JTextField(15);
			regisDiscount = new JTextField(15);
			register = new JButton ("注册");
			register.addActionListener(new registerActionListener());
			reg.add(j1);reg.add(regisName);
			reg.add(j2);reg.add(regisPhone);
			reg.add(j3);reg.add(regisWeChat);
			reg.add(j4);reg.add(regisMoney);
			reg.add(j5);reg.add(regisScore);
			reg.add(j6);reg.add(regisDiscount);
			reg.add(register);
			
			regis.setLocation(200, 200);
			regis.setSize(300, 500);
			regis.setVisible(true);
		}
		else if(e.getSource()==b2) {
			sele = new JFrame("查询会员");
			Container se = sele.getContentPane();
			se.setLayout(new BorderLayout());
			JPanel jp = new JPanel();
			selectString = new JTextField(10);
			select = new JButton("查询");
			select.addActionListener(new selectMesActionListener());
			selectResult = new JTextArea();
			selectResult.append("姓名" + "\t" + "尾号" + "\t" + "积分" + "\t" + "余额" + "\t" + "优惠");
			selectResult.append("\n");
			JScrollPane js = new JScrollPane();
			js.setViewportView(selectResult);
			js.setAutoscrolls(true);
			js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			selectResult.setEditable(false);
			jp.add(index);
			jp.add(selectString);
			jp.add(select);
			se.add(jp, BorderLayout.NORTH);
			se.add(js, BorderLayout.CENTER);
			js.revalidate();
			
			sele.setLocation(200, 300);
			sele.setSize(400, 300);
			sele.setVisible(true);
		}
		else if(e.getSource() == b3) {
			seleLog = new JFrame("交易查询");
			Container se = seleLog.getContentPane();
			se.setLayout(new BorderLayout());
			JPanel jp = new JPanel();
			selectString = new JTextField(10);
			select = new JButton("查询");
			select.addActionListener(new selectLogActionListener());
			selectResult = new JTextArea();
			selectResult.append("姓名" + "\t" + "尾号" + "\t" + "种类" + "\t" + "金额" + "\t" + "日期");
			selectResult.append("\n");
			JScrollPane js = new JScrollPane();
			js.setViewportView(selectResult);
			js.setAutoscrolls(true);
			js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			selectResult.setEditable(false);
			jp.add(index);
			jp.add(selectString);
			jp.add(select);
			se.add(jp, BorderLayout.NORTH);
			se.add(js, BorderLayout.CENTER);
			js.revalidate();
			
			seleLog.setLocation(200, 300);
			seleLog.setSize(500, 300);
			seleLog.setVisible(true);
		}
		else if(e.getSource() == b4) {
			cons = new JFrame("消费页面");
			Container con = cons.getContentPane();
			con.setLayout(new BorderLayout());
			JPanel jp1 = new JPanel();
			JPanel jp2 = new JPanel();
			consume = new JButton("消费");
			cons_select = new JButton ("查询");
			cons_select.addActionListener(new consumeActionListener());
			consume.addActionListener(new consumeActionListener());
			consumeString = new JTextField(8);
			consumeLog = new JTable();
			vName = new Vector();
			vData = new Vector();
			vRow = new Vector();
			vName.add("姓名");
			vName.add("尾号");
			vName.add("余额");
			vName.add("积分");
			vName.add("优惠");
			for(int i=0;i<5;i++) {
				vRow.add("0");
			}
			vData.add(vRow.clone());
			model = new DefaultTableModel(vData,vName);
			consumeLog.setModel(model);
			JScrollPane js = new JScrollPane(consumeLog);
			jp1.add(consType);
			jp1.add(type);
			jp1.add(index);
			jp1.add(consumeString);
			jp1.add(cons_select);
			jp2.add(consume);
			con.add(jp1,BorderLayout.NORTH);
			con.add(js,BorderLayout.CENTER);
			con.add(jp2,BorderLayout.SOUTH);
			
			cons.setLocation(200,200);
			cons.setSize(500, 400);
			cons.setVisible(true);
		}
		else if (e.getSource() == b5) {
			String temp_pass = JOptionPane.showInputDialog("请输入管理密码");
			if(!temp_pass.equals(password)) {
				JOptionPane.showMessageDialog(this, "密码错误，请核实密码", "输入错误", JOptionPane.WARNING_MESSAGE);
			}
			else {
				mana = new JFrame("会员管理");
				Container man = mana.getContentPane();
				man.setLayout(new BorderLayout());
				JPanel jp1 = new JPanel();
				JPanel jp2 = new JPanel();
				modify = new JButton("修改");
				modiSelect = new JButton ("查询");
				modiSelect.addActionListener(new manageActionListener());
				modify.addActionListener(new manageActionListener());
				modifyString = new JTextField(8);
				consumeLog = new JTable();
				vName = new Vector();
				vData = new Vector();
				vRow = new Vector();
				vName.add("姓名");
				vName.add("尾号");
				vName.add("余额");
				vName.add("微信");
				vName.add("优惠");
				for(int i=0;i<5;i++) {
					vRow.add("0");
				}
				vData.add(vRow.clone());
				model = new DefaultTableModel(vData,vName);
				consumeLog.setModel(model);
				JScrollPane js = new JScrollPane(consumeLog);
				jp1.add(manaType);
				jp1.add(index);
				jp1.add(modifyString);
				jp1.add(modiSelect);
				jp2.add(modify);
				man.add(jp1,BorderLayout.NORTH);
				man.add(js,BorderLayout.CENTER);
				man.add(jp2,BorderLayout.SOUTH);
				
				mana.setLocation(200,200);
				mana.setSize(500, 400);
				mana.setVisible(true);
			}
		}
	}
	class registerActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(regisName.getText().isEmpty()||regisPhone.getText().isEmpty()||regisWeChat.getText().isEmpty()||
					regisScore.getText().isEmpty()||regisDiscount.getText().isEmpty()) {
				JOptionPane.showMessageDialog(regis, "注册信息不能为空，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
			}
			else if(!isNumeric(regisPhone.getText())) {
				JOptionPane.showMessageDialog(regis, "联系方式必须为纯数字，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
			}
			else if (regisPhone.getText().length()!=11) {
				JOptionPane.showMessageDialog(regis, "联系方式应为11位，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
			}
			else if (!isNumeric(regisMoney.getText())){
				JOptionPane.showMessageDialog(regis, "余额必须为纯数字，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
			}
			else if (!isNumeric(regisScore.getText())) {
				JOptionPane.showMessageDialog(regis, "积分必须为纯数字，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
			}
			else if (!isDouble(regisDiscount.getText())) {
				JOptionPane.showMessageDialog(regis, "优惠应为小数，如九折为0.9，没有折扣应为1.0，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
			}
			else if (Float.parseFloat(regisDiscount.getText())>1.0) {
				JOptionPane.showMessageDialog(regis, "优惠不能大于1.0，如九折为0.9，没有折扣应为1.0", "请重新输入", JOptionPane.WARNING_MESSAGE);
			}
			else {
				PreparedStatement psql = null;
				try {
					psql = Database.prepareStatement("insert into account (name,phone,wechat,money,score,tail_num,discount) "+ "values(?,?,?,?,?,?,?)"); 
					String name = regisName.getText();
					System.out.println(name);
					String phone = regisPhone.getText();
					String wechat = regisWeChat.getText();
					String money = regisMoney.getText();
					String score = regisScore.getText();
					String discount = regisDiscount.getText();
					String tail_num = phone.substring(phone.length()-4);
					psql.setString(1, name);
					psql.setString(2, phone);
					psql.setString(3, wechat);
					psql.setFloat(4, Float.parseFloat(money));
					psql.setFloat(5, Float.parseFloat(score));
					psql.setString(6, tail_num);
					psql.setFloat(7, Float.parseFloat(discount));
					psql.executeUpdate();
					JOptionPane.showMessageDialog(regis, "注册成功！", "succeed", JOptionPane.INFORMATION_MESSAGE);
					regis.dispose();
					createAccountFile();
				}catch(Exception a) {
					JOptionPane.showMessageDialog(regis, "该用户已注册过会员", "注册失败", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			
		}
		
	}
	class selectMesActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String sql = null;
			if(selectString.getText().isEmpty()){
				JOptionPane.showMessageDialog(sele, "查询信息不能为空，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
			}
			else if(index.getSelectedItem().toString().equals("尾号查询")&&!isNumeric(selectString.getText())) {
				JOptionPane.showMessageDialog(sele, "尾号必须为纯数字，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
			}
			else {
				String temp;
				temp = selectString.getText();
				if(index.getSelectedItem().toString().equals("姓名查询")) {
					sql = "select * from account where name = \"" + temp + "\"";
				}
				else if(index.getSelectedItem().toString().equals("尾号查询"))
					sql = "select * from account where tail_num = \""+ temp +"\"";
				//PreparedStatement pstmt = null;
				Statement statement;
				String name,tail_num,score,money,discount,output;
				
				try {
					statement = Database.createStatement();
					ResultSet re = statement.executeQuery(sql);
					//pstmt = Database.prepareStatement(sql);
					//pstmt.setString(1, selectString.getText());
					//ResultSet re = pstmt.executeQuery();
					if(!re.next()) {
						JOptionPane.showMessageDialog(sele, "该用户并未办理会员，请核对", "输入错误", JOptionPane.WARNING_MESSAGE);
					}
					else
						re.previous();
					while(re.next()) {
						name = re.getString("name");
						tail_num = re.getString("tail_num");
						score = re.getString("score");
						money = re.getString("money");
						discount = re.getString("discount");
						output = name + "\t" + tail_num + "\t" + score + "\t" + money + "\t" + discount;
						selectResult.append(output);
						selectResult.append("\n");
						selectResult.setSelectionEnd(selectResult.getText().length());
					}
				}catch(Exception a) {
					a.printStackTrace();
					JOptionPane.showMessageDialog(sele, "该用户并未办理会员，请核对", "输入错误", JOptionPane.WARNING_MESSAGE);
				}
			}
			
		}
	}
	class selectLogActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String sql = null;
			if(selectString.getText().isEmpty()){
				JOptionPane.showMessageDialog(seleLog, "查询信息不能为空，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
			}
			else if(index.getSelectedItem().toString().equals("尾号查询")&&!isNumeric(selectString.getText())) {
				JOptionPane.showMessageDialog(seleLog, "尾号必须为纯数字，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
			}
			else {
				String temp;
				temp = selectString.getText();
				if(index.getSelectedItem().toString().equals("姓名查询")) {
					sql = "select * from transaction where name = \"" + temp + "\"";
				}
				else if(index.getSelectedItem().toString().equals("尾号查询"))
					sql = "select * from transaction where tail_num = \""+ temp +"\"";
				//PreparedStatement pstmt = null;
				Statement statement;
				String name,tail_num,type,money,date,output;
				
				try {
					statement = Database.createStatement();
					ResultSet re = statement.executeQuery(sql);
					//pstmt = Database.prepareStatement(sql);
					//pstmt.setString(1, selectString.getText());
					//ResultSet re = pstmt.executeQuery();
					if(!re.next()) {
						JOptionPane.showMessageDialog(seleLog, "该用户并未办理会员，请核对", "输入错误", JOptionPane.WARNING_MESSAGE);
					}
					else
						re.previous();
					while(re.next()) {
						name = re.getString("name");
						tail_num = re.getString("tail_num");
						money = re.getString("amount");
						type = re.getString("type");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		                date = sdf.format(re.getDate("date"));
						output = name + "\t" + tail_num + "\t" + type + "\t" + money + "\t" + date;
						selectResult.append(output);
						selectResult.append("\n");
						selectResult.setSelectionEnd(selectResult.getText().length());
					}
				}catch(Exception a) {
					a.printStackTrace();
					JOptionPane.showMessageDialog(seleLog, "该用户并未办理会员，请核对", "输入错误", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		
	}
	class consumeActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) throws IndexOutOfBoundsException {
			// TODO Auto-generated method stub
			if(e.getSource() == cons_select) { //消费查询
				String sql = null;
				if(consumeString.getText().isEmpty()) {
					JOptionPane.showMessageDialog(cons, "查询信息不能为空，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
				}
				else if(index.getSelectedItem().toString().equals("尾号查询")&&!isNumeric(consumeString.getText())) {
					JOptionPane.showMessageDialog(cons, "尾号必须为纯数字，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
				}
				else {
					String temp;
					temp = consumeString.getText();
					if(index.getSelectedItem().toString().equals("姓名查询")) {
						sql = "select * from account where name = \"" + temp + "\"";
					}
					else if(index.getSelectedItem().toString().equals("尾号查询"))
						sql = "select * from account where tail_num = \""+ temp +"\"";
					//PreparedStatement pstmt = null;
					Statement statement;
					try {
						vData = new Vector();
						statement = Database.createStatement();
						ResultSet re = statement.executeQuery(sql);
						//pstmt = Database.prepareStatement(sql);
						//pstmt.setString(1, selectString.getText());
						//ResultSet re = pstmt.executeQuery();
						if(!re.next()) {
							JOptionPane.showMessageDialog(cons, "该用户并未办理会员，请核对", "输入错误", JOptionPane.WARNING_MESSAGE);
						}
						else
							re.previous();
						while(re.next()) {
							Vector row = new Vector();
							row.add(re.getString("name"));
							row.add(re.getString("tail_num"));
							row.add(re.getString("money"));
							row.add(re.getString("score"));
							row.add(re.getString("discount"));
							vData.add(row.clone());
						}
						model=new DefaultTableModel(vData,vName);
						consumeLog.setModel(model);
					}catch(Exception a) {
						a.printStackTrace();
						JOptionPane.showMessageDialog(cons, "该用户并未办理会员，请核对", "输入错误", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
			else if(e.getSource() == consume) {  //消费action
				String name = null,tail_num = null;
				Float money = null,score = null,discount = null;
				try {
					int row = consumeLog.getSelectedRow();
					name = (String) consumeLog.getValueAt(row, 0);
					tail_num = (String)consumeLog.getValueAt(row, 1);
					money = Float.parseFloat((String) consumeLog.getValueAt(row, 2));
					score = Float.parseFloat((String) consumeLog.getValueAt(row, 3));
					discount = Float.parseFloat((String) consumeLog.getValueAt(row, 4));
					PreparedStatement ptst1 = null, ptst2 = null;
					String sql1= null,sql2 = null;
					String moneyStr,scoreStr;
					try {
						if(consType.getSelectedItem().toString().equals("余额消费")) {
							moneyStr = JOptionPane.showInputDialog(cons, "请输入消费金额:", "金额", JOptionPane.INFORMATION_MESSAGE);
							if(moneyStr.isEmpty()) {
								JOptionPane.showMessageDialog(cons, "消费金额不能为空", "输入错误", JOptionPane.WARNING_MESSAGE);
								moneyStr = JOptionPane.showInputDialog(cons, "请输入消费金额:", "金额", JOptionPane.INFORMATION_MESSAGE);
							}
							else if(!isNumeric(moneyStr)) {
								JOptionPane.showMessageDialog(cons, "消费金额必须为纯数字", "输入错误", JOptionPane.WARNING_MESSAGE);
								moneyStr = JOptionPane.showInputDialog(cons, "请输入消费金额:", "金额", JOptionPane.INFORMATION_MESSAGE);
							}
							else {
								Float saleMoney = Float.parseFloat(moneyStr)*discount;
								if(money < saleMoney) {
									JOptionPane.showMessageDialog(cons, "当前用户余额不足，请选择其他支付方式", "支付失败", JOptionPane.WARNING_MESSAGE);
								}
								else {
									money -= saleMoney;
									sql1 = "update account set money = ? , score = ? where name = \"" + name + "\"" + " and tail_num = \"" + tail_num + "\"";
									sql2 = "insert into transaction (name , tail_num , type , amount ,date) values(?,?,?,?,?)";
									ptst1 = Database.prepareStatement(sql1);
									ptst2 = Database.prepareStatement(sql2);
									ptst1.setString(1, money.toString());
									ptst1.setString(2, (score+=saleMoney).toString());
									ptst2.setString(1, name);
									ptst2.setString(2, tail_num);
									ptst2.setString(3, type.getSelectedItem().toString());
									ptst2.setFloat(4, saleMoney);
									Calendar now = Calendar.getInstance();  
							        Date d = new Date();    
							        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
							        String dateNowStr = sdf.format(d);  
							        System.out.println("格式化后的日期：" + dateNowStr);
							        Date myDate2 = sdf.parse(dateNowStr);
									ptst2.setDate(5,new java.sql.Date(myDate2.getTime()));
									ptst1.executeUpdate();
									ptst2.executeUpdate();
									JOptionPane.showMessageDialog(cons, "支付成功，该用户当前余额为：" + money.toString() + "元", "支付成功", JOptionPane.INFORMATION_MESSAGE);
									cons.dispose();
									createAccountFile();
									createLogFile();
								}
							}
						}
						else if (consType.getSelectedItem().toString().equals("积分消费")) {
							scoreStr = JOptionPane.showInputDialog(cons, "请输入消费积分:", "积分额度", JOptionPane.INFORMATION_MESSAGE);
							if(scoreStr.isEmpty()) {
								JOptionPane.showMessageDialog(cons, "消费积分不能为空", "输入错误", JOptionPane.WARNING_MESSAGE);
								scoreStr = JOptionPane.showInputDialog(cons, "请输入消费积分:", "积分额度", JOptionPane.INFORMATION_MESSAGE);
							}
							else if(!isNumeric(scoreStr)) {
								JOptionPane.showMessageDialog(cons, "消费积分必须为纯数字", "输入错误", JOptionPane.WARNING_MESSAGE);
								scoreStr = JOptionPane.showInputDialog(cons, "请输入消费积分:", "积分额度", JOptionPane.INFORMATION_MESSAGE);
							}
							else {
								Float saleScore = Float.parseFloat(scoreStr);
								if(score < saleScore) {
									JOptionPane.showMessageDialog(cons, "当前用户积分不足，请选择其他支付方式", "支付失败", JOptionPane.WARNING_MESSAGE);
								}
								else {
									score -= saleScore;
									sql1 = "update account set score = ? where name = \"" + name + "\"" + " and tail_num = \"" + tail_num + "\"";
									ptst1 = Database.prepareStatement(sql1);
									ptst1.setFloat(1,score);
									ptst1.executeUpdate();
									JOptionPane.showMessageDialog(cons, "支付成功，该用户当前积分为：" + score.toString(), "支付成功", JOptionPane.INFORMATION_MESSAGE);
									cons.dispose();
									createAccountFile();
									createLogFile();
								}
							}
						}
						else {
							moneyStr = JOptionPane.showInputDialog(cons, "请输入应支付金额:", "金额", JOptionPane.INFORMATION_MESSAGE);
							if(moneyStr.isEmpty()) {
								JOptionPane.showMessageDialog(cons, "金额不能为空", "输入错误", JOptionPane.WARNING_MESSAGE);
								moneyStr = JOptionPane.showInputDialog(cons, "请输入应支付金额:", "金额", JOptionPane.INFORMATION_MESSAGE);
							}
							else if(!isDouble(moneyStr)) {
								JOptionPane.showMessageDialog(cons, "金额应为纯数字", "输入错误", JOptionPane.WARNING_MESSAGE);
								moneyStr = JOptionPane.showInputDialog(cons, "请输入应支付金额:", "金额", JOptionPane.INFORMATION_MESSAGE);
							}
							else {
								Float saleScore = Float.parseFloat(moneyStr)*discount;
								String payMoney = saleScore.toString();
								int pay = JOptionPane.showConfirmDialog(cons, "应支付：" + payMoney + "元，支付成功后点击确认键", "支付金额", JOptionPane.YES_NO_OPTION);
								if(pay == JOptionPane.YES_OPTION) {
									score += saleScore;
									sql1 = "update account set score = ? where name = \"" + name + "\"" + " and tail_num = \"" + tail_num + "\"";
									sql2 = "insert into transaction (name , tail_num , type , amount ,date) values(?,?,?,?,?)";
									ptst1 = Database.prepareStatement(sql1);
									ptst2 = Database.prepareStatement(sql2);
									ptst1.setString(1, score.toString());
									ptst2.setString(1, name);
									ptst2.setString(2, tail_num);
									ptst2.setString(3, type.getSelectedItem().toString());
									ptst2.setFloat(4, saleScore);
									Calendar now = Calendar.getInstance();  
							        Date d = new Date();    
							        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
							        String dateNowStr = sdf.format(d);  
							        System.out.println("格式化后的日期：" + dateNowStr);
							        Date myDate2 = sdf.parse(dateNowStr);
									ptst2.setDate(5,new java.sql.Date(myDate2.getTime()));
									ptst1.executeUpdate();
									ptst2.executeUpdate();
									JOptionPane.showMessageDialog(cons, "支付成功，该用户当前积分为：" + score.toString(), "支付成功", JOptionPane.INFORMATION_MESSAGE);
									cons.dispose();
									createAccountFile();
									createLogFile();
								}
							}
						}
					}catch(Exception a) {
							a.printStackTrace();
					}
				}catch(Exception a){
					a.printStackTrace();
					JOptionPane.showMessageDialog(cons, "你好像还没选中一条记录哦，请用鼠标选中所消费会员", "操作失误", JOptionPane.WARNING_MESSAGE);
				}
				
			}
		}
	}
	class manageActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == modiSelect) {
				String sql = null;
				if(modifyString.getText().isEmpty()) {
					JOptionPane.showMessageDialog(mana, "查询信息不能为空，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
				}
				else if(index.getSelectedItem().toString().equals("尾号查询")&&!isNumeric(modifyString.getText())) {
					JOptionPane.showMessageDialog(mana, "尾号必须为纯数字，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
				}
				else {
					String temp;
					temp = modifyString.getText();
					if(index.getSelectedItem().toString().equals("姓名查询")) {
						sql = "select * from account where name = \"" + temp + "\"";
					}
					else if(index.getSelectedItem().toString().equals("尾号查询"))
						sql = "select * from account where tail_num = \""+ temp +"\"";
					//PreparedStatement pstmt = null;
					Statement statement;
					try {
						vData = new Vector();
						statement = Database.createStatement();
						ResultSet re = statement.executeQuery(sql);
						//pstmt = Database.prepareStatement(sql);
						//pstmt.setString(1, selectString.getText());
						//ResultSet re = pstmt.executeQuery();
						if(!re.next()) {
							JOptionPane.showMessageDialog(mana, "该用户并未办理会员，请核对", "输入错误", JOptionPane.WARNING_MESSAGE);
						}
						else
							re.previous();
						while(re.next()) {
							Vector row = new Vector();
							row.add(re.getString("name"));
							row.add(re.getString("tail_num"));
							row.add(re.getString("money"));
							row.add(re.getString("wechat"));
							row.add(re.getString("discount"));
							vData.add(row.clone());
						}
						model=new DefaultTableModel(vData,vName);
						consumeLog.setModel(model);
					}catch(Exception a) {
						a.printStackTrace();
						JOptionPane.showMessageDialog(mana, "该用户并未办理会员，请核对", "输入错误", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
			else if(e.getSource() == modify) {
				String name = null,tail_num = null;
				Float money = null;
				try {
					int row = consumeLog.getSelectedRow();
					name = (String) consumeLog.getValueAt(row, 0);
					tail_num = (String)consumeLog.getValueAt(row, 1);
					money = Float.parseFloat((String) consumeLog.getValueAt(row, 2));
					PreparedStatement ptst1 = null, ptst2 = null;
					String sql1= null,sql2 = null;
					String value;
					if(manaType.getSelectedItem().toString().equals("会员充值")) {
						
						value = JOptionPane.showInputDialog(mana, "请输入充值金额:", "金额", JOptionPane.INFORMATION_MESSAGE);
						while(value.isEmpty()) {
							JOptionPane.showMessageDialog(mana, "输入信息不能为空，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
							value  = JOptionPane.showInputDialog(mana, "请输入充值金额:", "金额", JOptionPane.INFORMATION_MESSAGE);
						}
						if(!isDouble(value)) {
							JOptionPane.showMessageDialog(mana, "金额应该为整数", "输入错误", JOptionPane.WARNING_MESSAGE);
							value = JOptionPane.showInputDialog(mana, "请输入充值金额:", "金额", JOptionPane.INFORMATION_MESSAGE);
						}
						else {
							try {
								Float valueMoney = Float.parseFloat(value);
								sql1 = "update account set money = ? where name = \"" + name + "\"" + " and tail_num = \"" + tail_num + "\"";
								ptst1 = Database.prepareStatement(sql1);
								money+= valueMoney;
								ptst1.setFloat(1, money);
								
								ptst1.executeUpdate();
								JOptionPane.showMessageDialog(mana, "充值成功，该用户当前金额为：" + money.toString(), "充值成功", JOptionPane.INFORMATION_MESSAGE);
								createAccountFile();
							}catch(Exception a) {}
						}
					}
					else if(manaType.getSelectedItem().toString().equals("更换微信")) {
						value  = JOptionPane.showInputDialog(mana, "请输入更换的微信号:", "微信", JOptionPane.INFORMATION_MESSAGE);
						if(value.isEmpty()) {
							JOptionPane.showMessageDialog(mana, "输入信息不能为空，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
							value  = JOptionPane.showInputDialog(mana, "请输入更换的微信号:", "微信", JOptionPane.INFORMATION_MESSAGE);
						}
						else {
							try {
								sql1 = "update account set wechat = ? where name = \"" + name + "\"" + " and tail_num = \"" + tail_num + "\"";
								ptst1 = Database.prepareStatement(sql1);
								ptst1.setString(1, value);
								
								ptst1.executeUpdate();
								JOptionPane.showMessageDialog(mana, "修改成功" , "成功", JOptionPane.INFORMATION_MESSAGE);
								createAccountFile();
							}catch(Exception a) {}
						}
					}
					else if(manaType.getSelectedItem().toString().equals("更换手机")) {
						value  = JOptionPane.showInputDialog(mana, "请输入更换的手机号:", "手机", JOptionPane.INFORMATION_MESSAGE);
						if(value.isEmpty()) {
							JOptionPane.showMessageDialog(mana, "输入信息不能为空，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
							value  = JOptionPane.showInputDialog(mana, "请输入更换的手机号:", "手机", JOptionPane.INFORMATION_MESSAGE);
						}
						else if (!isNumeric(value)) {
							JOptionPane.showMessageDialog(mana, "手机号必须为纯数字，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
							value  = JOptionPane.showInputDialog(mana, "请输入更换的手机号:", "手机", JOptionPane.INFORMATION_MESSAGE);
						}
						else if (value.length()!=11) {
							JOptionPane.showMessageDialog(mana, "手机号必须为11位纯数字，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
							value  = JOptionPane.showInputDialog(mana, "请输入更换的手机号:", "手机", JOptionPane.INFORMATION_MESSAGE);
						}
						else {
							try {
								sql1 = "update account set phone = ? ,tail_num = ? where name = \"" + name + "\"" + " and tail_num = \"" + tail_num + "\"";
								ptst1 = Database.prepareStatement(sql1);
								String tailNum = value.substring(value.length()-4);
								ptst1.setString(1, value);
								ptst1.setString(2, tailNum);
								ptst1.executeUpdate();
								JOptionPane.showMessageDialog(mana, "修改成功：" , "成功", JOptionPane.INFORMATION_MESSAGE);
								createAccountFile();
							}catch(Exception a) {}
						}	
					}
					else {
						value  = JOptionPane.showInputDialog(mana, "请输入新的优惠:", "优惠", JOptionPane.INFORMATION_MESSAGE);
						if(value.isEmpty()) {
							JOptionPane.showMessageDialog(mana, "输入信息不能为空，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
							value  = JOptionPane.showInputDialog(mana, "请输入新的优惠:", "优惠", JOptionPane.INFORMATION_MESSAGE);
						}
						else if (!isDouble(value)) {
							JOptionPane.showMessageDialog(mana, "优惠应为小数，如九折为0.9，没有折扣应为1.0，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
							value  = JOptionPane.showInputDialog(mana, "请输入新的优惠:", "优惠", JOptionPane.INFORMATION_MESSAGE);
						}
						else if (Float.parseFloat(value)>1.0) {
							JOptionPane.showMessageDialog(mana, "优惠不能大于1.0，如九折为0.9，没有折扣应为1.0", "请重新输入", JOptionPane.WARNING_MESSAGE);
							value  = JOptionPane.showInputDialog(mana, "请输入新的优惠:", "优惠", JOptionPane.INFORMATION_MESSAGE);
						}
						else {
							try {
								sql1 = "update account set discount = ? where name = \"" + name + "\"" + " and tail_num = \"" + tail_num + "\"";
								ptst1 = Database.prepareStatement(sql1);
								Float valueDis = Float.parseFloat(value);
								ptst1.setFloat(1, valueDis);
								
								ptst1.executeUpdate();
								JOptionPane.showMessageDialog(mana, "修改成功：" , "成功", JOptionPane.INFORMATION_MESSAGE);
								createAccountFile();
							}catch(Exception a) {}
						}	
					}
				}catch(Exception a){
					a.printStackTrace();
					JOptionPane.showMessageDialog(mana, "你好像还没选中一条记录哦，请用鼠标选中所消费会员", "操作失误", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		
	}
	class countActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			coun = new JFrame("今日统计");
			Container cou = coun.getContentPane();
			cou.setLayout(new BorderLayout());
			JLabel sumMoneyLabel = new JLabel("今日统计交易金额：");
			JTextField sumMoneyText = new JTextField(8);
			
			JTextArea logGroup = new JTextArea();
			JScrollPane js = new JScrollPane();
			js.setViewportView(logGroup);
			js.setAutoscrolls(true);
			js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			logGroup.setEditable(false);
			logGroup.append("种类" + "\t" + "数量");
			logGroup.append("\n");
			PreparedStatement sta1 = null,sta2 = null;
			String type = null, amount = null,output = null;
			try {
				String sql1 = "select sum(amount) from transaction where date = ?";
				String sql2 = "select type ,count(type) from transaction where date = ? group by type ";
				sta1 = Database.prepareStatement(sql1);
				sta2 = Database.prepareStatement(sql2);
				Calendar now = Calendar.getInstance();  
		        Date d = new Date();    
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		        String dateNowStr = sdf.format(d);  
		        //System.out.println("格式化后的日期：" + dateNowStr);
		        Date myDate2 = sdf.parse(dateNowStr);
				sta1.setDate(1,new java.sql.Date(myDate2.getTime()));
				sta2.setDate(1,new java.sql.Date(myDate2.getTime()));
				ResultSet re1 = sta1.executeQuery();
				ResultSet re2 = sta2.executeQuery();
				if(re1.next()) {
					sumMoneyText.setText(re1.getString("sum(amount)"));
				}else {
					JOptionPane.showMessageDialog(coun, "当日还未产生交易" , "交易记录", JOptionPane.INFORMATION_MESSAGE);
				}
				while(re2.next()) {
					type = re2.getString("type");
					amount = re2.getString("count(type)");
					output = type + "\t"  + amount;
					logGroup.append(output);
					logGroup.append("\n");
				}
			}catch(Exception a) {
				a.printStackTrace();
			}
			sumMoneyText.setEditable(false);
			JPanel jp = new JPanel();
			jp.add(sumMoneyLabel);
			jp.add(sumMoneyText);
			cou.add(jp,BorderLayout.NORTH);
			cou.add(js,BorderLayout.CENTER);
			
			coun.setLocation(200,300);;
			coun.setSize(400, 300);
			coun.setVisible(true);
		}
		
	}

}
