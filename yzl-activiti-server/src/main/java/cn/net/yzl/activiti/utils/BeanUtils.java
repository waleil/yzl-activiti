//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.net.yzl.activiti.utils;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BeanUtils {
    private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);
    private static final Converter CONVERTER = CopyConverter.getInstance();

    public BeanUtils() {
    }

    public static Object copyProperties(Object source, Object target) {
        if (source != null && target != null) {
            BeanCopier beanCopier = BeanCopier.create(source.getClass(), target.getClass(), true);
            beanCopier.copy(source, target, CONVERTER);
            return target;
        } else {
            return null;
        }
    }

    public static <E, T> List<T> e2T(List<E> poList, Class<T> tClass) {
        if (CollectionUtils.isEmpty(poList)) {
            return new ArrayList<>();
        } else {
            String errorMsg = poList.get(0).getClass().getSimpleName() + " 转换 " + tClass.getSimpleName() + " 失败,{}";
            List<T> list = new ArrayList();
            Iterator var4 = poList.iterator();

            while(var4.hasNext()) {
                Object e = var4.next();

                try {
                    list.add((T) copyProperties(e, tClass.newInstance()));
                } catch (InstantiationException var7) {
                    log.error(errorMsg, var7);
                } catch (IllegalAccessException var8) {
                    log.error(errorMsg, var8);
                }
            }

            return list;
        }
    }
}
