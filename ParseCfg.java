


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

class blockContents
{
	StringBuffer blockName;
	StringBuffer blockContent;
};
public class ParseCfg
{
  
   /**
* Construct a DOT graph in memory, convert it
* to image and store the image in the file system.
 * @throws Throwable 
*/
   private static String readFileAsString(String filePath)
   throws java.io.IOException{
       StringBuffer fileData = new StringBuffer(1000);
       BufferedReader reader = new BufferedReader(  new FileReader(filePath));
       char[] buf = new char[1024];
       int numRead=0;
       while((numRead=reader.read(buf)) != -1){
           String readData = String.valueOf(buf, 0, numRead);
           fileData.append(readData);
           buf = new char[1024];
       }
       reader.close();
       return fileData.toString();
   }

   
   public void start(myVertex[] myVertexList,myEdges[] myEdgeList, VEcount VEcount ) throws Throwable
   {
	   int i = 0;
	   	    
	   FileRead fr = new FileRead("Open the gcc output file to view");
	   File name = FileRead.name;
		//String s = readFileAsString("D:\\asc2011\\iitb_proj\\source\\gcc_output.txt");
	   	String s = readFileAsString(name.toString());
				
		LinkedList <String>TokenList = new LinkedList<String>();// List of tokens parsed and stored in TokenList 
	    StringTokenizer Token = new StringTokenizer(s,"\r\n");
	   
	    while (Token.hasMoreElements())
		   TokenList.addLast(Token.nextToken());
	   
		
		@SuppressWarnings("rawtypes")
		LinkedList[] TokenVector = new LinkedList[1024];
		for ( i = 0; i < TokenVector.length; i++ ) {
			TokenVector[i] = new LinkedList();
		}
		
		blockContents[] blocks = new blockContents[1024];
		for(i=0; i<1024;i++)
		{	
			blocks[i] = new  blockContents();
			blocks[i].blockName = new StringBuffer();
			blocks[i].blockContent = new StringBuffer(); 
		}
		 ParseBlock(  TokenList,TokenVector,blocks);// Take the TokenList and group the connected blocks in array of lists
		
		 //use Contents of TokenVVector array to draw the graphs
		String strToAdd;		
		
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		
		for ( i = 0; i < TokenVector.length; i++ ) {
    	  
    		  if(TokenVector[i].size()> 1)// if more than one element, it call for calling gv.addln
    		  {
    		      for ( int j = 1; j < TokenVector[i].size(); j++ )
    		      {
    		    	  strToAdd= null;
    		    	  strToAdd = (String)TokenVector[i].get(0);
    		    	  strToAdd = strToAdd+" -> ";
    		    	  strToAdd = strToAdd+(String)TokenVector[i].get(j);
    		    	  strToAdd = strToAdd + ";" ;
    		    	  gv.addln(strToAdd);
    		      }
    		  }
       		  else if(TokenVector[i].size()==1)// to draw single unconnected blocks
		      {
 		    	  strToAdd= null;
		    	  strToAdd = (String)TokenVector[i].get(0);
  		    	  gv.addln(strToAdd);
		      }

      }
      gv.addln(gv.end_graph());
      System.out.println(gv.getDotSource());
      
      gv.increaseDpi(); // 106 dpi
         
      String type = "dot";
      // String type = "pdf";
            
     // File out = new File("/tmp/out"+gv.getImageDpi()+"."+ type); // Linux
      fr = new FileRead("Select the location to save the file"); // Windows
	  name = FileRead.name;      
	  File out = new File(name + "."+ type); // Windows
      gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
      
 // adding  interactive graph
      //read the .dot file
      s = readFileAsString(out.toString());
      // create token of the file
      Token = new StringTokenizer(s,"\r\n\t");
      TokenList.clear();
      while (Token.hasMoreElements())
		   TokenList.addLast(Token.nextToken());
      
      ParseDotFile(TokenList, myVertexList,myEdgeList,VEcount);
   }
   
   
   
