package mygephi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.lang.Object; 
import javax.swing.JFrame;

import static mygephi.Hierarchy.paperFit;
import static mygephi.*;

import org.gephi.appearance.api.AppearanceController;
import org.gephi.appearance.api.AppearanceModel;
import org.gephi.appearance.api.Function;
import org.gephi.appearance.api.Partition;
import org.gephi.appearance.api.PartitionFunction;
import org.gephi.appearance.plugin.PartitionElementColorTransformer;
import org.gephi.appearance.plugin.RankingElementColorTransformer;
import org.gephi.appearance.plugin.RankingNodeSizeTransformer;
import org.gephi.appearance.plugin.palette.Palette;
import org.gephi.appearance.plugin.palette.PaletteManager;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
import org.gephi.layout.plugin.noverlap.NoverlapLayout;
import org.gephi.layout.plugin.noverlap.NoverlapLayoutBuilder;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDirectionDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2Builder;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.GraphDistance;
import org.gephi.toolkit.demos.plugins.preview.PreviewSketch;
import org.openide.util.Lookup;



public class given {
	private static Statement stmt = null;   //Statement鏄竴涓帴鍙ｏ紝鎻愪緵浜嗗悜鏁版嵁搴撳彂閫佹墽琛岃鍙ュ拰鑾峰彇缁撴灉鐨勬柟娉�
	private static Statement stmt2 = null;  //ResultSet鏄帴鍙ｆ槸鐢ㄦ潵鍒涘缓瀛樻斁浠庢暟鎹簱閲屽緱鍒扮殑缁撴灉鐨勫璞�
	private static Connection conn = null;  //Connection鏄敤浜庡皢JAVA鍜屾暟鎹簱杩炴帴鐨勭被
        private static Color[] colors=new Color[]{new Color(0xFF0000), new Color(0xFCA2CD), new Color(0x0426E2)};//~鐢ㄦ潵璁剧疆color鐨凴GB鍊�
        private static float[] positions={0f, 0.50f, 1f};
    
        public static Color getValue(float pos) {
            for (int a = 0; a < positions.length - 1; a++) {
                if (positions[a] == pos) {
                    return colors[a];
                }
                if (positions[a] < pos && pos < positions[a + 1]) {
                    float v = (pos - positions[a]) / (positions[a + 1] - positions[a]);
                    return tween(colors[a], colors[a + 1], v);
                }
            }
            if (pos <= positions[0]) {
                return colors[0];
            }
            if (pos >= positions[positions.length - 1]) {
                return colors[colors.length - 1];
            }
            return null;
        }

        private static Color tween(Color c1, Color c2, float p) {
            return new Color(
                    (int) (c1.getRed() * (1 - p) + c2.getRed() * (p)),
                    (int) (c1.getGreen() * (1 - p) + c2.getGreen() * (p)),
                    (int) (c1.getBlue() * (1 - p) + c2.getBlue() * (p)),
                    (int) (c1.getAlpha() * (1 - p) + c2.getAlpha() * (p)));
        } 
        
	public static String capitalize(String str) {
		StringBuffer sbn=new StringBuffer(str);
		StringBuffer ss=new StringBuffer("");
		String s=sbn.toString();
		String[] sb=s.split(" ");
		for(int i=0;i<sb.length;i++){
			sb[i]=sb[i].substring(0,1).toUpperCase()+sb[i].substring(1); 
		}
		for(int i=0;i<sb.length;i++){
		   ss.append(sb[i]);
		   ss.append(" ");
		}
		return ss.toString();
	}
		
	public static Color[] generateColors(Collection values) {
		int maxi = 1000;
		int mini = 3000;
		int sz = values.size();
		Object[] arr = values.toArray();
		int result[] = new int[sz];
		Color[] colorsNew = new Color[sz];
		for (int i = 0; i < sz; ++i) {
			int value = (int)((Double)arr[i]).doubleValue();
			value = Math.min(2016, value);
			value = Math.max(1986, value);      //闄愬埗骞翠唤鍦�1986-2016
			maxi = Math.max(value, maxi);       //淇濆瓨鏈�澶у勾浠�
			mini = Math.min(value, mini);       //淇濆瓨鏈�灏忓勾浠�
			result[i] = value;
		}
		int mid = (maxi + mini) / 2;
		for (int i = 0; i < sz; ++i) {
                    colorsNew[i]=getValue((float)(1.0*(result[i]-mini)/(maxi-mini)));//~棰滆壊鏄敤骞翠唤鏉ュ尯鍒嗙殑
		}
		return colorsNew;
	}
	
