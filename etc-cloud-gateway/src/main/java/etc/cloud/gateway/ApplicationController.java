package etc.cloud.gateway;

import etc.cloud.auth.mapper.WorkerMapper;
import etc.cloud.auth.mode.WorkerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xlm
 */
@RequestMapping("/app")
@RestController
public class ApplicationController {

    @Autowired
    private Environment environment;

    @Autowired
    private WorkerMapper workerMapper;

    @GetMapping("/test")
    public Object getxx(){

//        String xx = environment.getProperty("xx");
//        WorkerModel workerModel = workerMapper.selectById(1);
//
//        System.out.println(workerModel);
//        List<WorkerModel> workerModels = workerMapper.selectAll();
//        System.out.println(workerModels);
//
//        System.out.println(xx);
        return "xx";
    }
}
