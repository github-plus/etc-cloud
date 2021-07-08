package etc.cloud.auth.service;

import com.google.common.base.Preconditions;



public interface ILoginService {

//    Object loginHandler(int account, String passWord);
//
//    default void preCheckCondition(int account,String password){
//        Preconditions.checkNotNull(account,"username not null");
//        Preconditions.checkNotNull(password,"password not null");
//    }

        Object loginHandler(String workerName, String passWord);

    default void preCheckCondition(String workerName,String password){
    Preconditions.checkNotNull(workerName,"username not null");
    Preconditions.checkNotNull(password,"password not null");
}

Object loginOut(String data);

}
