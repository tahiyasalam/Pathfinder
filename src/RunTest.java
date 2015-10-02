public class RunTest {
    public static void main(String[] args) {
    	for (int i = 0; i < 10 ; i++) {
        	MyRobotClass.main(args);      	

    	}
 
      }
 }

//class ReadFileExample 
//{
//   public static void main(String[] args) 
//   {
//      System.out.println("Reading File from Java code");
//      //Name of the file
//      String fileName="pm.txt";
//      try{
//
//         //Create object of FileReader
//         FileReader inputFile = new FileReader(fileName);
//
//         //Instantiate the BufferedReader Class
//         BufferedReader bufferReader = new BufferedReader(inputFile);
//
//         //Variable to hold the one line data
//         String line;
//         
//         int count = 0;
//         int moves = 0;
//         int pings = 0;
//
//         // Read file line by line and print on the console
//         while ((line = bufferReader.readLine()) != null)   {
//           if(line.contains("Total number of moves: ")) {
//        	   moves += Integer.parseInt(line.split(": ")[2]);
//        	   count++;
//           }
//        	   
//           if(line.contains("Total number of pings: ")) {
//        	   pings += Integer.parseInt(line.split(": ")[2]);
//           }
//
//        	   
//         }
//         
//         System.out.println("count: " + count);
//         System.out.println("avg moves: " + moves/count);
//         System.out.println("avg pings: " + pings/count);
//
//         //Close the buffer reader
//         bufferReader.close();
//      }catch(Exception e){
//         System.out.println("Error while reading file line by line:" + e.getMessage());                      
//      }
//
//    }
// }