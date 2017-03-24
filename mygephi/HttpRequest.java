/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygephi;

/*import java.io.BufferedReader;  
import java.io.DataInputStream;  
import java.io.DataOutputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.net.Socket;  */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
  
/*public class socket {  
    public static final String IP_ADDR = "202.120.36.137";//鏈嶅姟鍣ㄥ湴鍧�   
    public static final int PORT = 10080;//鏈嶅姟鍣ㄧ鍙ｅ彿    
      
    public static void main(String[] args) {    
        System.out.println("瀹㈡埛绔惎鍔�...");    
        System.out.println("褰撴帴鏀跺埌鏈嶅姟鍣ㄧ瀛楃涓� \"OK\" 鐨勬椂鍊�, 瀹㈡埛绔皢缁堟\n");   
        while (true) {    
            Socket socket = null;  
            try {  
                //鍒涘缓涓�涓祦濂楁帴瀛楀苟灏嗗叾杩炴帴鍒版寚瀹氫富鏈轰笂鐨勬寚瀹氱鍙ｅ彿  
                socket = new Socket(IP_ADDR, PORT);    
                    
                //璇诲彇鏈嶅姟鍣ㄧ鏁版嵁    
                DataInputStream input = new DataInputStream(socket.getInputStream());    
                //鍚戞湇鍔″櫒绔彂閫佹暟鎹�    
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());    
                System.out.print("璇疯緭鍏�: \t");    
                String str = new BufferedReader(new InputStreamReader(System.in)).readLine();    
                out.writeUTF(str);    
                    
                String ret = input.readUTF();     
                System.out.println("鏈嶅姟鍣ㄧ杩斿洖杩囨潵鐨勬槸: " + ret);    
                // 濡傛帴鏀跺埌 "OK" 鍒欐柇寮�杩炴帴    
                if ("OK".equals(ret)) {    
                    System.out.println("瀹㈡埛绔皢鍏抽棴杩炴帴");    
                    Thread.sleep(500);    
                    break;    
                }    
                  
                out.close();  
                input.close();  
            } catch (Exception e) {  
                System.out.println("瀹㈡埛绔紓甯�:" + e.getMessage());   
            } finally {  
                if (socket != null) {  
                    try {  
                        socket.close();  
                    } catch (IOException e) {  
                        socket = null;   
                        System.out.println("瀹㈡埛绔� finally 寮傚父:" + e.getMessage());   
                    }  
                }  
            }  
        }    
    }    
}    */
public class HttpRequest {
    /**
     * 鍚戞寚瀹歎RL鍙戦�丟ET鏂规硶鐨勮姹�
     * 
     * @param url
     *            鍙戦�佽姹傜殑URL
     * @param param
     *            璇锋眰鍙傛暟锛岃姹傚弬鏁板簲璇ユ槸 name1=value1&name2=value2 鐨勫舰寮忋��
     * @return URL 鎵�浠ｈ〃杩滅▼璧勬簮鐨勫搷搴旂粨鏋�
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 鎵撳紑鍜孶RL涔嬮棿鐨勮繛鎺�
            URLConnection connection = realUrl.openConnection();
            // 璁剧疆閫氱敤鐨勮姹傚睘鎬�
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 寤虹珛瀹為檯鐨勮繛鎺�
            connection.connect();
            // 鑾峰彇鎵�鏈夊搷搴斿ご瀛楁
            Map<String, List<String>> map = connection.getHeaderFields();
            // 閬嶅巻鎵�鏈夌殑鍝嶅簲澶村瓧娈�
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 瀹氫箟 BufferedReader杈撳叆娴佹潵璇诲彇URL鐨勫搷搴�
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("鍙戦�丟ET璇锋眰鍑虹幇寮傚父锛�" + e);
            e.printStackTrace();
        }
        // 浣跨敤finally鍧楁潵鍏抽棴杈撳叆娴�
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 鍚戞寚瀹� URL 鍙戦�丳OST鏂规硶鐨勮姹�
     * 
     * @param url
     *            鍙戦�佽姹傜殑 URL
     * @param param
     *            璇锋眰鍙傛暟锛岃姹傚弬鏁板簲璇ユ槸 name1=value1&name2=value2 鐨勫舰寮忋��
     * @return 鎵�浠ｈ〃杩滅▼璧勬簮鐨勫搷搴旂粨鏋�
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 鎵撳紑鍜孶RL涔嬮棿鐨勮繛鎺�
            URLConnection conn = realUrl.openConnection();
            // 璁剧疆閫氱敤鐨勮姹傚睘鎬�
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 鍙戦�丳OST璇锋眰蹇呴』璁剧疆濡備笅涓よ
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 鑾峰彇URLConnection瀵硅薄瀵瑰簲鐨勮緭鍑烘祦
            out = new PrintWriter(conn.getOutputStream());
            // 鍙戦�佽姹傚弬鏁�
            out.print(param);
            // flush杈撳嚭娴佺殑缂撳啿
            out.flush();
            // 瀹氫箟BufferedReader杈撳叆娴佹潵璇诲彇URL鐨勫搷搴�
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("鍙戦�� POST 璇锋眰鍑虹幇寮傚父锛�"+e);
            e.printStackTrace();
        }
        //浣跨敤finally鍧楁潵鍏抽棴杈撳嚭娴併�佽緭鍏ユ祦
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) {  
        System.out.println(sendPost("http://202.120.36.137:10080","ABCDEFGH=HGFEDCBA"));
    }
}
