package com.eams.common.api;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 统一分页响应封装
 *
 * @param <T> 记录类型
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 当前页码（从 1 开始） */
    private long pageNum;

    /** 每页条数 */
    private long pageSize;

    /** 总记录数 */
    private long total;

    /** 总页数 */
    private long pages;

    /** 当前页数据 */
    private List<T> list;

    /**
     * 构建分页结果
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @param total    总记录数
     * @param list     当前页数据
     */
    public static <T> PageResult<T> of(long pageNum, long pageSize, long total, List<T> list) {
        PageResult<T> page = new PageResult<>();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setTotal(total);
        page.setPages(pageSize == 0 ? 0 : (total + pageSize - 1) / pageSize);
        page.setList(list == null ? Collections.emptyList() : list);
        return page;
    }

    /** 空分页结果 */
    public static <T> PageResult<T> empty(long pageNum, long pageSize) {
        return of(pageNum, pageSize, 0, Collections.emptyList());
    }
}
