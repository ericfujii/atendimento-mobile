package br.com.eric.atendimentomobile.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.eric.atendimentomobile.entidade.envio.MobileEnvio;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetorno;
import br.com.eric.atendimentomobile.utils.annotations.XmlDateFormat;
import br.com.eric.atendimentomobile.utils.annotations.XmlElement;
import br.com.eric.atendimentomobile.utils.annotations.XmlTransient;

/**
 * Created by eric on 09/02/15.
 */
public class JsonParser {

    public <T> T parseJsonToObject(JSONObject jsonObj, Class<T> tClass) throws JSONException, IllegalAccessException, InstantiationException, ParseException {
        Field[] fieldsOriginal = tClass.getDeclaredFields();

        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(fieldsOriginal));

        if (!tClass.getSuperclass().equals(Object.class) && tClass.getSuperclass().equals(MobileRetorno.class)) {
            Field[] fieldsSuperclass = tClass.getSuperclass().getDeclaredFields();
            fields.addAll(Arrays.asList(fieldsSuperclass));
        }

        T returnObject = tClass.newInstance();
        for (Field field : fields){
            XmlTransient xmlTransient = field.getAnnotation(XmlTransient.class);
            if (xmlTransient == null) {
                field.setAccessible(true);
                String xmlElementName = getXmlElementName(field);
                String jsonFieldValue = null;
                try {
                    jsonFieldValue = jsonObj.getString(xmlElementName);
                } catch (Exception e) {
                    jsonFieldValue = null;
                }
                if (jsonFieldValue == null || jsonFieldValue.equals("null")) {
                    field.set(returnObject, null);
                } else {
                    if (field.getType().equals(String.class)) {
                        field.set(returnObject, jsonFieldValue);
                    } else if (field.getType().equals(Integer.class)) {
                        field.set(returnObject, jsonObj.getInt(xmlElementName));
                    } else if (field.getType().equals(Long.class)) {
                        field.set(returnObject, jsonObj.getLong(xmlElementName));
                    } else if (field.getType().equals(Double.class)) {
                        field.set(returnObject, jsonObj.getDouble(xmlElementName));
                    } else if (field.getType().equals(BigDecimal.class)) {
                        field.set(returnObject, new BigDecimal(jsonObj.getString(xmlElementName)));
                    } else if (field.getType().equals(Float.class)) {
                        field.set(returnObject, Double.valueOf(jsonObj.getDouble(xmlElementName)).floatValue());
                    } else if (field.getType().equals(Boolean.class)) {
                        field.set(returnObject, jsonObj.getBoolean(xmlElementName));
                    } else if (field.getType().equals(Calendar.class)) {
                        XmlDateFormat xmlDateFormat = field.getAnnotation(XmlDateFormat.class);
                        Calendar calendar = Calendar.getInstance();
                        if (xmlDateFormat != null) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(xmlDateFormat.format());
                            calendar.setTime(simpleDateFormat.parse(jsonFieldValue));
                            field.set(returnObject, calendar);
                        } else {
                            calendar.setTimeInMillis(jsonObj.getLong(xmlElementName));
                            field.set(returnObject, calendar);
                        }
                    } else if (field.getType().isEnum()) {
                        field.set(returnObject, getEnum(field.getType(), jsonFieldValue));
                    } else if (field.getType().equals(List.class)) {
                        ParameterizedType objectsType = (ParameterizedType) field.getGenericType();
                        Class<?> objectsClass = (Class<?>) objectsType.getActualTypeArguments()[0];
                        field.set(returnObject, parseList(objectsClass, jsonObj.getJSONArray(xmlElementName)));
                    } else if (field.getType().equals(Map.class)) {
                        HashMap<String, Object> map = new HashMap<>();
                        JSONObject jObject = new JSONObject(jsonFieldValue);
                        Iterator<?> keys = jObject.keys();

                        while( keys.hasNext() ) {
                            String key = (String)keys.next();
                            Object value = jObject.get(key);
                            map.put(key, value);
                        }
                        field.set(returnObject, map);
                    } else {
                        field.set(returnObject, parseJsonToObject(jsonObj.getJSONObject(xmlElementName), field.getType()));
                    }
                }
            }
        }
        return returnObject;
    }

    private <T> List<T> parseList (Class<T> objectsClass, JSONArray list) throws JSONException, InstantiationException, IllegalAccessException, ParseException {
        List<T> returnList = new ArrayList<>();
        for(int i = 0; i < list.length(); i++){
            if (objectsClass.equals(String.class)) {
                returnList.add((T) list.getString(i));
            } else if (objectsClass.equals(Integer.class)) {
                returnList.add((T) new Integer(list.getInt(i)));
            } else if (objectsClass.equals(Long.class)) {
                returnList.add((T) new Long(list.getLong(i)));
            } else if (objectsClass.equals(Double.class)) {
                returnList.add((T) new Double(list.getDouble(i)));
            } else if (objectsClass.equals(Float.class)) {
                returnList.add((T) new Float(list.getDouble(i)));
            } else if (objectsClass.equals(Calendar.class)) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse(list.getString(i)));
                returnList.add((T) calendar);
            } else if (objectsClass.equals(Boolean.class)) {
                returnList.add((T) new Boolean(list.getBoolean(i)));
            } else {
                returnList.add(parseJsonToObject(list.getJSONObject(i), objectsClass));
            }
        }
        return returnList;
    }

    private String getXmlElementName(Field field){
        field.setAccessible(true);
        String xmlElementName;
        XmlElement xmlElement = field.getAnnotation(XmlElement.class);
        if (xmlElement != null) {
            xmlElementName = xmlElement.name();
        } else {
            xmlElementName = field.getName();
        }
        return xmlElementName;
    }

    private <T> T getEnum(Class<T> enumType, String value) {
        for (T c : enumType.getEnumConstants()) {
            if (c.toString().equals(value)) {
                return c;
            }
        }
        return null;
    }

    public <T> JSONObject parseObjectToJson(T object) throws IllegalAccessException, JSONException {
        JSONObject jsonObject = new JSONObject();
        Field[] fieldsOriginal = object.getClass().getDeclaredFields();

        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(fieldsOriginal));

        if (!object.getClass().getSuperclass().equals(Object.class) && object.getClass().getSuperclass().equals(MobileEnvio.class)) {
            Field[] fieldsSuperclass = object.getClass().getSuperclass().getDeclaredFields();
            fields.addAll(Arrays.asList(fieldsSuperclass));
        }

        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(object);
            if (fieldValue != null) {
                XmlTransient xmlTransient = field.getAnnotation(XmlTransient.class);
                if (xmlTransient == null) {
                    String xmlElementName = getXmlElementName(field);
                    if (field.getType().equals(String.class)) {
                        jsonObject.put(xmlElementName, (String) fieldValue);
                    } else if (field.getType().equals(Integer.class)) {
                        jsonObject.put(xmlElementName, (Integer) fieldValue);
                    } else if (field.getType().equals(Long.class)) {
                        jsonObject.put(xmlElementName, (Long) fieldValue);
                    } else if (field.getType().equals(Double.class)) {
                        jsonObject.put(xmlElementName, (Double) fieldValue);
                    } else if (field.getType().equals(BigDecimal.class)) {
                        jsonObject.put(xmlElementName, fieldValue.toString());
                    } else if (field.getType().equals(Float.class)) {
                        jsonObject.put(xmlElementName, (Float) fieldValue);
                    } else if (field.getType().equals(Boolean.class)) {
                        jsonObject.put(xmlElementName, (Boolean) fieldValue);
                    } else if (field.getType().equals(Calendar.class)) {
                        XmlDateFormat xmlDateFormat = field.getAnnotation(XmlDateFormat.class);
                        if (xmlDateFormat != null) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(xmlDateFormat.format());
                            jsonObject.put(xmlElementName, simpleDateFormat.format(((Calendar) fieldValue).getTime()));
                        } else {
                            jsonObject.put(xmlElementName, ((Calendar) fieldValue).getTimeInMillis());
                        }
                    } else if (field.getType().isEnum()) {
                        jsonObject.put(xmlElementName, fieldValue.toString());
                    } else if (field.getType().equals(List.class)) {
                        jsonObject.put(xmlElementName, parseListToJson((List<?>)fieldValue));
                    } else {
                        jsonObject.put(xmlElementName, parseObjectToJson(fieldValue));
                    }
                }
            }
        }

        String teste = jsonObject.toString();
        return jsonObject;
    }

    private <T> JSONArray parseListToJson(List<T> list) throws JSONException, IllegalAccessException {
        JSONArray jsonArray = new JSONArray();
        for (T object : list) {
            if (object.getClass().equals(String.class)) {
                jsonArray.put((String) object);
            } else if (object.getClass().equals(Integer.class)) {
                jsonArray.put((Integer) object);
            } else if (object.getClass().equals(Long.class)) {
                jsonArray.put((Long) object);
            } else if (object.getClass().equals(Double.class)) {
                jsonArray.put((Double) object);
            } else if (object.getClass().equals(BigDecimal.class)) {
                jsonArray.put(object.toString());
            } else if (object.getClass().equals(Float.class)) {
                jsonArray.put((Float) object);
            } else if (object.getClass().equals(Boolean.class)) {
                jsonArray.put((Boolean) object);
            } else if (object.getClass().equals(Calendar.class)) {
                jsonArray.put(((Calendar) object).getTimeInMillis());
            } else if (object.getClass().isEnum()) {
                jsonArray.put(object.toString());
            } else if (object.getClass().equals(List.class)) {
                jsonArray.put(parseListToJson((List<?>)object));
            } else {
                jsonArray.put(parseObjectToJson(object));
            }
        }
        return jsonArray;
    }
}
