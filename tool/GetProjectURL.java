import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class GetAllURLTest {
    public static GetAllURLTest anno = null;

    public static GetAllURLTest getInstance() {
        if (anno == null) {
            anno = new GetAllURLTest();
        }
        return anno;
    }

    @SuppressWarnings("all")
    public void loadVlaue(Class annotationClasss, String annotationField, String className, LinkedList<UrlDTO> list)
            throws Exception {
        Annotation an = Class.forName(className).getAnnotation(annotationClasss);

        String prename = "";
        if (an != null) {
            Method a = an.getClass().getDeclaredMethod(annotationField, null);
            String[] avalues = (String[]) a.invoke(an, null);

            prename = avalues[0];
        }

        Method[] methods = Class.forName(className).getDeclaredMethods();
        if (methods == null || methods.length == 0) {
            return;
        }
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotationClasss)) {
                Annotation p = method.getAnnotation(annotationClasss);
                Method m = p.getClass().getDeclaredMethod(annotationField, null);

                String url = "";
                String param = "{}";
                String[] values = (String[]) m.invoke(p, null);
                if (values != null && values.length > 0) {
                    url = prename + values[0] + ".json";
                    Class<?>[] ts = method.getParameterTypes();
                    if (ts != null && ts.length > 0) {
                        Object paramObject = ts[0].newInstance();
                        setNullProperties(paramObject);
                        param = JsonUtils.tojson(paramObject);
                    }
                    if (StringUtils.isBlank(param)) {
                        param = "{}";
                    }

                    UrlDTO urlDTO = new UrlDTO();
                    urlDTO.setParam(param);
                    urlDTO.setUrl(url);
                    list.add(urlDTO);
                }
            }
        }
    }

    public void listFile(String prepath, String path, LinkedList<UrlDTO> list) throws Exception {
        File file = new File(path);
        File[] f = file.listFiles();

        for (int i = 0; i < f.length; i++) {
            if (f[i].isDirectory()) {
                this.listFile(prepath, f[i].getPath(), list);
            } else {
                if (f[i].getName().indexOf("Controller") != -1) {
                    this.loadVlaue(RequestMapping.class, "value",
                            f[i].getPath().replaceAll("\\\\", "/").replaceFirst(prepath, "").replaceAll("/", ".")
                                    .replaceAll(".java", ""), list);
                }
            }
        }

    }

    /**
     * 递归设置null字段为""
     * 
     * @param entity
     */
    public static void setNullProperties(Object entity) {
        if (entity == null) {
            return;
        }
        BeanMap fieldMap = BeanMap.create(entity);
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Object properties = fieldMap.get(fieldName);
            if (isBaseObject(field.getType().getClass())) {
                if (field.getType().equals(String.class)) {
                    if (properties == null) {
                        fieldMap.put(fieldName, "");
                    }
                } else if (field.getType().equals(Integer.class)) {
                    if (properties == null) {
                        fieldMap.put(fieldName, 0);
                    }
                } else if (field.getType().equals(Long.class)) {
                    if (properties == null) {
                        fieldMap.put(fieldName, 0L);
                    }
                }
            }
            // 当时List容器时
            else if (field.getType().equals(List.class)) {
                if (properties != null) {
                    for (Object obj : (List<?>) properties) {
                        setNullProperties(obj);
                    }
                } else {
                    fieldMap.put(fieldName, new ArrayList<Object>());
                }
            } else if (properties != null) {
                setNullProperties(properties);
            }
        }
    }

    /**
     * 是否为Class对象
     * 
     * @param object
     * @return
     */
    public static boolean isBaseObject(Class<?> cla) {
        if (cla.equals(String.class) || cla.equals(Integer.class) || cla.equals(Long.class) || cla.equals(Float.class)
                || cla.equals(Double.class) || cla.equals(Byte.class) || cla.equals(Short.class)) {
            return true;
        }
        return false;
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String prepath = GetAllURLTest.getInstance().getClass().getClassLoader().getResource("").getPath()
                .replace("target/test-classes/", "")
                + "src/main/java/";
        prepath = prepath.startsWith("/") ? prepath.substring(1) : prepath;
        LinkedList<UrlDTO> list = new LinkedList<UrlDTO>();
        GetAllURLTest.getInstance().listFile(prepath, prepath + "com/nazir/controllers", list);
        System.out.println("{\"urlVersion\":1.0,\"urls\":" + JsonUtils.tojson(list) + "}");
    }
}