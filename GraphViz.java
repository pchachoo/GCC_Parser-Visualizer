



// GraphViz.java - a simple API to call dot from Java programs

/*$Id$*/
/*
******************************************************************************
* *
* (c) Copyright Laszlo Szathmary *
* *
* This program is free software; you can redistribute it and/or modify it *
* under the terms of the GNU Lesser General Public License as published by *
* the Free Software Foundation; either version 2.1 of the License, or *
* (at your option) any later version. *
* *
* This program is distributed in the hope that it will be useful, but *
* WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY *
* or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public *
* License for more details. *
* *
* You should have received a copy of the GNU Lesser General Public License *
* along with this program; if not, write to the Free Software Foundation, *
* Inc., 675 Mass Ave, Cambridge, MA 02139, USA. *
* *
******************************************************************************
*/

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;


public class GraphViz
{
   /**
* The dir. where temporary files will be created.
*/
   //private static String TEMP_DIR = "/tmp"; // Linux
  private static String TEMP_DIR = "c:\\temp"; // Windows

   /**
* Where is your dot program located? It will be called externally.
*/
   //private static String DOT = "/usr/bin/dot"; // Linux
 //private static String DOT = "c:/Program Files/Graphviz2.26.3/bin/dot.exe"; // Windows
 private static String DOT = "F:/IITB PROJECT/graphviz-2.28.0/bin/rebuilt.graphviz-bin-2.29.20110819.0445/release/bin/dot.exe"; // Windows
 //FileRead fr = new FileRead("Location of DOT.exe");//to do
 //File name = FileRead.name;// to do 
 //private  String DOT = name.toString();//to do
 
   /**
* The image size in dpi. 96 dpi is normal size. Higher values are 10% higher each.
* Lower values 10% lower each.

*/
   private int[] dpiSizes = {46, 51, 57, 63, 70, 78, 86, 96, 106, 116, 128, 141, 155, 170, 187, 206, 226, 249};
   
   /**
* Define the index in the image size array.
*/
   private int currentDpiPos = 7;
      
   /**
* Increase the image size (dpi).
*/
   public void increaseDpi() {
      if ( this.currentDpiPos < (this.dpiSizes.length - 1) ) {
         ++this.currentDpiPos;
      }
   }
   
   /**
* Decrease the image size (dpi).
*/
   public void decreaseDpi() {
      if (this.currentDpiPos > 0) {
         --this.currentDpiPos;
      }
   }
   
   public int getImageDpi() {
      return this.dpiSizes[this.currentDpiPos];
   }
   
   /**
* The source of the graph written in dot language.
*/
private StringBuilder graph = new StringBuilder();

   /**
* Constructor: creates a new GraphViz object that will contain
* a graph.
*/
   public GraphViz() {
   }

   /**
* Returns the graph's source description in dot language.
* @return Source of the graph in dot language.
*/
   public String getDotSource() {
      return this.graph.toString();
   }

   /**
* Adds a string to the graph's source (without newline).
*/
   public void add(String line) {
      this.graph.append(line);
   }

   /**
* Adds a string to the graph's source (with newline).
*/
   public void addln(String line) {
      this.graph.append(line + "\n");
   }

   /**
* Adds a newline to the graph's source.
*/
   public void addln() {
      this.graph.append('\n');
   }
   
   public void clearGraph(){
      this.graph = new StringBuilder();
   }

   /**
* Returns the graph as an image in binary format.
* @param dot_source Source of the graph to be drawn.
* @param type Type of the output image to be produced, e.g.: gif, dot, fig, pdf, ps, svg, png.
* @return A byte array containing the image of the graph.
*/
   public byte[] getGraph(String dot_source, String type)
   {
      File dot;
      byte[] img_stream = null;
   
      try {
         dot = writeDotSourceToFile(dot_source);
         if (dot != null)
         {
            img_stream = get_img_stream(dot, type);
            if (dot.delete() == false)
               System.err.println("Warning: " + dot.getAbsolutePath() + " could not be deleted!");
            return img_stream;
         }
         return null;
      } catch (java.io.IOException ioe) { return null; }
   }

