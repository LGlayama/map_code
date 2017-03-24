
package mygephi;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.lang.String;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public  class Hierarchy {
    
    
    public static class HierarchyTree{
        private class node{
            String fieldID;
            String fieldLevel;
            int parentIndex;
            public node()
            {fieldID="";
            fieldLevel="";
            parentIndex=0;
            }
        }
        private node[] data;
        private int currentlength;
        public HierarchyTree()
        {currentlength=1;
         data=new node[80000];
         for (int i=0;i<80000;++i)
         data[i]=new node();
         data[0].fieldID="";
         data[0].fieldLevel="";
         data[0].parentIndex=0;
        }
        
    
        private void insert(String ID,String level,int order)
        {data[currentlength].fieldID=ID;
         data[currentlength].parentIndex=order;
         data[currentlength].fieldLevel=level;
         ++currentlength;}
        
        public void createTree()throws SQLException, ClassNotFoundException, IOException
        {   Statement stmt = null;   //Statement鏄竴涓帴鍙ｏ紝鎻愪緵浜嗗悜鏁版嵁搴撳彂閫佹墽琛岃鍙ュ拰鑾峰彇缁撴灉鐨勬柟娉�
            Connection conn = null;//Connection鏄敤浜庡皢JAVA鍜屾暟鎹簱杩炴帴鐨勭被
            Class.forName("com.mysql.jdbc.Driver");
              
        String url="jdbc:mysql://202.120.36.137:6033/mag-new-160205";    //JDBC鐨刄RL URL缂栧啓鏂瑰紡锛歫dbc:mysql://涓绘満鍚嶇О锛氳繛鎺ョ鍙�/鏁版嵁搴撶殑鍚嶇О?鍙傛暟=鍊�   
		try {
			conn = DriverManager.getConnection(url, "map","map"); // 涓�涓狢onnection浠ｈ〃涓�涓暟鎹簱杩炴帴
		} catch (SQLException e1) {
			e1.printStackTrace();
                        
		}
        try {
        	
                stmt = conn.createStatement();    
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        
        //System.out.println("鎴愬姛杩炴帴鍒版暟鎹簱锛�");
        int operatelength1=0;//begin;
            int operatelength2=0;//end;
            String sql="SELECT DISTINCT ParentFieldOfStudyID,ParentFieldOfStudyLevel FROM `mag-new-160205`.FieldOfStudyHierarchy WHERE ParentFieldOfStudyLevel=\"L0\"";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
            {insert(rs.getString("ParentFieldOfStudyID"),"L0",0);}
            rs.close();
            operatelength1=1;
            operatelength2=currentlength;
            int l1begin=operatelength2;
            System.out.println("L0 finish");
            
            int i=0;
            for (i=1;i<operatelength2;++i)
            {sql="SELECT * FROM `mag-new-160205`.FieldOfStudyHierarchy WHERE ParentFieldOfStudyID=\""+data[i].fieldID+"\"AND ChildFieldOfStudyLevel=\"L1\"";
             rs=stmt.executeQuery(sql);
             while (rs.next())
             {insert(rs.getString("ChildFieldOfStudyID"),rs.getString("ChildFieldOfStudyLevel"),i);}
             rs.close();}
             operatelength1=operatelength2;
             operatelength2=currentlength;
             int l2begin=operatelength2;
             System.out.println("L1 finish");
             
             loop1:
             while (i<operatelength2)
             {for (int j=operatelength1;j<i;++j)
             { if (data[i].fieldID.equals(data[j].fieldID))
             {++i;
             continue loop1;}}
             sql="SELECT * FROM `mag-new-160205`.FieldOfStudyHierarchy WHERE ParentFieldOfStudyID=\""+data[i].fieldID+"\"AND ChildFieldOfStudyLevel=\"L2\"";
             rs=stmt.executeQuery(sql);
             while (rs.next())
             {insert(rs.getString("ChildFieldOfStudyID"),rs.getString("ChildFieldOfStudyLevel"),i);}
             rs.close();
             ++i;}
             System.out.println("L2 finish");
             
             operatelength1=operatelength2;
             operatelength2=currentlength;
             int l3begin=operatelength2;
             
             loop2:
             while (i<operatelength2)
             {for (int j=operatelength1;j<i;++j)
             {if (data[i].fieldID.equals(data[j].fieldID))
             {++i;
             continue loop2;}}
             sql="SELECT * FROM `mag-new-160205`.FieldOfStudyHierarchy WHERE ParentFieldOfStudyID=\""+data[i].fieldID+"\"AND ChildFieldOfStudyLevel=\"L3\"";
             rs=stmt.executeQuery(sql);
             while (rs.next())
             {insert(rs.getString("ChildFieldOfStudyID"),rs.getString("ChildFieldOfStudyLevel"),i);}
             rs.close();
             ++i;}
             operatelength1=operatelength2;
             operatelength2=currentlength;
             int l3end=currentlength;
             System.out.println("L3 finish");
             
             for (int j=1;j<l1begin;++j)
             {sql="SELECT * FROM `mag-new-160205`.FieldOfStudyHierarchy WHERE ParentFieldOfStudyID=\""+data[j].fieldID+"\"AND ChildFieldOfStudyLevel=\"L2\"";
              rs=stmt.executeQuery(sql);
              loop3:
             while (rs.next())
             {for(int m=l2begin;m<l3begin;++m)
             {if(rs.getString("ChildFieldOfStudyID").equals(data[m].fieldID))
                     continue loop3;}
             insert(rs.getString("ChildFieldOfStudyID"),rs.getString("ChildFieldOfStudyLevel"),j);}
              rs.close();}
             operatelength1=operatelength2;
             operatelength2=currentlength;
             
             loop4:
             for(i=operatelength1;i<operatelength2;++i)
             {for (int j=operatelength1;j<i;++j)
             {if (data[i].fieldID.equals(data[j].fieldID))
             {continue loop4;}}
             sql="SELECT * FROM `mag-new-160205`.FieldOfStudyHierarchy WHERE ParentFieldOfStudyID=\""+data[i].fieldID+"\"AND ChildFieldOfStudyLevel=\"L3\"";
             rs=stmt.executeQuery(sql);
             while (rs.next())
             {insert(rs.getString("ChildFieldOfStudyID"),rs.getString("ChildFieldOfStudyLevel"),i);}
             rs.close();
             }
             operatelength1=operatelength2;
             operatelength2=currentlength;
             System.out.println("L0-2-3 finish");
             
             loop5:
             for (int j=l1begin;j<l2begin;++j)
             {for (int n=l1begin;n<j;++n)
             {if (data[n].fieldID.equals(data[j].fieldID))
             {continue loop5;}}
             sql="SELECT * FROM `mag-new-160205`.FieldOfStudyHierarchy WHERE ParentFieldOfStudyID=\""+data[j].fieldID+"\"AND ChildFieldOfStudyLevel=\"L3\"";
              rs=stmt.executeQuery(sql);
              loop6:
             while (rs.next())
             {for(int m=l3begin;m<operatelength2;++m)
             { if(rs.getString("ChildFieldOfStudyID").equals(data[m].fieldID))
                     continue loop6;}
             insert(rs.getString("ChildFieldOfStudyID"),rs.getString("ChildFieldOfStudyLevel"),j);}
             rs.close();}
             operatelength1=operatelength2;
             operatelength2=currentlength;
             System.out.println("L1-3 finish");
             
             loop7:
             for (i=1;i<l1begin;++i)
             {
             sql="SELECT * FROM `mag-new-160205`.FieldOfStudyHierarchy WHERE ParentFieldOfStudyID=\""+data[i].fieldID+"\"AND ChildFieldOfStudyLevel=\"L3\"";
             rs=stmt.executeQuery(sql);
             loop8:
             while (rs.next())
             {for (int m=l3begin;m<operatelength2;++m)
             {if(rs.getString("ChildFieldOfStudyID").equals(data[m].fieldID))
                     continue loop8;}
             insert(rs.getString("ChildFieldOfStudyID"),rs.getString("ChildFieldOfStudyLevel"),i);}
             rs.close();} 
             System.out.println("L0-3 finish");}
             
        public String search(String aa)
        {//Queue<node>output=new LinkedList<node>();
         Stack<node>output=new Stack<node>();
        String result=new String();
        int length=0;
         for(int i=1;i<currentlength;++i)
        {if(data[i].fieldID.equals(aa))
        {output.push(data[i]);
        ++length;
        }}
        while(length!=0)
        {node tmp;
         tmp=output.pop();
         --length;
         result+=tmp.fieldID+"@"+tmp.fieldLevel+" ";
         for(int i=1;i<currentlength;++i)
        {if(data[i].fieldID.equals(data[tmp.parentIndex].fieldID))
        {output.push(data[i]);
        ++length;}
        }
        }
        return result;
        }

        public String match(String parentID,String childID)
        {String[] child;
        String field="";
        String result="";
        child=childID.split("\\|");
        int i=0;
        for(String s:child)
        {field=search(child[i]);
          if (field.indexOf(parentID)>=0)
              result=result+child[i]+field.substring(8, 11)+"|";
          ++i;
        }
        return result;
        } 
        public String searchLevel(String ID)
        {int i=0;
        for (i=1;i<currentlength;++i)
                if(data[i].fieldID.equals(ID))break;
        return data[i].fieldLevel;
                }}    
    
        public static double paperFit(String fieldID1,String fieldID2,String field,HierarchyTree tree)
                //fieldID1锛宖ieldID2鏄痯aper瀵瑰簲鐨勯鍩熶覆锛宖ield鏄綔鍥鹃鍩�,tree鏄彁鍓嶅垱寤虹殑鏍戯紱
                //fieldID1锛宖ieldID2鏍煎紡瑕佹眰"fieldID|fieldID"锛�
        {double result=0;
         double weightL3=1;
         double weightL2=0.5;
         double weightL1=0.25;
         double weightL0=0.0125;
         String []oneField;
         String relationField=tree.search(field);
         oneField=fieldID1.split("\\|");
         int number=0;
         for(String s:oneField)
         {if(relationField.indexOf(oneField[number])==-1&&fieldID2.indexOf(oneField[number])>=0)
         {if(tree.searchLevel(oneField[number]).equals("L0"))result+=weightL0;
         if(tree.searchLevel(oneField[number]).equals("L1"))result+=weightL1;
         if(tree.searchLevel(oneField[number]).equals("L2"))result+=weightL2;
         if(tree.searchLevel(oneField[number]).equals("L3"))result+=weightL3;
         }
         ++number;}
         return result;}
        
        public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {        
        HierarchyTree exam=new HierarchyTree();
        exam.createTree();
        String fieldID1,fieldID2,field;
        fieldID1="0AA820DC|0B0FEB68|05CD884F|073B64E4|0000109A|07982D63";
        fieldID2="0AA820DC|0B0FEB68|05CD884F|073B64E4|0000109A|07982D63|0002487D|000BDF08";
        field="0AC440B6";
        System.out.println(paperFit(fieldID1,fieldID2,field,exam));
    }}
