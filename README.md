Template Engine Benchmark POWED BY MAVEN
===
<pre>
作者决定废弃原EBM测试工具，重新设计了TEB，功能和准确度都较EBM有了提高；

目前网络上的Java模板引擎测试基本上都是非独立JVM测试的，
这样做后测试的引擎性能会较高，与实际性能相比有较大偏差，
因此本测试对每个引擎都使用独立JVM测试，保证了各个引擎间环境的公平性；
</pre>

使用
===
###1.编译
+ 请使用JDK 1.7编译测试, 低于JDK1.7部分引擎可能会不能测试
+ 请不要将Velocity加入并发测试, 会因异常终止, Velocity的共享对象导致并发问题

###2.修改参数
+ /target/classes/teb.bat
<pre>
JAVA_HOME : @set JAVA_HOME=D:\UserWork\JavaSDK\jdk7u55x64 设置JAVA_HOME, 如使用系统环境变量请使用rem注释掉
Libraries : @set Libraries=D:\UserWork\JavaSpace\discuss\teb\lib 设置运行时需要的类库路径, 必填
</pre>
+ /target/classes/teb.properties
<pre>
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
</pre>

###3.运行
+ Windows 下执行
<pre>
/target/classes/teb.bat
</pre>
+ Linux暂未支持

###4.结果
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
软件作者：kiang
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

许可证
===
修改地方：
  1. 使用Maven接管工程构建，管理
  2. 使用Common-Exec启动待测JVM，而不是直接使用创建批处理文件的方式， 已适应linux系统调用
  3. 删除了Wetter相关的部分， 这个应该是作者自己写的一个模版框架， 但是没有开源出来
  4. 升级了部分模版的版本