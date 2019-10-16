# Holder
## 简介
* 使用搭积木的方式创建页面

![列表](http://pyol6csk7.bkt.clouddn.com/test.jpg?imageView2/1/w/360/h/650)

## 配置

```
    arguments {
        arg("package_name", "$包名")
    }
    
    dependencies{
        implementation 'com.cf:holder:0.0.16'
        kapt 'com.cf:holder-complier:0.0.2'
    }
```

## 例子

```
class TestListActivity : ListActivity<TestListDL>() {
    override fun layoutId(): Int = R.layout.test_list_activity

    override fun bindHolder(adapter: QuickAdapter) {
        adapter.addHolder(TestListHolderBuilder())
    }

}

@Holder
class TestListHolder @JvmOverloads constructor(parent: ViewGroup?, layoutId: Int = R.layout.item_test) : BaseHolder<String>(parent, layoutId) {
    override fun convert(data: String) {
        // 业务代码
    }
}

class TestListDL : BaseDataLoader() {
    override fun loadData(result: (List<*>) -> Unit, exception: (Exception) -> Unit) {
        val list = mutableListOf<Any>()
        result(list)
    }
}
```

### ListActivity
- 业务activity只需要继承ListActivity，实现layoutId和bindHolder。
    ```
    注意：基类中用recyclerView这个id获取RecyclerView,所以如果不重写getRecyclerView方法，
    需要在xml中定义id为recyclerView的RecyclerView
    ```

### BaseHolder
- 这个类把data-ui做一个键值对绑定，比如String-TestListHolder
- TestListHolder需要继承BaseHolder，标记@Holder注解，编辑器会自动生成TestListHolderBuilder
- 然后在bindHolder中把adapter和holder builder绑定在一起

    ```
        adapter.addHolder(TestListHolderBuilder())
    ```
### BaseDataLoader

- 加载数据类，只需要把列表ui对应的data编程list，用result回调回去，就能根据数据生成相应的页面

