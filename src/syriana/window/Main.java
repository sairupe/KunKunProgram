package syriana.window;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import syriana.common.AbstractHandler;
import syriana.common.MainType;
import syriana.common.MenuManager;
import syriana.common.SubType;
import javax.swing.JButton;
import java.awt.Font;
import java.io.File;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	public JPanel contentPane;
	private JTextField txtver;
	private JComboBox comboBox;
	private JComboBox comboBox_1;
	public static JTextArea textArea;
	private JFileChooser jfc = new JFileChooser();  
	private JTextField pathShow;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	public Main() throws Exception {
		setTitle("莫式的专属处理工具 测试版本Ver 0.1 Design By Syriana");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 745, 418);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtver = new JTextField();
		txtver.setHorizontalAlignment(SwingConstants.CENTER);
		txtver.setFont(new Font("宋体", Font.PLAIN, 14));
		txtver.setText("左边选择大类，右边选择子类型");
		txtver.setBounds(21, 10, 293, 28);
		contentPane.add(txtver);
		txtver.setColumns(10);
		txtver.setEditable(false);
		
		// 初始化菜单
		initSelectMenu();
	}
	
	/**
	 * 初始化选择菜单
	 * @throws Exception
	 */
	public void initSelectMenu() throws Exception{
		
		MenuManager.getInstance().initMenu();
		
		// 处理信息输出框
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(21, 112, 698, 263);
		contentPane.add(textArea);
		
		// 左边主类型单选框
		comboBox = new JComboBox();
		comboBox.setBounds(21, 48, 177, 21);
		contentPane.add(comboBox);
		List<MainType> mainTypeList = MenuManager.getInstance().getMainTypeList();
		for(MainType mainType : mainTypeList){
			comboBox.addItem(mainType.getTypeName() + "(" + mainType.getTypeValue() + ")");
		}
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					JComboBox target = (JComboBox) e.getSource();
					String mainTypeStr = (String) target.getSelectedItem();
					int firstKuo = mainTypeStr.indexOf("(") + 1;
					String idStr = mainTypeStr.substring(firstKuo, mainTypeStr.lastIndexOf(")"));
					int id = Integer.parseInt(idStr);
					fillSubMenuList(id);
					clearLogger();
				}
			}
		});
		
		// 右边子类型单选框
		comboBox_1 = new JComboBox();
		comboBox_1.setBounds(219, 48, 161, 21);
		contentPane.add(comboBox_1);
		
		JButton button = new JButton("点一下选择目录或文件啊~");
		button.setBounds(21, 79, 177, 23);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int mainId = getTypeIdBySelectedItemString(comboBox.getSelectedItem());
				int subId = getTypeIdBySelectedItemString(comboBox_1.getSelectedItem());
				try {
					showFileChooser(mainId, subId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		contentPane.add(button);
		
		// 选择路径显示
		pathShow = new JTextField();
		pathShow.setBounds(219, 81, 422, 21);
		contentPane.add(pathShow);
		pathShow.setColumns(10);
		pathShow.setEditable(false);
	
		// 赋予JBOX默认值
		comboBox.setSelectedIndex(1);
		fillSubMenuList(MainType.DinnerFee.getTypeValue());
	}
	
	/**
	 * 根据单选框选择的菜单筛选出ID
	 */
	public int getTypeIdBySelectedItemString(Object obj){
		String mainTypeStr = (String) obj;
		int firstKuo = mainTypeStr.indexOf("(") + 1;
		String idStr = mainTypeStr.substring(firstKuo, mainTypeStr.lastIndexOf(")"));
		int id = Integer.parseInt(idStr);
		return id;
	}
	
	/**
	 * 根据主类型填充子类型菜单 
	 */
	public void fillSubMenuList(int mainTypeId){
		comboBox_1.removeAllItems();
		MainType mainType = MainType.getMainTypeById(mainTypeId);
		List<SubType> subList = MenuManager.getInstance().getSubListByMainType(mainType);
		for(SubType sub : subList){
			comboBox_1.addItem(sub.getTypeName() + "(" + sub.getTypeValue() + ")");
		}
	}
	
	/**
	 * 弹出选择文件框 
	 */
	public void showFileChooser(int mainId, int subId) throws Exception{
		clearLogger();
		if(subId == 99999){
			JOptionPane.showMessageDialog(null, "你选中了测试子类型，请重新选择", "叮咚", JOptionPane.ERROR_MESSAGE);
			return ;
		}
        SubType sub = SubType.getSubTypeById(subId);
        boolean isDirector = sub.isChooseDirector();
        jfc.setFileSelectionMode(isDirector ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
        jfc.setDialogTitle(isDirector ? "请选择目录....注意是文件目录!" : "请选择文件...注意是文件!"); 
        int result = jfc.showOpenDialog(this); 
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			String path = file.getAbsolutePath();
			pathShow.setText(path);
			pathShow.update(pathShow.getGraphics());
			AbstractHandler handler = MenuManager.getInstance().getHandlerByMainId(mainId);
			disableCompment();
			handler.disPatch(subId, path);
			enbleCompment();
		}
	}
	
	/**
	 * 清空输出日志
	 */
	public void clearLogger(){
		textArea.setText("");
		pathShow.setText("");
	}
	
	/**
	 * 输出日志
	 */
	public static void addLogger(String logger){
		textArea.append(logger + "\n");
		textArea.update(textArea.getGraphics());
	}
	
	/**
	 * 禁用所有选项
	 */
	public void disableCompment(){
		comboBox.setEnabled(false);
		comboBox_1.setEnabled(false);
		jfc.setEnabled(false);
	}
	/**
	 * 解除禁用
	 */
	public void enbleCompment(){
		comboBox.setEnabled(true);
		comboBox_1.setEnabled(true);
		jfc.setEnabled(true);
	}
}
