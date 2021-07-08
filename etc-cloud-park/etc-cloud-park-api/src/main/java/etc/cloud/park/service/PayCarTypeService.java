package etc.cloud.park.service;

public interface PayCarTypeService {

    Object inquiryOfPayCarType(String data);
    Object insertPayCarType(String data,String token);
    Object alterPayCarType(String data,String token);
    Object deletePayCarType(String data,String token);
}
