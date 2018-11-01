实验一、Prometheus起步查询实验
======

### 实验步骤

#### 1. 运行Prometheus HTTP Metrics Simulator

* 将[http-simulator](../http-simulator)导入Eclipse IDE
* Review代码理解模拟器原理
* 以Spring Boot方式运行模拟器
* 通过`http://localhost:8080/prometheus`查看metrics
* 通过Postman启用随机Spike模式

```
curl -X POST http://SERVICE_URL:8080/spike/random
```

#### 2. 安装运行Promethus

下载[Prometheus 2.4.3 for Windows](https://github.com/prometheus/prometheus/releases/download/v2.4.3/prometheus-2.4.3.windows-amd64.tar.gz)，并解压到本地目录。

调整全配置项
```yml

# my global config
global:
  scrape_interval:     5s # Set the scrape interval to every 5 seconds. Default is every 1 minute.
  scrape_timeout: 5s
  evaluation_interval: 5s # Evaluate rules every 5 seconds. The default is every 1 minute.

```

添加http-simulator Job配置项 

```yml
# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: 'prometheus'

    # metrics_path defaults to '/metrics'
    # scheme defaults to 'http'.

    static_configs:
    - targets: ['localhost:9090']	
  - job_name: 'http-simulator'
    metrics_path: /prometheus
    static_configs:
    - targets: ['localhost:8080']

```

运行Prometheus
```
./prometheus.exe
```

访问Prometheus Web UI
```
http://localhost:9090
```

通过`Status->Targets`，或者通过`Graph`查询
```
up
```
metric方式校验`prometheus`和`http-simulator`两个jobs在**UP**或**1**状态。

#### 3. 请求率(Request Rate)查询

校验http-simulator在**1**状态
```
up{job="http-simulator"}
```

查询http请求数
```
http_requests_total{job="http-simulator"}
```

查询成功login请求数
```
http_requests_total{job="http-simulator", status="200", endpoint="/login"}
```

查询成功请求数，以endpoint区分
```
http_requests_total{job="http-simulator", status="200"}
```

查询总成功请求数
```
sum(http_requests_total{job="http-simulator", status="200"})
```

查询成功请求率，以endpoint区分
```
rate(http_requests_total{job="http-simulator", status="200"}[5m])
```

查询总成功请求率
```
sum(rate(http_requests_total{job="http-simulator", status="200"}[5m]))
```

#### 4. 延迟分布(Latency distribution)查询

查询http-simulator延迟分布
```
http_request_duration_milliseconds_bucket{job="http-simulator"}
```

查询成功login延迟分布
```
http_request_duration_milliseconds_bucket{job="http-simulator", status="200", endpoint="/login"}
```

不超过200ms延迟的成功login请求占比
```
sum(http_request_duration_milliseconds_bucket{job="http-simulator", status="200", endpoint="/login", le="200.0"}) / sum(http_request_duration_milliseconds_count{job="http-simulator", status="200", endpoint="/login"})
```

成功login请求延迟的99百分位
```
histogram_quantile(0.99, rate(http_request_duration_milliseconds_bucket{job="http-simulator", status="200", endpoint="/login"}[5m]))
```