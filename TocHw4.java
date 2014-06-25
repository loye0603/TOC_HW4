import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.*;

public class TocHW4 {
        private static Object obj;
        public static void main(String[] args) throws Exception {
                try
                {
                String city,roadname,price,address,jsonString1="",line,URLNAME;
                  int count=0,number=1,namenum=0;
                
                  //URLNAME=args[0];
                // connect to URL
                  HttpURLConnection conn = null;
                  URL url = new URL(
                          "http://www.datagarage.io/api/5385b69de7259bb37d925971");  
                  //URL url = new URL(URLNAME);  
                  conn = (HttpURLConnection) url.openConnection();
               
                  conn.setRequestMethod("GET");
                  conn.connect();
                  if (Thread.interrupted()) {
                      throw new InterruptedException();
                  }
                  // 讀取資料
                  BufferedReader reader = new BufferedReader(new InputStreamReader(
                          conn.getInputStream(), "UTF-8"));//or UTF-8?
                  JSONTokener tokener = new JSONTokener(reader);
                  JSONArray jsonRealPrice = new JSONArray(tokener);

                 int[] total=new int[jsonRealPrice.length()];
                 int[] num=new int[jsonRealPrice.length()+5000];
                 String[] name=new String [jsonRealPrice.length()];
                 String[] realname=new String [jsonRealPrice.length()];
                for(int counter =0; counter<jsonRealPrice.length();counter++)
                {
                 JSONObject a =  jsonRealPrice.getJSONObject(counter);
            
                 address=a.get("土地區段位置或建物區門牌").toString();
              
                 price=a.get("總價元").toString();
                // System.out.println(price);
                 String time=a.get("交易年月").toString();
                 //System.out.println(time);
                 int year=0,month=0;
                 if(time.length()==5){
                  year=Integer.parseInt(time.substring(0,3));
                  month=Integer.parseInt(time.substring(2,5))%100;
                 //System.out.println(year+"  "+month);
                 }
                 else if (time.length()==4){
                	  year=Integer.parseInt(time.substring(0,2));
                      month=Integer.parseInt(time.substring(1,4))%100;
                   //  System.out.println(year+"  "+month);
                 }
                // System.out.println(year+"  "+month);
                 int timenumber=(month+year*13);
                 int n=0,l=0;
                 name[counter]= address;
                 if(timenumber%13!=0)
                 total[counter]= timenumber;// the spread time for object no.counter
                 //System.out.println(timenumber);
                   if(counter==0 && namenum==0)
                    {
                    realname[namenum]=name[counter];//new name that no set before
                    namenum++;
                    }
                   else{
                       for(int j=0;j<namenum;j++)
                         {
                           if(getaddress(name[counter],realname[j]))
                              {   l++;
                                  break;
                              }
                         }
                      if (l==0)
                      {
                       if((name[counter].contains("路"))|| (name[counter].contains("街")) || (name[counter].contains("巷"))) {
                            realname[namenum]=name[counter];
                            namenum++;
                         }
                      }
                      }
                  if(timenumber%13!=0){
                   if(counter==0)
                    num[0]=total[0];
                   else{
                       for(int j=0;j<number;j++)   //record the timenumber
                         {
                           if(total[counter]!=num[j])
                              {  n++;
                              }
                         }
                         if(n==number)
                         {
                            num[number]=total[counter];//new timenumber
                            number++;
                         }
                      }
                    for(int i=0;i<number-1;i++){
                    if(total[counter]==num[i]){
                        num[num[i]]++; //easyer to choice the right time
                     }
                    }
                 }
                 }
                
                 num[num[0]]++;
                int max=0,k,m;
                 for( k=0;k<number-1;k++){
                   if(num[num[k]]>max)
                   {
                      max= num[num[k]];//find the max time that spread
                   }
                  }
                 for(m=0;m<number-1;m++)
                 {
                   if(num[num[m]]==max)
                	  //System.out.println(num[m]);
                    break;
                 }
                 
                  int[][]comeon=new int[namenum][number];
                  int[] nametime=new int[namenum];
                   for(int counter =0; counter<jsonRealPrice.length();counter++)
                   {
                      JSONObject a =  jsonRealPrice.getJSONObject(counter);
                     
                      address=a.get("土地區段位置或建物區門牌").toString();
                  
                      price=a.get("總價元").toString();
                      String time=a.get("交易年月").toString();
                      int year=0,month=0;
                      if(time.length()==5){
                       year=Integer.parseInt(time.substring(0,3));
                       month=Integer.parseInt(time.substring(2,5))%100;
                      //System.out.println(year+"  "+month);
                      }
                      else if (time.length()==4){
                     	  year=Integer.parseInt(time.substring(0,2));
                           month=Integer.parseInt(time.substring(1,4))%100;
                        //  System.out.println(year+"  "+month);
                      }
                      int timenumber=(month+year*13);
                      int n=0;
                     if(timenumber%13!=0)
                      for(int i=0;i<namenum;i++)
                        if(getaddress(address,realname[i]))
                         for(int j=0;j<number;j++)
                          if(timenumber==num[j])
                            comeon[i][j]=1;   //if the most spread road comeon==1;
                  }
                    for(int i=0;i<namenum;i++)
                      for(int j=0;j<number;j++)
                        if(comeon[i][j]==1)
                             nametime[i]++;
                     int maxtimes=0,y,z=0;
                     String[] final1=new String [namenum];
                     for( y=0;y<namenum;y++){
                      if(nametime[y]>maxtimes)
                      {
                        maxtimes = nametime[y];
                      }
                     }
                     for(y=0;y<namenum;y++)
                     {
                      if(nametime[y]>= maxtimes)
                        {
                          final1[z]=realname[y];
                          z++;
                        }
                     }
                     int[][] finalprice=new int[z][jsonRealPrice.length()];
                     int g=0;
                     int[]  finaltimes=new int [z];
                     for(int i=0;i<z;i++){
                     for(int counter =0; counter<jsonRealPrice.length();counter++)
                     {
                        JSONObject a =  jsonRealPrice.getJSONObject(counter);
                       /// JSONArray details=(JSONArray) a.get("details");
                     //   JSONObject fields=(JSONObject) a.get("fields");
                        price=a.get("總價元").toString();
                        address=a.get("土地區段位置或建物區門牌").toString();
                           if(getaddress(address,final1[i]))
                           {
                            finalprice[i][g]=Integer.parseInt(price);
                            g++;
                           }
                    }
                      finaltimes[i]=g;
                      g=0;
                    }
                     
                    int maxprice=0,minprice=finalprice[0][0];
                    String high,low;
                    int[] thebig=new int[z];
                        for(int i=0;i<z;i++){
                              for( int j=0;j<finaltimes[i];j++){
                              if(finalprice[i][j]>maxprice)
                              {
                              maxprice= finalprice[i][j];
                              }
                                if(finalprice[i][j]<=minprice){
                              minprice=finalprice[i][j];
                              }
                              }
                               high = Integer.toString(maxprice);
                               low = Integer.toString(minprice);
                              if(final1[i].contains("路"))
                               {
                                String[] v=final1[i].split("路");
                                  System.out.println(v[0]+ "路, 最高成交價：" + high + ", 最低成交價：" + low);
                               }
                               else if(final1[i].contains("街"))
                               {
                                String[] v=final1[i].split("街");
                                  System.out.println(v[0]+ "街, 最高成交價：" + high + ", 最低成交價：" + low);
                               }
                               else if(final1[i].contains("巷"))
                               {
                                String[] v=final1[i].split("巷");
                                  System.out.println(v[0]+ "巷, 最高成交價：" + high + ", 最低成交價：" + low);
                               }
                               maxprice=0;minprice=finalprice[0][0];
                               }
                }
                catch(Exception e)  {
                e.printStackTrace();
            }
        }
        
        
        
        //make sure the address is effective
       public static Boolean getaddress(String address1,String address2)
      {
      if((address1.contains("路"))&&(address2.contains("路")))
                       {
                      String[] road1=address1.split("路");
                      String[] road2=address2.split("路");
                       if(road1[0].equals(road2[0]))
                       return true;
                        else
                        return false;
                       }
     else if((address1.contains("街"))&&(address2.contains("街")))
                       {
                      String[] street1=address1.split("街");
                      String[] street2=address2.split("街");
                      if(street1[0].equals(street2[0]))
                        return true;
                          else
                           return false;
                       }
      else if((address1.contains("巷"))&&(address2.contains("巷")))
                       {
                      String[] alley1=address1.split("巷");
                      String[] alley2=address2.split("巷");
                       if(alley1[0].equals(alley2[0]))
                         return true;
                         else
                         return false;
                       }
    else
    return false;
    }
}

