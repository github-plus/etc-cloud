package etc.cloud.park.service;

public interface TempPayService {
    public Object SelectTempPay(String data);

    public Object SelectTempTotal(String data);

    public Object SelectOfflinePay(String data);

    public Object SelectOnlinePay(String data);
}
