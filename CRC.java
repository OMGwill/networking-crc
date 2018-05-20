import java.io.*;
import java.util.Scanner;

/**
 Will Luttmann
 */
public class CRC
{
    //converts line to binary string
   public static String stringToBinary(String s){
        int[] word = new int[50];
        String binaryWord;
        String binaryString = "";
        
        for(int i= 0; i < s.length(); i++){
        
        
            word[i] = (int)s.charAt(i);
            binaryWord = Integer.toBinaryString(word[i]);
            
            //appends 0 to front of binary num if less than 8 char
            while(binaryWord.length() < 8){
                binaryWord = "0" + binaryWord;
            }
            
            binaryString += binaryWord;
        
        }
        
        return binaryString;
   }
    
   //convert binary to hex
   public static String binaryToHex(String s){
       s = Integer.toHexString(Integer.parseInt(s,2));
           
       //pad 0's on left 
       while(s.length() < 4){
            s = "0" + s;
        }
            
       return s;
    }
    
    //convert hex to binary
    public static String hexToBinary(String s){
        int convert;
        
        convert = Integer.parseInt(s,16);
        s = Integer.toBinaryString(convert);
            
            
        //pad 0's on left
        while(s.length() < 16){
            s = "0" + s;
        }
        
        return s;
    }
   
    
   //compute crc given input in binary and place to start and end to xor with dividend
   public static String computeCRC(String inputWord, String div, int start, int end){
       String temp;
       String remainder = "";
       int xor;
       
        //gets first 17 chars
        temp = inputWord.substring(start,end);
        
        //moves to the first '1'
        if(temp.charAt(start) == '0'){
            start++;
            end++;
            temp = inputWord.substring(start,end);
        }
        
        
        
        //finds CRC for entire binary string
        while(end < inputWord.length()){
            
            
            //gets rid of preceding 0's
            while(temp.charAt(0) == '0'){ 
                if(temp.length() == 1){
                    break;
                 }
                temp = temp.substring(1);
                
            }
            
            //append char to make temp = 17 char
            while(temp.length() < 17){
                temp += Character.toString(inputWord.charAt(end));
                
                end++;
                
                //end if at end of binary string
                if (end >= inputWord.length()) {
                    break;
                }
                
                
            }
            
            //if temp is less than 17 char then END
            if(temp.length() != 17){
                break;
            }
            

            
            
            //xor temp and dividend, store in remainder
            for(int i = 0; i < temp.length(); i ++){
                xor = (int)temp.charAt(i) ^ (int)div.charAt(i);
                remainder += Integer.toString(xor);
            }
            
            temp = remainder;
            remainder = "";
            

        }
        
                     //append 0's if remainder is not 16 char
            while(temp.length() < 16){
                temp = "0" + temp;
            }
        return temp;
    
   }
    
   public static void main()throws FileNotFoundException{
       String line;
       String binaryString = "";
       String div = "10001000000100001";
       String append = "0000000000000000";
       String remainder = "";
       String[] CRC = new String [10];
       String[] CRCCHECK = new String [10];
       int j = 0;
       
       
       Scanner in = new Scanner(new FileReader("crctest.txt"));
       
       System.out.println("Program Output:\n~Line read in file~\n~Line in binary string~\n~CRC value in hex~");
       System.out.println();
       System.out.println();
       
       //iterates through whole file
       while(in.hasNextLine()){
           //initialize binaryString to null
           binaryString = "";
           // reads line
           line = in.nextLine();
           System.out.println(line);
           
           //converts line to binary string
           binaryString = stringToBinary(line);
           
            //binary string plus dividend-1 0's
            binaryString = binaryString + append;
            System.out.println(binaryString);
            

            
            remainder = computeCRC(binaryString, div, 0, div.length());

            //convert remainder to hex and store hex value in CRC[]
            CRC[j] = binaryToHex(remainder);
            System.out.println("CRC = " + CRC[j]);
            System.out.println();
            j++;
        }

        System.out.println();
        System.out.println();
       
        //prints all CRC
       for(int i = 0; i < CRC.length; i++){
           System.out.println("CRC[" + i + "] = " + CRC[i]);
        }
      
       
        /////////////////////////////////////////////////////////////////////////
        //error check
        System.out.println();
        System.out.println();
        System.out.println("**************CRC ERROR CHECK***************");
       //reset j to 0 to iterate through CRC
       j = 0;
       

       Scanner in2 = new Scanner(new FileReader("crctest.txt"));
       
       //iterates through whole file
       while(in2.hasNextLine()){
           //initialize binaryString to null
           binaryString = "";
           
           // reads line
           line = in2.nextLine();
           System.out.println(line);
           
           //converts line to binary string
           binaryString = stringToBinary(line);
           
            //convert CRC to binary
            append = hexToBinary(CRC[j]);

            //binary string plus CRC in binary
            binaryString = binaryString + append;
            System.out.println(binaryString);

            
            remainder = computeCRC(binaryString, div, 0, div.length());
            
            //store hex value in CRC[]
            CRCCHECK[j] = binaryToHex(remainder);
            //pad 0's on left
            while(CRCCHECK[j].length() < 4){
                CRCCHECK[j] = "0" + CRCCHECK[j];
            }
            System.out.println("CRCCHECK = "+ CRCCHECK[j]);
            System.out.println();
            j++;
        }
       

        System.out.println();
        System.out.println();       
        //prints all CRCCHECK
       for(int i = 0; i < CRCCHECK.length; i++){
           System.out.println("CRCCHECK[" + i + "] = " + CRCCHECK[i]);
        }
    }
}
