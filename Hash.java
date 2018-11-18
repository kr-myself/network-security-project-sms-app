package hash;

public class Hash {
    public static void split(String hash_string){
        System.out.println(hash_string);
    }
    
    public static int fits(int a, int b){
        int value = 0;
        for(; a > 0; a -= b)
            value++;
 
        return value;
    }
    
    public static String to_hex(String m){
        char chars[] = m.toCharArray();
        StringBuilder r = new StringBuilder();
        for(char c: chars){
            r.append(Integer.toHexString((int)c));
        }
        return r.toString();
    }
    
    public static String to_string(String m){
        StringBuilder r = new StringBuilder("");
        for(int x = 0; x < m.length(); x += 2){
            String temp = m.substring(x, x+2);
            r.append((char) Integer.parseInt(temp, 16));
        }
        return r.toString();
    }
    
    public static String to_binary(String m){
        StringBuilder r = new StringBuilder("");
        for(int x = 0; x < m.length(); x += 2){
            String hex_chr = m.substring(x, x+2);
            int hex_value = (Integer.parseInt(hex_chr, 16));
            if (Integer.toBinaryString(hex_value).length() < 8){
             for (int y = 0; y < 8 - Integer.toBinaryString(hex_value).length(); y ++){
                 r.append("0");
             }   
            }
            r.append(Integer.toBinaryString(hex_value));
        }
        return r.toString();
    }
    
    public static String binary_to_hex(String m){
        StringBuilder r = new StringBuilder("");
         for(int x = 0; x < m.length(); x += 4){
            String temp_string = m.substring(x, x+4);
            int temp = Integer.parseInt(temp_string, 2);
            r.append(Integer.toString(temp, 16));
        }
        return r.toString();
    }
    
    public static String xor_result(String a, String b){
        StringBuilder r = new StringBuilder("");
        a = to_binary(a);
        b = to_binary(b);
        for(int x = 0; x < a.length(); x++){
            if(a.substring(x, x+1).equals(b.substring(x, x+1)))
                r.append("0");
            else
                r.append("1");
        }
        return binary_to_hex(r.toString());
    }
    
    public static String and_result(String a, String b){
        StringBuilder r = new StringBuilder("");
        a = to_binary(a);
        b = to_binary(b);
        for(int x = 0; x < a.length(); x++){
            if(a.substring(x, x+1).equals(b.substring(x, x+1)) && a.substring(x, x+1).equals("1"))
                r.append("1");
            else
                r.append("0");
        }
        return binary_to_hex(r.toString());
    }
    
    public static String hasher(String hash_string){
        String k[] = {"243F6A88", "85A308D3", "13198A2E", "03707344", "A4093822", "299F31D0", "082EFA98", "EC4E6C89"};
        while(hash_string.length() != 128){
            hash_string += " ";
        }
        int string_length = hash_string.length();
        int t = fits(string_length, 4);
        String w[] = new String[t];
        
        for(int x= 0; x < t; x++){
            int start = x * 4;
            int end = start + 4;
            if(end < string_length){
                w[x] = hash_string.substring(start, end);
            }
            else{
                w[x] = hash_string.substring(start, string_length);
                int temp = 4 - w[x].length();
                for(int y = 0; y < temp; y++)
                    w[x] += " ";
            }
            w[x] = to_hex(w[x]);
        }

        for(int x = 0; x < t; x++){
            int xx[] = new int[8];
            for(int y = 0; y < 8; y++){
                xx[y] = 7-y-x;
                while(xx[y] < 0){
                    xx[y] += 8;
                }
            }

            k[xx[0]] = FF(k[xx[0]], k[xx[1]], k[xx[2]], k[xx[3]], k[xx[4]], k[xx[5]], k[xx[6]], k[xx[7]], w[x]);
            //for(int y = 0; y < 8; y++){
            //    System.out.printf("xx[" + xx[y] + "] ");
            //}
            //System.out.println(x);
            //System.out.println(k[xx[0]]);
        }
        
        //Round 2
        String r = k[0];
        r += k[2];
        r+= k[3];
        r += k[4];
        return r;
    }
    
    public static String F(String A6, String A5, String A4, String A3, String A2, String A1, String A0){
        String temp1 = and_result(A1, A4);
        String temp2 = and_result(A2, A5);
        String temp3 = and_result(A3, A6);
        String temp4 = and_result(A0, A1);
        String r = xor_result(temp1, temp2);
        r = xor_result(r, temp3);
        r = xor_result(r, temp4);
        r = xor_result(r, A0);
        return r;
    }
    
