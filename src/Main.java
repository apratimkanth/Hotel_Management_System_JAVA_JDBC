import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
class data{
    int sl_no;
    int id;
    int room_no;
    String name;
    String date_of_checkIn;
    String Date_of_checkout;
    String time_of_checkout;
    String time_of_checkin;
    int status;
}
class SqlOperation{
    public static int checkroom(){
        int roomNo=-1;
        try {
            String query="select * from roomstatus where Status=1;";
            Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);


            if(rs.next()){
                roomNo= rs.getInt("Room_No");
//                String Name = rs.getString("Status");
            }
            rs.close();
            connection.close();
            st.close();


        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());

        }
        return roomNo;
    }

    public static int newReservation(String CustName,int CustID,int room_no,String checkOutDate){
        int affectedRow=-1;
        try {
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Define the time zone for India (Asia/Kolkata)
            ZoneId indiaZone = ZoneId.of("Asia/Kolkata");

            // Convert current time to India's time zone
            ZonedDateTime indiaDateTime = currentDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(indiaZone);

            // Format the date and time for display
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(" HH:mm:ss");
            String currDate=indiaDateTime.format(formatter1);
            String currTime=indiaDateTime.format(formatter2);
            String checkOutTime="11:00:00";
            String query="insert into persondetail(`Name`,`Room_No`,`Id_Proof`,`Date_Of_CheckIn`,`Time_Of_CheckIn`,`Date_Of_CheckOut`,`Time_Of_CheckOut`)"
                    +" values(?,?,?,?,?,?,?);";
            Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1,CustName);
            stmt.setInt(2, room_no);
            stmt.setInt(3, CustID);
            stmt.setString(4, currDate);
            stmt.setString(5, currTime);
            stmt.setString(6, checkOutDate);
            stmt.setString(7, checkOutTime);

            affectedRow = stmt.executeUpdate();

            connection.close();
            stmt.close();


        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());

        }
        return affectedRow;
    }
    public static int updateDatabase(){
        int affectedRow=-1;
        try {
            String query3="UPDATE roomstatus\n" +
                    "SET Status = ? \n" +
                    "WHERE Room_No IN (\n" +
                    "select Room_No from \n" +
                    "(SELECT Room_No,MAx(Date_Of_CheckOut) as Date_Of_CheckOut,max(Time_Of_CheckOut) as Time_Of_CheckOut FROM persondetail group by Room_No) as tb \n" +
                    "WHERE STR_TO_DATE(Date_Of_CheckOut, '%d-%m-%Y') <= CURDATE() \n" +
                    "    AND (STR_TO_DATE(Date_Of_CheckOut, '%d-%m-%Y') < CURDATE() \n" +
                    "    OR (STR_TO_DATE(Date_Of_CheckOut, '%d-%m-%Y') = CURDATE() AND Time_Of_CheckOut <= CURTIME())));";
            Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
            PreparedStatement pstmt = connection.prepareStatement(query3);
            pstmt.setInt(1,1);
            affectedRow = pstmt.executeUpdate();
            connection.close();
            pstmt.close();


        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());

        }
        return affectedRow;
    }

    public static int updateStatus(int Room_No,int Status){
        int affectedRow=-1;
        try {
            String query="Update roomstatus set Status="+Status+" where Room_No="+Room_No+";";
            Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
            Statement stmt = connection.createStatement();
            affectedRow = stmt.executeUpdate(query);

            connection.close();
            stmt.close();
        }
        catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());

        }
        return affectedRow;

    }
    public static ArrayList<data> checkResevation(int id,String name){
        ArrayList<data>ar=new ArrayList<data>();
        System.out.println(id);
        try {
            String query="select pd.Room_No,pd.Name,Id_Proof,Status,Date_Of_CheckIn,Time_Of_CheckIn,Date_Of_CheckOut,Time_Of_CheckOut from persondetail as pd inner join roomstatus as rs on pd.Room_No=rs.Room_No where Name=? or Id_Proof=?;";
            Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1,name);
            stmt.setInt(2,id);
            ResultSet set = stmt.executeQuery();
            while(set.next()){
                data obj=new data();
                obj.room_no= set.getInt("Room_No");
                obj.name= set.getString("Name");
                obj.id= set.getInt("Id_Proof");
                obj.status= set.getInt("Status");
                obj.date_of_checkIn= set.getString("Date_Of_CheckIn");
                obj.time_of_checkin= set.getString("Time_Of_CheckIn");
                obj.Date_of_checkout= set.getString("Date_Of_CheckOut");
                obj.time_of_checkout= set.getString("Time_Of_CheckOut");
                ar.add(obj);
            }

            connection.close();
            stmt.close();
        }
        catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());

        }
        return ar;
    }
}

