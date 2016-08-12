/*
 * InteractiveGraphView1.java
 *
 * Created on Jan 18, 2012
 *
 
 */



import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;


import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import javax.swing.JFrame;


class myVertex
{
	Integer vertexId; 
	String vertexName;
	Point2D Point;
};
class myEdges
{
	String edgeName;
	String vertexId1;
	String vertexId2;
};
class VEcount
{
	Integer noVertices = 0; 
	Integer noEdges = 0;
};
public class InteractiveGraphView1 {
    Graph<String, String> g;
    /** Creates a new instance of SimpleGraphView */
    public InteractiveGraphView1() {
        // Graph<V, E> where V is the type of the vertices and E is the type of the edges
    	g = new SparseMultigraph<String, String>();
   }
    
    /**
     * @param args the command line arguments
     * @throws Throwable 
     */
  
	public static void main(String[] args) throws Throwable {
    	
    	VEcount VEcount = new VEcount();
    	myVertex[] myVertexList = new myVertex[1024] ;
		myEdges[] myEdgeList = new myEdges[1024];
		VEcount.noEdges = new Integer(0);
		VEcount.noVertices = new Integer(0);
		
		for(int i=0; i<1024;i++)
		{	
			myVertexList[i] = new  myVertex();
	
			myVertexList[i].vertexName = new String();
			myVertexList[i].vertexId = new Integer(0);
			myVertexList[i].Point = new Point(); 
			myEdgeList[i] = new  myEdges();
			
			myEdgeList[i].vertexId1 = new String();
			myEdgeList[i].vertexId2 = new String();
			myEdgeList[i].edgeName = new String();
		}    		
		
    	//read the gcc output
    ParseCfg p = new ParseCfg();
    p.start(myVertexList,myEdgeList,VEcount);
    
 
    InteractiveGraphView1 sgv = new InteractiveGraphView1(); // Creates the graph...
    int len = VEcount.noVertices;
    int index = 0;
    while(len > 0)
    {
    	sgv.g.addVertex(myVertexList[index].vertexName);
    	len--;
    	index++;
    }
    len = VEcount.noEdges;
    index = 0;
    while(len > 0)
    {
    	boolean success = sgv.g.addEdge(myEdgeList[index].edgeName,  myEdgeList[index].vertexId1, myEdgeList[index].vertexId2,EdgeType.DIRECTED);
    	if (success) {
		} else {
		}
    	len--;
    	index++;
    }     
       // Layout<String, String> layout = new CircleLayout<String, String>(sgv.g);
        //((CircleLayout<String, String>) layout).setRadius(300);
    AntHillLayout<String, String> layout = new AntHillLayout<String,String> (sgv.g,myVertexList[0].vertexId) ;
    
    len = VEcount.noVertices;
    index = 0;
    while(len > 0)
    {
   	layout.setLocation(myVertexList[index].vertexName,myVertexList[index].Point);    	
    len--;
	index++;
    }
     
        layout.setSize(new Dimension(800,800));
        VisualizationViewer<String,String> vv = new VisualizationViewer<String,String>(layout);
    
        vv.setPreferredSize(new Dimension(850,850));
        
        // Show vertex and edge labels
      
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<String>());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        
         // Create a graph mouse and add it to the visualization component
        DefaultModalGraphMouse<?, ?> gm = new DefaultModalGraphMouse<Object, Object>();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        vv.setGraphMouse(gm); 
        JFrame frame = new JFrame("Interactive Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);       
    }
    
}
