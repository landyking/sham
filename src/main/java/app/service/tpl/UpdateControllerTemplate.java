package app.service.tpl;

import app.common.JdbcTool;
import app.service.AbstractTemplate;
import org.beetl.sql.core.kit.StringKit;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by landy on 2018/10/6.
 */
@Service
public class UpdateControllerTemplate extends AbstractTemplate {
    @Override
    protected String gen(JdbcTool.TableStruct model, Integer prefixLength) throws IOException {
        return renderBeetl(model, new ClassPathResource("/btl/updateController.btl"), prefixLength);
    }

    @Override
    protected String name() {
        return "UpdateController";
    }

    @Override
    public String genFileName(String dataSource, String catalog, String schema, String table) {
        String lowerName = StringKit.deCodeUnderlined(table.toLowerCase());
        String upperName = StringKit.toUpperCaseFirstOne(lowerName);
        return lowerName + "/Update" + upperName + "Controller.java";
    }
}
