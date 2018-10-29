实验五、NodeExporter系统监控实验
======

### 实验步骤

#### 1. 下载和运行wmi-exporter

下载[wmi_exporter-amd64](https://github.com/martinlindhe/wmi_exporter/releases/download/v0.4.3/wmi_exporter-amd64.zip)，并解压到本地目录

运行wmi-exporter

```
./wmi_exporter.exe
```

校验metrics端点

```
http://localhost:9182/metrics
```

#### 2. 配置和运行Promethus

在`Prometheus`安装目录下

在`prometheus.yml` 中添加针对`wmi-exporter`的监控job

```
  - job_name: 'wmi-exporter'
    static_configs:
      - targets: ['localhost:9182']
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

通过`Prometheus->Graph`查询`wmi-exporter`在UP**1**状态

```
up{job="wmi-exporter"}
```

#### 3. Grafana Dashboard for wmi-exporter

在Grafana安装目录下启动Grafana服务器

```
./bin/grafana-server.exe
```

登录Grafana UI(admin/admin)

```
http://localhost:3000
```

通过Grafana的**+**图标导入(**Import**) wmi-exporter dashboard：

* grafana id = **2129**
* 注意选中`prometheus`数据源

查看`Windows Node` dashboard。

#### 4. 参考

Grafana Dashboard仓库

```
https://grafana.com/dashboards
```






