package etc.cloud.auth.service;

public interface ILoginFilterService {
    Object userFilter(String token);
    Object loginFilter(String token);
}
