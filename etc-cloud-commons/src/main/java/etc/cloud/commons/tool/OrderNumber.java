package etc.cloud.commons.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderNumber {
    private OrderNumber(){};

    private static SimpleDateFormat orderFormat=new SimpleDateFormat("yyyyMMddHHmmssSS");

    public static String CreateOrderNumber()
    {
        Integer number=100;
        number++;
        if(number>=1000)
        {
            number-=900;
        }
        return "HYWL"+orderFormat.format(new Date())+number;
    }
}
