import java.io.*;
import java.awt.*;
import javax.swing.*;
class FileRead extends JFrame {
	static File name;
		
public 	FileRead(String title){
	try{
	  FileDialog fd = new FileDialog(this, title, FileDialog.LOAD); 
      fd.setDirectory(System.getProperty("user.dir")); 
      fd.setVisible(true);	
   	  name= new File(fd.getDirectory(), fd.getFile());
   	 }catch(Exception e){
   	 	System.err.println(e.getMessage());
   	 	}
   	//setSize(400,400);
   //	setVisible(true);
   	 }
   }