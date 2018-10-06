package app.common.beetl;

import com.google.common.base.Throwables;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeetlTool {
    private static final Logger LOG = LoggerFactory.getLogger(BeetlTool.class);
    private static final GroupTemplate groupTemplate = initGroupTemplate();

    private static GroupTemplate initGroupTemplate() {
        try {
            StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
            Configuration cfg = Configuration.defaultConfiguration();
            cfg.setErrorHandlerClass("org.beetl.core.ReThrowConsoleErrorHandler");
            cfg.setStrict(false);
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
//            gt.registerTag("param", ParamTag.class);
//            gt.registerTag("where", WhereTag.class);
//            gt.registerTag("trim", TrimTag.class);
//            gt.registerTag("if", IfTag.class);
//            gt.registerTag("val", ValTag.class);
            return gt;
        } catch (Exception e) {
            LoggerFactory.getLogger(BeetlTool.class).error("构造Beetl表达式处理器失败", e);
            Throwables.propagate(e);
        }
        return null;
    }

    public static GroupTemplate getTemplate() {
        return groupTemplate;
    }
}
