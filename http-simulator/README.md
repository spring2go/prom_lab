# Prometheus HTTP Metrics Simulator

模拟一个简单的HTTP微服务，生成Prometheus Metrics，可以Spring Boot方式运行

### Metrics

运行时访问端点：
```
http://SERVICE_URL:8080/prometheus
```

包括：
* `http_requests_total`：请求计数器，`endpoint`和`status`为label
* `http_request_duration_milliseconds`：请求延迟分布(histogram)

### 运行时options

#### Spike Mode

在Spike模式下，请求数会乘以一个因子(5~15)，延迟加倍

Spike模式可以是`on`, `off`或者`random`, 改变方式：

```
# ON
curl -X POST http://SERVICE_URL:8080/spike/on

# OFF
curl -X POST http://SERVICE_URL:8080/spike/off

# RANDOM
curl -X POST http://SERVICE_URL:8080/spike/random

```

#### Error rate

缺省错误率1%，可以调整(0~100)，方法：

```
# Setting error to 50%
curl -X POST http://SERVICE_URL:8080/error_rate/50

```

#### 其它参数

配置在`application.properties`中

```
opts.endpoints=/login, /login, /login, /login, /login, /login, /login, /users, /users, /users, /users/{id}, /register, /register, /logout, /logout, /logout, /logout
opts.request_rate=1000
opts.request_rate_uncertainty=70
opts.latency_min=10
opts.latency_p50=25
opts.latency_p90=150
opts.latency_p99=750
opts.latency_max=10000
opts.latency_uncertainty=70

opts.error_rate=1
opts.spike_start_chance=5
opts.spike_end_chance=30
```


运行时校验端点：
```
http://SERVICE_URL:8080/opts
```

### 参考

https://github.com/PierreVincent/prom-http-simulator