	public static void generateGml(String fieldOfStudyID) {
		try{
            String sql = "SELECT * FROM PaperRefStats WHERE FieldsOfStudyID='" + fieldOfStudyID + "'";    //瑕佹墽琛岀殑SQL
            ResultSet rs = stmt.executeQuery(sql);
            int numberOfPapers = 900; //number of papers to show before filtered
            double delta = 0.1;
            int PaperNum = 0;
            String fieldOfStudyName = fieldOfStudyID;
            int qualified = 0;
            while (rs.next()){
            	PaperNum = rs.getInt("TotalAmountOfPapers");
            	//fieldOfStudyName = rs.getString("FieldsOfStudyName");
                qualified = rs.getInt("PapersQualified");
            	//numberOfPapers = Math.min((int) Math.min(PaperNum, 5 * Math.sqrt(PaperNum)), numberOfPapers);
                //numberOfPapers = Math.min(PaperNum, numberOfPapers);
            }
            rs.close();
            
            if (qualified > numberOfPapers*delta && PaperNum>numberOfPapers) {
                qualified = (int) (numberOfPapers*delta);
            }
            numberOfPapers = Math.min(PaperNum, numberOfPapers);
            
            System.out.println("(" + numberOfPapers + "/" + qualified + ")" + " papers out of " + PaperNum + " in the field of " + fieldOfStudyName + "(" + fieldOfStudyID + ") will be displayed.");
            
            class Node{
            String PaperID=""; 
            String Author=""; 
            String Year=""; 
            String Count="";
            StringBuffer Child=new StringBuffer(fieldOfStudyID); 
            }
            Node[] nodemap=new Node[numberOfPapers+qualified+1000];
            System.out.println("NOW IS:    " + (numberOfPapers+qualified+1000));
            for(int i=0;i<numberOfPapers+qualified+1000;i++)
            {nodemap[i]=new Node();}
            sql="SELECT PaperPublishYear, Papers.PaperID,CNT\n" +
"                       FROM Papers \n" +
"                       INNER JOIN \n" +
"((SELECT TB.PaperID, PaperReferenceCount.ReferenceCount AS CNT\n" +
"FROM\n" +
"    (SELECT DISTINCT\n" +
"        PaperKeywords.PaperID\n" +
"    FROM\n" +
"        PaperKeywords\n" +
"    WHERE\n" +
"        FieldOfStudyIDMappedToKeyword = '" + fieldOfStudyID + "') AS TB\n" +
"        INNER JOIN\n" +
"    PaperReferenceCount ON TB.PaperID =  PaperReferenceCount.PaperID\n" +
"ORDER BY PaperReferenceCount.ReferenceCount DESC LIMIT " + numberOfPapers + ") UNION (SELECT DISTINCT\n" +
"    PaperRefKeywords.PaperID, FieldCitation\n" +
"FROM\n" +
"    PaperRefKeywords\n" +
"WHERE\n" +
"    FieldOfStudyIDMappedToKeyword = '" + fieldOfStudyID + "'\n" +
"ORDER BY FieldCitation DESC\n" +
"LIMIT "+qualified+"))AS TB4\n" +
"ON  TB4.PaperID=Papers.PaperID ORDER BY CNT DESC";//鎵惧埌璇ラ鍩熸墍閫塸aper鐨勫彂琛ㄥ勾浠姐�両D鍜屽紩鐢ㄦ暟閲�
 /*           sql =     "	 SELECT PaperPublishYear, AuthorName, Papers.PaperID,CNT\n" +
"                       FROM Papers \n" +
"                       INNER JOIN \n" +
"                       (SELECT Authors.AuthorName, PaperID, CNT\n" +
"                        FROM Authors INNER JOIN \n" +
" (SELECT TB1.PaperID, AuthorID,CNT\n" +
"                               FROM (PaperAuthorAffiliations \n" +
"                               INNER JOIN \n" +
" ((SELECT PaperID, PaperReferenceCount AS CNT\n" +
"FROM\n" +
"    (SELECT DISTINCT\n" +
"        PaperID\n" +
"    FROM\n" +
"        PaperKeywords\n" +
"    WHERE\n" +
"        FieldOfStudyIDMappedToKeyword = '" + fieldOfStudyID + "') AS TB\n" +
"        INNER JOIN\n" +
"    PaperReferenceCount ON TB.PaperID = PaperReferenceID\n" +
"ORDER BY PaperReferenceCount DESC LIMIT " + numberOfPapers + ") UNION (SELECT DISTINCT\n" +
"    PaperID, FieldCitation\n" +
"FROM\n" +
"    PaperRefKeywords\n" +
"WHERE\n" +
"    FieldOfStudyIDMappedToKeyword = '" + fieldOfStudyID + "'\n" +
"ORDER BY FieldCitation DESC\n" +
"LIMIT "+qualified+"))AS TB1 "
+ "ON TB1.PaperID = PaperAuthorAffiliations.PaperID ) "
                    + "WHERE AuthorSequenceNumber = 1 ORDER BY CNT DESC)AS TB3\n" +
"ON TB3.AuthorID = Authors.AuthorID ORDER BY CNT DESC) AS TB4\n" +
"                       ON  TB4.PaperID=Papers.PaperID ORDER BY CNT DESC" ;   //sql1:鎵惧埌瀛愰鍩熺殑id鍜宲aper鎬绘暟
            */
            rs = stmt.executeQuery(sql);
            System.out.println("Retrieved paper successfully.");
            
          HashSet<String> hashSet = new HashSet<String>();
            int count=0;System.out.println("Output count:   "  +count);
            while (rs.next()) {
                    if (!hashSet.contains(rs.getString("PaperID")))
                    {hashSet.add(rs.getString("PaperID"));}
                    nodemap[count].PaperID = rs.getString("PaperID");
                    nodemap[count].Count = rs.getString("CNT");
                    //nodemap[count].Author= rs.getString("AuthorName");
                    nodemap[count].Year= rs.getString("PaperPublishYear");
                    count++;
                   //System.out.println("NOW IS:    " + count);
                
            }
            rs.close(); 
             System.out.println(count + "are in this field");
             
              sql =  "SELECT DISTINCT\n" +
"    PaperRefKeywords.FieldOfStudyIDMappedToKeyword AS RefFieldOfStudyID,TB1.PaperID,CNT\n" +
"FROM\n" +
"    ((SELECT \n" +
"        TB.PaperID, PaperReferenceCount.ReferenceCount AS CNT\n" +
"    FROM\n" +
"        (SELECT DISTINCT\n" +
"        PaperKeywords.PaperID\n" +
"    FROM\n" +
"        PaperKeywords\n" +
"    WHERE\n" +
"        FieldOfStudyIDMappedToKeyword = '" + fieldOfStudyID + "') AS TB\n" +
"    INNER JOIN PaperReferenceCount ON TB.PaperID = PaperReferenceCount.PaperID\n" +
"    ORDER BY PaperReferenceCount.ReferenceCount DESC\n" +
"    LIMIT " + numberOfPapers + ") UNION (SELECT DISTINCT\n" +
"         PaperRefKeywords.PaperID, FieldCitation\n" +
"    FROM\n" +
"        PaperRefKeywords\n" +
"    WHERE\n" +
"        FieldOfStudyIDMappedToKeyword = '" + fieldOfStudyID + "'\n" +
"    ORDER BY FieldCitation DESC\n" +
"    LIMIT "+qualified+")) AS TB1\n" +
"    INNER JOIN PaperRefKeywords\n" +
"	ON TB1.PaperID=PaperRefKeywords.PaperID";   //sql2:鎵惧埌鎵�鏈夊瓙棰嗗煙鐨刾aper淇℃伅
                rs = stmt.executeQuery(sql);
                 System.out.println("Retrieved child successfully.");
                int point=0;
                while(rs.next()){
                    if(point>1 && point<100){point-=2;}
                    if(point>99 && point<500){point-=10;}
                    if(point>500){point-=15;}
                   
                    String tmpPaperID=rs.getString("PaperID");
                    String tmpchild=rs.getString("RefFieldOfStudyID");
                     if (hashSet.contains(tmpPaperID)){
                while(!tmpPaperID.equals(nodemap[point].PaperID) && point<numberOfPapers ){ ++point;}
                if(nodemap[point].PaperID.equals(tmpPaperID)){System.out.println("point now is:"+point);
                  //System.out.println("paperID:" + nodemap[point].PaperID+ "  this sql: "+tmpPaperID );
                   nodemap[point].Child.append("|"); nodemap[point].Child.append(tmpchild);
                  // System.out.println( nodemap[point].Child);
                }
                }
                
                }rs.close();
             System.out.println( point+"       done all    ");
          OutputStream os = null;
    		try {
    			os = new FileOutputStream(System.getProperty("user.dir") + "/gml-svg/" + fieldOfStudyID + ".gml");//鍒涘缓涓�涓枃浠惰矾寰�
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		}
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write("graph [\n");
            writer.write("directed 1\n");
            
           
           
            
          
          
          int j=0;
            while (j<count) {//鑻ヤ粛鏈夊厓绱犲彲浠ヨ凯浠ｅ氨杩斿洖true
                
                   
                      // System.out.println(nodemap[j].PaperID+": "+ (j+1));
                        writer.write("node [\n");  
                        writer.write("id \"" + nodemap[j].PaperID + "\"\n");
                        writer.write("label \"" + nodemap[j].Author + "\"\n");
                        writer.write("reference \"" + nodemap[j].Count + "\"\n");
                        writer.write("year " + nodemap[j].Year + "\n");
                        writer.write("field \"" + nodemap[j].Child + "\"\n]\n");
                 j++;
                //System.out.println(nodemap[j].PaperID+":      " + nodemap[j].Child);
                  
                   //System.out.println(cnt1 + "  child  it have");
                    }
          
               
                
              // }
            
         /*  Iterator<String> iterator= hashSet.iterator();
            
            while (iterator.hasNext())
            {
            	String paperID = iterator.next();
            	
            	sql = "SELECT PaperReferenceID FROM PaperReferences WHERE PaperID = '" + paperID + "'";     //鎵惧埌姣忕瘒paper鐨勫紩鐢�
            	rs = stmt.executeQuery(sql); 
            	while (rs.next()) {
            		String tmp = rs.getString("PaperReferenceID");
            		if (hashSet.contains(tmp) && !tmp.equals(paperID)) {
            			writer.write("edge [\n");
            			writer.write("source \"" + paperID + "\"\n");
            			writer.write("target \"" + tmp + "\"\n]\n");
            		}
            	}
            	rs.close();
            }*/
       
         /* sql = "SELECT PaperReferenceID, TB5.PaperID FROM PaperReferences INNER JOIN (\n" +
"                        SELECT PaperPublishYear, AuthorName, Papers.PaperID, CNT FROM Papers INNER JOIN (SELECT Authors.AuthorName, PaperID, CNT FROM Authors INNER JOIN \n" +
"            		(SELECT PaperID, AuthorID, CNT FROM (PaperAuthorAffiliations INNER JOIN\n" +
"            		(SELECT PaperReferenceID, PaperReferenceCount.PaperReferenceCount AS CNT FROM ((SELECT DISTINCT PaperID FROM PaperKeywords WHERE FieldOfStudyIDMappedToKeyword = '" + fieldOfStudyID + "') AS TB \n" +
"            		 INNER JOIN PaperReferenceCount ON TB.PaperID = PaperReferenceID) ORDER BY PaperReferenceCount.PaperReferenceCount DESC LIMIT " + numberOfPapers + ") AS TB2 \n" +
"            		 ON PaperReferenceID = PaperAuthorAffiliations.PaperID ) WHERE AuthorSequenceNumber = 1) AS TB3 ON TB3.AuthorID = Authors.AuthorID) AS TB4 \n" +
"            		 ON Papers.PaperID = TB4.PaperID) AS TB5 \n" +
"                    ON PaperReferences.PaperID = TB5.PaperID";*/
          rs.close();
            
            String post = "";
            for(Iterator it=hashSet.iterator();it.hasNext();) {
                post += it.next();
                post += ",";
            }
            String[] results = HttpRequest.sendPost("http://202.120.36.137:10080",post).split(";");
            System.out.println(results);
            for (String result : results) {
                String tmp1 = result.split(",")[0];
                String tmp2 = result.split(",")[1];
                if (!tmp1.equals(tmp2)) {
                    writer.write("edge [\n");
                    writer.write("source \"" + tmp1 + "\"\n");
                    writer.write("target \"" + tmp2 + "\"\n]\n");
                }
            }
           

            
        
           
           
            writer.write("]");
            writer.close();
            os.close();
            
            System.out.println("Content written to " + fieldOfStudyID + ".gml");
       } catch(Exception e) {
    	   e.printStackTrace();
       }
	}
	