   /**
* Writes the graph's image in a file.
* @param img A byte array containing the image of the graph.
* @param file Name of the file to where we want to write.
* @return Success: 1, Failure: -1
*/
   public int writeGraphToFile(byte[] img, String file)
   {
      File to = new File(file);
      return writeGraphToFile(img, to);
   }

   /**
* Writes the graph's image in a file.
* @param img A byte array containing the image of the graph.
* @param to A File object to where we want to write.
* @return Success: 1, Failure: -1
*/
   public int writeGraphToFile(byte[] img, File to)
   {
      try {
         FileOutputStream fos = new FileOutputStream(to);
         fos.write(img);
         fos.close();
      } catch (java.io.IOException ioe) { return -1; }
      return 1;
   }

   /**
* It will call the external dot program, and return the image in
* binary format.
* @param dot Source of the graph (in dot language).
* @param type Type of the output image to be produced, e.g.: gif, dot, fig, pdf, ps, svg, png.
* @return The image of the graph in .gif format.
*/
   private byte[] get_img_stream(File dot, String type)
   {
      File img;
      byte[] img_stream = null;

      try {
         img = File.createTempFile("graph_", "."+type, new File(GraphViz.TEMP_DIR));
         Runtime rt = Runtime.getRuntime();
         
         String[] args = {DOT, "-T"+type, "-Gdpi="+dpiSizes[this.currentDpiPos], dot.getAbsolutePath(), "-o", img.getAbsolutePath()};
         Process p = rt.exec(args);
         
         p.waitFor();

         FileInputStream in = new FileInputStream(img.getAbsolutePath());
         img_stream = new byte[in.available()];
         in.read(img_stream);
         // Close it if we need to
         if( in != null ) in.close();

         if (img.delete() == false)
            System.err.println("Warning: " + img.getAbsolutePath() + " could not be deleted!");
      }
      catch (java.io.IOException ioe) {
         System.err.println("Error: in I/O processing of tempfile in dir " + GraphViz.TEMP_DIR+"\n");
         System.err.println(" or in calling external command");
         ioe.printStackTrace();
      }
      catch (java.lang.InterruptedException ie) {
         System.err.println("Error: the execution of the external program was interrupted");
         ie.printStackTrace();
      }

      return img_stream;
   }

   /**
* Writes the source of the graph in a file, and returns the written file
* as a File object.
* @param str Source of the graph (in dot language).
* @return The file (as a File object) that contains the source of the graph.
*/
   private File writeDotSourceToFile(String str) throws java.io.IOException
   {
      File temp;
      try {
         temp = File.createTempFile("graph_", ".dot.tmp", new File(GraphViz.TEMP_DIR));
         FileWriter fout = new FileWriter(temp);
         fout.write(str);
         fout.close();
      }
      catch (Exception e) {
         System.err.println("Error: I/O error while writing the dot source to temp file!");
         return null;
      }
      return temp;
   }

   /**
* Returns a string that is used to start a graph.
* @return A string to open a graph.
*/
   public String start_graph() {
      return "digraph G {";
   }

   /**
* Returns a string that is used to end a graph.
* @return A string to close a graph.
*/
   public String end_graph() {
      return "}";
   }

   /**
* Read a DOT graph from a text file.
*
* @param input Input text file containing the DOT graph
* source.
*/
   public void readSource(String input)
   {
StringBuilder sb = new StringBuilder();

try
{
FileInputStream fis = new FileInputStream(input);
DataInputStream dis = new DataInputStream(fis);
BufferedReader br = new BufferedReader(new InputStreamReader(dis));
String line;
while ((line = br.readLine()) != null) {
sb.append(line);
}
dis.close();
}
catch (Exception e) {
System.err.println("Error: " + e.getMessage());
}

this.graph = sb;
   }
   
} // end of class GraphViz

