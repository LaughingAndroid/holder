package com.cf.holder.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author helex
 * @version V5.7.1
 * @since 16/10/26
 * 管理不同请求(tag区分,如new、hot)的page和result
 */
public class ListDataImpl<R> implements ListData<R>, Serializable {
    private static final Object DEFAULT_TAG = "default_request_tag";

    @Override
    public void clearCache() {
        mDataCache.clear();
    }

    /**
     * 数据请求第一页
     */
    public static final int START = 1;

    /**
     * 默认分页page size
     */
    public static final int LIMIT = 15;

    /**
     * 当前请求页码
     */
    protected Map<Object, Integer> mPage = new Hashtable<>();
    /**
     * 记录上一次请求的页码
     */
    protected Map<Object, Integer> mLastPage = new Hashtable<>();
    /* {
         "tag1":{"1":R1,"2":R2}
         "tag2":{"1":R1,"2":R2}
     }*/
    protected Map<Object, TreeMap<Integer, Result<Object>>> mDataCache = new Hashtable<>();

    protected Map<Object, Object> mTagList = new Hashtable<>();

    private Object mTag = DEFAULT_TAG;

    public ListDataImpl() {
        setTag(mTag);
        setPage(getTag(), START);
    }


    @Override
    public ListDataImpl<R> setPage(int page) {
        mPage.put(getTag(), page);
        return this;
    }

    @Override
    public ListDataImpl<R> setPage(Object requestTag, int page) {
        mPage.put(requestTag, page);
        return this;
    }

    @Override
    public int getPage() {
        return getPage(getTag());
    }

    @Override
    public int getPage(Object requestTag) {
        if (null == requestTag) {
            return START;
        }
        Integer page = mPage.get(requestTag);
        return null == page ? START : page;
    }

    @Override
    public ListDataImpl<R> setLastPage(int lastPage) {
        mLastPage.put(getTag(), lastPage);
        return this;
    }

    @Override
    public ListDataImpl<R> setLastPage(Object requestTag, int lastPage) {
        mLastPage.put(requestTag, lastPage);
        return this;
    }

    public int getLastPage() {
        return getLastPage(getTag());
    }

    public int getLastPage(Object requestTag) {
        return null == mLastPage.get(requestTag) ? START : mLastPage.get(requestTag);
    }

    @Override
    public ListDataImpl<R> putCache(Object value) {
        putCache(getTag(), getPage(), value);
        return this;
    }

    @Override
    public ListDataImpl<R> putCache(Object tag, int page, Object value) {
        TreeMap<Integer, Result<Object>> tagMap = mDataCache.get(tag);
        if (null == tagMap) {
            tagMap = new TreeMap<>();
            mDataCache.put(tag, tagMap);
        }
        tagMap.put(page, new Result(value));
        return this;
    }

    @Override
    public <T> T getCache() {
        return getCache(getTag(), getPage());
    }

    @Override
    public <T> T getCache(Object tag, int page) {
        return getCache(tag, page, 0);
    }

    /**
     * @param tag
     * @param page
     * @param validTime 数据有效时间
     * @return T putCache设置的数据类型
     */
    @Override
    public <T> T getCache(Object tag, int page, long validTime) {
        TreeMap<Integer, Result<Object>> tagMap = mDataCache.get(tag);
        if (null != tagMap) {
            Result<Object> result = tagMap.get(page);
            if (result == null || result.isExpire(validTime) || result.getData() == null) {
                return null;
            } else {
                return (T) result.getData();
            }
        }
        return null;
    }

    @Override
    public List getData() {
        return getData(getTag());
    }

    @Override
    public List getData(Object requestTag) {
        TreeMap<Integer, Result<Object>> treeMap = mDataCache.get(requestTag);
        if (null != treeMap && !treeMap.isEmpty()) {
            List result = new ArrayList();
            Set<Integer> keys = treeMap.keySet();
            Iterator<Integer> keyIt = keys.iterator();
            while (keyIt.hasNext()) {
                Object temp = treeMap.get(keyIt.next()).getData();
                if (temp != null && temp instanceof List) {
                    result.addAll((List) temp);
                }
            }
            return result;
        }
        return null;
    }


    @Override
    public Object getTag() {
        return mTag;
    }

    @Override
    public ListDataImpl<R> setTag(Object tag) {
        if (null != tag) {
            mTag = tag;
            if (!mTagList.containsKey(tag)) {
                mTagList.put(tag, tag);
            }
        }
        return this;
    }

    @Override
    public boolean isFirst() {
        return START == getPage();
    }

    @Override
    public boolean isFirst(Object requestTag) {
        return START == getPage(requestTag);
    }

    @Override
    public ListDataImpl<R> resetPage() {
        setPage(START);
        setLastPage(START);
        mDataCache.remove(getTag());
        return this;
    }

    @Override
    public ListDataImpl<R> resetPage(Object requestTag) {
        setPage(requestTag, START);
        setLastPage(requestTag, START);
        mDataCache.remove(requestTag);
        return this;
    }

    @Override
    public ListDataImpl<R> recordLastPage() {
        setLastPage(getPage());
        return this;
    }

    @Override
    public ListDataImpl<R> recordLastPage(Object requestTag) {
        setLastPage(requestTag, getPage());
        return this;
    }

    /**
     * page++
     *
     * @return
     */
    @Override
    public ListDataImpl<R> pageAdd() {
        setPage(getPage() + 1);
        return this;
    }

    /**
     * page ++
     *
     * @param requestTag
     * @return
     */
    @Override
    public ListDataImpl<R> pageAdd(Object requestTag) {
        setPage(requestTag, getPage() + 1);
        return this;
    }

    /**
     * 重新回复上一次的page
     */
    @Override
    public void revokePage() {
        setPage(getLastPage());
    }

    /**
     * 重新回复上一次的page
     */
    @Override
    public void revokePage(Object requestTag) {
        setPage(requestTag, getLastPage(requestTag));
    }

    @Override
    public boolean isCurrentTag(Object tag) {
        return mTag.equals(tag);
    }
}
