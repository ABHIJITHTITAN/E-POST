package Database;
import customer.Authentication;

import java.awt.List;
import java.awt.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import login.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import customer.DatasForCustomer.*;
import customer.WalletAuthentication;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import java.sql.Statement;
import java.util.ArrayList;
public class DatabaseOperations 
{
   private static Connection connection;

   public static Connection getConnection() 
   {
        
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","E_Post","123");
        }
  
        
        catch(Exception ex)
        {
            System.out.println("  "+ex);
        }
        return connection;
    }  
    public static Object[][] getCustomerConsignmentDetails(){
       try {
           Connection con=DatabaseOperations.getConnection();
           String Customer_ID="Kishore P";
           String query="select *from Consignment where customer_ID='"+Customer_ID+"'";
           PreparedStatement  st1=con.prepareStatement(query);
           ResultSet res=st1.executeQuery(query);
           
           while(res.next()){
              ConsignmentData ob=new ConsignmentData();
            
              ob.setConsignment_ID(res.getString("consignment_ID"));
              ob.setCustomer_ID(res.getString("customer_ID"));
              ob.setDelivery_ID(res.getString("delivery_ID"));
              ob.setReceiver_ID(res.getString("receiver_ID"));
              ob.setItem_code(res.getString("item_code"));
              ob.setItem(res.getString("item"));
              ob.setItem_price(res.getFloat("item_price"));
              ob.setItem_weight(res.getFloat("item_weight"));
              ob.setReceiver_first_name(res.getString("receiver_first_name"));
              ob.setReceiver_last_name(res.getString("receiver_last_name"));
              ob.setReceiver_address(res.getString("receiver_address"));
              ob.setReceiver_contact_number(res.getLong("receiver_contact_number"));
              ob.setCustomer_first_name(res.getString("customer_first_name"));
              ob.setCustomer_last_name(res.getString("customer_last_name"));
              ob.setCustomer_contact_number(res.getLong("customer_contact_number"));
              ob.setShipping_address(res.getString("shipping_address"));
              ob.setPayment_method(res.getString("payment_method"));
              ob.setOrder_date(res.getDate("order_date"));
              ob.setDelivery_date(res.getDate("delivery_date"));
              ob.setStatus(res.getString("status"));

              ConsignmentData.listForConsignment.add(ob); 
             
              
           }
           System.out.println("Success in Consignment--->"+ConsignmentData.listForConsignment.size());
       } catch (Exception ex) {
           System.out.println("Error in getCustomerConsignmentDetails---->"+ex.toString());
       }
       Object[][] row=new Object[ConsignmentData.listForConsignment.size()][8];
       int i=0;
       for(ConsignmentData Data:ConsignmentData.listForConsignment){
           row[i][0]=Data.getConsignment_ID();
           row[i][1]=Data.getCustomer_first_name();
           row[i][2]=Data.getReceiver_first_name();
           row[i][3]=Data.getItem();
           row[i][4]=Data.getDelivery_ID();
           row[i][5]=Data.getPayment_method();
           row[i][6]=Data.getOrder_date();
           row[i][7]=Data.getStatus();
          
          
         i++;
             
       }
       return row;
    }
    
    
    
    
    public static boolean CheckIdPresentOrNot(String UserId){
        try{
        Connection con=DatabaseOperations.getConnection();
        Statement st=con.createStatement();
        String query="select * from customer where customer_ID='"+UserId+"'";
        ResultSet rs=st.executeQuery(query);
        
        if(rs.next()){
            //new WalletAuthentication();
            return true;
            
        }
        /**
        else{
            
            JOptionPane.showMessageDialog(null,"Invalid userId");
        }
        * */
        }catch(SQLException ex){
            
            System.out.println("Error in checkIdPresentOrNot Function (DatabaseOperations)---->"+ex.toString());
            
        }
        return false;
        
        
        
    }
    
    public static void WalletUpdatationOnMoneyOrder(){
        
        try{
        String senderid="",recieverid="",recieverName="",balanceS="",balanceR="";
        Connection con=DatabaseOperations.getConnection();
        String saccountnoquery="select account_number from Customer where customer_id='Kishore P'";
        PreparedStatement  stSAccNo=con.prepareStatement(saccountnoquery);
        ResultSet rs1=stSAccNo.executeQuery(saccountnoquery);
        if(rs1.next()){
            senderid=rs1.getString(1);
        }
        
        String raccountnoquery="select account_number from Customer where customer_id='"+WalletData.MoneyOrderValues.get(0)+"'";
        PreparedStatement  stRAccNo=con.prepareStatement(raccountnoquery);
        ResultSet rs2=stRAccNo.executeQuery(raccountnoquery);
        if(rs2.next()){
            recieverid=rs2.getString(1);
        }
        
        String rnamequery="select first_name from Customer where customer_id='"+WalletData.MoneyOrderValues.get(0)+"'";
        PreparedStatement  rnamest=con.prepareStatement(rnamequery);
        ResultSet rs3=rnamest.executeQuery(rnamequery);
        if(rs3.next()){
            recieverName=rs3.getString(1);
        }
        
        String bsquery="select bank_balance from Customer where customer_id='Kishore P'";
        PreparedStatement  stsbal=con.prepareStatement(bsquery);
        ResultSet rs4=stsbal.executeQuery(bsquery);
        if(rs4.next()){
            balanceS=rs4.getString(1);
        }
        
        String brquery="select bank_balance from Customer where customer_id='"+WalletData.MoneyOrderValues.get(0)+"'";
        PreparedStatement  strbal=con.prepareStatement(brquery);
        ResultSet rs5=strbal.executeQuery(brquery);
        if(rs5.next()){
            balanceR=rs5.getString(1);
        }
        
        
        String q="insert into wallet values(?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement  st=con.prepareStatement(q);
        
        st.setString(1,getTransactionIdGenerator());
        st.setString(2,"Kishore P");
        st.setString(3,WalletData.MoneyOrderValues.get(0));
        st.setString(4,WalletData.MoneyOrderValues.get(2));
        st.setString(5,WalletData.MoneyOrderValues.get(3));
        st.setString(6,WalletData.MoneyOrderValues.get(4) );
        st.setDate(7,java.sql.Date.valueOf(java.time.LocalDate.now()));
        st.setString(8,senderid);
        st.setString(9, recieverid);
        st.setString(10,WalletData.MoneyOrderValues.get(1));
        st.setString(11,recieverName);
        st.setString(12,Integer.toString(Integer.parseInt(balanceS)-Integer.parseInt(WalletData.MoneyOrderValues.get(1))));
        st.executeUpdate();
        
        String q1="update customer  set BANK_BALANCE='"+Integer.toString(Integer.parseInt(balanceS)-Integer.parseInt(WalletData.MoneyOrderValues.get(1)))+"' where customer_id='Kishore P'";
        PreparedStatement stq1=con.prepareStatement(q1);
        stq1.executeQuery();
        
        String q2="update customer set BANK_BALANCE='"+Integer.toString(Integer.parseInt(balanceR)+Integer.parseInt(WalletData.MoneyOrderValues.get(1)))+"' where customer_id='"+WalletData.MoneyOrderValues.get(0)+"'";
        PreparedStatement stq2=con.prepareStatement(q2);
        stq2.executeQuery();
        
        con.setAutoCommit(true);
        con.close(); 
        
        }catch(Exception e){
            System.out.println("Error in WalletUpdatationOnMoneyOrder() in DatabaseOperations------->"+e.toString());
        }
    }
    
    public static ArrayList getOngoingDeliveryConsignmentDetils(String delivery_id)
    {
        ArrayList ongoing = new ArrayList();        
        try
        {
            Statement st = getConnection().createStatement();
            ResultSet rs =  st.executeQuery("select * from Consignment where delivery_ID ='"+delivery_id+"'");
            if(rs.next())
            {
                ongoing.add(rs.getString("consignment_id"));
                ongoing.add(rs.getString("customer_id"));
                ongoing.add(rs.getString("receiver_id"));
                ongoing.add(rs.getString("item"));
                ongoing.add(rs.getString("delivery_id"));        
                ongoing.add(rs.getString("payment_method"));                
                ongoing.add(rs.getString("order_date"));
                ongoing.add(rs.getString("status"));
                
                /*ongoing.add(rs.getString("consignment_id"));
                ongoing.add(rs.getString("consignment_id"));
                ongoing.add(rs.getString("consignment_id"));
                ongoing.add(rs.getString("consignment_id"));
                ongoing.add(rs.getString("consignment_id"));
                ongoing.add(rs.getString("consignment_id"));
                ongoing.add(rs.getString("consignment_id"));
                ongoing.add(rs.getString("consignment_id"));
                ongoing.add(rs.getString("consignment_id"));
                ongoing.add(rs.getString("consignment_id"));
                ongoing.add(rs.getString("consignment_id"));
                ongoing.add(rs.getString("consignment_id"));*/
                
                
            }
        }
        catch(Exception e)
        {
            
        }
        
        return ongoing;
    }
    
   public static String getConsignmentIdGenerator() {
       try{
          Connection con=DatabaseOperations.getConnection();
				Statement st=con.createStatement();
				String stsql="select count(*) from CONSIGNMENT";
				ResultSet rs=st.executeQuery(stsql);
				rs.next();
				int c=rs.getInt(1);
				con.setAutoCommit(true);
				
				String s="CONID";
                                System.out.println(s+Integer.toString(c+1));
				return s+Integer.toString(c+1); 
       }
       catch(Exception ex1){
            System.out.println("Error in getconsignmentIdGenerator---->"+ex1.toString());
       }
				
	return "";			
     }
   public static String getTransactionIdGenerator()  {
       try{
          Connection con=DatabaseOperations.getConnection();
		Statement st=con.createStatement();
		String stsql="select count(*) from WALLET";
		ResultSet rs=st.executeQuery(stsql);
		rs.next();
		int c=rs.getInt(1);
		con.setAutoCommit(true);
		String s="TRANSID";
		return s+Integer.toString(c+1); 
       }
       catch(Exception ex1){
            System.out.println("Error in getTransactionIdGenerator---->"+ex1.toString());
       }
       
      return "";
   }
   
   public static String getMessageIdGenerator() {
      try{
          Connection con=DatabaseOperations.getConnection();
		Statement st=con.createStatement();
		String stsql="select count(*) from INBOX";
		ResultSet rs=st.executeQuery(stsql);
		rs.next();
		int c=rs.getInt(1);
		con.setAutoCommit(true);
		String s="MSGID";
		return s+Integer.toString(c+1);
          
      }
      catch(Exception ex1){
            System.out.println("Error in getTransactionIdGenerator---->"+ex1.toString());
       }
	return "";	
  }
 
   
   //Database
    public static ArrayList getLoginCredentials(JTextField usernameTextField, JTextField passwordField)
    {
          ArrayList loginCredentials = new ArrayList();
          
          try
            {                
                String userName = usernameTextField.getText();
                String userPassword = passwordField.getText();                

                
                Statement st = getConnection().createStatement();
                ResultSet rs = st.executeQuery("select user_ID from Login where user_ID like '"+userName+"'");
                boolean next = rs.next();
                
                if(next)
                {
                    ResultSet rspass = st.executeQuery("select password,salt,login_type from Login where user_ID like '"+userName+"'");
                    if(rspass.next())
                    {         
                        String pass = rspass.getString("password");
                        String salt = rspass.getString("salt");
                        String type = rspass.getString("login_type");
                        
                        System.out.println(pass);
                        System.out.println(salt);
                        System.out.println(type);
                        //System.out.println(hash_pass);
                        
                        loginCredentials.add(pass);
                        loginCredentials.add(salt);
                        loginCredentials.add(type);
                        //loginCredentials.add(hash_pass);                          
                    }
                }                  
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(null, ex.toString());
            }
          
          return loginCredentials;
    }
    
    public static void customerCreate(ArrayList list)
    {
        
        String userName = list.get(0).toString();
        
        try
        {                
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery("select customer_ID  from Customer where customer_ID  like '"+userName+"'");
            boolean next = rs.next();   

            if(next)
            {
               JOptionPane.showMessageDialog(null, "You have Already Registered to E-Post"); 
            }
            else
            {
               String query="INSERT INTO Customer VALUES ('"+list.get(0).toString()+"','"+list.get(1).toString()+"','"+list.get(2).toString()+"','"+list.get(3).toString()+"','"+list.get(4).toString()+"','"+list.get(5).toString()+"',"
                       + "'"+list.get(6).toString()+"','"+list.get(7).toString()+"','"+list.get(8).toString()+"','"+list.get(9).toString()+"','"+list.get(10).toString()+"','"+list.get(11).toString()+"','"+list.get(12).toString()+"')";

               st.executeUpdate(query);
               getConnection().setAutoCommit(true);
               JOptionPane.showMessageDialog(null, "Registration Success!"); 
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
    
    public static void loginUpdate(String userName, String password, String confirmPassword, int salt)
    {
        try
        {                
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery("select user_ID  from Login where user_ID like '"+userName+"'");
            boolean next = rs.next();
            
            if(next)
            {
                String query="UPDATE Login SET password = '"+password+"', salt = '"+salt+"' WHERE user_ID = '"+userName+"'";
                st.executeUpdate(query);
                getConnection().setAutoCommit(true);
                JOptionPane.showMessageDialog(null, "Successful");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Account Does not Exist. Please Register");
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
    
//    public static Object[][] getCustomerConsignmentDetails(){
//       try {
//           Connection con=DatabaseOperations.getConnection();
//           String Customer_ID="Asif M";
//           String query="select *from Consignment where customer_ID='"+Customer_ID+"'";
//           PreparedStatement  st1=con.prepareStatement(query);
//           ResultSet res=st1.executeQuery(query);
//           
//           while(res.next()){
//              ConsignmentData ob=new ConsignmentData();
//            
//              ob.setConsignment_ID(res.getString("consignment_ID"));
//              ob.setCustomer_ID(res.getString("customer_ID"));
//              ob.setDelivery_ID(res.getString("delivery_ID"));
//              ob.setReceiver_ID(res.getString("receiver_ID"));
//              ob.setItem_code(res.getString("item_code"));
//              ob.setItem(res.getString("item"));
//              ob.setItem_price(res.getFloat("item_price"));
//              ob.setItem_weight(res.getFloat("item_weight"));
//              ob.setReceiver_first_name(res.getString("receiver_first_name"));
//              ob.setReceiver_last_name(res.getString("receiver_last_name"));
//              ob.setReceiver_address(res.getString("receiver_address"));
//              ob.setReceiver_contact_number(res.getLong("receiver_contact_number"));
//              ob.setCustomer_first_name(res.getString("customer_first_name"));
//              ob.setCustomer_last_name(res.getString("customer_last_name"));
//              ob.setCustomer_contact_number(res.getLong("customer_contact_number"));
//              ob.setShipping_address(res.getString("shipping_address"));
//              ob.setPayment_method(res.getString("payment_method"));
//              ob.setOrder_date(res.getDate("order_date"));
//              ob.setDelivery_date(res.getDate("delivery_date"));
//              ob.setStatus(res.getString("status"));
//
//              ConsignmentData.listForConsignment.add(ob); 
//             
//              
//           }
//           System.out.println("Success in Consignment--->"+ConsignmentData.listForConsignment.size());
//       } catch (SQLException ex) {
//           System.out.println("Error in getCustomerConsignmentDetails---->"+ex.toString());
//       }
//       Object[][] row=new Object[ConsignmentData.listForConsignment.size()][8];
//       int i=0;
//       for(ConsignmentData Data:ConsignmentData.listForConsignment){
//           row[i][0]=Data.getConsignment_ID();
//           row[i][1]=Data.getCustomer_first_name();
//           row[i][2]=Data.getReceiver_first_name();
//           row[i][3]=Data.getItem();
//           row[i][4]=Data.getDelivery_ID();
//           row[i][5]=Data.getPayment_method();
//           row[i][6]=Data.getOrder_date();
//           row[i][7]=Data.getStatus();
//         
//         i++;
//             
//       }
//       return row;
//    }
}

