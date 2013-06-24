import java.io.File;
import java.net.ConnectException;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

/** 
 * @author �򳿻� E-mail: acmersch@163.com
 * @version ����ʱ�䣺2013-5-6 ����10:23:14 
 * ��˵�� 
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
			OpenOfficeConnection connection = new SocketOpenOfficeConnection(8080);   
			  
			try {
				connection.connect();
				long t1 = System.currentTimeMillis();
				System.out.println(input+"�ļ���ʼת��");
				DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
				
				converter.convert(input, output);
				long t2 = System.currentTimeMillis();
				System.out.println(input+"�ļ�ת������: ���� "+(t2-t1)+" ms");
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
 