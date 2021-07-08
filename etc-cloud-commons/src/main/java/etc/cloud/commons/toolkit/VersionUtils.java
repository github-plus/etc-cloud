package etc.cloud.commons.toolkit;

import cn.hutool.core.util.ClassLoaderUtil;
import etc.cloud.commons.func.FuncWrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author lm_xu
 */
@Slf4j
public final class VersionUtils {

    private static final String VERSION_DEFAULT = "lambda-version";
    private static final String PROJECT_NAME = "lambda-name";
    private static final String FILE_NAME = "bootstrap.yml";
    private static final AtomicReference<Pair<String,String>> ATOMIC_REFERENCE = new AtomicReference();

    public static Pair<String, String> getProjectLogoInfo() {

        for (;;){
            Pair<String, String> pair = ATOMIC_REFERENCE.get();
            if(Objects.nonNull(pair)) return pair;
            try (InputStream in = ClassLoaderUtil.getClassLoader().getResourceAsStream(FILE_NAME)) {
                YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
                yaml.setResources(new InputStreamResource(Objects.requireNonNull(in)));
                Properties props = yaml.getObject();
                pair = FuncWrappers.wrapperPair(
                        () -> Objects.requireNonNull(props.getProperty(VERSION_DEFAULT), "version is null"),
                        () -> Objects.requireNonNull(props.getProperty(PROJECT_NAME), "projectName is null")
                );
                if(ATOMIC_REFERENCE.compareAndSet(null,pair)){
                    return pair;
                }
            } catch (IOException e) {
                log.error("version read error ", e);
            }
        }

    }


}