	public static void generateSvg(String fieldOfStudyID,Hierarchy.HierarchyTree exam) throws IOException, SQLException {
		File file = new File(System.getProperty("user.dir") + "/papermaps/" + fieldOfStudyID + ".php");
		if (file.exists()) {
			System.out.println(fieldOfStudyID + " already exists");
			return;
		}
		generateGml(fieldOfStudyID);
		
		//Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        //Get models and controllers for this new workspace - will be useful later
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();  //GraphModel濂芥瘮瀛樻斁鏁翠釜鍥惧舰鍏冪礌鐨勫鍣紝鍖呮嫭鑺傜偣銆佽竟銆佹爣绛剧瓑淇℃伅
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
        //RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);
        
        //Import file       
        Container container;
        File gmlFile = new File(System.getProperty("user.dir") + "/gml-svg/" + fieldOfStudyID + ".gml");
        try {
            container = importController.importFile(gmlFile);
            container.getLoader().setEdgeDefault(EdgeDirectionDefault.DIRECTED);   //Force DIRECTED
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        //Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);

        //See if graph is well imported
        DirectedGraph graph = graphModel.getDirectedGraph();
        System.out.println("Nodes: " + graph.getNodeCount());
        System.out.println("Edges: " + graph.getEdgeCount());

        //Filter      
        DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
        degreeFilter.init(graph);
        degreeFilter.setRange(new Range(2, Integer.MAX_VALUE));     //Remove nodes with degree = 0
        Query query = filterController.createQuery(degreeFilter);
        GraphView view = filterController.filter(query);
        graphModel.setVisibleView(view);    //Set the filter result as the visible view
        
        //filterController.exportToNewWorkspace(query);
        Graph result = graphModel.getGraph(view);
        Workspace newWorkspace = pc.newWorkspace(pc.getCurrentProject());
        //GraphModel newGraphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(newWorkspace);
        graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(newWorkspace);
        graphModel.bridge().copyNodes(result.getNodes().toArray());
        pc.openWorkspace(newWorkspace);
        
        //System.out.println(pc.getCurrentWorkspace());
        //FilterControllerImpl.java
        PreviewModel model = Lookup.getDefault().lookup(PreviewController.class).getModel();        //PreviewModel绛変簬鑾峰緱浜嗕竴涓浣曡繘琛岄瑙堝睍鐜扮殑鎿嶄綔鍏ュ彛        
        AppearanceController appearanceController = Lookup.getDefault().lookup(AppearanceController.class);
        AppearanceModel appearanceModel = appearanceController.getModel();
        
        //See visible graph stats
        UndirectedGraph graphVisible = graphModel.getUndirectedGraphVisible();
        System.out.println("Nodes: " + graphVisible.getNodeCount());
        System.out.println("Edges: " + graphVisible.getEdgeCount());
        
        if (graphVisible.getNodeCount() == 0) {
        	System.out.println("No node!");
        	gmlFile.delete();
        	return;
        }
        //System.out.println(pc.getCurrentWorkspace());
        
         //Add indgree column
        Column indgreeCol = graphModel.getNodeTable().addColumn("InDgree", Integer.class);
        for (Node n : graphModel.getDirectedGraph().getNodes()) {
            n.setAttribute(indgreeCol, graphModel.getDirectedGraph().getInDegree(n));
        }
        //Add node size column
        Column yearCol = graphModel.getNodeTable().getColumn("year");
        Column nodeSizeCol = graphModel.getNodeTable().addColumn("Size", Double.class);
        Double year,nodeIndgree,nodeSize;
        Double maxsize=0.0,minsize=200.0,maxsize2=0.0;
        
        for (Node n : graphModel.getDirectedGraph().getNodes()) { 
            year = Double.parseDouble(n.getAttribute(yearCol).toString());
            nodeIndgree = Double.parseDouble(n.getAttribute(indgreeCol).toString());
            nodeSize = 0.5 * nodeIndgree + 0.5 * nodeIndgree * Math.exp((year-2015)/10);
            // nodeSize = 5*Math.log(nodeSize);
           nodeSize=nodeSize;
           // nodeSize=Math.exp(nodeSize);
            if(nodeSize>maxsize)maxsize=nodeSize;
            if(nodeSize>maxsize2 && nodeSize<maxsize)maxsize2=nodeSize;
            if(nodeSize<minsize)minsize=nodeSize;
            n.setAttribute(nodeSizeCol, nodeSize);
        }
         /*for (Node n : graphModel.getDirectedGraph().getNodes()) { 
             nodeSize=Double.parseDouble(n.getAttribute(nodeSizeCol).toString());
             if(nodeSize>maxsize*0.6){nodeSize=maxsize*0.9+0.1*nodeSize/maxsize;nodeSize*=nodeSize;n.setAttribute(nodeSizeCol, nodeSize);}
             
               }*/
        
        System.out.println("max:"+maxsize+"    max2:"+maxsize2+"    min:"+minsize);
        
        
     /*   
        //use preference to set node size 鍚庡姞鍏ョ殑
        Column referenceCol = graphModel.getNodeTable().getColumn("reference");
        //Add node size column
        Column yearCol = graphModel.getNodeTable().getColumn("year");
        Column nodeSizeCol = graphModel.getNodeTable().addColumn("Size", Double.class);
        Double year,nodereference,nodeSize;
        for (Node n : graphModel.getDirectedGraph().getNodes()) { 
            year = Double.parseDouble(n.getAttribute(yearCol).toString());
            nodereference = Double.parseDouble(n.getAttribute(referenceCol).toString());
            nodeSize = Math.sqrt(0.5 * nodereference + 0.5 * nodereference * Math.exp((year-2015)/15));
            n.setAttribute(nodeSizeCol, nodeSize);
        }*/
     
     
      //Set edge weight
       //Double sourceYear,targetYear;
       String sourceField, targetField;
        Column fieldCol = graphModel.getNodeTable().getColumn("field");
     
       int refcnt=0;
        for (Edge e : graphModel.getDirectedGraph().getEdges()) {
            sourceField = e.getSource().getAttribute(fieldCol).toString();
            targetField = e.getTarget().getAttribute(fieldCol).toString();
            
             Double tmpweight=0.0;
        tmpweight=paperFit(sourceField,targetField,fieldOfStudyID,exam);
            
          System.out.println(" set edge weight     " + tmpweight); 
          // e.setWeight(tmpweight);
            e.setWeight(5*tmpweight);
             // System.out.println(tmpweight+"   of this field");
        }
      System.out.println("finish set edge weight");  //鏂版柟娉�,鐢ㄥ皝瑁呭ソ鐨勬爲璁剧疆杈规潈鍊�
     
     
        //Set edge weight
       //Double sourceYear,targetYear;
     /*  String sourceField, targetField;
        Column fieldCol = graphModel.getNodeTable().getColumn("field");
     
       int refcnt=0;
        for (Edge e : graphModel.getDirectedGraph().getEdges()) {
            sourceField = e.getSource().getAttribute(fieldCol).toString();
            targetField = e.getTarget().getAttribute(fieldCol).toString();
            String []sf=sourceField.split("\\|");
            String []tf=targetField.split("\\|");
             Double tmpweight=0.0;
             for (int i = 0 ; i <sf.length ; i++ ) {
        for(int j=0;j<tf.length;j++){
      if(sf[i].equals(tf[j])){tmpweight++;
      
    }}}
            
           refcnt+=tmpweight;
          // e.setWeight(tmpweight);
            e.setWeight(5*Math.sqrt(tmpweight));
             // System.out.println(tmpweight+"   of this field");
        }
      System.out.println(refcnt + "  lines are in the same field");*/  //鏃ф柟娉�,姣旇緝涓ょ瘒paper鐨勫紩鐢ㄩ鍩熼噸澶嶉噺,浠ユ璁剧疆鏉冭亴
      
      
     
      
      
      
      
      /*  int[] weight = new int[]{50,10,50}; //涓嶅悓棰嗗煙寮曠敤锛屽悓棰嗗煙鏂板锛屽悓棰嗗煙寮曠敤
        double lineMaxWeight = 20; //绾跨殑鏈�澶х矖缁� 
        double lineWeight = 0; 
        Column fieldCol = graphModel.getNodeTable().getColumn("field");
        String sourceField, targetField;
        for (Edge e : graphModel.getDirectedGraph().getEdges()) {
            sourceField = e.getSource().getAttribute(fieldCol).toString();
            targetField = e.getTarget().getAttribute(fieldCol).toString();
            double tmpWeight = (e.getSource().size() + e.getTarget().size()) / 2 - 19;
            if (tmpWeight>lineWeight) lineWeight = tmpWeight;
                       if (sourceField.equals(targetField)) 
            {
                    //double maxWeight = Math.max(Double.parseDouble(e.getTarget().getAttribute(indgreeCol).toString()), Double.parseDouble(e.getSource().getAttribute(indgreeCol).toString()));
                    double avgWeight = (e.getSource().size() + e.getTarget().size()) / 2 - 19;
                    e.setWeight(weight[2] * (avgWeight/lineWeight*lineMaxWeight)); 
                           
            }
            else
            {
                e.setWeight(weight[0]);
            }
                   
                    
                }*/
         
        
      
      
        
        //Get Centrality
        GraphDistance distance = new GraphDistance();
        distance.setDirected(true);
        distance.execute(graphModel);
        HashMap<String, Color> yearColor = new HashMap();
        yearColor.put("1986.0", new Color(0x0426E2));
        yearColor.put("1987.0", new Color(0x142EE0));
        yearColor.put("1988.0", new Color(0x2536DF));
        yearColor.put("1989.0", new Color(0x353EDD));
        yearColor.put("1990.0", new Color(0x4647DC));
        yearColor.put("1991.0", new Color(0x564FDB));
        yearColor.put("1992.0", new Color(0x6757D9));
        yearColor.put("1993.0", new Color(0x775FD8));
        yearColor.put("1994.0", new Color(0x8868D6));
        yearColor.put("1995.0", new Color(0x9870D5));
        yearColor.put("1996.0", new Color(0xA978D4));
        yearColor.put("1997.0", new Color(0xB980D2));
        yearColor.put("1998.0", new Color(0xCA89D1));
        yearColor.put("1999.0", new Color(0xDA91CF));
        yearColor.put("2000.0", new Color(0xEB99CE));
        yearColor.put("2001.0", new Color(0xFCA2CD));
        yearColor.put("2002.0", new Color(0xFC97BF));
        yearColor.put("2003.0", new Color(0xFC8CB1));
        yearColor.put("2004.0", new Color(0xFC81A3));
        yearColor.put("2005.0", new Color(0xFC7696));
        yearColor.put("2006.0", new Color(0xFD6B88));
        yearColor.put("2007.0", new Color(0xFD617B));
        yearColor.put("2008.0", new Color(0xFD566D));
        yearColor.put("2009.0", new Color(0xFD4B5F));
        yearColor.put("2010.0", new Color(0xFD4051));
        yearColor.put("2011.0", new Color(0xFE3644));
        yearColor.put("2012.0", new Color(0xFE2B36));
        yearColor.put("2013.0", new Color(0xFE2029));
        yearColor.put("2014.0", new Color(0xFE151B));
        yearColor.put("2015.0", new Color(0xFE0A0D));
        yearColor.put("2016.0", new Color(0xFF0000));
        
         Iterator<Node> nodeIter = graphModel.getGraphVisible().getNodes().iterator();
        while (nodeIter.hasNext()) {
            Node node = nodeIter.next();
            if ((int)(Double.parseDouble(node.getAttribute("year").toString()))<1986) node.setColor(new Color(0x0426E2));
            else node.setColor(yearColor.get(node.getAttribute("year").toString()));
        }
        //Rank node color by year
        /*Column column = graphModel.getNodeTable().getColumn("year");
        Function func = appearanceModel.getNodeFunction(graph, column, RankingElementColorTransformer.class);
        
        if (func == null) {
        	func = appearanceModel.getNodeFunction(graph, column, PartitionElementColorTransformer.class);
        	System.out.println("Use PartitionElementColorTransformer");
        	Partition partition = ((PartitionFunction) func).getPartition();
            partition.setColors(generateColors(partition.getValues()));
            appearanceController.transform(func);
        } else {
        	System.out.println("Use RankingElementColorTransformer");
            RankingElementColorTransformer degreeTransformer = (RankingElementColorTransformer) func.getTransformer();
            degreeTransformer.setColors(new Color[]{new Color(0xFF0000), new Color(0xFCA2CD), new Color(0x0426E2)});
            degreeTransformer.setColorPositions(new float[]{0f, 0.5f, 1f});
            appearanceController.transform(func);
        }*/

        //Rank node size by nodeSizeCol
        nodeSizeCol = graphModel.getNodeTable().getColumn("Size");
        Function sizeRanking = appearanceModel.getNodeFunction(graph, nodeSizeCol, RankingNodeSizeTransformer.class);
        if (sizeRanking == null){
            System.out.println("----NodeSizeRanking failed----");
            //Rank size by inDegree
            Function degreeRanking2 = appearanceModel.getNodeFunction(graph, AppearanceModel.GraphFunction.NODE_INDEGREE, RankingNodeSizeTransformer.class);
            RankingNodeSizeTransformer degreeTransformer2 = (RankingNodeSizeTransformer) degreeRanking2.getTransformer();
            degreeTransformer2.setMaxSize(400);
            degreeTransformer2.setMinSize(60);
            appearanceController.transform(degreeRanking2);
        }
        else{
            RankingNodeSizeTransformer sizeTransformer = (RankingNodeSizeTransformer) sizeRanking.getTransformer();
            sizeTransformer.setMaxSize(400);
            sizeTransformer.setMinSize(60);
            appearanceController.transform(sizeRanking);
        }
        double nodesize=0.0;
        double maxnd=0.0;
         Iterator<Node> nodeIter1 = graphModel.getGraphVisible().getNodes().iterator();
        while (nodeIter1.hasNext()) {
            Node node = nodeIter1.next();
            nodesize=node.size();
            if(nodesize>maxnd){maxnd=nodesize;}
            
        }
         Iterator<Node> nodeIter2 = graphModel.getGraphVisible().getNodes().iterator();
        while (nodeIter2.hasNext()) {
            Node node = nodeIter2.next();
            nodesize=node.size();
            if(nodesize>0.7*maxnd){nodesize=1.5*nodesize;}
            node.setSize((float)nodesize);
            
        }
        
        //ForceAtlas
     ForceAtlasLayout faLayout = new ForceAtlasLayout(null);
        faLayout.setGraphModel(graphModel);
        faLayout.initAlgo();
        faLayout.resetPropertiesValues();
        //System.out.println(faLayout.getSpeed());
        // faLayout.setRepulsionStrength(Math.min(30000, Math.max(100.0, (double)graphVisible.getNodeCount()*10)));
        //  faLayout.setMaxDisplacement(40.0);
        faLayout.setRepulsionStrength(Math.min(40000, Math.max(100.0, (double)graphVisible.getNodeCount()*10)));
        faLayout.setMaxDisplacement(45.0);
          faLayout.setAttractionStrength(4.9);
        faLayout.setOutboundAttractionDistribution(Boolean.TRUE);
      
      // faLayout.setAdjustSizes(Boolean.FALSE);//鏂板姞鍏ヨ皟鏁村嚱鏁�
        //faLayout.setGravity(2.4);//鏂板姞鍏ュ紩鍔涘嚱鏁�
        
        faLayout.setSpeed(Math.min(20.0, Math.max(1.0, graphVisible.getNodeCount())));
        for (int i = 0; i <1500 && faLayout.canAlgo(); i++) {
            faLayout.goAlgo(); //System.out.print(i);
             System.out.println( "running  "+i);
        }
      
        //ForceAtlas2
     /*   ForceAtlas2 fa2Layout = new ForceAtlas2(new ForceAtlas2Builder());
       fa2Layout.setGraphModel(graphModel);
       fa2Layout.resetPropertiesValues();
       fa2Layout.setEdgeWeightInfluence(1.0);
       fa2Layout.setGravity(1.0);
       fa2Layout.setScalingRatio(2.0);
        fa2Layout.setBarnesHutTheta(1.2);
       fa2Layout.setJitterTolerance(0.1);
       fa2Layout.initAlgo();
        fa2Layout.setAdjustSizes(true);
       for (int i = 0; i < 1000 && fa2Layout.canAlgo(); i++) 
      	fa2Layout.goAlgo();
       fa2Layout.endAlgo();*/
        
        /*faLayout.setAdjustSizes(Boolean.TRUE);
        for (int i = 0; i < 100 && faLayout.canAlgo(); i++) {
            faLayout.goAlgo();
        }
        faLayout.endAlgo(); */
        
        NoverlapLayout layout2 = new NoverlapLayout(new NoverlapLayoutBuilder());
        layout2.setGraphModel(graphModel);
        layout2.resetPropertiesValues();
       
       /* layout2.setMargin(45.0);
        layout2.setRatio(1.2);
        layout2.setSpeed(3.0);*/
        layout2.setMargin(60.0);
        layout2.setRatio(2.2);
        layout2.setSpeed(4.0);
        layout2.initAlgo();
        System.out.println( "total  "+( Math.max(graphVisible.getNodeCount() / 4, 50)));
        for (int i = 0; i < Math.min(graphVisible.getNodeCount() / 2, 120) && layout2.canAlgo(); i++) 
         {layout2.goAlgo();System.out.println( "layout2:  "+i);}
        layout2.endAlgo();
        
        /*//Filter
        InDegreeRangeFilter inDegreeFilter = new InDegreeRangeFilter();
        inDegreeFilter.init(graph);
        inDegreeFilter.setRange(new Range(1, Integer.MAX_VALUE));     //Remove nodes with inDegree = 0
        Query query = filterController.createQuery(inDegreeFilter);
        GraphView view = filterController.filter(query);
        graphModel.setVisibleView(view);*/
        
        //Preview
      //  PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        model.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        //model.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(new Color(186, 172, 212)));
        model.getProperties().putValue(PreviewProperty.NODE_LABEL_PROPORTIONAL_SIZE, Boolean.TRUE);
        model.getProperties().putValue(PreviewProperty.SHOW_EDGES, Boolean.TRUE);
        //model.getProperties().putValue(PreviewProperty.EDGE_RESCALE_WEIGHT, Boolean.TRUE);        
        //model.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, new Float(2.0f));
        model.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, new java.awt.Font("Dialog", 0, 2));
        model.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 50);
        //previewController.refreshPreview();

