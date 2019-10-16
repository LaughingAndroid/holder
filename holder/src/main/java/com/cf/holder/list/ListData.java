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
public interface ListData<R>  {

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

    ListData<R> setPage(int page);

    ListData<R> setPage(Object requestTag, int page);

    int getPage();

    int getPage(Object requestTag);

    ListData<R> setLastPage(int lastPage);

    ListData<R> setLastPage(Object requestTag, int lastPage);

    int getLastPage();

    int getLastPage(Object requestTag);

    ListData<R> putCache(Object value);

    ListData<R> putCache(Object tag, int page, Object value);

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

    ListData<R> setTag(Object tag);

    boolean isFirst();

    boolean isFirst(Object requestTag);

    ListData<R> resetPage();

    ListData<R> resetPage(Object requestTag);

    ListData<R> recordLastPage();

    ListData<R> recordLastPage(Object requestTag);

    /**
     * page++
     *
     * @return
     */
    ListData<R> pageAdd();

    /**
     * page ++
     *
     * @param requestTag
     * @return
     */
    ListData<R> pageAdd(Object requestTag);

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