    public static String G(String A6, String A5, String A4, String A3, String A2, String A1, String A0){
        String temp1 = and_result(A1, A2);
        temp1 = and_result(temp1, A3);
        String temp2 = and_result(A2, A4);
        temp2 = and_result(temp2, A5);
        String temp3 = and_result(A1, A2);
        String temp4 = and_result(A1, A4);
        String temp5 = and_result(A2, A6);
        String temp6 = and_result(A3, A5);
        String temp7 = and_result(A4, A5);
        String temp8 = and_result(A0, A2);
        String r = xor_result(temp1, temp2);
        r = xor_result(r, temp3);
        r = xor_result(r, temp4);
        r = xor_result(r, temp5);
        r = xor_result(r, temp6);
        r = xor_result(r, temp7);
        r = xor_result(r, temp8);
        r = xor_result(r, A0);
        return r;
    }
    
    public static String H(String A6, String A5, String A4, String A3, String A2, String A1, String A0){
        String temp1 = and_result(A1, A2);
        temp1 = and_result(temp1, A3);
        String temp2 = and_result(A1, A4);
        String temp3 = and_result(A2, A5);
        String temp4 = and_result(A3, A6);
        String temp5 = and_result(A0, A3);
        String r = xor_result(temp1, temp2);
        r = xor_result(r, temp3);
        r = xor_result(r, temp4);
        r = xor_result(r, temp5);
        r = xor_result(r, A0);
        return r;
    }
    
    public static String F_phi(String A6, String A5, String A4, String A3, String A2, String A1, String A0){
        return F(A1, A0, A3, A5, A6, A2, A4);
    }
    
    public static String G_phi(String A6, String A5, String A4, String A3, String A2, String A1, String A0){
        return F(A4, A2, A1, A0, A5, A3, A6);
    }
    
    public static String H_phi(String A6, String A5, String A4, String A3, String A2, String A1, String A0){
        return F(A6, A1, A2, A3, A4, A5, A0);
    }
    
    public static String special_shift(String a, int length){
        String r = a.substring(a.length() - length);
        r += a.substring(0, a.length() - length);
        return r;
    }
    
    public static String FF(String A7, String A6, String A5, String A4, String A3, String A2, String A1, String A0, String w){
        String r = "";
        String temp = F_phi(A6, A5, A4, A3, A2, A1, A0);
        String temp7 = special_shift(Long.toBinaryString(Long.parseLong(temp, 16)), 7);
        String temp11 = special_shift(Long.toBinaryString(Long.parseLong(A7, 16)), 11);
        
        long hh = Long.parseLong(temp7, 2) + Long.parseLong(temp11, 2) + Long.parseLong(w, 16);
        r = Long.toHexString(hh);
        if(r.length() > 8){
            r = r.substring(r.length() - 8);
        }

        return r;
    }
            //System.out.println("Temp: " + Long.toHexString(h) + " Shift: " + Long.toHexString(h>>>7));
        //System.out.println("A7: " + Long.toHexString(aa) + " Shift: " + Long.toHexString(aa>>>11));  //14d7ab0
        //System.out.println("W:  " + Long.toHexString(aaa) + " Shift: " + aaa);    //1d89cd
    public static String GG(String A7, String A6, String A5, String A4, String A3, String A2, String A1, String A0, String w, String c){
        String r = "";
        String temp = G_phi(A6, A5, A4, A3, A2, A1, A0);
        String temp7 = special_shift(Long.toBinaryString(Long.parseLong(temp, 16)), 7);
        String temp11 = special_shift(Long.toBinaryString(Long.parseLong(A7, 16)), 11);
        
        long hh = Long.parseLong(temp7, 2) + Long.parseLong(temp11, 2) + Long.parseLong(w, 16)  + Long.parseLong(c, 16);
        r = Long.toHexString(hh);
        if(r.length() > 8){
            r = r.substring(r.length() - 8);
        }
        
        return r;
    }
    
    public static String HH(String A7, String A6, String A5, String A4, String A3, String A2, String A1, String A0, String w, String c){
        String r = "";
        String temp = H_phi(A6, A5, A4, A3, A2, A1, A0);
        String temp7 = special_shift(Long.toBinaryString(Long.parseLong(temp, 16)), 7);
        String temp11 = special_shift(Long.toBinaryString(Long.parseLong(A7, 16)), 11);
        
        long hh = Long.parseLong(temp7, 2) + Long.parseLong(temp11, 2) + Long.parseLong(w, 16)  + Long.parseLong(c, 16);
        r = Long.toHexString(hh);
        if(r.length() > 8){
            r = r.substring(r.length() - 8);
        }
        
        return r;
    }
    
    public static void main(String[] args) {
        String message = "This is only message for testing how HAVAL algorithm work and show how the result were getting after hashing, this were made iam";
        System.out.println(hasher(message));

 
    }
    
}
