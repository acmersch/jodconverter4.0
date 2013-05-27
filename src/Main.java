import java.io.File;
import java.net.ConnectException;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

/** 
 * @author 沈晨辉 E-mail: acmersch@163.com
 * @version 创建时间：2013-5-6 上午10:23:14 
 * 类说明 
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}
	
	
	class ConverThread extends Thread {
		private File input;
		private File output;
		
		public ConverThread(File in, File out){
			input = in;
			output = out;
		}

		@Override
		public void run() {
			OpenOfficeConnection connection = new SocketOpenOfficeConnection("192.168.0.119", 8080);   
			  
			try {
				connection.connect();
				long t1 = System.currentTimeMillis();
				System.out.println(input+"文件开始转换");
				DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
				
				converter.convert(input, output);
				long t2 = System.currentTimeMillis();
				System.out.println(input+"文件转换结束: 花费 "+(t2-t1)+" ms");
			} catch (ConnectException e) {
				e.printStackTrace();
			} finally {
				if (connection.isConnected()){
					connection.disconnect();
				}
			}
		}
		
		
	}
	
	public Main(){
		String[] files = {"1.dps", "2.et", "3.wps", "4.ppt", "5.xlsx", "6.pptx", "7.vsd", "8.doc", "9.docx"};
		
		
		
		for (int i=0;i<9;i++){
			File inputFile = new File("/Users/acmersch/Downloads/test/"+files[i]);   		  	
			File outputFile = new File("/Users/acmersch/Downloads/test/"+i+".pdf");   		  			 
			new ConverThread(inputFile, outputFile).start();
		}
		
		   
		  
	}

}
 