        //Export
       ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        File svgFile = new File(System.getProperty("user.dir") + "/gml-svg/" + fieldOfStudyID + ".svg");
        File pngFile = new File(System.getProperty("user.dir") + "/gml-svg/" + fieldOfStudyID + ".png");
        File gephiFile = new File(System.getProperty("user.dir") + "/gml-svg/" + fieldOfStudyID + ".gephi");

        pc.saveProject(pc.getCurrentProject(), gephiFile).run();
        ec.exportFile(svgFile);
        ec.exportFile(pngFile);
		InputStream is = null;
		OutputStream os = null;

		is = new FileInputStream(System.getProperty("user.dir") + "/gml-svg/" + fieldOfStudyID + ".svg");
		os = new FileOutputStream(System.getProperty("user.dir") + "/papermaps/" + fieldOfStudyID + ".php");

        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        for (int i = 0; i < 9; ++i) { //skip useless codes
        	reader.readLine();
        }
        line = reader.readLine();
        while (line != null) {
            writer.write(line + '\n');
            line = reader.readLine();
        }
        reader.close();
        writer.close();
        is.close();
        os.close();
        //svgFile.delete();
        //gmlFile.delete();
	}
       
       
        //New Processing target, get the PApplet
    /*    G2DTarget target = (G2DTarget) previewController.getRenderTarget(RenderTarget.G2D_TARGET);
        PreviewSketch previewSketch = new PreviewSketch(target);
        previewController.refreshPreview();
        previewSketch.resetZoom();
        
        //Export
//        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
//        try {
//            ec.exportFile(new File("wirelessnetwork.png"));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return;
//        }
//        
//		InputStream is = null;
//		OutputStream os = null;
//		try {
//			is = new FileInputStream("wirelessnetwork.svg");
//			os = new FileOutputStream("out.php");
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//        String line = null; // 鐢ㄦ潵淇濆瓨姣忚璇诲彇鐨勫唴瀹�
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
//        for (int i = 0; i < 9; ++i) { //skip useless codes
//        	reader.readLine();
//        }
//        line = reader.readLine();
//        while (line != null) { // 濡傛灉 line 涓虹┖璇存槑璇诲畬浜�
//            writer.write(line + '\n');
//            line = reader.readLine(); // 璇诲彇涓嬩竴琛�
//        }
//        reader.close();
//        writer.close();
//        is.close();
        
        //Add the applet to a JFrame and display
        JFrame frame = new JFrame("Test Preview");
        frame.setLayout(new BorderLayout());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(previewSketch, BorderLayout.CENTER);

        frame.setSize(1024, 768);
        frame.setVisible(true);
       
        }       */
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {        
		OutputStream os = null;

		os = new FileOutputStream("logs.txt");

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
		Class.forName("com.mysql.jdbc.Driver");
                // or:
                // com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
                // or锛�
                // new com.mysql.jdbc.Driver();
		
        System.out.println("鎴愬姛鍔犺浇MySQL椹卞姩锛�");
            
        String url="jdbc:mysql://202.120.36.137:6033/mag-new-160205";    //JDBC鐨刄RL URL缂栧啓鏂瑰紡锛歫dbc:mysql://涓绘満鍚嶇О锛氳繛鎺ョ鍙�/鏁版嵁搴撶殑鍚嶇О?鍙傛暟=鍊�   
		try {
			conn = DriverManager.getConnection(url, "map","map");     // 涓�涓狢onnection浠ｈ〃涓�涓暟鎹簱杩炴帴
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        try {
			stmt = conn.createStatement();
			//stmt2 = conn.createStatement();     // Statement閲岄潰甯︽湁寰堝鏂规硶锛屾瘮濡俥xecuteUpdate鍙互瀹炵幇鎻掑叆锛屾洿鏂板拰鍒犻櫎绛�
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        System.out.println("鎴愬姛杩炴帴鍒版暟鎹簱锛�");
          Hierarchy.HierarchyTree exam=new Hierarchy.HierarchyTree();
        exam.createTree();
        stmt.close();
        conn.close();
       // stmt2.close();
        Class.forName("com.mysql.jdbc.Driver");
                // or:
                // com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
                // or锛�
                // new com.mysql.jdbc.Driver();
		
        System.out.println("鎴愬姛鍔犺浇MySQL椹卞姩锛�");
            
        String url1="jdbc:mysql://202.120.36.137:6033/mag-new-160205";    //JDBC鐨刄RL URL缂栧啓鏂瑰紡锛歫dbc:mysql://涓绘満鍚嶇О锛氳繛鎺ョ鍙�/鏁版嵁搴撶殑鍚嶇О?鍙傛暟=鍊�   
		try {
			conn = DriverManager.getConnection(url1, "map","map");     // 涓�涓狢onnection浠ｈ〃涓�涓暟鎹簱杩炴帴
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        try {
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();     // Statement閲岄潰甯︽湁寰堝鏂规硶锛屾瘮濡俥xecuteUpdate鍙互瀹炵幇鎻掑叆锛屾洿鏂板拰鍒犻櫎绛�
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        System.out.println("鎴愬姛杩炴帴鍒版暟鎹簱锛�");
      /*  String sql = "select tb.ChildFieldOfStudyID as FieldsOfStudyID,FieldsOfStudy.FieldsOfStudyName,FieldsOfStudy.PaperNum from FieldsOfStudy inner join "
                + "(select distinct ChildFieldOfStudyID from FieldOfStudyHierarchy where ChildFieldOfStudyLevel='L1') as tb "
                + "on tb.ChildFieldOfStudyID=FieldsOfStudyID order by PaperNum desc limit 230";*/
       //String sql = "SELECT * FROM FieldsOfStudy having PaperNum >= 100000";
     /* String sql = "SELECT * FROM FieldsOfStudy\n" +
"INNER JOIN\n" +
"(SELECT * FROM FieldOfStudyHierarchy WHERE ChildFieldOfStudyID='0361C4E6')AS TB\n" +
"ON TB.ChildFieldOfStudyID=FieldsOfStudy.FieldsOfStudyID ";*/    //杩欎竴sql璇彞鐢ㄤ簬鍙朙1灞傛煇涓�鐗瑰畾棰嗗煙
      String sql = "SELECT * \n" +
"FROM FieldsOfStudy\n" +
"INNER JOIN\n" +
"    (SELECT *\n" +
"    FROM FieldsOfStudy\n" +
"    WHERE FieldsOfStudyLevel = 'L1') AS TB \n" +
"    ON TB.FieldsOfStudyID = FieldsOfStudy.FieldsOfStudyID\n" +
"WHERE FieldsOfStudy.PaperNum < 10000\n" +
"ORDER BY FieldsOfStudy.PaperNum DESC"; //杩欎竴sql璇彞鐢ㄤ簬鍙栧洖L1灞俻aper鏁板湪鏌愪竴鑼冨洿鐨勯鍩�
        ResultSet rs2 = stmt2.executeQuery(sql);            // executeQuery浼氳繑鍥炵粨鏋滅殑闆嗗悎锛屽惁鍒欒繑鍥炵┖鍊�
        String fieldsOfStudyID = "";
        String fieldsOfStudyName = "";
        int PaperNum = 0;
        int cnt = 0;
        
        while (rs2.next()) {
			try {
				fieldsOfStudyID = rs2.getString("FieldsOfStudyID");
				fieldsOfStudyName = rs2.getString("FieldsOfStudyName");
				//PaperNum = rs2.getInt("PaperNum");
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				System.out.println(fieldsOfStudyName);
				//System.out.println(PaperNum);
	        	writer.write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	        	writer.newLine();
	        	writer.write(fieldsOfStudyName);
	        	writer.newLine();
	        	writer.write(PaperNum);
	        	writer.newLine();
	        	generateSvg(fieldsOfStudyID,exam);
			} catch (Exception e1) {
				e1.printStackTrace();
				try {
					writer.write(e1.getMessage());
					writer.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} finally {
				++cnt;
				System.out.println("Having processed " + cnt + " fields");
			}
        }
        rs2.close();
     /*  StringBuffer list=new StringBuffer(""); 
        while (rs2.next()) {
			try {
				fieldsOfStudyID = rs2.getString("FieldsOfStudyID");
				fieldsOfStudyName = rs2.getString("FieldsOfStudyName");
				//PaperNum = rs2.getInt("PaperNum");
				
				//System.out.println(fieldsOfStudyName);
				//System.out.println(PaperNum);
	        	writer.write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	        	writer.newLine();
	        	writer.write(fieldsOfStudyName);
	        	writer.newLine();
	        	writer.write(PaperNum);
	        	writer.newLine();
				//generateSvg(fieldsOfStudyID);
                               list.append("|");list.append(fieldsOfStudyID);
			} catch (Exception e1) {
				e1.printStackTrace();
				try {
					writer.write(e1.getMessage());
					writer.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} finally {
				++cnt;
				//System.out.println("Having processed " + cnt + " fields");
			}
        }
        rs2.close();
        String list2=list.toString();
        String []list3=list2.split("\\|");
        System.out.println(list2);
       for(int j=0;j<cnt;j++){
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            System.out.println(list3[j]);
            generateSvg(list3[j]);
            System.out.println("Having processed " + j + " fields");
        }*/
       // generateSvg("0271BC14"); 
	stmt.close();
        conn.close();
        stmt2.close();
        writer.close();
        os.close();
	}
}
//e.toString()锛�  鑾峰緱寮傚父绉嶇被鍜岄敊璇俊鎭�
//e.getMessage(): 鑾峰緱閿欒淇℃伅
//e.printStackTrace()锛氬湪鎺у埗鍙版墦鍗板嚭寮傚父绉嶇被锛岄敊璇俊鎭拰鍑洪敊浣嶇疆绛�
