package etc.cloud.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import etc.cloud.auth.mode.WorkerModel;

public interface IWorkerService extends IService<WorkerModel> {
    Object findWorker(String data);

// //增加员工
// MsgResponse addWorker(WorkerModel wokerModel);
//
// //修改员工
// MsgResponse updateWorker(WorkerModel workerModel);
//
// //删除员工
// MsgResponse deleteWorker(WorkerModel workerModel);
//
// //查找员工
// MsgResponse findWorker(WorkerModel workerModel);
//
// //修改密码
// MsgResponse updatePassword(WorkerModel workerModel);
}
