package cn.net.yzl.activiti.utils;

import net.sf.cglib.core.Converter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CopyConverter implements Converter {


    private CopyConverter() {
    }

    public static final CopyConverter getInstance() {
        return CopyConverter.SingletonHolder.INSTANCE;
    }

    public Object convert(Object value, Class target, Object context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (value != null) {
            String s = value.toString();
            if (value instanceof BigDecimal && target.equals(Integer.class)) {
                return ((BigDecimal)value).intValue();
            } else if (target.equals(Integer.TYPE) || target.equals(Integer.class)) {
                return Integer.parseInt(s);
            } else if (!target.equals(Long.TYPE) && !target.equals(Long.class)) {
                if (!target.equals(Float.TYPE) && !target.equals(Float.class)) {
                    if (target.equals(Double.TYPE) || target.equals(Double.class)) {
                        return Double.parseDouble(s);
                    } else if (target.equals(Byte.TYPE) || target.equals(Byte.class)) {
                        return Byte.parseByte(s);
                    } else {
                        String nan;
                        if (value instanceof String && target.equals(Date.class)) {
                            for(nan = "-"; s.indexOf(nan) > 0; s = s.replace("-", "/")) {
                            }

                            return new Date(s);
                        } else {
                            if (target.equals(BigDecimal.class)) {
                                nan = "NaN";
                                if (StringUtils.isNotBlank(s) && !nan.equals(s)) {
                                    return new BigDecimal(s);
                                }
                            } else {
                                if (value instanceof Date && target.equals(String.class)) {
                                    return sdf.format(value);
                                }

                                if (value instanceof Integer && target.equals(String.class)) {
                                    return sdf.format(new Date(Long.valueOf(s) * 1000L));
                                }

                                if (target.equals(String.class) || target.equals(String.class)) {
                                    return s;
                                }
                            }

                            return value;
                        }
                    }
                } else {
                    return Float.parseFloat(s);
                }
            } else {
                return Long.parseLong(s);
            }
        } else {
            return value;
        }
    }

    private static class SingletonHolder {
        private static final CopyConverter INSTANCE = new CopyConverter();

        private SingletonHolder() {
        }
    }
}
