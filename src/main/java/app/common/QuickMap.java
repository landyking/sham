package app.common;

import org.springframework.util.Assert;

import java.util.HashMap;

/**
 * Created by landy on 2018/9/20.
 */
public class QuickMap<T> extends HashMap<T, Object> {
    public static <T> QuickMap of(T key, Object val, Object... other) {
        QuickMap<T> tmp = new QuickMap<T>();
        tmp.put(key, val);
        if (other != null && other.length > 0) {
            Assert.isTrue(other.length % 2 == 0, "参数长度应该是偶数");
            for (int i = 0; i < other.length; i += 2) {
                T k = (T) other[i];
                Object v = other[i + 1];
                Assert.notNull(k, "key不能为空:" + (i + 3));
                tmp.put(k, v);
            }
        }
        return tmp;
    }

    @Override
    public QuickMap put(T key, Object value) {
        super.put(key, value);
        return this;
    }

    public static QuickMap<String> strMap() {
        return new QuickMap<String>();
    }
}
