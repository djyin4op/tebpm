Template Engine Benchmark Powered By Maven
===
+ 原工程
<pre>
http://git.oschina.net/yinjun622/teb
</pre>

+ 修改地方：
<div style="word-break:break-all;">
  使用Maven接管工程构建，管理
  使用commons-exec启动待测JVM，而不是直接使用创建批处理文件的方式， 适应linux系统
  删除了Wetter相关的部分， 和jop部分， 这些牵扯到原作者自己写的模版框架， 但是没有开源出来， 找不到源代码
  升级了部分模版的版本
  加入了Thymeleaf的测试, 性能确实有问题.
  加入了DEBUG运行模式, DEBUG模式是直接通过函数调用的方式, 读取classpath中的模版, 方便开发调试; 非DEBUG模式是启动独立的JVM,读取当前目录下的模版文件.
  修正了一些蛋疼问题, 比如beetl读取classpath的资源出错的问题...
</div>

+ 吐槽的地方：
<pre>
  httl, 支持本地目录的模版有问题, 不支持相对路径, 非要在路径前面增加一个'/'
  beelt, 支持classpath中的模版有问题, 无法读取jar中的模版.
</pre>
使用
===
###1.编译打包
+ 请使用JDK 1.7编译测试, 低于JDK1.7部分引擎可能会不能测试
+ 使用命令 mvn clean install  打包, 在target/kiang.teb-integrated可以找到可执行程序
+ 请不要将Velocity加入并发测试, 会因异常终止, Velocity的共享对象导致并发问题
###2.修改参数
+ teb.bat 或者 teb.sh
<pre>
设置JAVA_HOME
</pre>
+ teb.properties
<div style="word-break:break-all;">
thread    : 并发线程数, 最小设置1
record    : 渲染页面的模型记录数, 最小设置1
period    : 内存采样周期, 运行多少次采样一次, 内存取新生代之外的内存(新生代内存会被很快回收)
warmed    : 引擎预热次数, 一般编译型引擎在-server模式下需要10000次预热以上jvm才会优化执行, 该段采集IO,OUT信息, 最小设置1
looped    : 引擎测试次数, 该段对时间、内存采样并计算吞吐量, 最小值1
stream    : 输出流格式, 支持多输出流测试结果到同一测试报告, 字节流:byte, 字符流:char, 双流:all
source    : 引擎模板文件输入编码, 默认UTF-8
target    : 引擎模板文件输出编码, 默认UTF-8, 支持多输出流测试结果到同一测试报告, 使用半角分号分割, 如UTF-8;GBK
option    : JVM启动参数优化配置, 例如:-server -Xms512m -Xmx512m -XX:+UseConcMarkSweepGC
engine    : 引擎测试别名配置, 支持多个配置, 支持多个测试结果到同一测试报告 使用半角分号分割, 例如: jop;jsp;wet
xxx.name  : 引擎测试别名对应的引擎名称及版本号, xxx=jop/jsp/wet等engine中自定义的别名
xxx.site  : 引擎测试别名对应的引擎站点, xxx=jop/jsp/wet等engine中自定义的别名
xxx.test  : 引擎测试别名对应的引擎测试实现, xxx=jop/jsp/wet等engine中自定义的别名
</div>

###3.运行
+ 开发环境 下执行
<pre>
kiang.teb.Benchmark.main()
</pre>
+ Windows 下执行
<pre>
teb.bat
</pre>
+ Linux 下执行
<pre>
./teb.sh
</pre>

###4.结果
+ 打包后运行, 结果报告文件是
<pre>
RPT20.html
</pre>

+ 开发环境运行, 结果报告文件
<pre>
./target/classes/RPT20.html
</pre>

+ 测试结果信息
<pre>
结果采用纯HTML输出, 包含关键参数的作图和全部数据信息
TPS       : 引擎的吞吐量, 单位时间内引擎渲染次数, 单位: 次/秒
Time      : 引擎全部渲染的执行时间, 单位: 毫秒
OnceIo    : 引擎单次渲染的IO次数, 单位: 次
MassIo    : 引擎全部渲染的IO次数, 单位: 次
OnceOut   : 引擎单次渲染的输出字节(字符)数, 单位: 字节/字符
MassOut   : 引擎全部渲染的输出字节(字符)数, 单位: 字节/字符
PermMem   : 内存消耗, 内存取新生代之外的内存(新生代内存会被很快回收), 单位: 字节
</pre>
+ 测试环境信息
<pre>
TestDate  : 测试时间
JavaVM    : JVM版本及位数
Thread    : 并发线程数
Record    : 渲染页面的模型记录数
Period    : 内存采样周期
Warmed    : 引擎预热次数
Looped    : 引擎测试次数
Target    : 输出编码
Stream    : 输出流格式
</pre>

软件作者
===
<pre>
原软件作者：kiang
修改者：djyin@gmail.com
</pre>

特别感谢
===
<pre>
感谢sept,webit,jetbrick,beetl对TEB提供的建议
</pre>

许可证
===
<pre>
Template Engine Benchmark Test is released under the MIT License.
See the bundled LICENSE file for details.

Template Engine Benchmark 依据MIT许可证发布。
使用结果请包含TEB版权信息, 详细请看捆绑的LICENSE文件。
</pre>

