package process_a;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import com.opencsv.CSVWriter;

public class start_compare {


	public static void main(String[] args) throws IOException{


		FileInputStream f = new FileInputStream(new File("C:\\Author_test\\thiyagu\\Excel\\1.xlsx"));

		FileOutputStream fos = null;
		OutputStreamWriter osw;


		try{
			fos = new FileOutputStream("C:\\Author_test\\Jimmy\\source\\Aprocess_compare.csv"); 
		}catch(FileNotFoundException | NullPointerException ff){
			System.out.println("Please close the output file");
			System.exit(0);
		}



		osw=new OutputStreamWriter(fos);
		CSVWriter writer = new CSVWriter(osw);

		XSSFWorkbook wb = new XSSFWorkbook(f);
		XSSFSheet sheet = wb.getSheetAt(0);


		String[] head = {};
		writer.writeNext(head);
		for(int j=0;j<sheet.getLastRowNum()+1;j++){

			Row r = sheet.getRow(j);	
			Cell ce = r.getCell(0);
			Cell c2 = r.getCell(1);

			String col1=ce.toString();
			String col2=c2.toString();


			String finalcol2 = String.format("%03d", Math.round(Float.parseFloat(col2))+1);	
			String xmlfilename = "C:\\Author_test\\thiyagu\\xml\\"+col1+"\\ITEMS\\E0000"+finalcol2+".IDT";	
			//			String xmlfilename = "U:\\"+col1+"\\ITEMS\\E0000"+finalcol2+".IDT";

			String finalcol2idt = String.format("%03d", Math.round(Float.parseFloat(col2)));
			String filename = "C:\\Author_test\\thiyagu\\Files\\"+col1+"\\ITEMS\\"+finalcol2idt+"A.IDT";
			//			String filename = "W:\\"+col1+"\\ITEMS\\"+finalcol2idt+"A.IDT";

			String content ="";
			String xml_content="";

			File xmlfile = new File(xmlfilename);
			File file = new File(filename);
			

			try{
				xml_content = FileUtils.readFileToString(xmlfile,"UTF-8");	
				content = FileUtils.readFileToString(file, "UTF-8");
			}catch(IOException e){
			
				String [] all2={"Not Available",col1,col2};
				writer.writeNext(all2);
				continue;
			}
			System.out.println(col1);
			Element docx = Jsoup.parse(xml_content.replaceAll("<keyword>(.+?)</keyword>", ""));		
			Element doc = Jsoup.parse(content.replaceAll("<keyword>(.+?)</keyword>", ""));

			String xtitle_count = Integer.toString(docx.getElementsByTag("title").size());
			String title_count = Integer.toString(doc.getElementsByTag("title").size());
			int title_match = 0;
			int title_mismatch = 0;
			
			String xtitle=docx.getElementsByTag("title").text();
			String title = doc.getElementsByTag("title").text();
			
			if(xtitle.equals(title)){
					title_match++;
			}else{
				title_mismatch++;
			}

			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			String gs_j="";
			String gxi="";
			String gs_itype="";
			
			try{
			gs_j = doc.getElementsByTag("journalid").first().text();	
			}catch(Exception e){
				
			}
			try{
			gxi = docx.getElementsByTag("publisher").first().text();
			}catch(NullPointerException n){
				
			}
			try{
			gs_itype = doc.getElementsByTag("itemtype").first().text();
			}catch(IndexOutOfBoundsException e){
				
			}
			
			String[] all={gs_j,gxi,gs_itype,xtitle_count,title_count,Integer.toString(title_match),Integer.toString(title_mismatch)};
			
			writer.writeNext(all);
			
		}

		wb.close();
		writer.close();
		osw.close();
		fos.close();
		f.close();
		System.exit(0);

	}
}
