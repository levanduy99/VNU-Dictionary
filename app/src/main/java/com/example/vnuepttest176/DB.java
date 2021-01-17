package com.example.vnuepttest176;

public class DB {
    public static String[] getData(int id){
        if(id == R.id.action_eng_vn){
            return getEngVn();
        }
        else if(id == R.id.action_vn_eng){
            return getVnEng();
        }
        return new String[0];
    }
    public static String[] getEngVn(){
        String[] source = new String[]{
                "January"
                ,"February"
                ,"March"
                ,"April"
                ,"May"
                ,"June"
                ,"July"
                ,"August"
                ,"September"
                ,"October"
                ,"November"
                ,"December"
                ,"Monday"
                ,"Tuesday"
                ,"Wednesday"
                ,"Thursday"
                ,"Friday"
                ,"Saturday"
                ,"Sunday"
                ,"first"
                ,"second"
                ,"third"
        };
        return source;
    }
    public static String[] getVnEng(){
        String[] source = new String[]{
                "Ba"
                ,"Má"
                ,"Mẹ"
                ,"Ông"
                ,"Bà"
                ,"Cha"
                ,"Cô"
                ,"Chú"
                ,"Gì"
                ,"O"
                ,"Út"
                ,"Tèo"
                ,"Tí"
                ,"Dần"
                ,"Mẹo"
                ,"Đông"
                ,"Hạ"
                ,"Thu"
                ,"Xuân"
                ,"Hè"
                ,"Tới"
                ,"Rồi"
        };
        return source;
    }
}
