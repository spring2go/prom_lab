实验六、Spring Boot Actuator监控实验
======

### 实验步骤

#### 1. 运行Spring Boot + Actuator

将[actuatordemo](actuatordemo)应用导入Eclipse IDE

Review `actuatordemo`代码

以Spring Boot方式运行actuatordemo

校验metrics端点

```
http://localhost:8080/prometheus
```

#### 2. 配置和运行Promethus

在`Prometheus`安装目录下

在`prometheus.yml` 中添加针对`wmi-exporter`的监控job

```
  - job_name: 'actuator-demo'
    metrics_path: '/prometheus'
    static_configs:
      - targets: ['localhost:8080']
```

运行`Prometheus`

```
./prometheus.exe
```

访问`Prometheus` UI

```
http://localhost:9090
```

通过`Prometheus->Status`的`configuration`和`targets`校验配置正确

通过`Prometheus->Graph`查询`actuator-demo`在UP=**1**状态

```
up{job="actuatordemo"}
```

#### 3. Grafana Dashboard for JVM (Micrometer)

在Grafana安装目录下启动Grafana服务器

```
./bin/grafana-server.exe
```

登录Grafana UI(admin/admin)

```
http://localhost:3000
```

通过Grafana的**+**图标导入(**Import**) `JVM (Micrometer)` dashboard：

* grafana id = **4701**
* 注意选中`prometheus`数据源

查看`JVM (Micormeter)` dashboard。

#### 4. 参考

Grafana Dashboard仓库
```
https://grafana.com/dashboards
```

Micrometer Prometheus支持
```
https://micrometer.io/docs/registry/prometheus
```
Micrometer Springboot 1.5支持
```
https://micrometer.io/docs/ref/spring/1.5
```






