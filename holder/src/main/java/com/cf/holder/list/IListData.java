package com.cf.holder.list;

import java.util.List;

/**
 * @author helex
 * @version V1.0
 * @since 18/7/7
 * <p>
 *
 *
 *
 */
public interface IListData<R>  {

    void clearCache();

    public class Result<T> {
        T data;
        /**
         * 有效时间,默认一个小时
         */
        long valid_time = 1000 * 60 * 60;
        /**
         * 创建时间
         */
        long create_time;

        public Result(T data) {
            this.data = data;
            create_time = System.currentTimeMillis();
        }

        public boolean isExpire(long expire) {
            if (0 == expire) {
                expire = create_time;
            }
            return (System.currentTimeMillis() - create_time) > expire;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public long getValid_time() {
            return valid_time;
        }

        public void setValid_time(long valid_time) {
            this.valid_time = valid_time;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }
    }

    /**
     * 数据请求第一页
     */
    int START = 1;

    /**
     * 默认分页page size
     */
    int LIMIT = 10;

    IListData<R> setPage(int page);

    IListData<R> setPage(Object requestTag, int page);

    int getPage();

    int getPage(Object requestTag);

    IListData<R> setLastPage(int lastPage);

    IListData<R> setLastPage(Object requestTag, int lastPage);

    int getLastPage();

    int getLastPage(Object requestTag);

    IListData<R> putCache(Object value);

    IListData<R> putCache(Object tag, int page, Object value);

    <T> T getCache();

    <T> T getCache(Object tag, int page);

    /**
     * @param tag
     * @param page
     * @param validTime 数据有效时间
     * @return T putCache设置的数据类型
     */
    <T> T getCache(Object tag, int page, long validTime);

    List getData();

    List getData(Object requestTag);


    Object getTag();

    IListData<R> setTag(Object tag);

    boolean isFirst();

    boolean isFirst(Object requestTag);

    IListData<R> resetPage();

    IListData<R> resetPage(Object requestTag);

    IListData<R> recordLastPage();

    IListData<R> recordLastPage(Object requestTag);

    /**
     * page++
     *
     * @return
     */
    IListData<R> pageAdd();

    /**
     * page ++
     *
     * @param requestTag
     * @return
     */
    IListData<R> pageAdd(Object requestTag);

    /**
     * 重新回复上一次的page
     */
    void revokePage();

    /**
     * 重新回复上一次的page
     */
    void revokePage(Object requestTag);

    boolean isCurrentTag(Object tag);
}
