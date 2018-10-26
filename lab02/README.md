实验二、Prometheus+Grafana展示实验
======

### 实验步骤

#### 1. 先决条件

* 运行Prometheus HTTP Metrics Simulator
* 运行Prometheus服务器

#### 2. 安装运行Grafana

下载[Grafana 5.3.2 for Windows](https://s3-us-west-2.amazonaws.com/grafana-releases/release/grafana-5.3.2.windows-amd64.zip)，并解压到本地目录。

运行：
```
./bin/grafana-server.exe
```

访问Granfa UI，使用缺省账号`admin/admin`登录
```
http://localhost:3000
```

添加Proemethes数据源

* Name -> prom-datasource
* Type -> Prometheus
* HTTP URL -> http://localhost:9090

其它缺省即可

**Save & Test**确保连接成功


#### 3. 创建一个Dashboard

点击**+**图标创建一个Dashbaord，点击**保存**图标保存Dashboard，使用缺省Folder，给Dashboard起名为`prom-demo`。

#### 4. 展示请求率

点击**Add panel**图标，点击**Graph**图标添加一个Graph，

点击Graph上的*Panel Title*->*Edit*进行编辑

修改Title：*General* -> Title = Request Rate

设置*Metrics*
```
sum(rate(http_requests_total{job="http-simulator"}[5m]))
``` 

调整*Lagend*

* 以表格展示*As Table*
* 显示*Min/Max/Avg/Current/Total*
* 根据需要调整*Axis*

注意保存Dahsboard。

#### 5. 展示实时错误率

点击**Add panel**图标，点击**Singlestat**图标添加一个Singlestat，

点击Graph上的*Panel Title->Edit*进行编辑

修改Title：*General* -> Title = Live Error Rate

设置*Metrics*
```
sum(rate(http_requests_total{job="http-simulator", status="500"}[5m])) / sum(rate(http_requests_total{job="http-simulator"}[5m]))
```

调整显示单位unit：*Options->Unit*，设置为*None->percent(0.0-1.0)*

调整显示值(目前为平均)为当前值(now)：*Options->Value->Stat*，设置为*Current*

添加阀值和颜色：*Options->Coloring*，选中*Value*，将*Threshold*设置为*0.01,0.05*，表示
* 绿色：0-1%
* 橙色：1-5%
* 红色：>5%

添加测量仪效果：*Options->Gauge*，选中*Show*，并将*Max*设为*1*

添加错误率演变曲线：选中*Spark lines -> Show*

注意保存Dahsboard。

#### 6. 展示Top requested端点

点击**Add panel**图标，点击**Table**图标添加一个Table，

设置*Metrics*
```
sum(rate(http_requests_total{job="http-simulator"}[5m])) by (endpoint)
```
减少表中数据项，在*Metrics*下，选中*Instant*只显示当前值

隐藏*Time*列，在*Column Sytle*下，*Apply to columns named*为*Time*，将*Type->Type*设置为*Hidden*

将*Value*列重命名，添加一个*Column Style*，*Apply to columns named*为*Value*，将*Column Header*设置为*Requests/s*

点击表中的*Requests/s* header，让其中数据根据端点活跃度进行排序。

注意调整Widget位置并保存Dahsboard。



