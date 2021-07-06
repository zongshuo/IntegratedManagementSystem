package com.zongshuo.util;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-25
 * @Time: 13:35
 * @Description: 列表分页、排序、搜索通用接收参数封装
 */
@Data
@Slf4j
public class PageParam<T> extends Page<T> {
    private static final String FIELD_PAGE = "page";    //页码
    private static final String FIELD_LIMIT = "pageSize";  //每页记录数
    private static final String FIELD_SORT = "sort";    //排序字段参数名称
    private static final String FIELD_ORDER = "ORDER";  //排序方式
    private static final String VALUE_ORDER_ASC = "asc";    //升序排序
    private static final String VALUE_ORDER_DESC = "desc";    //降序排序
    private static final Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");   //驼峰转下划线正则匹配

    //搜索条件
    private transient Map<String, Object> params;

    //是否把字段名称从驼峰转下划线
    private boolean needToLine = true;

    public PageParam() {
        super();
    }

    public PageParam(HttpServletRequest request) {
        init(request);
    }

    public PageParam(HttpServletRequest request, boolean needToLine) {
        setNeedToLine(needToLine);
        init(request);
    }

    /**
     * 从request中获取参数并填充到pageParam中
     *
     * @param request
     * @return
     */
    private PageParam<T> init(HttpServletRequest request) {
        Enumeration<String> paramNames = request.getParameterNames();
        String name;
        String value;
        String sortValue = null ;
        String orderValue = null;
        Map<String, Object> tmpParams = new HashMap<>();
        while (paramNames.hasMoreElements()) {
            name = paramNames.nextElement();
            value = request.getParameter(name);
            if (StringUtils.isBlank(value)) {
                continue;
            }

            // TODO 根据页面传递参数格式，修改为支持多字段排序
            switch (name) {
                case FIELD_PAGE:
                    setCurrent(Long.parseLong(value));
                    break;
                case FIELD_LIMIT:
                    setSize(Long.parseLong(value));
                    break;
                case FIELD_SORT:
                    sortValue = (needToLine ? humpToLine(value) : value);
                    break;
                case FIELD_ORDER:
                    orderValue = value;
                    break;
                default:
                    tmpParams.put(name, value.trim());
            }

            // 同步排序方式到MyBatisPlus中
            if (StringUtils.isNotEmpty(sortValue)) {
                if (VALUE_ORDER_DESC.equals(orderValue)) {
                    addOrderDesc(sortValue);
                } else {
                    addOrderAsc(sortValue);
                }
            }
        }
        setParams(tmpParams);
        return this;
    }

    /**
     * 将驼峰格式转成蛇形格式
     *
     * @param str
     * @return
     */
    private String humpToLine(String str) {
        if (StringUtils.isBlank(str)) return "";

        Matcher matcher = HUMP_PATTERN.matcher(str);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * 蛇形格式转驼峰格式
     *
     * @param str
     * @return
     */
    public String lineToHump(String str) {
        if (StringUtils.isBlank(str)) return "";
        StringBuilder builder = new StringBuilder();
        String[] keys = str.split("_");
        builder.append(keys[0]);
        for (int i = 1; i < keys.length; i++) {
            if (keys[i].length() > 0) builder.append(keys[i].substring(0, 1).toUpperCase());
            if (keys[i].length() > 1) builder.append(keys[i].substring(1));
        }
        return builder.toString();
    }


    /**
     * 获取所有的升序字段
     *
     * @return
     */
    private List<String> getOrderAscList() {
        List<String> ascs = new ArrayList<>();
        List<OrderItem> items = getOrders();
        if (items != null) {
            for (OrderItem item : items) {
                if (item.isAsc()) ascs.add(item.getColumn());
            }
        }

        return ascs;
    }

    /**
     * 增加升序字段
     *
     * @return
     */
    public PageParam<T> addOrderAsc(String... ascArray) {
        if (ascArray != null) {
            List<String> ascList = getOrderAscList();
            List<OrderItem> addItems = new ArrayList<>(ascArray.length);
            for (String fieldName : ascArray) {
                if (ascList.contains(fieldName)) continue;

                OrderItem item = new OrderItem();
                item.setAsc(true);
                item.setColumn(fieldName);
                addItems.add(item);
            }

            if (getOrders() == null) {
                setOrders(addItems);
            } else {
                getOrders().addAll(addItems);
            }
        }
        return this;
    }

    /**
     * 获取所有倒叙字段
     *
     * @return
     */
    private List<String> getOrderDescList() {
        List<String> descList = new ArrayList<>();
        List<OrderItem> items = getOrders();
        if (items != null) {
            for (OrderItem item : items) {
                if (item.isAsc()) continue;
                descList.add(item.getColumn());
            }
        }

        return descList;
    }

    /**
     * 新增倒叙字段
     *
     * @param descArray
     * @return
     */
    public PageParam<T> addOrderDesc(String... descArray) {
        if (descArray != null) {
            List<String> descList = getOrderDescList();
            List<OrderItem> addItems = new ArrayList<>(descArray.length);
            for (String fieldName : descArray) {
                if (descList.contains(fieldName)) continue;

                OrderItem item = new OrderItem();
                item.setAsc(false);
                item.setColumn(fieldName);
                addItems.add(item);
            }

            if (getOrders() == null) {
                setOrders(addItems);
            } else {
                getOrders().addAll(addItems);
            }
        }

        return this;
    }


    /**
     * 移除排序字段
     *
     * @param fieldName
     * @param isAsc
     * @return
     */
    public PageParam<T> removeOrder(@NotNull String fieldName, Boolean isAsc) {
        List<OrderItem> items = getOrders();
        if (items != null) {
            Iterator<OrderItem> itemIterator = items.iterator();
            while (itemIterator.hasNext()) {
                OrderItem item = itemIterator.next();
                if ((isAsc == null || isAsc == item.isAsc()) && fieldName.equals(item.getColumn())) {
                    itemIterator.remove();
                }
            }
        }
        return this;
    }

    /**
     * 添加查询参数
     *
     * @param key
     * @param value
     * @return
     */
    public PageParam<T> put(String key, Object value) {
        if (params == null) params = new HashMap<>();
        params.put(key, value);
        return this;
    }

    public Object get(String key) {
        if (params == null) return null;
        return params.get(key);
    }

    /**
     * 获取对象属性值
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public Object getFieldValue(Object obj, String fieldName) {
        if (obj == null || StringUtils.isBlank(fieldName)) return null;

        try {
            Field field = obj.getClass().getField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error(e.getMessage());
        }

        return null;
    }
}