class Menu{
    public static void menu(){
        System.out.println("1.] Room availability");
        System.out.println("2.] New Resevation");
        System.out.println("3.] Check Resevation");
        System.out.println("4.] Update Resevation");
        System.out.println("5.] Delete Resevation");
        System.out.println("6.] Exit");
        int choice;
        if(Main.sc1.hasNextInt()) {
            choice = Main.sc1.nextInt();
        }
        else{
            choice=6;
        }

        switch(choice){
            case 1:
                RoomAvailable();
                break;
            case 2:
                NewReservation();
                break;
            case 3:
                CheckReservation();
                break;
            case 4:
                UpdateReservation();
                break;
            case 5:
                DeleteReservation();
                break;
            case 6:
                break;
        }
    }

    private  static void RoomAvailable(){
        int getRoom=SqlOperation.checkroom();
        if(getRoom==-1){
            System.out.println("Sorry.....Room not Available");
        }
        else{
            System.out.println("Room no "+getRoom+" is available");
        }
        menu();
    }
    private  static void NewReservation(){
        int getRoom=SqlOperation.checkroom();
        if(getRoom==-1){
            System.out.println("Sorry.....Room not Available");
            menu();
        }
        System.out.print("Enter Customer Name : ");
        String CustName;
        if(Main.sc2.hasNextLine()){
            CustName=Main.sc2.nextLine();
        }
        else{
            CustName=null;
        }
        System.out.print("Enter Customer ID Proof No : ");
        int CustID;
        if(Main.sc1.hasNextInt()){
            CustID=Main.sc1.nextInt();
        }
        else{
            CustID=0;
        }
        System.out.print("Enter Customer CheckOut Date : ");
        String checkOutDate;
        if(Main.sc2.hasNextLine()){
            checkOutDate=Main.sc2.nextLine();
        }
        else{
            checkOutDate=null;
        }
        int status=SqlOperation.newReservation(CustName,CustID,getRoom,checkOutDate);
        if(status==-1){
            System.out.println("Sorry.....Room are not Allocated");
        }
        else {
            int updatestatus=SqlOperation.updateStatus(getRoom,0);
            if(updatestatus!=-1) {
                System.out.println("Congratulation " + CustName + " you got Room No : " + getRoom);
            }
            else {
                System.out.println("Room Allocated at Room No : "+ getRoom+" But not Updated into roomstatus Database");
            }
        }
        menu();
    }
    private static  void CheckReservation(){
        ArrayList<data>ar=new ArrayList<data>();
        System.out.print("1.] Enter Customer Name  : ");
        String name = Main.sc2.nextLine();
        System.out.print("2.] Enter Id Proof : ");
        int id = Main.sc1.nextInt();
        ar=SqlOperation.checkResevation(id,name);
        int i=1;
        for(data obj:ar){
            System.out.println(i+".]");
            System.out.println("Name : "+obj.name);
            System.out.println("Id : "+obj.id);
            System.out.println("Room : "+obj.room_no);
            System.out.println("Date Of CheckIn : "+obj.date_of_checkIn);
            System.out.println("Time Of CheckIn : "+obj.time_of_checkin);
            System.out.println("Date Of CheckOut : "+obj.Date_of_checkout);
            System.out.println("Time Of CheckOut : "+obj.time_of_checkout);
            if(obj.status==1){
                System.out.println("Status : "+"Available");
            }
            else{
                System.out.println("Status : "+"Booked");
            }
            System.out.println();
            i++;
        }
    }
    private  static void UpdateReservation(){

    }
    private  static void DeleteReservation(){

    }

}
public class Main {
    final static String url = "jdbc:mysql://localhost:3306/javaproject";
    // Database credentials
    final static String username = "root";
    final static String password = "Password@mysql#123";
    static Scanner sc1=new Scanner(System.in);
    static Scanner sc2=new Scanner(System.in);
    public static void main(String[] args){
        int status=SqlOperation.updateDatabase();
        System.out.println("Welcome to the Hotel Maurya");
        Menu.menu();
        sc1.close();
        sc2.close();
    }
}
