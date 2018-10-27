实验四、Java应用埋点和监控实验
======

### 实验步骤

#### 1. Review和运行埋点样例代码

* 将[instrumentation-example](instrumentation-example)导入Eclipse IDE
* Review代码理解模拟任务系统原理和埋点方式
* 以Spring Boot方式运行埋点案例
* 通过`http://localhost:8080/prometheus`查看metrics

#### 2. 配置和运行Promethus

添加针对`instrumentation-example`的监控job

```
  - job_name: 'instrumentation-example'
    metrics_path: /prometheus
    static_configs:
    - targets: ['localhost:8080']
```

运行Prometheus

```
./prometheus.exe
```

通过`Prometheus->Status`的`configuration`和`targets`校验配置正确

#### 3. 生成测试数据和查询Metrics

查询`instrumentation-example`在UP**1**状态
```
up{job="instrumentation-example"}
```

运行[queueUpJobs.sh](queueUpJobs.sh)产生100个job
```
./queueUpJobs.sh
```

查询JobQueueSize变化曲线(调整时间范围到5m)：
```
job_queue_size{job="instrumentation-example"}
```

查询90分位Job执行延迟分布：
```
histogram_quantile(0.90, rate(jobs_completion_duration_seconds_bucket{job="instrumentation-example"}[5m]))
```