   private void ParseDotFile(LinkedList<String> TokenList, myVertex[] myVertexList, myEdges[] myEdgeList, VEcount VEcount) {
		// TODO Auto-generated method stub
		   int size = TokenList.size();
		   int posB = -1;
		   int index = -1 ;
		   int VertexListIndex = 0, EdgeListIndex = 0;
		   String token = null; 
		   //int substrend;
		   posB = 4;
		   token = TokenList.get(posB);
		   while(token.indexOf("-",0)== -1 )// it is not an edge 
		   {
			   //int ret1 = token.indexOf("<");
			   int ret1 = token.indexOf("[");
			   int ret2 = token.indexOf(",");
			   String substr = token.substring(0, ret1-1);
			   
			   myVertexList[VertexListIndex].vertexId = VEcount.noVertices;
			   //if (substr.charAt(0) == '<')
			   //{
				 //  substrend = substr.indexOf('>');
				  // substr = substr.substring(1, substrend);
			   //}
			   myVertexList[VertexListIndex].vertexName = substr;
			   
			   substr = token.substring(ret1+6, ret2);
			  
			   int ret3= token.indexOf("\"",ret2);
			   String substr2 = token.substring(ret2+1, ret3);
			   
			   myVertexList[VertexListIndex++].Point.setLocation(Float.parseFloat(substr),Float.parseFloat(substr2));
			   VEcount.noVertices++;
			   posB++;
			   token = TokenList.get(posB);
		   } 
		   while(posB < (size -1))
		   {
			   int k = 0;
			   int ret1 = token.indexOf("-");
			   if(ret1 != -1)
			   {
				   String substr = token.substring(0, ret1-1);
				  
				   while(k<1024)
				   {
						   if(myVertexList[k].vertexName.equals(substr))
							   break;
						   k++;
				   }
				   myEdgeList[EdgeListIndex].vertexId1 = myVertexList[k].vertexName;//Integer.parseInt(substr,10);
				   
				   int ret2 = token.indexOf("[",ret1);
				   String substr2 = token.substring(ret1+3, ret2-1);
				   k=0;
				   while(k<1024)
				   {
						   if(myVertexList[k].vertexName.equals(substr2))
							   break;
						   k++;
				   }
				   myEdgeList[EdgeListIndex].vertexId2 = myVertexList[k].vertexName;//Integer.parseInt(substr2,10);
				   
				   if(EdgeListIndex > 0)
					   myEdgeList[EdgeListIndex].edgeName = myEdgeList[EdgeListIndex-1].edgeName + " ";
				   else 
					   myEdgeList[EdgeListIndex].edgeName = " ";
				   EdgeListIndex++;
				   VEcount.noEdges++;
				   }
				   posB++;
				   token = TokenList.get(posB);	   
			   }
	}

   /**
* Read the DOT source from a file,
* convert to image and store the image in the file system.
*/

   private void  ParseBlock(LinkedList<String> TokenList ,LinkedList<String>[] TokenVector, blockContents[] blocks)
   {
		int i,index;
		String substr;//improve this
		LinkedList<String> AliasList = new LinkedList<String>();

		index = 0;
		int size = TokenList.size();
		
		String token = TokenList.getFirst(); // copy the first block to TokenVector as it is
		
		i = token.indexOf(":");
		token = token.substring(0, i);// remove ':', else will not work
		TokenVector[index].addLast(new String(token));
		blocks[index].blockName.insert(0, token);
		
		int posB = 1;
		while(posB != size)
		{
			token = TokenList.get(posB); // get next block
	
			if((token.equalsIgnoreCase("else"))||(token.equalsIgnoreCase("  else")))// just discard it
			{
				posB++;
				continue;
			}
			substr = "";
			
			int ret = token.indexOf(":",0);
			if(ret != -1) //":" exists so it may be a new block
			{
				//check if the string upto ':" is an alias ; if yes then it is a new block
				// otherwise check that ">" precedes ":" ; only then it is a new block
				
				String s = token.substring(0,ret);
				if (AliasList.indexOf(s)!= -1)
				{
				index++; // next vector to be used
				token = token.substring(0, ret);// remove ':', else will not work
				TokenVector[index].addLast(new String(token));
				blocks[index].blockName.insert(0, token);
				posB++;

				continue;
				}	
				if (token.charAt(ret-1)=='>')
				{
					index++; // next vector to be used
					token = token.substring(0, ret);// remove ':', else will not work
					TokenVector[index].addLast(new String(token));
					blocks[index].blockName.insert(0, token);
					posB++;

					continue;
				}
			}

			ret = token.indexOf(" goto ");// for lines like 'D.3607 = yypgoto[D.3606];'
	
			if(ret == -1)// there is no goto ; it is a plain block to be added under blocks 
			{
				blocks[index].blockContent.append(token);
			}
			else
			{
				// first check if < bb xx> is followed by an alias; if yes just use it 
				int ret1 = token.indexOf("(");
				if (ret1 == -1)// there is no alias
				{
					int ret3 = token.indexOf("<");
					int ret4 = token.indexOf(">");
					substr = token.substring(ret3, ret4+1);
				}
				else
				{
					int ret2 = token.indexOf(")");
					substr = token.substring(ret1+1, ret2);
					AliasList.addLast(substr);
				}
				TokenVector[index].addLast(substr);
			}
			posB++;
		}  
		return;
   }
 }

